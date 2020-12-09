package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAccessor;
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
import edu.byu.cs.tweeter.server.service.TimeFormatter;

public class StoryDAO {
    private static final String tableName = "Story";

    private static final String aliasAttr = "alias";
    private static final String timeAttr = "time";
    private static final String messageAttr = "message";

    private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

    public StatusResponse getStory(StatusRequest request) {
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
            for (Map<String, AttributeValue> item : items) {
                long epochSecond = Long.parseLong(item.get(timeAttr).getN());
                LocalDateTime ldt = LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.UTC);
                String timeString = TimeFormatter.format(ldt);
                responseStatuses.add(new Status(
                        item.get(messageAttr).getS(),
                        timeString,
                        request.getUser()));
            }
        }

        Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
        if (lastKey == null) {
            hasMorePages = false;
        }

        return new StatusResponse(responseStatuses, hasMorePages);
    }

    public void postStatus(PostStatusRequest request) {
        Table table = dynamoDB.getTable(tableName);

        LocalDateTime localDateTime = TimeFormatter.getFromString(request.getStatus().getTime());
        long epochSecond = localDateTime.toEpochSecond(ZoneOffset.UTC);

        Item item = new Item()
                .withPrimaryKey(
                        aliasAttr,
                        request.getStatus().getUser().getAlias(),
                        timeAttr,
                        epochSecond)
                .withString(messageAttr, request.getStatus().getMessage());
        try {
            table.putItem(item);
        } catch (Exception e) {
            throw new RuntimeException("InternalServerError: " + e.getMessage());
        }
    }
}
