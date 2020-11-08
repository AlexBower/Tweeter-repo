package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class LogoutService {

    public LogoutResponse logout(LogoutRequest request) {
        ServerFacade serverFacade = getServerFacade();
        return serverFacade.logout(request);
    }

    public ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
