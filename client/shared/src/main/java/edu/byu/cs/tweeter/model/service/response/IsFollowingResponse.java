package edu.byu.cs.tweeter.model.service.response;

public class IsFollowingResponse extends Response {

    private boolean following;

    public IsFollowingResponse(boolean following) {
        super(true, "");
        this.following = following;
    }

    public IsFollowingResponse(String message) {
        super(false, message);
    }

    public boolean isFollowing() {
        return following;
    }
}
