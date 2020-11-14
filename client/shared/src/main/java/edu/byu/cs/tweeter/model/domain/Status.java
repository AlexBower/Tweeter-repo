package edu.byu.cs.tweeter.model.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Status {
    private String message;

    private String time;

    private User user;

    private Status() {}

    public Status(String message, String time, User user) {

        this.setMessage(message);
        this.setTime(time);
        this.setUser(user);
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Status)) return false;
        Status status = (Status) o;
        return Objects.equals(message, status.message) &&
                Objects.equals(time, status.time) &&
                Objects.equals(user, status.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, time, user);
    }
}
