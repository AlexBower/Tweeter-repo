package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;
import edu.byu.cs.tweeter.server.TestWithAuthToken;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

public class IsFollowingServiceImplTest extends TestWithAuthToken {

    private IsFollowingRequest request;
    private IsFollowingResponse expectedResponse;
    private FollowDAO mockFollowDAO;
    private IsFollowingServiceImpl isFollowingServiceImplSpy;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", null);

        User otherUser = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup a request object to use in the tests
        request = new IsFollowingRequest(currentUser, otherUser, authToken);

        // Setup a mock FollowDAO that will return known responses
        expectedResponse = new IsFollowingResponse(true);
        mockFollowDAO = Mockito.mock(FollowDAO.class);
        Mockito.when(mockFollowDAO.isFollowing(request)).thenReturn(expectedResponse);

        isFollowingServiceImplSpy = Mockito.spy(IsFollowingServiceImpl.class);
        Mockito.when(isFollowingServiceImplSpy.getFollowDAO()).thenReturn(mockFollowDAO);
    }

    @Test
    public void testIsFollowing_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        IsFollowingResponse response = isFollowingServiceImplSpy.isFollowing(request);
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    public void testIsFollowing_invalidRequest_throwsError() {
        IsFollowingRequest invalidRequest = new IsFollowingRequest(null, null, authToken);

        String failureResponse = "BadRequest: " + "No user";
        try {
            Assertions.assertThrows(RuntimeException.class
                    , (Executable) isFollowingServiceImplSpy.isFollowing(invalidRequest)
                    , failureResponse);
        } catch (Exception e) {
            Assertions.assertEquals(failureResponse, e.getMessage());
        }
    }
}