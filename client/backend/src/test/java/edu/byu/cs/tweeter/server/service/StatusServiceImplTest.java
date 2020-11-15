package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.StatusRequest;
import edu.byu.cs.tweeter.model.service.response.StatusResponse;
import edu.byu.cs.tweeter.server.TestWithAuthToken;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;

public class StatusServiceImplTest extends TestWithAuthToken {

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

    private final User user = new User("Testing", "User", MALE_IMAGE_URL);

    private final Status status1 = new Status("Hey, 1 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status2 = new Status("Hey, 2 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status3 = new Status("Hey, 3 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status4 = new Status("Hey, 4 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status5 = new Status("Hey, 5 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status6 = new Status("Hey, 6 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status7 = new Status("Hey, 7 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status8 = new Status("Hey, 8 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status9 = new Status("Hey, 9 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status10 = new Status("Hey, 10 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);

    private List<Status> dummyStatuses = Arrays.asList(status1, status2, status3, status4, status5, status6, status7,
            status8, status9, status10);

    private StatusRequest request;
    private StatusResponse expectedResponse;
    private FeedDAO mockFeedDAO;
    private StoryDAO mockStoryDAO;
    private StatusServiceImpl statusServiceImplSpy;

    @BeforeEach
    public void setup() {

        // Setup a request object to use in the tests
        request = new StatusRequest(user, 10, null, authToken);

        expectedResponse = new StatusResponse(dummyStatuses, true);
        mockFeedDAO = Mockito.mock(FeedDAO.class);
        Mockito.when(mockFeedDAO.getFeed(request)).thenReturn(expectedResponse);
        mockStoryDAO = Mockito.mock(StoryDAO.class);
        Mockito.when(mockStoryDAO.getStory(request)).thenReturn(expectedResponse);

        statusServiceImplSpy = Mockito.spy(StatusServiceImpl.class);
        Mockito.when(statusServiceImplSpy.getFeedDAO()).thenReturn(mockFeedDAO);
        Mockito.when(statusServiceImplSpy.getStoryDAO()).thenReturn(mockStoryDAO);
    }

    @Test
    public void testGetStory_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        StatusResponse response = statusServiceImplSpy.getStory(request);
        Assertions.assertEquals(expectedResponse.isSuccess(), response.isSuccess());
    }

    @Test
    public void testGetFeed_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        StatusResponse response = statusServiceImplSpy.getFeed(request);
        Assertions.assertEquals(expectedResponse.isSuccess(), response.isSuccess());
    }

    @Test
    public void testGetStory_invalidRequest_throwsError() {
        StatusRequest invalidRequest = new StatusRequest(null, 10, null, authToken);

        String failureResponse = "BadRequest: " + "No user";
        try {
            Assertions.assertThrows(RuntimeException.class
                    , (Executable) statusServiceImplSpy.getStory(invalidRequest)
                    , failureResponse);
        } catch (Exception e) {
            Assertions.assertEquals(failureResponse, e.getMessage());
        }
    }

    @Test
    public void testGetFeed_invalidRequest_throwsError() {
        StatusRequest invalidRequest = new StatusRequest(null, 10, null, authToken);

        String failureResponse = "BadRequest: " + "No user";
        try {
            Assertions.assertThrows(RuntimeException.class
                    , (Executable) statusServiceImplSpy.getFeed(invalidRequest)
                    , failureResponse);
        } catch (Exception e) {
            Assertions.assertEquals(failureResponse, e.getMessage());
        }
    }
}