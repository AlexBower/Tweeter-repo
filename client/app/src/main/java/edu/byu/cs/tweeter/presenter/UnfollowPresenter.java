package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.UnfollowService;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;

public class UnfollowPresenter {

    private final View view;

    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    public UnfollowPresenter(View view) {
        this.view = view;
    }

    public UnfollowResponse unfollow(UnfollowRequest request) {
        UnfollowService unfollowService = getUnfollowService();
        return unfollowService.unfollow(request);
    }

    UnfollowService getUnfollowService() { return new UnfollowService(); }
}