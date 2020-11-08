package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class LogoutServiceTest {

    private LogoutRequest validRequest;
    private LogoutRequest invalidRequest;

    private LogoutResponse successResponse;
    private LogoutResponse failureResponse;

    private LogoutService logoutServiceSpy;

    @BeforeEach
    public void setup() {
        User user = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        AuthToken authToken = new AuthToken();

        // Setup request objects to use in the tests
        validRequest = new LogoutRequest(user, authToken);
        invalidRequest = new LogoutRequest(null, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new LogoutResponse(true);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.logout(validRequest)).thenReturn(successResponse);

        failureResponse = new LogoutResponse(false);
        Mockito.when(mockServerFacade.logout(invalidRequest)).thenReturn(failureResponse);

        // Create a service instance and wrap it with a spy that will use the mock service
        logoutServiceSpy = Mockito.spy(new LogoutService());
        Mockito.when(logoutServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testLogout_validRequest_correctResponse() {
        LogoutResponse response = logoutServiceSpy.logout(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testLogout_invalidRequest_returnsFalse() {
        LogoutResponse response = logoutServiceSpy.logout(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}