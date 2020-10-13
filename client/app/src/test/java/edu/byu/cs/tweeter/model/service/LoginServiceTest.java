package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;

public class LoginServiceTest {

    private LoginRequest validRequest;
    private LoginRequest invalidRequest;

    private LoginResponse successResponse;
    private LoginResponse failureResponse;

    private LoginService loginServiceSpy;

    @BeforeEach
    public void setup() {
        User resultUser = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        AuthToken resultAuthToken = new AuthToken();

        // Setup request objects to use in the tests
        validRequest = new LoginRequest("@Test", "password");
        invalidRequest = new LoginRequest("notFound", "invalid");

        // Setup a mock ServerFacade that will return known responses
        successResponse = new LoginResponse(resultUser, resultAuthToken);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.login(validRequest)).thenReturn(successResponse);

        failureResponse = new LoginResponse("An exception occurred");
        Mockito.when(mockServerFacade.login(invalidRequest)).thenReturn(failureResponse);

        // Create a service instance and wrap it with a spy that will use the mock service
        loginServiceSpy = Mockito.spy(new LoginService());
        Mockito.when(loginServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testLogin_validRequest_correctResponse() throws IOException {
        LoginResponse response = loginServiceSpy.login(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testLogin_validRequest_loadsProfileImage() throws IOException {
        LoginResponse response = loginServiceSpy.login(validRequest);

        Assertions.assertNotNull(response.getUser().getImageBytes());
    }

    @Test
    public void testLogin_invalidRequest_returnsUserNotFound() throws IOException {
        LoginResponse response = loginServiceSpy.login(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}