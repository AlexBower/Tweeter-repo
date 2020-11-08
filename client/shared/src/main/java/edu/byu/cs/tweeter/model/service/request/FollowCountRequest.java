package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowCountRequest {

    private final User user;

    public FollowCountRequest(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}