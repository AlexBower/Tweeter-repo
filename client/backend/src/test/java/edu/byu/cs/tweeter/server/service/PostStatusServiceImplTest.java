package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDateTime;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.service.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.service.response.StatusResponse;
import edu.byu.cs.tweeter.server.TestWithAuthToken;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;

public class PostStatusServiceImplTest extends TestWithAuthToken {

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

    private final User user = new User("Testing", "User", MALE_IMAGE_URL);

    private final Status status = new Status("Hey, 1 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);

    private PostStatusRequest request;
    private PostStatusResponse expectedResponse;
    private FeedDAO mockFeedDAO;
    private StoryDAO mockStoryDAO;
    private PostStatusServiceImpl postStatusServiceImplSpy;

    @BeforeEach
    public void setup() {

        // Setup a request object to use in the tests
        request = new PostStatusRequest(status, authToken);

        expectedResponse = new PostStatusResponse(true);
        mockFeedDAO = Mockito.mock(FeedDAO.class);
        Mockito.when(mockFeedDAO.postStatus(request)).thenReturn(true);
        mockStoryDAO = Mockito.mock(StoryDAO.class);
        Mockito.when(mockStoryDAO.postStatus(request)).thenReturn(true);

        postStatusServiceImplSpy = Mockito.spy(PostStatusServiceImpl.class);
        Mockito.when(postStatusServiceImplSpy.getFeedDAO()).thenReturn(mockFeedDAO);
        Mockito.when(postStatusServiceImplSpy.getStoryDAO()).thenReturn(mockStoryDAO);
    }

    @Test
    public void testPostStatus_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        PostStatusResponse response = postStatusServiceImplSpy.postStatus(request);
        Assertions.assertEquals(expectedResponse.isSuccess(), response.isSuccess());
    }

    @Test
    public void testPostStatus_invalidRequest_throwsError() {
        PostStatusRequest invalidRequest = new PostStatusRequest(null, null);

        String failureResponse = "BadRequest: " + "No status";
        try {
            Assertions.assertThrows(RuntimeException.class
                    , (Executable) postStatusServiceImplSpy.postStatus(invalidRequest)
                    , failureResponse);
        } catch (Exception e) {
            Assertions.assertEquals(failureResponse, e.getMessage());
        }
    }
}