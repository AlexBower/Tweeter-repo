package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.UnfollowService;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

public class UnfollowServiceImpl implements UnfollowService {
    @Override
    public UnfollowResponse unfollow(UnfollowRequest request) {
        if (request.getCurrentUser() == null || request.getOtherUser() == null) {
            throw new RuntimeException("BadRequest: " + "No user");
        }

        if (!getAuthTokenDAO().checkAuthToken(request.getCurrentUser().getAlias(), request.getAuthToken().getToken())) {
            throw new RuntimeException("BadRequest: Invalid or Expired AuthToken");
        }

        return getFollowDAO().unfollow(request);
    }

    FollowDAO getFollowDAO() {
        return new FollowDAO();
    }
    AuthTokenDAO getAuthTokenDAO() {
        return new AuthTokenDAO();
    }
}
