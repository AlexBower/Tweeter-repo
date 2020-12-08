package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.AttributeUpdate;
import com.amazonaws.services.dynamodbv2.document.BatchGetItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableKeysAndAttributes;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.KeysAndAttributes;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.User;

public class UserDAO {
    //private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    //private final User test_boi = new User("Testing", "User", MALE_IMAGE_URL);
    //private final User regi_boi = new User("Regi", "Boi", MALE_IMAGE_URL);
    //private AuthToken test_authToken = new AuthToken("brilliantly_secure_token");

    private static final String tableName = "User";

    private static final String aliasAttr = "alias";
    private static final String securePasswordAttr = "securePassword";
    private static final String saltAttr = "salt";
    private static final String firstNameAttr = "firstName";
    private static final String lastNameAttr = "lastName";
    private static final String imageUrlAttr = "imageUrl";
    public static final String followerCountAttr = "followerCount";
    public static final String followeeCountAttr = "followeeCount";

    private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    public User login(String alias, String password) {
        Table table = dynamoDB.getTable(tableName);

        Item item = table.getItem(aliasAttr, alias);
        if (item == null) {
            throw new RuntimeException("BadRequest: Incorrect login");
        }
        else {
            try {
                String securePassword = item.getString(securePasswordAttr);
                String salt = item.getString(saltAttr);

                String regeneratedPasswordToVerify = SaltedSHAHashing.getSecurePassword(password, salt);

                if (!securePassword.equals(regeneratedPasswordToVerify)) {
                    throw new RuntimeException("BadRequest: Incorrect login");
                }

                String firstName = item.getString(firstNameAttr);
                String lastName = item.getString(lastNameAttr);
                String imageUrl = item.getString(imageUrlAttr);

                return new User(firstName, lastName, alias, imageUrl);

            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException("InternalServerError: " + e.getMessage());
            }
        }
    }

    public User getUser(String alias){
        Table table = dynamoDB.getTable(tableName);

        Item item = table.getItem(aliasAttr, alias);
        if (item == null) {
            throw new RuntimeException("BadRequest: No User Found");
        }
        else {
            try {
                String firstName = item.getString(firstNameAttr);
                String lastName = item.getString(lastNameAttr);
                String imageUrl = item.getString(imageUrlAttr);

                return new User(firstName, lastName, alias, imageUrl);

            } catch (Exception e) {
                throw new RuntimeException("InternalServerError: " + e.getMessage());
            }
        }
    }

    public List<User> getUsers(List<String> userAliases) {
        TableKeysAndAttributes tableKeysAndAttributes = new TableKeysAndAttributes(tableName);
        // Add a partition key
        tableKeysAndAttributes.addHashOnlyPrimaryKeys(aliasAttr, userAliases.toArray());

        List<User> returnUsers = new ArrayList<>();

        try {
            BatchGetItemOutcome outcome = dynamoDB.batchGetItem(tableKeysAndAttributes);

            Map<String, KeysAndAttributes> unprocessed = null;

            do {
                for (String tableName : outcome.getTableItems().keySet()) {
                    List<Item> items = outcome.getTableItems().get(tableName);
                    for (Item item : items) {
                        returnUsers.add(new User(
                                item.getString(firstNameAttr),
                                item.getString(lastNameAttr),
                                item.getString(aliasAttr),
                                item.getString(imageUrlAttr)));
                    }
                }

                unprocessed = outcome.getUnprocessedKeys();

                if (!unprocessed.isEmpty()) {
                    outcome = dynamoDB.batchGetItemUnprocessed(unprocessed);
                }
            } while (!unprocessed.isEmpty());
        } catch (Exception e) {
            throw new RuntimeException("InternalServerError: " + e.getMessage());
        }
        return returnUsers;
    }

    public User register(String alias,
                                     String password,
                                     String firstName,
                                     String lastName,
                                     byte[] imageBytes) {
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion("us-west-2")
                .build();

        InputStream stream = new ByteArrayInputStream(imageBytes);

        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(imageBytes.length);
        meta.setContentType("image/png");

        s3Client.putObject(new PutObjectRequest(
                "tweeter-pictures", "images/" + alias + ".png", stream, meta)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        String imageUrl = s3Client.getUrl("tweeter-pictures", "images/" + alias + ".png").toString();

        Table table = dynamoDB.getTable(tableName);

        String salt = SaltedSHAHashing.getSalt();
        String securePassword = SaltedSHAHashing.getSecurePassword(password, salt);

        Item item = new Item()
                .withPrimaryKey(aliasAttr, alias)
                .withString(securePasswordAttr, securePassword)
                .withString(saltAttr, salt)
                .withString(firstNameAttr, firstName)
                .withString(lastNameAttr, lastName)
                .withString(imageUrlAttr, imageUrl)
                .withNumber(followerCountAttr, 0)
                .withNumber(followeeCountAttr, 0);
        try {
            table.putItem(item);
        } catch (Exception e) {
            throw new RuntimeException("InternalServerError: " + e.getMessage());
        }
        return new User(firstName, lastName, alias, imageUrl);
    }

    public Integer getFollowCount(String alias, String followCountAttr) {
        Table table = dynamoDB.getTable(tableName);

        Item item = table.getItem(aliasAttr, alias);
        if (item == null) {
            throw new RuntimeException("BadRequest: No User Found");
        }
        else {
            try {
                return item.getInt(followCountAttr);
            } catch (Exception e) {
                throw new RuntimeException("InternalServerError: " + e.getMessage());
            }
        }
    }

    public void addToFollowCount(String alias, String followCountAttr, int addNumber) {
        int newFollowCount = getFollowCount(alias, followCountAttr) + addNumber;

        Table table = dynamoDB.getTable(tableName);

        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey(aliasAttr, alias)
                .addAttributeUpdate(new AttributeUpdate(followCountAttr).put(newFollowCount))
                .withReturnValues(ReturnValue.UPDATED_NEW);
        try {
            table.updateItem(updateItemSpec);
        }
        catch (Exception e) {
            throw new RuntimeException("InternalServerError: " + e.getMessage());
        }
    }
}
