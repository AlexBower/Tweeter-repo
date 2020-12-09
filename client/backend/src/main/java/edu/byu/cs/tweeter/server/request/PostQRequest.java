package edu.byu.cs.tweeter.server.request;

import edu.byu.cs.tweeter.model.domain.Status;

public class PostQRequest {

    private String message;
    private String time;
    private String alias;

    private PostQRequest() {

    }

    public PostQRequest(Status status) {
        this.message = status.getMessage();
        this.time = status.getTime();
        this.alias = status.getUser().getAlias();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
