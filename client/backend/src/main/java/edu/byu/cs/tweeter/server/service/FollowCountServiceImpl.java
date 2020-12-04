package edu.byu.cs.tweeter.server.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FollowCountService;
import edu.byu.cs.tweeter.model.service.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class FollowCountServiceImpl implements FollowCountService {

    @Override
    public FollowCountResponse getFollowCount(FollowCountRequest request) {
        if (request.getUser() == null) {
            throw new RuntimeException("BadRequest: " + "No user");
        }

        UserDAO userDAO = getUserDAO();
        return new FollowCountResponse(
                userDAO.getFollowerCount(request.getUser().getAlias()),
                userDAO.getFolloweeCount(request.getUser().getAlias()));
    }

    UserDAO getUserDAO() {
        return new UserDAO();
    }
}
