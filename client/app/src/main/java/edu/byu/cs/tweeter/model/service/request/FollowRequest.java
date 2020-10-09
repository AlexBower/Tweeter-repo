package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowRequest extends CurrentUserOtherUserRequest {

    public FollowRequest(User currentUser, User otherUser) {
        super(currentUser, otherUser);
    }
}