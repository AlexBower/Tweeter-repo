package edu.byu.cs.tweeter.model.service.response;

public class FollowCountResponse extends Response{

    private int followersCount;
    private int followingCount;

    public FollowCountResponse(String message) {
        super(false, message);
    }

    public FollowCountResponse(int followersCount, int followingCount) {
        super(false, null);
        this.followersCount = followersCount;
        this.followingCount = followingCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }
}
