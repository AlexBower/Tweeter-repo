package edu.byu.cs.tweeter.model.service.request;

public class GetUserRequest {

        private String username;

        public GetUserRequest(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
}
