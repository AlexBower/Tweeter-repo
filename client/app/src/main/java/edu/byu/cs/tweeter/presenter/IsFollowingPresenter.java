package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.IsFollowingService;
import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;

public class IsFollowingPresenter {

    private final View view;

    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    public IsFollowingPresenter(View view) {
        this.view = view;
    }

    public IsFollowingResponse isFollowing(IsFollowingRequest request) {
        IsFollowingService isFollowingService = getIsFollowingService();
        return isFollowingService.isFollowing(request);
    }

    IsFollowingService getIsFollowingService() { return new IsFollowingService(); }
}
