package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Base64;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.server.TestWithAuthToken;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class RegisterServiceImplTest extends TestWithAuthToken {

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private final User user = new User("Regi", "Boi", MALE_IMAGE_URL);

    private RegisterRequest request;
    private RegisterResponse expectedResponse;
    private UserDAO mockUserDAO;
    private RegisterServiceImpl registerServiceImplSpy;

    private AuthTokenDAO mockAuthTokenDAO;

    @BeforeEach
    public void setup() {

        // Setup a request object to use in the tests
        request = new RegisterRequest("FirstName",
                "password",
                "Test",
                "Boi",
                "MacQueen");

        byte[] decoded = Base64.getDecoder().decode(request.getEncodedImageBytes().getBytes());

        expectedResponse = new RegisterResponse(user, authToken);
        mockUserDAO = Mockito.mock(UserDAO.class);
        Mockito.when(mockUserDAO.register(
                request.getUsername(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
                decoded)).thenReturn(expectedResponse.getUser());

        mockAuthTokenDAO = Mockito.mock(AuthTokenDAO.class);
        Mockito.when(mockAuthTokenDAO.createAuthToken(request.getUsername())).thenReturn(authToken);

        registerServiceImplSpy = Mockito.spy(RegisterServiceImpl.class);
        Mockito.when(registerServiceImplSpy.getUserDAO()).thenReturn(mockUserDAO);
        Mockito.when(registerServiceImplSpy.getAuthTokenDAO()).thenReturn(mockAuthTokenDAO);
    }

    @Test
    public void testRegister_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        RegisterResponse response = registerServiceImplSpy.register(request);
        Assertions.assertEquals(expectedResponse.getUser(), response.getUser());
        Assertions.assertEquals(expectedResponse.getAuthToken(), response.getAuthToken());
    }

    @Test
    public void testRegister_invalidRequest_throwsError() {
        RegisterRequest invalidRequest = new RegisterRequest(null,
                null,
                null,
                null,
                null);

        String failureResponse = "BadRequest: " + "No username and/or password";
        try {
            Assertions.assertThrows(RuntimeException.class
                    , (Executable) registerServiceImplSpy.register(invalidRequest)
                    , failureResponse);
        } catch (Exception e) {
            Assertions.assertEquals(failureResponse, e.getMessage());
        }
    }
}