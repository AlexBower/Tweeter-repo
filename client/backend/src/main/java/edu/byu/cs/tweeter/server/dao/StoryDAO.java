package edu.byu.cs.tweeter.server.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.service.request.StatusRequest;
import edu.byu.cs.tweeter.model.service.response.StatusResponse;
import edu.byu.cs.tweeter.server.service.TimeFormatter;

public class StoryDAO {
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

    private final User user = new User("Testing", "User", MALE_IMAGE_URL);

    private final Status status1 = new Status("Hey, 1 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status2 = new Status("Hey, 2 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status3 = new Status("Hey, 3 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status4 = new Status("Hey, 4 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status5 = new Status("Hey, 5 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status6 = new Status("Hey, 6 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status7 = new Status("Hey, 7 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status8 = new Status("Hey, 8 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status9 = new Status("Hey, 9 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status10 = new Status("Hey, 10 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status11 = new Status("Hey, 11 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status12 = new Status("Hey, 12 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status13 = new Status("Hey, 13 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status14 = new Status("Hey, 14 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status15 = new Status("Hey, 15 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status16 = new Status("Hey, 16 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status17 = new Status("Hey, 17 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status18 = new Status("Hey, 18 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status19 = new Status("Hey, 19 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);
    private final Status status20 = new Status("Hey, 20 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);

    private List<Status> dummyStory = Arrays.asList(status1, status2, status3, status4, status5, status6, status7,
            status8, status9, status10, status11, status12, status13, status14, status15, status16, status17, status18,
            status19, status20);

    public boolean postStatus(PostStatusRequest request) {
        return true;
    }

    public StatusResponse getStory(StatusRequest request) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert request.getLimit() > 0;
        assert request.getUser() != null;

        List<Status> allStatuses = dummyStory;
        List<Status> responseStatuses = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allStatuses != null) {
                int statusIndex = getStatusStartingIndex(request.getLastStatus(), allStatuses);

                for(int limitCounter = 0; statusIndex < allStatuses.size() && limitCounter < request.getLimit(); statusIndex++, limitCounter++) {
                    responseStatuses.add(allStatuses.get(statusIndex));
                }

                hasMorePages = statusIndex < allStatuses.size();
            }
        }

        return new StatusResponse(responseStatuses, hasMorePages);
    }

    private int getStatusStartingIndex(Status lastStatus, List<Status> allStatuses) {

        int statusIndex = 0;

        if(lastStatus != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatuses.size(); i++) {
                if(lastStatus.equals(allStatuses.get(i))) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    statusIndex = i + 1;
                    break;
                }
            }
        }

        return statusIndex;
    }
}
