package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class GetUserRequest {

    private String currentAlias;
    private String username;
    private AuthToken authToken;

    private GetUserRequest() {
    }

    public GetUserRequest(String currentAlias, String username, AuthToken authToken) {
        this.currentAlias = currentAlias;
        this.username = username;
        this.authToken = authToken;
    }

    public String getCurrentAlias() {
        return currentAlias;
    }

    public void setCurrentAlias(String currentAlias) {
        this.currentAlias = currentAlias;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
