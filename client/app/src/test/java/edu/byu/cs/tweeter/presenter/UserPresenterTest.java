package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.FollowService;
import edu.byu.cs.tweeter.model.service.IsFollowingService;
import edu.byu.cs.tweeter.model.service.UnfollowService;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;

public class UserPresenterTest {

    private IsFollowingRequest isFollowingRequest;
    private IsFollowingResponse isFollowingResponse;
    private IsFollowingService mockIsFollowingService;

    private FollowRequest followRequest;
    private FollowResponse followResponse;
    private FollowService mockFollowService;

    private UnfollowRequest unfollowRequest;
    private UnfollowResponse unfollowResponse;
    private UnfollowService mockUnfollowService;

    private UserPresenter presenter;

    @BeforeEach
    public void setup() throws IOException {
        User user = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User otherUser = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        isFollowingRequest = new IsFollowingRequest(user, otherUser);
        isFollowingResponse = new IsFollowingResponse(true);
        mockIsFollowingService = Mockito.mock(IsFollowingService.class);
        Mockito.when(mockIsFollowingService.isFollowing(isFollowingRequest)).thenReturn(isFollowingResponse);

        followRequest = new FollowRequest(user, otherUser);
        followResponse = new FollowResponse(true);
        mockFollowService = Mockito.mock(FollowService.class);
        Mockito.when(mockFollowService.follow(followRequest)).thenReturn(followResponse);

        unfollowRequest = new UnfollowRequest(user, otherUser);
        unfollowResponse = new UnfollowResponse(true);
        mockUnfollowService = Mockito.mock(UnfollowService.class);
        Mockito.when(mockUnfollowService.unfollow(unfollowRequest)).thenReturn(unfollowResponse);

        presenter = Mockito.spy(new UserPresenter(new UserPresenter.View() {}));
        Mockito.when(presenter.getIsFollowingService()).thenReturn(mockIsFollowingService);
        Mockito.when(presenter.getFollowService()).thenReturn(mockFollowService);
        Mockito.when(presenter.getUnfollowService()).thenReturn(mockUnfollowService);
    }

    @Test
    public void testIsFollowing_returnsServiceResult() {
        // Assert that the presenter returns the same response as the service
        Assertions.assertEquals(isFollowingResponse, presenter.isFollowing(isFollowingRequest));
    }

    @Test
    public void testFollow_returnsServiceResult() {
        // Assert that the presenter returns the same response as the service
        Assertions.assertEquals(followResponse, presenter.follow(followRequest));
    }

    @Test
    public void testUnfollow_returnsServiceResult() {
        // Assert that the presenter returns the same response as the service
        Assertions.assertEquals(unfollowResponse, presenter.unfollow(unfollowRequest));
    }
}