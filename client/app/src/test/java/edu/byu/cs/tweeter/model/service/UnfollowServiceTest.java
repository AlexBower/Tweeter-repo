package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;

public class UnfollowServiceTest {

    private UnfollowRequest validRequest;
    private UnfollowRequest invalidRequest;

    private UnfollowResponse successResponse;
    private UnfollowResponse failureResponse;

    private UnfollowService unfollowServiceSpy;

    @BeforeEach
    public void setup() {
        User user = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User otherUser = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup request objects to use in the tests
        validRequest = new UnfollowRequest(user, otherUser);
        invalidRequest = new UnfollowRequest(null, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new UnfollowResponse(true);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.unfollow(validRequest)).thenReturn(successResponse);

        failureResponse = new UnfollowResponse(false);
        Mockito.when(mockServerFacade.unfollow(invalidRequest)).thenReturn(failureResponse);

        // Create a service instance and wrap it with a spy that will use the mock service
        unfollowServiceSpy = Mockito.spy(new UnfollowService());
        Mockito.when(unfollowServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testUnfollow_validRequest_correctResponse() {
        UnfollowResponse response = unfollowServiceSpy.unfollow(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testUnfollow_invalidRequest_returnsFalse() {
        UnfollowResponse response = unfollowServiceSpy.unfollow(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}