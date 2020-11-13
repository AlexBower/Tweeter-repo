package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;

public interface UnfollowService {
    public UnfollowResponse unfollow(UnfollowRequest request) throws IOException, TweeterRemoteException;
}
