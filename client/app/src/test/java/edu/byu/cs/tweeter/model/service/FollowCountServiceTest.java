package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;

public class FollowCountServiceTest {

    private FollowCountRequest validRequest;
    private FollowCountRequest invalidRequest;

    private FollowCountResponse successResponse;
    private FollowCountResponse failureResponse;

    private FollowCountService followCountServiceSpy;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", null);

        // Setup request objects to use in the tests
        validRequest = new FollowCountRequest(currentUser);
        invalidRequest = new FollowCountRequest(null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new FollowCountResponse(100, 27);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getFollowCount(validRequest)).thenReturn(successResponse);

        failureResponse = new FollowCountResponse("An exception occurred");
        Mockito.when(mockServerFacade.getFollowCount(invalidRequest)).thenReturn(failureResponse);

        // Create a service instance and wrap it with a spy that will use the mock service
        followCountServiceSpy = Mockito.spy(new FollowCountService());
        Mockito.when(followCountServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testGetFollowCounts_validRequest_correctResponse() throws IOException {
        FollowCountResponse response = followCountServiceSpy.getFollowCount(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetFollowCounts_invalidRequest_returnsNoFollowCounts() throws IOException {
        FollowCountResponse response = followCountServiceSpy.getFollowCount(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}