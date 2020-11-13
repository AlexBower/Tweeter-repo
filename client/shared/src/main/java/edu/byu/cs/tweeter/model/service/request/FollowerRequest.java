package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowerRequest {
    private User followee;
    private int limit;
    private User lastFollower;
    private AuthToken authToken;

    private FollowerRequest() {
    }

    public FollowerRequest(User followee, int limit, User lastFollower, AuthToken authToken) {
        this.followee = followee;
        this.limit = limit;
        this.lastFollower = lastFollower;
        this.authToken = authToken;
    }

    public User getFollowee() {
        return followee;
    }

    public void setFollowee(User followee) {
        this.followee = followee;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public User getLastFollower() {
        return lastFollower;
    }

    public void setLastFollower(User lastFollower) {
        this.lastFollower = lastFollower;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
