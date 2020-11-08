package edu.byu.cs.tweeter.model.service.response;

public class IsFollowingResponse extends Response {

    private boolean isFollowing;

    public IsFollowingResponse(boolean isFollowing) {
        super(true, "");
        this.isFollowing = isFollowing;
    }

    public IsFollowingResponse(String message) {
        super(false, message);
    }

    public boolean isFollowing() {
        return isFollowing;
    }
}
