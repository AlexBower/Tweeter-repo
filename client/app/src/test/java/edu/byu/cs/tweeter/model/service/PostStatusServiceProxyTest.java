package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDateTime;

import edu.byu.cs.tweeter.TestWithAuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.service.response.PostStatusResponse;

public class PostStatusServiceProxyTest extends TestWithAuthToken {

    private PostStatusRequest validRequest;
    private PostStatusRequest invalidRequest;

    private PostStatusResponse successResponse;
    private PostStatusResponse failureResponse;

    private PostStatusServiceProxy mPostStatusServiceProxySpy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User user = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        Status status = new Status("Test Message", TimeFormatter.format(LocalDateTime.now()), user);

        // Setup request objects to use in the tests
        validRequest = new PostStatusRequest(status, authToken);
        invalidRequest = new PostStatusRequest(null, authToken);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new PostStatusResponse(true);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.postStatus(validRequest, PostStatusServiceProxy.URL_PATH)).thenReturn(successResponse);

        failureResponse = new PostStatusResponse(false);
        Mockito.when(mockServerFacade.postStatus(invalidRequest, PostStatusServiceProxy.URL_PATH)).thenReturn(failureResponse);

        // Create a service instance and wrap it with a spy that will use the mock service
        mPostStatusServiceProxySpy = Mockito.spy(new PostStatusServiceProxy());
        Mockito.when(mPostStatusServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testPostStatus_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        PostStatusResponse response = mPostStatusServiceProxySpy.postStatus(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testRegister_invalidRequest_returnsFalse() throws IOException, TweeterRemoteException {
        PostStatusResponse response = mPostStatusServiceProxySpy.postStatus(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}