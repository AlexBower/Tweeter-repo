package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;

public class UserDAO {

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private final User testy_boi = new User("Testing", "User", MALE_IMAGE_URL);

    private AuthToken test_authToken = new AuthToken("brilliantly_secure_token");

    public LoginResponse login(LoginRequest request) {
        return new LoginResponse(testy_boi, test_authToken);
    }

}
