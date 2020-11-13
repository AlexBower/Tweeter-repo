package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UnfollowRequest extends CurrentUserOtherUserRequest {

    private  UnfollowRequest() {
        
    }

    public UnfollowRequest(User currentUser, User otherUser, AuthToken authToken) {
        super(currentUser, otherUser, authToken);
    }
}
