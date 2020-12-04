package edu.byu.cs.tweeter.server.service;

import java.util.Base64;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.RegisterService;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class RegisterServiceImpl implements RegisterService {
    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (request.getUsername() == null || request.getPassword() == null) {
            throw new RuntimeException("BadRequest: " + "No username and/or password");
        }
        byte[] decoded = Base64.getDecoder().decode(request.getEncodedImageBytes().getBytes());
        User user = getUserDAO().register(
                request.getUsername(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
                decoded);
        AuthToken authToken = getAuthTokenDAO().createAuthToken(request.getUsername());
        return new RegisterResponse(user, authToken);
    }

    UserDAO getUserDAO() {
        return new UserDAO();
    }
    AuthTokenDAO getAuthTokenDAO() {
        return new AuthTokenDAO();
    }
}
