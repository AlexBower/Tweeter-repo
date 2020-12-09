package edu.byu.cs.tweeter.server.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.StatusService;
import edu.byu.cs.tweeter.model.service.request.StatusRequest;
import edu.byu.cs.tweeter.model.service.response.StatusResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;

public class StatusServiceImpl implements StatusService {
    @Override
    public StatusResponse getStory(StatusRequest request) {
        if (request.getUser() == null) {
            throw new RuntimeException("BadRequest: " + "No user");
        }
        return getStoryDAO().getStory(request);
    }

    @Override
    public StatusResponse getFeed(StatusRequest request) {
        if (request.getUser() == null) {
            throw new RuntimeException("BadRequest: " + "No user");
        }

        if (!getAuthTokenDAO().checkAuthToken(request.getUser().getAlias(), request.getAuthToken().getToken())) {
            throw new RuntimeException("BadRequest: Invalid or Expired AuthToken");
        }

        return getFeedDAO().getFeed(request);
    }

    StoryDAO getStoryDAO() {
        return new StoryDAO();
    }
    FeedDAO getFeedDAO() {
        return new FeedDAO();
    }
    AuthTokenDAO getAuthTokenDAO() {
        return new AuthTokenDAO();
    }
}
