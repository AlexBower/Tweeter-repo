package edu.byu.cs.tweeter.server.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FollowCountService;
import edu.byu.cs.tweeter.model.service.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;
import edu.byu.cs.tweeter.server.dao.FollowerDAO;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;

public class FollowCountServiceImpl implements FollowCountService {

    @Override
    public FollowCountResponse getFollowCount(FollowCountRequest request) {
        return new FollowCountResponse(
                getFollowerDAO().getFollowerCount(request.getUser())
                , getFollowingDAO().getFolloweeCount(request.getUser()));
    }

    FollowerDAO getFollowerDAO() {
        return new FollowerDAO();
    }

    FollowingDAO getFollowingDAO() { return new FollowingDAO(); }
}
