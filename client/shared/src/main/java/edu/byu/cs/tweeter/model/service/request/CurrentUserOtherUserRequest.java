package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class CurrentUserOtherUserRequest {
    private User currentUser;
    private User otherUser;
    private AuthToken authToken;

    protected CurrentUserOtherUserRequest() {
    }

    public CurrentUserOtherUserRequest(User currentUser, User otherUser, AuthToken authToken) {
        this.currentUser = currentUser;
        this.otherUser = otherUser;
        this.authToken = authToken;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(User otherUser) {
        this.otherUser = otherUser;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
