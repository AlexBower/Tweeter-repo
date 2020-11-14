package edu.byu.cs.tweeter.server.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.IsFollowingService;
import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

public class IsFollowingServiceImpl implements IsFollowingService {
    @Override
    public IsFollowingResponse isFollowing(IsFollowingRequest request) {
        return getFollowDAO().isFollowing(request);
    }

    FollowDAO getFollowDAO() {
        return new FollowDAO();
    }
}
