package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;

import edu.byu.cs.tweeter.TestWithAuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.GetUserServiceProxy;
import edu.byu.cs.tweeter.model.service.request.GetUserRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserResponse;

public class GetUserServiceIntegrationTest extends TestWithAuthToken {

    private GetUserRequest validRequest;
    private GetUserRequest invalidRequest;

    private GetUserResponse successResponse;
    private String failureResponse;

    private GetUserServiceProxy serviceProxy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", null);

        // Setup request objects to use in the tests
        validRequest = new GetUserRequest("currentUser", authToken);
        invalidRequest = new GetUserRequest(null, authToken);

        successResponse = new GetUserResponse(currentUser);

        failureResponse = "BadRequest: " + "No Username";

        serviceProxy = new GetUserServiceProxy();
    }

    @Test
    public void testGetUser_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        GetUserResponse response = serviceProxy.getUser(validRequest);
        Assertions.assertEquals(successResponse.isSuccess(), response.isSuccess());
    }

    @Test
    public void testGetUser_invalidRequest_throwsError() {
        try {
            Assertions.assertThrows(RuntimeException.class
                    , (Executable) serviceProxy.getUser(invalidRequest)
                    , failureResponse);
        } catch (Exception e) {
            Assertions.assertEquals(failureResponse, e.getMessage());
        }
    }
}