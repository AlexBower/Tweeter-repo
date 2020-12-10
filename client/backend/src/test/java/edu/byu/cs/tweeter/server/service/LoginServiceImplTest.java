package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.server.TestWithAuthToken;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class LoginServiceImplTest extends TestWithAuthToken {

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private final User user = new User("Testing", "User", MALE_IMAGE_URL);

    private LoginRequest request;
    private LoginResponse expectedResponse;
    private UserDAO mockUserDAO;
    private LoginServiceImpl loginServiceImplSpy;

    private AuthTokenDAO mockAuthTokenDAO;

    @BeforeEach
    public void setup() {
        // Setup a request object to use in the tests
        request = new LoginRequest("FirstName", "password");

        expectedResponse = new LoginResponse(user, authToken);
        mockUserDAO = Mockito.mock(UserDAO.class);
        Mockito.when(mockUserDAO.login(request.getUsername(), request.getPassword())).thenReturn(expectedResponse.getUser());

        mockAuthTokenDAO = Mockito.mock(AuthTokenDAO.class);
        Mockito.when(mockAuthTokenDAO.createAuthToken(request.getUsername())).thenReturn(authToken);

        loginServiceImplSpy = Mockito.spy(LoginServiceImpl.class);
        Mockito.when(loginServiceImplSpy.getUserDAO()).thenReturn(mockUserDAO);
        Mockito.when(loginServiceImplSpy.getAuthTokenDAO()).thenReturn(mockAuthTokenDAO);
    }

    @Test
    public void testLogin_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LoginResponse response = loginServiceImplSpy.login(request);
        Assertions.assertEquals(expectedResponse.getUser(), response.getUser());
        Assertions.assertEquals(expectedResponse.getAuthToken(), response.getAuthToken());
    }

    @Test
    public void testLogin_invalidRequest_throwsError() {
        LoginRequest invalidRequest = new LoginRequest(null, null);

        String failureResponse = "BadRequest: " + "No username and/or password";
        try {
            Assertions.assertThrows(RuntimeException.class
                    , (Executable) loginServiceImplSpy.login(invalidRequest)
                    , failureResponse);
        } catch (Exception e) {
            Assertions.assertEquals(failureResponse, e.getMessage());
        }
    }
}