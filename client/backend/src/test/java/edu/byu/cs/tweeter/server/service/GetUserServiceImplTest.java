package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.GetUserRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserResponse;
import edu.byu.cs.tweeter.server.TestWithAuthToken;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class GetUserServiceImplTest extends TestWithAuthToken {

    private GetUserRequest request;
    private GetUserResponse expectedResponse;
    private UserDAO mockUserDAO;
    private GetUserServiceImpl getUserServiceImplSpy;

    @BeforeEach
    public void setup() {
        User responseUser = new User("FirstName", "LastName", null);

        // Setup a request object to use in the tests
        request = new GetUserRequest("FirstName", authToken);

        expectedResponse = new GetUserResponse(responseUser);
        mockUserDAO = Mockito.mock(UserDAO.class);
        Mockito.when(mockUserDAO.getUser(request)).thenReturn(expectedResponse);

        getUserServiceImplSpy = Mockito.spy(GetUserServiceImpl.class);
        Mockito.when(getUserServiceImplSpy.getUserDAO()).thenReturn(mockUserDAO);
    }

    @Test
    public void testGetUser_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        GetUserResponse response = getUserServiceImplSpy.getUser(request);
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    public void testGetUser_invalidRequest_throwsError() {
        GetUserRequest invalidRequest = new GetUserRequest(null, authToken);

        String failureResponse = "BadRequest: " + "No Username";
        try {
            Assertions.assertThrows(RuntimeException.class
                    , (Executable) getUserServiceImplSpy.getUser(invalidRequest)
                    , failureResponse);
        } catch (Exception e) {
            Assertions.assertEquals(failureResponse, e.getMessage());
        }
    }
}