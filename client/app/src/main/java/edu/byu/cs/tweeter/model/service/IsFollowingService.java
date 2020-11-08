package edu.byu.cs.tweeter.model.service;

import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;

public class IsFollowingService {

    public IsFollowingResponse isFollowing(IsFollowingRequest request) {
        ServerFacade serverFacade = getServerFacade();
        return serverFacade.isFollowing(request);
    }

    public ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}