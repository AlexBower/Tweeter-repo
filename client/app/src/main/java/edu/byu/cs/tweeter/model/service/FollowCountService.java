package edu.byu.cs.tweeter.model.service;

import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;

public class FollowCountService {

    public FollowCountResponse getFollowCount(FollowCountRequest request) {
        ServerFacade serverFacade = getServerFacade();
        return serverFacade.getFollowCount(request);
    }

    public ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
