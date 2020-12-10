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
        validRequest = new RegisterRequest("@RegiBoi",
                "password",
                "Regi",
                "Boi",
                "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"
                );
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
        Assertions.assertEquals(successResponse.getUser().getAlias(), response.getUser().getAlias());
        Assertions.assertEquals(successResponse.getUser().getFirstName(), response.getUser().getFirstName());
        Assertions.assertEquals(successResponse.getUser().getLastName(), response.getUser().getLastName());
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