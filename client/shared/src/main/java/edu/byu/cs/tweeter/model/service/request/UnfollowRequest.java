package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.User;

public class UnfollowRequest extends CurrentUserOtherUserRequest {
    public UnfollowRequest(User currentUser, User otherUser) {
        super(currentUser, otherUser);
    }
}
