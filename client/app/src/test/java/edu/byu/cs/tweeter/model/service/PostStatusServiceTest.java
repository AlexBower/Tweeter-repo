package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.service.response.PostStatusResponse;

public class PostStatusServiceTest {

    private PostStatusRequest validRequest;
    private PostStatusRequest invalidRequest;

    private PostStatusResponse successResponse;
    private PostStatusResponse failureResponse;

    private PostStatusService postStatusServiceSpy;

    @BeforeEach
    public void setup() {
        User user = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        Status status = new Status("Test Message", LocalDateTime.now(), user);

        // Setup request objects to use in the tests
        validRequest = new PostStatusRequest(status);
        invalidRequest = new PostStatusRequest(null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new PostStatusResponse(true);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.postStatus(validRequest)).thenReturn(successResponse);

        failureResponse = new PostStatusResponse(false);
        Mockito.when(mockServerFacade.postStatus(invalidRequest)).thenReturn(failureResponse);

        // Create a service instance and wrap it with a spy that will use the mock service
        postStatusServiceSpy = Mockito.spy(new PostStatusService());
        Mockito.when(postStatusServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testPostStatus_validRequest_correctResponse() {
        PostStatusResponse response = postStatusServiceSpy.postStatus(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testRegister_invalidRequest_returnsFalse() {
        PostStatusResponse response = postStatusServiceSpy.postStatus(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}