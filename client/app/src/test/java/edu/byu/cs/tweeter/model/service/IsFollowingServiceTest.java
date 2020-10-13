package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;

public class IsFollowingServiceTest {

    private IsFollowingRequest validRequest;
    private IsFollowingRequest invalidRequest;

    private IsFollowingResponse successResponse;
    private IsFollowingResponse failureResponse;

    private IsFollowingService isFollowingService;

    @BeforeEach
    public void setup() {
        User user = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User otherUser = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup request objects to use in the tests
        validRequest = new IsFollowingRequest(user, otherUser);
        invalidRequest = new IsFollowingRequest(null, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new IsFollowingResponse(true);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.isFollowing(validRequest)).thenReturn(successResponse);

        failureResponse = new IsFollowingResponse(false);
        Mockito.when(mockServerFacade.isFollowing(invalidRequest)).thenReturn(failureResponse);

        // Create a service instance and wrap it with a spy that will use the mock service
        isFollowingService = Mockito.spy(new IsFollowingService());
        Mockito.when(isFollowingService.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testIsFollowing_validRequest_correctResponse() {
        IsFollowingResponse response = isFollowingService.isFollowing(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testIsFollowing_invalidRequest_returnsFalse() {
        IsFollowingResponse response = isFollowingService.isFollowing(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}