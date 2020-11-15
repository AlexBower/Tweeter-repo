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
import edu.byu.cs.tweeter.model.service.UnfollowServiceProxy;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;

public class UnfollowServiceProxyTest extends TestWithAuthToken {

    private UnfollowRequest validRequest;
    private UnfollowRequest invalidRequest;

    private UnfollowResponse successResponse;
    private UnfollowResponse failureResponse;

    private UnfollowServiceProxy mUnfollowServiceProxySpy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User user = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User otherUser = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup request objects to use in the tests
        validRequest = new UnfollowRequest(user, otherUser, authToken);
        invalidRequest = new UnfollowRequest(null, null, authToken);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new UnfollowResponse(true);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.unfollow(validRequest, UnfollowServiceProxy.URL_PATH)).thenReturn(successResponse);

        failureResponse = new UnfollowResponse(false);
        Mockito.when(mockServerFacade.unfollow(invalidRequest, UnfollowServiceProxy.URL_PATH)).thenReturn(failureResponse);

        // Create a service instance and wrap it with a spy that will use the mock service
        mUnfollowServiceProxySpy = Mockito.spy(new UnfollowServiceProxy());
        Mockito.when(mUnfollowServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testUnfollow_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        UnfollowResponse response = mUnfollowServiceProxySpy.unfollow(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testUnfollow_invalidRequest_returnsFalse() throws IOException, TweeterRemoteException {
        UnfollowResponse response = mUnfollowServiceProxySpy.unfollow(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}