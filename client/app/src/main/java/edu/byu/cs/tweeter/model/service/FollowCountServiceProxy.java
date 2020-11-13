package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;

public class FollowCountServiceProxy implements FollowCountService {

    public static final String URL_PATH = "/getfollowcount";

    @Override
    public FollowCountResponse getFollowCount(FollowCountRequest request) throws IOException, TweeterRemoteException {
        ServerFacade serverFacade = getServerFacade();
        return serverFacade.getFollowCount(request, URL_PATH);
    }

    public ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
