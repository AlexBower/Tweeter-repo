package edu.byu.cs.tweeter.model.service;

import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;

public class FollowService {

    public FollowResponse follow(FollowRequest request) {
        ServerFacade serverFacade = getServerFacade();
        return serverFacade.follow(request);
    }

    public ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}