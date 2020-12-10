package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.TestWithAuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.StatusService;
import edu.byu.cs.tweeter.model.service.StatusServiceProxy;
import edu.byu.cs.tweeter.model.service.TimeFormatter;
import edu.byu.cs.tweeter.model.service.request.StatusRequest;
import edu.byu.cs.tweeter.model.service.response.StatusResponse;

public class StatusServiceIntegrationTest extends TestWithAuthToken {

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

    private final User user = new User("Status", "Test", MALE_IMAGE_URL);

    private final Status status1 = new Status("testStatus1",
            "Dec 10 2020  01:18:03 AM", user);
    private final Status status2 = new Status("testStatus2",
            "Dec 10 2020  01:18:04 AM", user);

    private List<Status> dummyStatuses = Arrays.asList(status2, status1);

    private StatusRequest validRequest;
    private StatusRequest invalidRequest;

    private StatusResponse successResponse;
    private String failureResponse;

    private StatusService serviceProxy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("Status", "Test", MALE_IMAGE_URL);

        // Setup request objects to use in the tests
        validRequest = new StatusRequest(currentUser, 10, null, authToken);
        invalidRequest = new StatusRequest(null, 0, null, authToken);

        successResponse = new StatusResponse(dummyStatuses, false);

        failureResponse = "BadRequest: " + "No user";

        serviceProxy = new StatusServiceProxy();
    }

    @Test
    public void testGetStory_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        StatusResponse response = serviceProxy.getStory(validRequest);
        checkStatuses(successResponse.getStatuses(), response.getStatuses());
    }

    @Test
    public void testGetFeed_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        StatusResponse response = serviceProxy.getFeed(validRequest);
        checkStatuses(successResponse.getStatuses(), response.getStatuses());
    }

    private void checkStatuses(List<Status> list1, List<Status> list2) {
        for (int i = 0; i < list1.size(); i++) {
            Assertions.assertEquals(list1.get(i).getMessage(), list2.get(i).getMessage());
        }
    }

    @Test
    public void testGetFeed_invalidRequest_throwsError() {
        try {
            Assertions.assertThrows(RuntimeException.class
                    , (Executable) serviceProxy.getFeed(invalidRequest)
                    , failureResponse);
        } catch (Exception e) {
            Assertions.assertEquals(failureResponse, e.getMessage());
        }
    }

    @Test
    public void testGetStory_invalidRequest_throwsError() {
        try {
            Assertions.assertThrows(RuntimeException.class
                    , (Executable) serviceProxy.getStory(invalidRequest)
                    , failureResponse);
        } catch (Exception e) {
            Assertions.assertEquals(failureResponse, e.getMessage());
        }
    }
}