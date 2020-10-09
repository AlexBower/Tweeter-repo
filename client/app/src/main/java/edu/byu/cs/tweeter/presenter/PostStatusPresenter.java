package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.PostStatusService;
import edu.byu.cs.tweeter.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.service.response.PostStatusResponse;

public class PostStatusPresenter  {

    private final View view;

    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    public PostStatusPresenter(View view) {
        this.view = view;
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        PostStatusService postStatusService = getPostStatusService();
        return postStatusService.postStatus(request);
    }

    PostStatusService getPostStatusService() { return new PostStatusService(); }
}