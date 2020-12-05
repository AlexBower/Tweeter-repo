package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.AttributeUpdate;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.AuthToken;
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

    //private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    //private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

    //private final User user1 = new User("Allen", "Anderson", MALE_IMAGE_URL);
    //private final User user2 = new User("Amy", "Ames", FEMALE_IMAGE_URL);
    //private final User user3 = new User("Bob", "Bobson", MALE_IMAGE_URL);
    //private final User user4 = new User("Bonnie", "Beatty", FEMALE_IMAGE_URL);
    //private final User user5 = new User("Chris", "Colston", MALE_IMAGE_URL);
    //private final User user6 = new User("Cindy", "Coats", FEMALE_IMAGE_URL);
    //private final User user7 = new User("Dan", "Donaldson", MALE_IMAGE_URL);
    //private final User user8 = new User("Dee", "Dempsey", FEMALE_IMAGE_URL);
    //private final User user9 = new User("Elliott", "Enderson", MALE_IMAGE_URL);
    //private final User user10 = new User("Elizabeth", "Engle", FEMALE_IMAGE_URL);
    //private final User user11 = new User("Frank", "Frandson", MALE_IMAGE_URL);
    //private final User user12 = new User("Fran", "Franklin", FEMALE_IMAGE_URL);
    //private final User user13 = new User("Gary", "Gilbert", MALE_IMAGE_URL);
    //private final User user14 = new User("Giovanna", "Giles", FEMALE_IMAGE_URL);
    //private final User user15 = new User("Henry", "Henderson", MALE_IMAGE_URL);
    //private final User user16 = new User("Helen", "Hopwell", FEMALE_IMAGE_URL);
    //private final User user17 = new User("Igor", "Isaacson", MALE_IMAGE_URL);
    //private final User user18 = new User("Isabel", "Isaacson", FEMALE_IMAGE_URL);
    //private final User user19 = new User("Justin", "Jones", MALE_IMAGE_URL);
    //private final User user20 = new User("Jill", "Johnson", FEMALE_IMAGE_URL);

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

    //public FollowerResponse getFollowers(FollowerRequest request) {
    //    Table table = dynamoDB.getTable(tableName);
    //    Index index = table.getIndex(indexName);
//
    //    System.out.println("request limit: " + request.getLimit());
    //    System.out.println("request last follower: " + request.getLastFollower());
    //    System.out.println("request followee: " + request.getFollowee());
//
    //    QuerySpec querySpec = new QuerySpec()
    //            .withHashKey(followeeAliasAttr,
    //                    request.getFollowee().getAlias())
    //            .withMaxPageSize(request.getLimit())
    //            .withScanIndexForward(true);
    //    System.out.println("1");
    //    if (request.getLastFollower() != null) {
    //        querySpec.withExclusiveStartKey(followeeAliasAttr,
    //                request.getFollowee().getAlias(),
    //                followerAliasAttr,
    //                request.getLastFollower().getAlias());
    //        System.out.println("2");
    //    }
//
    //    ItemCollection<QueryOutcome> items = null;
    //    Iterator<Item> iterator = null;
    //    Item item = null;
    //    boolean hasMorePages = true;
    //    List<User> responseFollowers = new ArrayList<>(request.getLimit());
//
    //    try {
    //        items = index.query(querySpec);
    //        System.out.println("3");
    //        if (items != null && items.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey() == null) {
    //            hasMorePages = false;
    //        }
    //        System.out.println("4");
//
    //        UserDAO userDAO = new UserDAO();
