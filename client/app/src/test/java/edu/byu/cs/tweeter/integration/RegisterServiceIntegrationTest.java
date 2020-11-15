package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;

import edu.byu.cs.tweeter.TestWithAuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.RegisterServiceProxy;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

public class RegisterServiceIntegrationTest extends TestWithAuthToken {

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private final User user = new User("Regi", "Boi", MALE_IMAGE_URL);

    private RegisterRequest validRequest;
    private RegisterRequest invalidRequest;

    private RegisterResponse successResponse;
    private String failureResponse;

    private RegisterServiceProxy serviceProxy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        validRequest = new RegisterRequest("FirstName",
                "password",
                "done",
                "Finally!",
                "SANDWICH");
        invalidRequest = new RegisterRequest(null,
                null,
                null,
                null,
                null);

        successResponse = new RegisterResponse(user, authToken);

        failureResponse = "BadRequest: " + "No username and/or password";

        serviceProxy = new RegisterServiceProxy();
    }

    @Test
    public void testRegister_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        RegisterResponse response = serviceProxy.register(validRequest);
        Assertions.assertEquals(successResponse.isSuccess(), response.isSuccess());
    }

    @Test
    public void testRegister_invalidRequest_throwsError() {
        try {
            Assertions.assertThrows(RuntimeException.class
                    , (Executable) serviceProxy.register(invalidRequest)
                    , failureResponse);
        } catch (Exception e) {
            Assertions.assertEquals(failureResponse, e.getMessage());
        }
    }
}