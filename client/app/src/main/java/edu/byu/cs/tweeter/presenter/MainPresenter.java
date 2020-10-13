package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.LogoutService;
import edu.byu.cs.tweeter.model.service.PostStatusService;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.model.service.response.PostStatusResponse;

public class MainPresenter extends FollowCountPresenter{

    private final View view;

    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    public MainPresenter(View view) {
        super(new FollowCountPresenter.View() {
        });
        this.view = view;
    }

    public LogoutResponse logout(LogoutRequest request) {
        LogoutService logoutService = getLogoutService();
        return logoutService.logout(request);
    }

    LogoutService getLogoutService() { return new LogoutService(); }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        PostStatusService postStatusService = getPostStatusService();
        return postStatusService.postStatus(request);
    }

    PostStatusService getPostStatusService() { return new PostStatusService(); }
}
