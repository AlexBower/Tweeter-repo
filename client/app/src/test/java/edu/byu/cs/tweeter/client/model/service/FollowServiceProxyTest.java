package edu.byu.cs.tweeter.client.model.service;

import com.google.gson.Gson;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.TestWithAuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FollowServiceProxy;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import com.google.gson.Gson;

public class FollowServiceProxyTest extends TestWithAuthToken {

    private FollowRequest validRequest;
    private FollowRequest invalidRequest;

    private FollowResponse successResponse;
    private FollowResponse failureResponse;

    private FollowServiceProxy mFollowServiceProxySpy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User user = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User otherUser = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup request objects to use in the tests
        validRequest = new FollowRequest(user, otherUser, authToken);
        invalidRequest = new FollowRequest(null, null, authToken);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new FollowResponse(true);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.follow(validRequest, FollowServiceProxy.URL_PATH)).thenReturn(successResponse);

        failureResponse = new FollowResponse(false);
        Mockito.when(mockServerFacade.follow(invalidRequest, FollowServiceProxy.URL_PATH)).thenReturn(failureResponse);

        // Create a service instance and wrap it with a spy that will use the mock service
        mFollowServiceProxySpy = Mockito.spy(new FollowServiceProxy());
        Mockito.when(mFollowServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testFollow_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowResponse response = mFollowServiceProxySpy.follow(validRequest);
        Assertions.assertEquals(successResponse, response);
        // String json = (new Gson()).toJson(validRequest);
    }

    @Test
    public void testFollow_invalidRequest_returnsFalse() throws IOException, TweeterRemoteException {
        FollowResponse response = mFollowServiceProxySpy.follow(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}