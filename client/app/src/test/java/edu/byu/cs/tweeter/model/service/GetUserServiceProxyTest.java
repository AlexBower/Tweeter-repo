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
import edu.byu.cs.tweeter.model.service.request.GetUserRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserResponse;

public class GetUserServiceProxyTest extends TestWithAuthToken {

    private GetUserRequest validRequest;
    private GetUserRequest invalidRequest;

    private GetUserResponse successResponse;
    private GetUserResponse failureResponse;

    private GetUserServiceProxy mGetUserServiceProxySpy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User resultUser = new User("FirstName", "LastName", "@Test",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup request objects to use in the tests
        validRequest = new GetUserRequest("@Test", authToken);
        invalidRequest = new GetUserRequest( null, authToken);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new GetUserResponse(resultUser);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getUser(validRequest, GetUserServiceProxy.URL_PATH)).thenReturn(successResponse);

        failureResponse = new GetUserResponse("An exception occurred");
        Mockito.when(mockServerFacade.getUser(invalidRequest, GetUserServiceProxy.URL_PATH)).thenReturn(failureResponse);

        mGetUserServiceProxySpy = Mockito.spy(new GetUserServiceProxy());
        Mockito.when(mGetUserServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testGetUser_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        GetUserResponse response = mGetUserServiceProxySpy.getUser(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetUser_validRequest_loadsProfileImage() throws IOException, TweeterRemoteException {
        GetUserResponse response = mGetUserServiceProxySpy.getUser(validRequest);

        Assertions.assertNotNull(response.getUser().getImageBytes());
    }

    @Test
    public void testGetUser_invalidRequest_returnsNoUser() throws IOException, TweeterRemoteException {
        GetUserResponse response = mGetUserServiceProxySpy.getUser(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
