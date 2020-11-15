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
import edu.byu.cs.tweeter.model.service.FollowCountServiceProxy;
import edu.byu.cs.tweeter.model.service.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;

import static org.mockito.Mockito.doReturn;

public class FollowCountServiceProxyTest extends TestWithAuthToken {

    private FollowCountRequest validRequest;
    private FollowCountRequest invalidRequest;

    private FollowCountResponse successResponse;
    private FollowCountResponse failureResponse;

    private FollowCountServiceProxy followCountServiceProxySpy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", null);

        // Setup request objects to use in the tests
        validRequest = new FollowCountRequest(currentUser, authToken);
        invalidRequest = new FollowCountRequest(null, authToken);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new FollowCountResponse(100, 27);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getFollowCount(validRequest, FollowCountServiceProxy.URL_PATH)).thenReturn(successResponse);

        failureResponse = new FollowCountResponse("An exception occurred");
        Mockito.when(mockServerFacade.getFollowCount(invalidRequest, FollowCountServiceProxy.URL_PATH)).thenReturn(failureResponse);

        // Create a service instance and wrap it with a spy that will use the mock service
        followCountServiceProxySpy = Mockito.spy(new FollowCountServiceProxy());
        Mockito.when(followCountServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testGetFollowCounts_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowCountResponse response = followCountServiceProxySpy.getFollowCount(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetFollowCounts_invalidRequest_returnsNoFollowCounts() throws IOException, TweeterRemoteException {
        FollowCountResponse response = followCountServiceProxySpy.getFollowCount(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}