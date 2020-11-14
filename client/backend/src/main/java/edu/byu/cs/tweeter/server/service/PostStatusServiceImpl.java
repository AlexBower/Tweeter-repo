package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.PostStatusService;
import edu.byu.cs.tweeter.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.service.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;

public class PostStatusServiceImpl implements PostStatusService {
    @Override
    public PostStatusResponse postStatus(PostStatusRequest request) {
        getStoryDAO().postStatus(request);
        getFeedDAO().postStatus(request);
        return new PostStatusResponse(true);

    }

    StoryDAO getStoryDAO() {
        return new StoryDAO();
    }
    FeedDAO getFeedDAO() {
        return new FeedDAO();
    }
}
