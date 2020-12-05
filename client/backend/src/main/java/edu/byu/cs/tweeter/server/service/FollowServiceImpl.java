package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.FollowService;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

public class FollowServiceImpl implements FollowService {
    @Override
    public FollowResponse follow(FollowRequest request) {
        if (request.getCurrentUser() == null || request.getOtherUser() == null) {
            throw new RuntimeException("BadRequest: " + "No user");
        }

        if (!getAuthTokenDAO().checkAuthToken(request.getCurrentUser().getAlias(), request.getAuthToken().getToken())) {
            throw new RuntimeException("BadRequest: Invalid or Expired AuthToken");
        }

        return getFollowDAO().follow(request);
    }

    FollowDAO getFollowDAO() {
        return new FollowDAO();
    }
    AuthTokenDAO getAuthTokenDAO() {
        return new AuthTokenDAO();
    }
}
