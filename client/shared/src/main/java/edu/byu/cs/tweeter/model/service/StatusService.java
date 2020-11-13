package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.StatusRequest;
import edu.byu.cs.tweeter.model.service.response.StatusResponse;

public interface StatusService {
    public StatusResponse getStory(StatusRequest request) throws IOException, TweeterRemoteException;
    public StatusResponse getFeed(StatusRequest request) throws IOException, TweeterRemoteException;
}
