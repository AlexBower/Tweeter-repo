package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.FollowCountService;
import edu.byu.cs.tweeter.model.service.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;

abstract public class FollowCountPresenter {

    public FollowCountResponse getFollowCount(FollowCountRequest request) throws IOException {
        FollowCountService followCountService = getFollowCountService();
        return followCountService.getFollowCount(request);
    }

    FollowCountService getFollowCountService() { return new FollowCountService(); }
}
