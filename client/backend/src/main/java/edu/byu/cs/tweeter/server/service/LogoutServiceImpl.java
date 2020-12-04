package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.LogoutService;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;

public class LogoutServiceImpl implements LogoutService {
    @Override
    public LogoutResponse logout(LogoutRequest request) {
        if (request.getUser() == null) {
            throw new RuntimeException("BadRequest: " + "No user");
        }
        return new LogoutResponse(getAuthTokenDAO()
                .deleteAuthToken(request.getUser().getAlias(), request.getAuthToken().getToken()));
    }

    AuthTokenDAO getAuthTokenDAO() {
        return new AuthTokenDAO();
    }
}
