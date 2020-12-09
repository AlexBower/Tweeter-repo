package edu.byu.cs.tweeter.server.request;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class JobQRequest {
    private List<String> aliases;
    private String message;
    private String time;
    private String author;

    private JobQRequest() {

    }

    public JobQRequest(List<String> aliases, PostQRequest postQRequest) {
        this.aliases = aliases;
        this.message = postQRequest.getMessage();
        this.time = postQRequest.getTime();
        this.author = postQRequest.getAlias();
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
