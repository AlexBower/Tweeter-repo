package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;

import edu.byu.cs.tweeter.TestWithAuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.IsFollowingServiceProxy;
import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;

public class IsFollowingServiceIntegrationTest extends TestWithAuthToken {

    private IsFollowingRequest validRequest;
    private IsFollowingRequest invalidRequest;

    private IsFollowingResponse successResponse;
    private String failureResponse;

    private IsFollowingServiceProxy serviceProxy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", null);
        User otherUser = new User("Other", "guy", null);

        // Setup request objects to use in the tests
        validRequest = new IsFollowingRequest(currentUser, otherUser, authToken);
        invalidRequest = new IsFollowingRequest(null, null, authToken);

        successResponse = new IsFollowingResponse(true);

        failureResponse = "BadRequest: " + "No user";

        serviceProxy = new IsFollowingServiceProxy();
    }

    @Test
    public void testIsFollowing_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        IsFollowingResponse response = serviceProxy.isFollowing(validRequest);
        Assertions.assertEquals(successResponse.isSuccess(), response.isSuccess());
    }

    @Test
    public void testIsFollowing_invalidRequest_throwsError() {
        try {
            Assertions.assertThrows(RuntimeException.class
                    , (Executable) serviceProxy.isFollowing(invalidRequest)
                    , failureResponse);
        } catch (Exception e) {
            Assertions.assertEquals(failureResponse, e.getMessage());
        }
    }
}