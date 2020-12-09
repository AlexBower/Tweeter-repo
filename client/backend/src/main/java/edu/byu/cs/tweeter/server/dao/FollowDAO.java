package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;

public class FollowDAO {

    private static final String tableName = "Follow";
    private static final String indexName = "followeeAlias-followerAlias-index";

    private static final String followerAliasAttr = "followerAlias";
    private static final String followeeAliasAttr = "followeeAlias";

    private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

    public FollowerResponse getFollowers(FollowerRequest request) {
        Map<String, String> attrNames = new HashMap<String, String>();
        attrNames.put("#fol", followeeAliasAttr);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":followee", new AttributeValue().withS(request.getFollowee().getAlias()));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(tableName)
                .withIndexName(indexName)
                .withKeyConditionExpression("#fol = :followee")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withLimit(request.getLimit())
                .withScanIndexForward(true);

        if (request.getLastFollower() != null && isNonEmptyString(request.getLastFollower().getAlias())) {
            Map<String, AttributeValue> lastKey = new HashMap<>();
            lastKey.put(followeeAliasAttr, new AttributeValue().withS(request.getFollowee().getAlias()));
            lastKey.put(followerAliasAttr, new AttributeValue().withS(request.getLastFollower().getAlias()));

            queryRequest = queryRequest.withExclusiveStartKey(lastKey);
        } else {
            queryRequest = queryRequest.withExclusiveStartKey(null);
        }

        boolean hasMorePages = true;
        List<User> responseFollowers = new ArrayList<>(request.getLimit());
        UserDAO userDAO = new UserDAO();

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
        List<Map<String, AttributeValue>> items = queryResult.getItems();
        if (items != null) {
            List<String> userAliases = new ArrayList<>();
            for (Map<String, AttributeValue> item : items) {
                userAliases.add(item.get(followerAliasAttr).getS());
            }
            if (!userAliases.isEmpty()) {
                responseFollowers = userDAO.getUsers(userAliases);
            }
        }

        Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
        if (lastKey == null) {
            hasMorePages = false;
        }

        return new FollowerResponse(responseFollowers, hasMorePages);
    }

    public FollowingResponse getFollowees(FollowingRequest request) {
        Map<String, String> attrNames = new HashMap<String, String>();
        attrNames.put("#fol", followerAliasAttr);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":follower", new AttributeValue().withS(request.getFollower().getAlias()));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(tableName)
                .withKeyConditionExpression("#fol = :follower")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withLimit(request.getLimit())
                .withScanIndexForward(true);

        if (request.getLastFollowee() != null && isNonEmptyString(request.getLastFollowee().getAlias())) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(followerAliasAttr, new AttributeValue().withS(request.getFollower().getAlias()));
            startKey.put(followeeAliasAttr, new AttributeValue().withS(request.getLastFollowee().getAlias()));

            queryRequest = queryRequest.withExclusiveStartKey(startKey);
        } else {
            queryRequest = queryRequest.withExclusiveStartKey(null);
        }

        boolean hasMorePages = true;
        List<User> responseFollowees = new ArrayList<>(request.getLimit());
        UserDAO userDAO = new UserDAO();

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
        List<Map<String, AttributeValue>> items = queryResult.getItems();
        if (items != null) {
            List<String> userAliases = new ArrayList<>();
            for (Map<String, AttributeValue> item : items) {
                userAliases.add(item.get(followeeAliasAttr).getS());
            }
            if (!userAliases.isEmpty()) {
                responseFollowees = userDAO.getUsers(userAliases);
            }
        }

        Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
        if (lastKey == null) {
            hasMorePages = false;
        }

        return new FollowingResponse(responseFollowees, hasMorePages);
    }

    public List<String> getAllFollowers(String alias) {
        Map<String, String> attrNames = new HashMap<String, String>();
        attrNames.put("#fol", followeeAliasAttr);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":follow", new AttributeValue().withS(alias));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(tableName)
                .withIndexName(indexName)
                .withKeyConditionExpression("#fol = :follow")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues);

        List<String> responseFollowers = new ArrayList<>();
        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
        List<Map<String, AttributeValue>> items = queryResult.getItems();
        if (items != null) {
            for (Map<String, AttributeValue> item : items) {
                System.out.println(item.get(followerAliasAttr).getS());
                responseFollowers.add(item.get(followerAliasAttr).getS());
            }
        }

        return responseFollowers;
    }

    public FollowResponse follow(FollowRequest request) {
        Table table = dynamoDB.getTable(tableName);

        Item item = new Item()
                .withPrimaryKey(followerAliasAttr,
                        request.getCurrentUser().getAlias(),
                        followeeAliasAttr,
                        request.getOtherUser().getAlias());
        try {
            table.putItem(item);

            UserDAO userDAO = new UserDAO();
            // update follower's followeecount
            userDAO.addToFollowCount(request.getCurrentUser().getAlias(),
                    UserDAO.followeeCountAttr,
                    1);

            // update followee's followercount
            userDAO.addToFollowCount(request.getOtherUser().getAlias(),
                    UserDAO.followerCountAttr,
                    1);

        } catch (Exception e) {
            throw new RuntimeException("InternalServerError: " + e.getMessage());
        }

        return new FollowResponse(true);
    }

    public UnfollowResponse unfollow(UnfollowRequest request) {
        Table table = dynamoDB.getTable(tableName);

        try {
            table.deleteItem(followerAliasAttr,
                    request.getCurrentUser().getAlias(),
                    followeeAliasAttr,
                    request.getOtherUser().getAlias());

            UserDAO userDAO = new UserDAO();
            // update follower's followeecount
            userDAO.addToFollowCount(request.getCurrentUser().getAlias(),
                    UserDAO.followeeCountAttr,
                    -1);

            // update followee's followercount
            userDAO.addToFollowCount(request.getOtherUser().getAlias(),
                    UserDAO.followerCountAttr,
                    -1);
        } catch (Exception e) {
            throw new RuntimeException("InternalServerError: " + e.getMessage());
        }
        return new UnfollowResponse(true);
    }

    public IsFollowingResponse isFollowing(IsFollowingRequest request) {
        Table table = dynamoDB.getTable(tableName);

        Item item = table.getItem(followerAliasAttr,
                request.getCurrentUser().getAlias(),
                followeeAliasAttr,
                request.getOtherUser().getAlias());
        if (item == null) {
            System.out.println("is returning false");
            return new IsFollowingResponse(false);
        }
        else {
            System.out.println("is returning true");
            return new IsFollowingResponse(true);
        }
    }
}
