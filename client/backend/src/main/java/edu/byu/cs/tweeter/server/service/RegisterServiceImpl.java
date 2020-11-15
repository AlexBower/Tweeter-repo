package edu.byu.cs.tweeter.server.service;

import java.util.Base64;

import edu.byu.cs.tweeter.model.service.RegisterService;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class RegisterServiceImpl implements RegisterService {
    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (request.getUsername() == null || request.getPassword() == null) {
            throw new RuntimeException("BadRequest: " + "No username and/or password");
        }
        byte[] decoded = Base64.getDecoder().decode(request.getEncodedImageBytes().getBytes());
        return getUserDAO().register(request);
    }

    UserDAO getUserDAO() {
        return new UserDAO();
    }
}
