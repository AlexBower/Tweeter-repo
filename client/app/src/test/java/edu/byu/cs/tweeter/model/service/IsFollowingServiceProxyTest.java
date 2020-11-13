package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.TestWithAuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;

public class IsFollowingServiceProxyTest extends TestWithAuthToken {

    private IsFollowingRequest validRequest;
    private IsFollowingRequest invalidRequest;

    private IsFollowingResponse successResponse;
    private IsFollowingResponse failureResponse;

    private IsFollowingServiceProxy mIsFollowingServiceProxy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User user = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User otherUser = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup request objects to use in the tests
        validRequest = new IsFollowingRequest(user, otherUser, authToken);
        invalidRequest = new IsFollowingRequest(null, null, authToken);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new IsFollowingResponse(true);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.isFollowing(validRequest, IsFollowingServiceProxy.URL_PATH)).thenReturn(successResponse);

        failureResponse = new IsFollowingResponse(false);
        Mockito.when(mockServerFacade.isFollowing(invalidRequest, IsFollowingServiceProxy.URL_PATH)).thenReturn(failureResponse);

        // Create a service instance and wrap it with a spy that will use the mock service
        mIsFollowingServiceProxy = Mockito.spy(new IsFollowingServiceProxy());
        Mockito.when(mIsFollowingServiceProxy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testIsFollowing_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        IsFollowingResponse response = mIsFollowingServiceProxy.isFollowing(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testIsFollowing_invalidRequest_returnsFalse() throws IOException, TweeterRemoteException {
        IsFollowingResponse response = mIsFollowingServiceProxy.isFollowing(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}