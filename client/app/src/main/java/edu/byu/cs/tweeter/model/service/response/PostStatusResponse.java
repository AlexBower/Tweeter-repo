package edu.byu.cs.tweeter.model.service.response;

public class PostStatusResponse extends Response {
    public PostStatusResponse(boolean success) {
        super(success);
    }

    public PostStatusResponse(String message) {
        super(false, message);
    }
}
