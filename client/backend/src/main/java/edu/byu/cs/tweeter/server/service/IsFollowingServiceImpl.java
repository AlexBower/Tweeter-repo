package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.IsFollowingService;
import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

public class IsFollowingServiceImpl implements IsFollowingService {
    @Override
    public IsFollowingResponse isFollowing(IsFollowingRequest request) {
        if (request.getCurrentUser() == null || request.getOtherUser() == null) {
            throw new RuntimeException("BadRequest: " + "No user");
        }
        return getFollowDAO().isFollowing(request);
    }

    FollowDAO getFollowDAO() {
        return new FollowDAO();
    }
}
