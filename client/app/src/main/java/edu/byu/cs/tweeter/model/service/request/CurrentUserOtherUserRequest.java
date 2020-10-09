package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.User;

public class CurrentUserOtherUserRequest {
    private final User currentUser;
    private final User otherUser;

    public CurrentUserOtherUserRequest(User currentUser, User otherUser) {
        this.currentUser = currentUser;
        this.otherUser = otherUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public User getOtherUser() {
        return otherUser;
    }
}
