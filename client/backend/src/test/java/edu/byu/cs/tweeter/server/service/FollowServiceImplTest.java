package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.server.TestWithAuthToken;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

public class FollowServiceImplTest extends TestWithAuthToken {

    private FollowRequest request;
    private FollowResponse expectedResponse;
    private FollowDAO mockFollowDAO;
    private FollowServiceImpl followServiceImplSpy;

    private AuthTokenDAO mockAuthTokenDAO;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", null);

        User otherUser = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup a request object to use in the tests
        request = new FollowRequest(currentUser, otherUser, authToken);

        // Setup a mock FollowDAO that will return known responses
        expectedResponse = new FollowResponse(true);
        mockFollowDAO = Mockito.mock(FollowDAO.class);
        Mockito.when(mockFollowDAO.follow(request)).thenReturn(expectedResponse);

        mockAuthTokenDAO = Mockito.mock(AuthTokenDAO.class);
        Mockito.when(mockAuthTokenDAO.checkAuthToken(
                request.getCurrentUser().getAlias(), request.getAuthToken().getToken())).thenReturn(true);

        followServiceImplSpy = Mockito.spy(FollowServiceImpl.class);
        Mockito.when(followServiceImplSpy.getFollowDAO()).thenReturn(mockFollowDAO);
        Mockito.when(followServiceImplSpy.getAuthTokenDAO()).thenReturn(mockAuthTokenDAO);
    }

    @Test
    public void testGetFollow_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowResponse response = followServiceImplSpy.follow(request);
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    public void testGetFollow_invalidRequest_throwsError() {
        FollowRequest invalidRequest = new FollowRequest(null, null, authToken);

        String failureResponse = "BadRequest: " + "No user";
        try {
            Assertions.assertThrows(RuntimeException.class
                    , (Executable) followServiceImplSpy.follow(invalidRequest)
                    , failureResponse);
        } catch (Exception e) {
            Assertions.assertEquals(failureResponse, e.getMessage());
        }
    }
}