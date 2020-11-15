package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.TestWithAuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.LoginServiceProxy;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;

public class LoginServiceIntegrationTest extends TestWithAuthToken {

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private final User user = new User("Testing", "User", MALE_IMAGE_URL);

    private LoginRequest validRequest;
    private LoginRequest invalidRequest;

    private LoginResponse successResponse;
    private String failureResponse;

    private LoginServiceProxy serviceProxy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        validRequest = new LoginRequest("FirstName", "password");
        invalidRequest = new LoginRequest(null, null);

        successResponse = new LoginResponse(user, authToken);

        failureResponse = "BadRequest: " + "No username and/or password";

        serviceProxy = new LoginServiceProxy();
    }

    @Test
    public void testLogin_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LoginResponse response = serviceProxy.login(validRequest);
        Assertions.assertEquals(successResponse.isSuccess(), response.isSuccess());
    }

    @Test
    public void testLogin_invalidRequest_throwsError() {
        try {
            Assertions.assertThrows(RuntimeException.class
                    , (Executable) serviceProxy.login(invalidRequest)
                    , failureResponse);
        } catch (Exception e) {
            Assertions.assertEquals(failureResponse, e.getMessage());
        }
    }
}