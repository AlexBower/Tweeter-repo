package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.User;

public class IsFollowingRequest extends CurrentUserOtherUserRequest {
    public IsFollowingRequest(User currentUser, User otherUser) {
        super(currentUser, otherUser);
    }
}