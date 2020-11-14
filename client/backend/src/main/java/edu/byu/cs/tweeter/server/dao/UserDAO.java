package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.GetUserRequest;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserResponse;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

public class UserDAO {

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private final User test_boi = new User("Testing", "User", MALE_IMAGE_URL);

    private AuthToken test_authToken = new AuthToken("brilliantly_secure_token");

    public LoginResponse login(LoginRequest request) {
        return new LoginResponse(test_boi, test_authToken);
    }

    public GetUserResponse getUser(GetUserRequest request){
        return new GetUserResponse(test_boi);
    }

    public RegisterResponse register(RegisterRequest request) {
        return new RegisterResponse(test_boi, test_authToken);
    }
}
