package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;
import edu.byu.cs.tweeter.server.TestWithAuthToken;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

public class FollowCountServiceImplTest extends TestWithAuthToken {

    private FollowCountRequest request;
    private FollowCountResponse expectedResponse;
    private FollowDAO mockFollowDAO;
    private FollowCountServiceImpl followCountServiceImplSpy;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", null);

        // Setup a request object to use in the tests
        request = new FollowCountRequest(currentUser, authToken);

        // Setup a mock FollowDAO that will return known responses
        expectedResponse = new FollowCountResponse(20,20);
        mockFollowDAO = Mockito.mock(FollowDAO.class);
        Mockito.when(mockFollowDAO.getFolloweeCount(request.getUser())).thenReturn(20);
        Mockito.when(mockFollowDAO.getFollowerCount(request.getUser())).thenReturn(20);

        followCountServiceImplSpy = Mockito.spy(FollowCountServiceImpl.class);
        Mockito.when(followCountServiceImplSpy.getFollowDAO()).thenReturn(mockFollowDAO);
    }

    @Test
    public void testGetFollowCount_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowCountResponse response = followCountServiceImplSpy.getFollowCount(request);
        Assertions.assertEquals(expectedResponse.getFollowersCount(), response.getFollowersCount());
        Assertions.assertEquals(expectedResponse.getFollowingCount(), response.getFollowingCount());
    }

    @Test
    public void testGetFollowCount_invalidRequest_throwsError() {
        FollowCountRequest invalidRequest = new FollowCountRequest(null, authToken);

        String failureResponse = "BadRequest: " + "No user";
        try {
            Assertions.assertThrows(RuntimeException.class
                    , (Executable) followCountServiceImplSpy.getFollowCount(invalidRequest)
                    , failureResponse);
        } catch (Exception e) {
            Assertions.assertEquals(failureResponse, e.getMessage());
        }
    }
}