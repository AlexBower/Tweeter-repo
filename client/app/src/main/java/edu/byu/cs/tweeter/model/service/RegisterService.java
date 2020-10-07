package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.util.ByteArrayUtils;

public class RegisterService {

    public RegisterResponse register(RegisterRequest request) throws IOException {
        ServerFacade serverFacade = getServerFacade();
        RegisterResponse registerResponse = serverFacade.register(request);

        if(registerResponse.isSuccess()) {
            loadImage(registerResponse.getUser());
        }

        return registerResponse;
    }

    private void loadImage(User user) throws IOException {
        byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
        user.setImageBytes(bytes);
    }

    ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
