package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.TestWithAuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FollowCountServiceProxy;
import edu.byu.cs.tweeter.model.service.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;

public class FollowCountServiceIntegrationTest extends TestWithAuthToken {

    private FollowCountRequest validRequest;
    private FollowCountRequest invalidRequest;

    private FollowCountResponse successResponse;
    private String failureResponse;

    private FollowCountServiceProxy followCountServiceProxy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", null);

        // Setup request objects to use in the tests
        validRequest = new FollowCountRequest(currentUser, authToken);
        invalidRequest = new FollowCountRequest(null, authToken);

        successResponse = new FollowCountResponse(2, 2);

        failureResponse = "BadRequest: " + "No user";

        followCountServiceProxy = new FollowCountServiceProxy();
    }

    @Test
    public void testGetFollowCount_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowCountResponse response = followCountServiceProxy.getFollowCount(validRequest);
        Assertions.assertEquals(successResponse.getFollowersCount(), response.getFollowersCount());
        Assertions.assertEquals(successResponse.getFollowingCount(), response.getFollowingCount());
    }

    @Test
    public void testGetFollowCount_invalidRequest_throwsError() {
        try {
            Assertions.assertThrows(RuntimeException.class
                    , (Executable) followCountServiceProxy.getFollowCount(invalidRequest)
                    , failureResponse);
        } catch (Exception e) {
            Assertions.assertEquals(failureResponse, e.getMessage());
        }
    }
}