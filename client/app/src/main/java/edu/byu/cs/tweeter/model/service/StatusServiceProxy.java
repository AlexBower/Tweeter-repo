package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.StatusRequest;
import edu.byu.cs.tweeter.model.service.response.StatusResponse;
import edu.byu.cs.tweeter.util.ByteArrayUtils;

public class StatusServiceProxy implements StatusService {

    public static final String GET_STORY_URL_PATH = "/getstory";
    public static final String GET_FEED_URL_PATH = "/getfeed";

    public StatusResponse getStory(StatusRequest request) throws IOException, TweeterRemoteException {
        StatusResponse response = getServerFacade().getStory(request, GET_STORY_URL_PATH);

        if(response.isSuccess()) {
            loadImages(response);
        }

        return response;
    }

    public StatusResponse getFeed(StatusRequest request) throws IOException, TweeterRemoteException {
        StatusResponse response = getServerFacade().getFeed(request, GET_FEED_URL_PATH);

        if(response.isSuccess()) {
            loadImages(response);
        }

        return response;
    }

    private void loadImages(StatusResponse response) throws IOException {
        for(Status status : response.getStatuses()) {
            if (status.getUser().getImageBytes() != null) {
                continue;
            }
            byte [] bytes = ByteArrayUtils.bytesFromUrl(status.getUser().getImageUrl());
            status.getUser().setImageBytes(bytes);
        }
    }

    public ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
