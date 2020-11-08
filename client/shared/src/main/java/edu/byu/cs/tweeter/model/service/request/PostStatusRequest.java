package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.Status;

public class PostStatusRequest {

    private final Status status;

    public PostStatusRequest(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
