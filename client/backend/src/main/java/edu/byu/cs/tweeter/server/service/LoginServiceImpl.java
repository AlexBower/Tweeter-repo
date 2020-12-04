package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.LoginService;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class LoginServiceImpl implements LoginService {

    @Override
    public LoginResponse login(LoginRequest request) {
        if (request.getUsername() == null || request.getPassword() == null) {
            throw new RuntimeException("BadRequest: " + "No username and/or password");
        }
        User user = getUserDAO().login(request.getUsername(), request.getPassword());
        AuthToken authToken = getAuthTokenDAO().createAuthToken(request.getUsername());
        return new LoginResponse(user, authToken);
    }

    UserDAO getUserDAO() {
        return new UserDAO();
    }
    AuthTokenDAO getAuthTokenDAO() {
        return new AuthTokenDAO();
    }
}
