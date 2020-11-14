package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.UnfollowService;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

public class UnfollowServiceImpl implements UnfollowService {
    @Override
    public UnfollowResponse unfollow(UnfollowRequest request) {
        return getFollowDAO().unfollow(request);
    }

    FollowDAO getFollowDAO() {
        return new FollowDAO();
    }
}
