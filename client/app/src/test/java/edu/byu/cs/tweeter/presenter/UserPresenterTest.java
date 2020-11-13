package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.TestWithAuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FollowServiceProxy;
import edu.byu.cs.tweeter.model.service.IsFollowingServiceProxy;
import edu.byu.cs.tweeter.model.service.UnfollowServiceProxy;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;

public class UserPresenterTest extends TestWithAuthToken {

    private IsFollowingRequest isFollowingRequest;
    private IsFollowingResponse isFollowingResponse;
    private IsFollowingServiceProxy mMockIsFollowingServiceProxy;

    private FollowRequest followRequest;
    private FollowResponse followResponse;
    private FollowServiceProxy mMockFollowServiceProxy;

    private UnfollowRequest unfollowRequest;
    private UnfollowResponse unfollowResponse;
    private UnfollowServiceProxy mMockUnfollowServiceProxy;

    private UserPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User user = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User otherUser = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        isFollowingRequest = new IsFollowingRequest(user, otherUser, authToken);
        isFollowingResponse = new IsFollowingResponse(true);
        mMockIsFollowingServiceProxy = Mockito.mock(IsFollowingServiceProxy.class);
        Mockito.when(mMockIsFollowingServiceProxy.isFollowing(isFollowingRequest)).thenReturn(isFollowingResponse);

        followRequest = new FollowRequest(user, otherUser, authToken);
        followResponse = new FollowResponse(true);
        mMockFollowServiceProxy = Mockito.mock(FollowServiceProxy.class);
        Mockito.when(mMockFollowServiceProxy.follow(followRequest)).thenReturn(followResponse);

        unfollowRequest = new UnfollowRequest(user, otherUser, authToken);
        unfollowResponse = new UnfollowResponse(true);
        mMockUnfollowServiceProxy = Mockito.mock(UnfollowServiceProxy.class);
        Mockito.when(mMockUnfollowServiceProxy.unfollow(unfollowRequest)).thenReturn(unfollowResponse);

        presenter = Mockito.spy(new UserPresenter(new UserPresenter.View() {}));
        Mockito.when(presenter.getIsFollowingService()).thenReturn(mMockIsFollowingServiceProxy);
        Mockito.when(presenter.getFollowService()).thenReturn(mMockFollowServiceProxy);
        Mockito.when(presenter.getUnfollowService()).thenReturn(mMockUnfollowServiceProxy);
    }

    @Test
    public void testIsFollowing_returnsServiceResult() throws IOException, TweeterRemoteException {
        // Assert that the presenter returns the same response as the service
        Assertions.assertEquals(isFollowingResponse, presenter.isFollowing(isFollowingRequest));
    }

    @Test
    public void testFollow_returnsServiceResult() throws IOException, TweeterRemoteException {
        // Assert that the presenter returns the same response as the service
        Assertions.assertEquals(followResponse, presenter.follow(followRequest));
    }

    @Test
    public void testUnfollow_returnsServiceResult() throws IOException, TweeterRemoteException {
        // Assert that the presenter returns the same response as the service
        Assertions.assertEquals(unfollowResponse, presenter.unfollow(unfollowRequest));
    }
}