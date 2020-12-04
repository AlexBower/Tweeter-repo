package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

import java.security.SecureRandom;
import java.time.Instant;

public class AuthTokenDAO {

    private static final int timeOutMinutes = 15;

    private static final String tableName = "AuthToken";

    private static final String aliasAttr = "alias";
    private static final String tokenAttr = "token";
    private static final String expTimeAttr = "expTime";

    private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    public boolean deleteAuthToken(String alias, String token) {
        Table table = dynamoDB.getTable(tableName);
        try {
            table.deleteItem(aliasAttr, alias, tokenAttr, token);
        } catch (Exception e) {
            throw new RuntimeException("InternalServerError: " + e.getMessage());
        }
        return true;
    }

    public boolean checkAuthToken(String alias, String token) {

        Table table = dynamoDB.getTable(tableName);

        Item item = table.getItem(aliasAttr, alias, tokenAttr, token);
        if (item == null) {
            throw new RuntimeException("BadRequest: Invalid or Expired AuthToken");
        }
        else {
            try {
                item.getInt(expTimeAttr);
            } catch (Exception e) {
                return true;
            }
            return updateAuthTokenExpTime(alias, token, table);
        }
    }

    public AuthToken createAuthToken(String alias) {

        Table table = dynamoDB.getTable(tableName);

        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        String token = bytes.toString();

        long newExpTime = Instant.now().getEpochSecond() + (timeOutMinutes * 60);

        Item item = new Item()
                .withPrimaryKey(aliasAttr, alias, tokenAttr, token)
                .withNumber(expTimeAttr, newExpTime);

        try {
            table.putItem(item);
        } catch (Exception e) {
            throw new RuntimeException("InternalServerError: " + e.getMessage());
        }

        return new AuthToken(token);
    }

    public boolean updateAuthTokenExpTime(String alias, String token, Table table) {

        long newExpTime = Instant.now().getEpochSecond() + (timeOutMinutes * 60);

        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(aliasAttr, alias, tokenAttr, token)
                .addAttributeUpdate(new AttributeUpdate(expTimeAttr).put(newExpTime))
                .withReturnValues(ReturnValue.UPDATED_NEW);

        try {
            //Updating the item...
            table.updateItem(updateItemSpec);
            //UpdateItem succeeded:\n
            return true;
        }
        catch (Exception e) {
            throw new RuntimeException("InternalServerError: " + e.getMessage());
        }
    }
}
