package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

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

public class UserPresenter extends FollowCountPresenter{

    private final View view;

    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    public UserPresenter(View view) {
        super(new FollowCountPresenter.View() {
        });
        this.view = view;
    }

    public IsFollowingResponse isFollowing(IsFollowingRequest request) throws IOException, TweeterRemoteException {
        IsFollowingServiceProxy isFollowingService = getIsFollowingService();
        return isFollowingService.isFollowing(request);
    }

    public IsFollowingServiceProxy getIsFollowingService() { return new IsFollowingServiceProxy(); }

    public FollowResponse follow(FollowRequest request) throws IOException, TweeterRemoteException {
        FollowServiceProxy followService = getFollowService();
        return followService.follow(request);
    }

    public FollowServiceProxy getFollowService() { return new FollowServiceProxy(); }

    public UnfollowResponse unfollow(UnfollowRequest request) throws IOException, TweeterRemoteException {
        UnfollowServiceProxy unfollowService = getUnfollowService();
        return unfollowService.unfollow(request);
    }

    public UnfollowServiceProxy getUnfollowService() { return new UnfollowServiceProxy(); }
}
