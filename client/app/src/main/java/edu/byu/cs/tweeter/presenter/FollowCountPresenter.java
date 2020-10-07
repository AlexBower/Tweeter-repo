package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.FollowCountService;
import edu.byu.cs.tweeter.model.service.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;

public class FollowCountPresenter {

    private final View view;

    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    public FollowCountPresenter(View view) {
        this.view = view;
    }

    public FollowCountResponse getFollowCount(FollowCountRequest request) throws IOException {
        FollowCountService followCountService = getFollowCountService();
        return followCountService.getFollowCount(request);
    }

    FollowCountService getFollowCountService() { return new FollowCountService(); }
}
