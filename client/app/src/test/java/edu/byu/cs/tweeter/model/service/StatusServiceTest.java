package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.StatusRequest;
import edu.byu.cs.tweeter.model.service.response.StatusResponse;

public class StatusServiceTest {

    private StatusRequest validRequest;
    private StatusRequest invalidRequest;

    private StatusResponse successResponse;
    private StatusResponse failureResponse;

    private StatusService statusServiceSpy;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        Status resultStatus1 = new Status("Test Message1", LocalDateTime.now(), resultUser1);
        Status resultStatus2 = new Status("Test Message2", LocalDateTime.now(), resultUser2);
        Status resultStatus3 = new Status("Test Message3", LocalDateTime.now(), resultUser3);

        // Setup request objects to use in the tests
        validRequest = new StatusRequest(currentUser, 3, null);
        invalidRequest = new StatusRequest(null, 0, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new StatusResponse(Arrays.asList(resultStatus1, resultStatus2, resultStatus3), false);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getFeed(validRequest)).thenReturn(successResponse);
        Mockito.when(mockServerFacade.getStory(validRequest)).thenReturn(successResponse);

        failureResponse = new StatusResponse("An exception occurred");
        Mockito.when(mockServerFacade.getFeed(invalidRequest)).thenReturn(failureResponse);
        Mockito.when(mockServerFacade.getStory(invalidRequest)).thenReturn(failureResponse);

        // Create a service instance and wrap it with a spy that will use the mock service
        statusServiceSpy = Mockito.spy(new StatusService());
        Mockito.when(statusServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    // Test to make sure correct Feed is returned is
    @Test
    public void testGetFeed_validRequest_correctResponse() throws IOException {
        StatusResponse response = statusServiceSpy.getFeed(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    // Test to make sure correct Story is returned
    @Test
    public void testGetStory_validRequest_correctResponse() throws IOException {
        StatusResponse response = statusServiceSpy.getStory(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetFeed_validRequest_loadsProfileImages() throws IOException {
        StatusResponse response = statusServiceSpy.getFeed(validRequest);

        for(Status status : response.getStatuses()) {
            Assertions.assertNotNull(status.getUser().getImageBytes());
        }
    }

    @Test
    public void testGetStory_validRequest_loadsProfileImages() throws IOException {
        StatusResponse response = statusServiceSpy.getStory(validRequest);

        for(Status status : response.getStatuses()) {
            Assertions.assertNotNull(status.getUser().getImageBytes());
        }
    }

    @Test
    public void testGetFeed_invalidRequest_returnsNoStatuses() throws IOException {
        StatusResponse response = statusServiceSpy.getFeed(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }

    @Test
    public void testGetStory_invalidRequest_returnsNoStatuses() throws IOException {
        StatusResponse response = statusServiceSpy.getStory(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
