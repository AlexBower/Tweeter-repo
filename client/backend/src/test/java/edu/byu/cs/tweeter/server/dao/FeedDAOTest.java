package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.StatusRequest;
import edu.byu.cs.tweeter.model.service.response.StatusResponse;
import edu.byu.cs.tweeter.server.TestWithAuthToken;
import edu.byu.cs.tweeter.server.request.JobQRequest;
import edu.byu.cs.tweeter.server.request.PostQRequest;
import edu.byu.cs.tweeter.server.service.TimeFormatter;

public class FeedDAOTest extends TestWithAuthToken {

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

    private final User user = new User("Status", "Test", MALE_IMAGE_URL);

    private final Status status1 = new Status("testStatus1",
            "Dec 10 2020  01:18:03 AM", user);
    private final Status status2 = new Status("testStatus2",
            "Dec 10 2020  01:18:04 AM", user);

    User batchUser = new User("FirstName", "LastName", MALE_IMAGE_URL);

    private final Status batchStatus = new Status("Hey, "
            + batchUser.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), batchUser);

    private List<Status> dummyStatuses = Arrays.asList(status2, status1);

    private FeedDAO feedDAO;

    @BeforeEach
    public void setup() {
        feedDAO = new FeedDAO();
    }

    @Test
    public void testPostStatuses_validRequest() {
        List<String> aliases = new ArrayList<>();
        aliases.add("@1");
        aliases.add("@2");
        aliases.add("@3");
        aliases.add("@4");
        aliases.add("@5");
        LocalDateTime localDateTime = TimeFormatter.getFromString(batchStatus.getTime());
        long epochSecond = localDateTime.toEpochSecond(ZoneOffset.UTC);
        Status tempStatus = new Status(batchStatus.getMessage(), epochSecond + "", batchStatus.getUser());
        PostQRequest postQRequest = new PostQRequest(tempStatus);
        JobQRequest jobQRequest = new JobQRequest(aliases, postQRequest);

        feedDAO.postStatuses(jobQRequest);
        // as long as this doesn't throw an exception the test passes
    }

    @Test
    public void testGetFeed_validRequest() {
        User currentUser = new User("Status", "Test", MALE_IMAGE_URL);
        StatusRequest validRequest = new StatusRequest(currentUser, 10, null, authToken);
        StatusResponse successResponse = new StatusResponse(dummyStatuses, false);

        StatusResponse response = feedDAO.getFeed(validRequest);
        checkStatuses(successResponse.getStatuses(), response.getStatuses());
    }

    private void checkStatuses(List<Status> list1, List<Status> list2) {
        for (int i = 0; i < list1.size(); i++) {
            Assertions.assertEquals(list1.get(i).getMessage(), list2.get(i).getMessage());
        }
    }
}
