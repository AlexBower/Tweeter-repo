package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.FollowService;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

public class FollowServiceImpl implements FollowService {
    @Override
    public FollowResponse follow(FollowRequest request) {
        if (request.getCurrentUser() == null || request.getOtherUser() == null) {
            throw new RuntimeException("BadRequest: " + "No user");
        }
        return getFollowDAO().follow(request);
    }

    FollowDAO getFollowDAO() {
        return new FollowDAO();
    }
}
