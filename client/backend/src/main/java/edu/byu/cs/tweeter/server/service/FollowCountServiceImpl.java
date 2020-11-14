package edu.byu.cs.tweeter.server.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FollowCountService;
import edu.byu.cs.tweeter.model.service.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

public class FollowCountServiceImpl implements FollowCountService {

    @Override
    public FollowCountResponse getFollowCount(FollowCountRequest request) {
        FollowDAO followDAO = getFollowDAO();
        return new FollowCountResponse(
                followDAO.getFollowerCount(request.getUser())
                , followDAO.getFolloweeCount(request.getUser()));
    }

    FollowDAO getFollowDAO() {
        return new FollowDAO();
    }
}
