package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.LogoutService;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.server.TestWithAuthToken;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;

public class LogoutServiceImplTest extends TestWithAuthToken {

    private LogoutRequest request;
    private LogoutResponse expectedResponse;
    private AuthTokenDAO mockAuthTokenDAO;
    private LogoutServiceImpl logoutServiceImplSpy;

    @BeforeEach
    public void setup() {
        User user = new User("FirstName", "LastName", null);

        // Setup a request object to use in the tests
        request = new LogoutRequest(user, authToken);

        expectedResponse = new LogoutResponse(true);
        mockAuthTokenDAO = Mockito.mock(AuthTokenDAO.class);
        Mockito.when(mockAuthTokenDAO.logout(request)).thenReturn(expectedResponse);

        logoutServiceImplSpy = Mockito.spy(LogoutServiceImpl.class);
        Mockito.when(logoutServiceImplSpy.getAuthTokenDAO()).thenReturn(mockAuthTokenDAO);
    }

    @Test
    public void testLogout_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LogoutResponse response = logoutServiceImplSpy.logout(request);
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    public void testLogout_invalidRequest_throwsError() {
        LogoutRequest invalidRequest = new LogoutRequest(null, null);

        String failureResponse = "BadRequest: " + "No user";
        try {
            Assertions.assertThrows(RuntimeException.class
                    , (Executable) logoutServiceImplSpy.logout(invalidRequest)
                    , failureResponse);
        } catch (Exception e) {
            Assertions.assertEquals(failureResponse, e.getMessage());
        }
    }
}