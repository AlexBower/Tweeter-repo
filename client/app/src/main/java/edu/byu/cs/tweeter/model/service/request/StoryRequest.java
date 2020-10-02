package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryRequest {
    private final User user;
    private final int limit;
    private final Status lastStatus;

    public StoryRequest(User user, int limit, Status lastStatus) {
        this.user = user;
        this.limit = limit;
        this.lastStatus = lastStatus;
    }

    public Status getLastStatus() {
        return lastStatus;
    }

    public int getLimit() {
        return limit;
    }

    public User getUser() {
        return user;
    }
}
