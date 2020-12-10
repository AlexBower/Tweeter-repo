package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.TestWithAuthToken;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

public class UnfollowServiceImplTest extends TestWithAuthToken {

    private UnfollowRequest request;
    private UnfollowResponse expectedResponse;
    private FollowDAO mockFollowDAO;
    private UnfollowServiceImpl unfollowServiceImplSpy;

    private AuthTokenDAO mockAuthTokenDAO;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", null);

        User otherUser = new User("FirstName1", "LastName1",
                null);

        // Setup a request object to use in the tests
        request = new UnfollowRequest(currentUser, otherUser, authToken);

        // Setup a mock FollowDAO that will return known responses
        expectedResponse = new UnfollowResponse(true);
        mockFollowDAO = Mockito.mock(FollowDAO.class);
        Mockito.when(mockFollowDAO.unfollow(request)).thenReturn(expectedResponse);

        mockAuthTokenDAO = Mockito.mock(AuthTokenDAO.class);
        Mockito.when(mockAuthTokenDAO.checkAuthToken(
                request.getCurrentUser().getAlias(), request.getAuthToken().getToken())).thenReturn(true);

        unfollowServiceImplSpy = Mockito.spy(UnfollowServiceImpl.class);
        Mockito.when(unfollowServiceImplSpy.getFollowDAO()).thenReturn(mockFollowDAO);
        Mockito.when(unfollowServiceImplSpy.getAuthTokenDAO()).thenReturn(mockAuthTokenDAO);
    }

    @Test
    public void testUnfollow_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        UnfollowResponse response = unfollowServiceImplSpy.unfollow(request);
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    public void testUnfollow_invalidRequest_throwsError() {
        UnfollowRequest invalidRequest = new UnfollowRequest(null, null, authToken);

        String failureResponse = "BadRequest: " + "No user";
        try {
            Assertions.assertThrows(RuntimeException.class
                    , (Executable) unfollowServiceImplSpy.unfollow(invalidRequest)
                    , failureResponse);
        } catch (Exception e) {
            Assertions.assertEquals(failureResponse, e.getMessage());
        }
    }
}