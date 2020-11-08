package edu.byu.cs.tweeter.model.service.response;

public class UnfollowResponse extends Response {
    public UnfollowResponse(boolean success) {
        super(success);
    }

    public UnfollowResponse(boolean success, String message) {
        super(success, message);
    }
}
