package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.PostStatusService;
import edu.byu.cs.tweeter.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.service.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;

public class PostStatusServiceImpl implements PostStatusService {
    @Override
    public PostStatusResponse postStatus(PostStatusRequest request) {

        if (!getAuthTokenDAO().checkAuthToken(request.getStatus().getUser().getAlias(), request.getAuthToken().getToken())) {
            throw new RuntimeException("BadRequest: Invalid or Expired AuthToken");
        }

        getStoryDAO().postStatus(request);

        return new PostStatusResponse(true);
    }

    public StoryDAO getStoryDAO() {
        return new StoryDAO();
    }
    public AuthTokenDAO getAuthTokenDAO() {
        return new AuthTokenDAO();
    }
}
