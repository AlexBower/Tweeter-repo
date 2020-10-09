package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.FollowService;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;

public class FollowPresenter {

    private final View view;

    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    public FollowPresenter(View view) {
        this.view = view;
    }

    public FollowResponse follow(FollowRequest request) throws IOException {
        FollowService followService = getFollowService();
        return followService.follow(request);
    }

    FollowService getFollowService() { return new FollowService(); }
}