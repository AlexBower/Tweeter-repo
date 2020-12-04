package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.GetUserService;
import edu.byu.cs.tweeter.model.service.request.GetUserRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class GetUserServiceImpl implements GetUserService {
    @Override
    public GetUserResponse getUser(GetUserRequest request) {
        if (request.getUsername() == null) {
            throw new RuntimeException("BadRequest: " + "No Username");
        }


        return getUserDAO().getUser(request.getUsername());
    }

    UserDAO getUserDAO() {
        return new UserDAO();
    }
}
