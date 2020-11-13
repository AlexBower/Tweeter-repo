package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.service.response.PostStatusResponse;

public class PostStatusServiceProxy implements PostStatusService {

    public static final String URL_PATH = "/poststatus";

    public PostStatusResponse postStatus(PostStatusRequest request) throws IOException, TweeterRemoteException {
        ServerFacade serverFacade = getServerFacade();
        return serverFacade.postStatus(request, URL_PATH);
    }

    public ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}