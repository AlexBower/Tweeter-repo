package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;

import edu.byu.cs.tweeter.TestWithAuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.UnfollowServiceProxy;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;

public class UnfollowServiceIntegrationTest extends TestWithAuthToken {

    private UnfollowRequest validRequest;
    private UnfollowRequest invalidRequest;

    private UnfollowResponse successResponse;
    private String failureResponse;

    private UnfollowServiceProxy serviceProxy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", null);
        User otherUser = new User("Other", "guy", null);

        // Setup request objects to use in the tests
        validRequest = new UnfollowRequest(currentUser, otherUser, authToken);
        invalidRequest = new UnfollowRequest(null, null, authToken);

        successResponse = new UnfollowResponse(true);

        failureResponse = "BadRequest: " + "No user";

        serviceProxy = new UnfollowServiceProxy();
    }

    @Test
    public void testUnfollow_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        UnfollowResponse response = serviceProxy.unfollow(validRequest);
        Assertions.assertEquals(successResponse.isSuccess(), response.isSuccess());
    }

    @Test
    public void testUnfollow_invalidRequest_throwsError() {
        try {
            Assertions.assertThrows(RuntimeException.class
                    , (Executable) serviceProxy.unfollow(invalidRequest)
                    , failureResponse);
        } catch (Exception e) {
            Assertions.assertEquals(failureResponse, e.getMessage());
        }
    }
}