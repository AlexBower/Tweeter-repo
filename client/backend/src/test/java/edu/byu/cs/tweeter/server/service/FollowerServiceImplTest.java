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
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.server.TestWithAuthToken;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

public class FollowerServiceImplTest extends TestWithAuthToken {

    private FollowerRequest request;
    private FollowerResponse expectedResponse;
    private FollowDAO mockFollowDAO;
    private FollowerServiceImpl followerServiceImplSpy;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        // Setup a request object to use in the tests
        request = new FollowerRequest(currentUser, 3, null, authToken);

        // Setup a mock FollowDAO that will return known responses
        expectedResponse = new FollowerResponse(Arrays.asList(resultUser1, resultUser2, resultUser3), false);
        mockFollowDAO = Mockito.mock(FollowDAO.class);
        Mockito.when(mockFollowDAO.getFollowers(request)).thenReturn(expectedResponse);

        followerServiceImplSpy = Mockito.spy(FollowerServiceImpl.class);
        Mockito.when(followerServiceImplSpy.getFollowDAO()).thenReturn(mockFollowDAO);
    }

    @Test
    public void testGetFollowers_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowerResponse response = followerServiceImplSpy.getFollowers(request);
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    public void testGetFollowers_invalidRequest_throwsError() {
        FollowerRequest invalidRequest = new FollowerRequest(null, 0, null, authToken);

        String failureResponse = "BadRequest: " + "No user";

        try {
            Assertions.assertThrows(RuntimeException.class
                    , (Executable) followerServiceImplSpy.getFollowers(invalidRequest)
                    , failureResponse);
        } catch (Exception e) {
            Assertions.assertEquals(failureResponse, e.getMessage());
        }
    }
}
