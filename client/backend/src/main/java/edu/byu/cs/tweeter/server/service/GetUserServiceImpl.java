package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.GetUserService;
import edu.byu.cs.tweeter.model.service.request.GetUserRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class GetUserServiceImpl implements GetUserService {
    @Override
    public GetUserResponse getUser(GetUserRequest request) {
        if (request.getUsername() == null) {
            throw new RuntimeException("BadRequest: " + "No Username");
        }

        if (getAuthTokenDAO().checkAuthToken(request.getUsername(), request.getAuthToken().getToken())) {
            return new GetUserResponse(getUserDAO().getUser(request.getUsername()));
        }
        return null;
    }

    UserDAO getUserDAO() {
        return new UserDAO();
    }
    AuthTokenDAO getAuthTokenDAO() {
        return new AuthTokenDAO();
    }
}
