package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.GetUserRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserResponse;

public class GetUserServiceTest {

    private GetUserRequest validRequest;
    private GetUserRequest invalidRequest;

    private GetUserResponse successResponse;
    private GetUserResponse failureResponse;

    private GetUserService getUserServiceSpy;

    @BeforeEach
    public void setup() {
        User resultUser = new User("FirstName", "LastName", "@Test",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup request objects to use in the tests
        validRequest = new GetUserRequest("@Test");
        invalidRequest = new GetUserRequest( null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new GetUserResponse(resultUser);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getUser(validRequest)).thenReturn(successResponse);

        failureResponse = new GetUserResponse("An exception occurred");
        Mockito.when(mockServerFacade.getUser(invalidRequest)).thenReturn(failureResponse);

        getUserServiceSpy = Mockito.spy(new GetUserService());
        Mockito.when(getUserServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testGetUser_validRequest_correctResponse() throws IOException {
        GetUserResponse response = getUserServiceSpy.getUser(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetUser_validRequest_loadsProfileImage() throws IOException {
        GetUserResponse response = getUserServiceSpy.getUser(validRequest);

        Assertions.assertNotNull(response.getUser().getImageBytes());
    }

    @Test
    public void testGetUser_invalidRequest_returnsNoUser() throws IOException {
        GetUserResponse response = getUserServiceSpy.getUser(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
