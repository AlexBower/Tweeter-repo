package edu.byu.cs.tweeter.presenter;

import edu.byu.cs.tweeter.model.service.FollowService;
import edu.byu.cs.tweeter.model.service.IsFollowingService;
import edu.byu.cs.tweeter.model.service.UnfollowService;
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

    public IsFollowingResponse isFollowing(IsFollowingRequest request) {
        IsFollowingService isFollowingService = getIsFollowingService();
        return isFollowingService.isFollowing(request);
    }

    IsFollowingService getIsFollowingService() { return new IsFollowingService(); }

    public FollowResponse follow(FollowRequest request) {
        FollowService followService = getFollowService();
        return followService.follow(request);
    }

    FollowService getFollowService() { return new FollowService(); }

    public UnfollowResponse unfollow(UnfollowRequest request) {
        UnfollowService unfollowService = getUnfollowService();
        return unfollowService.unfollow(request);
    }

    UnfollowService getUnfollowService() { return new UnfollowService(); }
}
