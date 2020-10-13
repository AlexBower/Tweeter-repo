package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

public class RegisterServiceTest {

    private RegisterRequest validRequest;
    private RegisterRequest invalidRequest;

    private RegisterResponse successResponse;
    private RegisterResponse failureResponse;

    private RegisterService registerServiceSpy;

    @BeforeEach
    public void setup() {
        byte[] testBytes= {0, 1, 0, 1};

        User resultUser = new User("FirstName",
                "LastName",
                "@Test",
                testBytes);
        AuthToken resultAuthToken = new AuthToken();

        // Setup request objects to use in the tests
        validRequest = new RegisterRequest("@Test",
                "password",
                "FirstName",
                "LastName",
                testBytes);
        invalidRequest = new RegisterRequest("", "", "", "", testBytes);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new RegisterResponse(resultUser, resultAuthToken);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.register(validRequest)).thenReturn(successResponse);

        failureResponse = new RegisterResponse("An exception occurred");
        Mockito.when(mockServerFacade.register(invalidRequest)).thenReturn(failureResponse);

        // Create a service instance and wrap it with a spy that will use the mock service
        registerServiceSpy = Mockito.spy(new RegisterService());
        Mockito.when(registerServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testRegister_validRequest_correctResponse() throws IOException {
        RegisterResponse response = registerServiceSpy.register(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testRegister_validRequest_loadsProfileImage() throws IOException {
        RegisterResponse response = registerServiceSpy.register(validRequest);

        Assertions.assertNotNull(response.getUser().getImageBytes());
    }

    @Test
    public void testRegister_invalidRequest_returnsMissingInformation() throws IOException {
        RegisterResponse response = registerServiceSpy.register(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}