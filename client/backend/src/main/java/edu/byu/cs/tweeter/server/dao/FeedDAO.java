package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import org.w3c.dom.UserDataHandler;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.service.request.StatusRequest;
import edu.byu.cs.tweeter.model.service.response.StatusResponse;
import edu.byu.cs.tweeter.server.request.JobQRequest;
import edu.byu.cs.tweeter.server.service.TimeFormatter;

public class FeedDAO {

    private static final String tableName = "Feed";

    private static final String aliasAttr = "alias";
    private static final String timeAttr = "time";
    private static final String authorAliasAttr = "authorAlias";
    private static final String messageAttr = "message";

    private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

    public StatusResponse getFeed(StatusRequest request) {
        Map<String, String> attrNames = new HashMap<String, String>();
        attrNames.put("#ali", aliasAttr);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":alias", new AttributeValue().withS(request.getUser().getAlias()));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(tableName)
                .withKeyConditionExpression("#ali = :alias")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withLimit(request.getLimit())
                .withScanIndexForward(false);

        if (request.getLastStatus() != null && isNonEmptyString(request.getLastStatus().getTime())) {
            Map<String, AttributeValue> lastKey = new HashMap<>();
            lastKey.put(aliasAttr, new AttributeValue().withS(request.getUser().getAlias()));

            LocalDateTime localDateTime = TimeFormatter.getFromString(request.getLastStatus().getTime());
            long epochSecond = localDateTime.toEpochSecond(ZoneOffset.UTC);
            String lastStatusTime = epochSecond + "";
            lastKey.put(timeAttr, new AttributeValue().withN(lastStatusTime));

            queryRequest = queryRequest.withExclusiveStartKey(lastKey);
        } else {
            queryRequest = queryRequest.withExclusiveStartKey(null);
        }

        boolean hasMorePages = true;
        List<Status> responseStatuses = new ArrayList<>(request.getLimit());

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
        List<Map<String, AttributeValue>> items = queryResult.getItems();
        if (items != null) {
            List<String> userAliases = new ArrayList<>();
            List<User> responseUsers = new ArrayList<>();
            UserDAO userDAO = new UserDAO();
            for (Map<String, AttributeValue> item : items) {
                userAliases.add(item.get(authorAliasAttr).getS());
            }
            if (!userAliases.isEmpty()) {
                responseUsers = userDAO.getUsers(userAliases);
            }
            for (int i = 0; i < items.size(); i++) {
                Map<String, AttributeValue> item = items.get(i);
                long epochSecond = Long.parseLong(item.get(timeAttr).getN());
                LocalDateTime ldt = LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.UTC);
                String timeString = TimeFormatter.format(ldt);
                responseStatuses.add(new Status(
                        item.get(messageAttr).getS(),
                        timeString,
                        responseUsers.get(i)));
            }
        }

        Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
        if (lastKey == null) {
            hasMorePages = false;
        }

        return new StatusResponse(responseStatuses, hasMorePages);
    }

    public void postStatuses(JobQRequest jobQRequest) {
        try {
            System.out.println(jobQRequest.getAliases());
            List<Item> items = new ArrayList<>();
            for (String alias : jobQRequest.getAliases()) {
                items.add(new Item()
                        .withPrimaryKey(aliasAttr, alias, timeAttr, Long.parseLong(jobQRequest.getTime()))
                        .withString(messageAttr, jobQRequest.getMessage())
                        .withString(authorAliasAttr, jobQRequest.getAuthor()));
            }

            TableWriteItems tableWriteItems = new TableWriteItems(tableName)
                    .withItemsToPut(items);
            BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(tableWriteItems);
            do {
                Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();

                if (outcome.getUnprocessedItems().size() != 0) {
                    outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
                }
            } while (outcome.getUnprocessedItems().size() > 0);

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            throw new RuntimeException("InternalServerError: " + e.getMessage());
        }
    }
}
