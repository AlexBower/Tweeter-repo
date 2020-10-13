package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;

public class FollowServiceTest {

    private FollowRequest validRequest;
    private FollowRequest invalidRequest;

    private FollowResponse successResponse;
    private FollowResponse failureResponse;

    private FollowService followServiceSpy;

    @BeforeEach
    public void setup() {
        User user = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User otherUser = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup request objects to use in the tests
        validRequest = new FollowRequest(user, otherUser);
        invalidRequest = new FollowRequest(null, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new FollowResponse(true);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.follow(validRequest)).thenReturn(successResponse);

        failureResponse = new FollowResponse(false);
        Mockito.when(mockServerFacade.follow(invalidRequest)).thenReturn(failureResponse);

        // Create a service instance and wrap it with a spy that will use the mock service
        followServiceSpy = Mockito.spy(new FollowService());
        Mockito.when(followServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testFollow_validRequest_correctResponse() {
        FollowResponse response = followServiceSpy.follow(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testFollow_invalidRequest_returnsFalse() {
        FollowResponse response = followServiceSpy.follow(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}