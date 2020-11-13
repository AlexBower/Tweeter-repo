package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;

public interface FollowCountService {

    public FollowCountResponse getFollowCount(FollowCountRequest request) throws IOException, TweeterRemoteException;

}