//
    //        if (items != null) {
    //            iterator = items.iterator();
    //            while (iterator.hasNext()) {
    //                item = iterator.next();
    //                System.out.println("followerAliasAttr: " + item.getString(followerAliasAttr));
    //                responseFollowers.add(userDAO.getUser(item.getString(followerAliasAttr)));
    //            }
    //        }
    //        System.out.println("5");
    //    }
    //    catch (Exception e) {
    //        throw new RuntimeException("InternalServerError: " + e.getMessage());
    //    }
    //    return new FollowerResponse(responseFollowers, hasMorePages);
    //}

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
                .withLimit(request.getLimit());

        if (request.getLastFollower() != null && isNonEmptyString(request.getLastFollower().getAlias())) {
            Map<String, AttributeValue> lastKey = new HashMap<>();
            lastKey.put(followeeAliasAttr, new AttributeValue().withS(request.getFollowee().getAlias()));
            lastKey.put(followerAliasAttr, new AttributeValue().withS(request.getLastFollower().getAlias()));

            queryRequest = queryRequest.withExclusiveStartKey(lastKey);
        }

        boolean hasMorePages = true;
        List<User> responseFollowers = new ArrayList<>(request.getLimit());
        UserDAO userDAO = new UserDAO();

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
        List<Map<String, AttributeValue>> items = queryResult.getItems();
        if (items != null) {
            for (Map<String, AttributeValue> item : items){
                responseFollowers.add(userDAO.getUser(item.get(followerAliasAttr).getS()));
            }
        }

        Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
        if (lastKey == null) {
            hasMorePages = false;
        }

        return new FollowerResponse(responseFollowers, hasMorePages);
    }

    //public FollowingResponse getFollowees(FollowingRequest request) {
    //    Table table = dynamoDB.getTable(tableName);
//
    //    QuerySpec querySpec = new QuerySpec()
    //            .withHashKey(followerAliasAttr,
    //                    request.getFollower().getAlias())
    //            .withMaxPageSize(request.getLimit())
    //            .withScanIndexForward(true);
    //    if (request.getLastFollowee() != null) {
    //        querySpec.withExclusiveStartKey(followerAliasAttr,
    //                request.getFollower().getAlias(),
    //                followeeAliasAttr,
    //                request.getLastFollowee().getAlias());
    //    }
//
    //    ItemCollection<QueryOutcome> items = null;
    //    Iterator<Item> iterator = null;
    //    Item item = null;
    //    boolean hasMorePages = true;
    //    List<User> responseFollowees = new ArrayList<>(request.getLimit());
//
    //    try {
    //        items = table.query(querySpec);
//
    //        if (items != null && items.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey() == null) {
    //            hasMorePages = false;
    //        }
//
    //        UserDAO userDAO = new UserDAO();
//
    //        if (items != null) {
    //            iterator = items.iterator();
    //            while (iterator.hasNext()) {
    //                item = iterator.next();
    //                System.out.println("followeeAliasAttr: " + item.getString(followeeAliasAttr));
    //                responseFollowees.add(userDAO.getUser(item.getString(followeeAliasAttr)));
    //            }
    //        }
    //    }
    //    catch (Exception e) {
    //        throw new RuntimeException("InternalServerError: " + e.getMessage());
    //    }
    //    return new FollowingResponse(responseFollowees, hasMorePages);
    //}

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
                .withLimit(request.getLimit());

        if (request.getLastFollowee() != null && isNonEmptyString(request.getLastFollowee().getAlias())) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(followerAliasAttr, new AttributeValue().withS(request.getFollower().getAlias()));
            startKey.put(followeeAliasAttr, new AttributeValue().withS(request.getLastFollowee().getAlias()));

            queryRequest = queryRequest.withExclusiveStartKey(startKey);
        }

        boolean hasMorePages = true;
        List<User> responseFollowees = new ArrayList<>(request.getLimit());
        UserDAO userDAO = new UserDAO();

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
        List<Map<String, AttributeValue>> items = queryResult.getItems();
        if (items != null) {
            for (Map<String, AttributeValue> item : items) {
                responseFollowees.add(userDAO.getUser(item.get(followeeAliasAttr).getS()));
            }
        }

        Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
        if (lastKey == null) {
            hasMorePages = false;
        }

        return new FollowingResponse(responseFollowees, hasMorePages);
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
