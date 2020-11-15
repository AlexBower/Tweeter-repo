package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.TestWithAuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.LogoutServiceProxy;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class LogoutServiceProxyTest extends TestWithAuthToken {

    private LogoutRequest validRequest;
    private LogoutRequest invalidRequest;

    private LogoutResponse successResponse;
    private LogoutResponse failureResponse;

    private LogoutServiceProxy mLogoutServiceProxySpy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User user = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup request objects to use in the tests
        validRequest = new LogoutRequest(user, authToken);
        invalidRequest = new LogoutRequest(null, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new LogoutResponse(true);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.logout(validRequest, LogoutServiceProxy.URL_PATH)).thenReturn(successResponse);

        failureResponse = new LogoutResponse(false);
        Mockito.when(mockServerFacade.logout(invalidRequest, LogoutServiceProxy.URL_PATH)).thenReturn(failureResponse);

        // Create a service instance and wrap it with a spy that will use the mock service
        mLogoutServiceProxySpy = Mockito.spy(new LogoutServiceProxy());
        Mockito.when(mLogoutServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testLogout_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LogoutResponse response = mLogoutServiceProxySpy.logout(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testLogout_invalidRequest_returnsFalse() throws IOException, TweeterRemoteException {
        LogoutResponse response = mLogoutServiceProxySpy.logout(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}