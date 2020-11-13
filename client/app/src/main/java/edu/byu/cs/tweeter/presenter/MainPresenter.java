package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.LogoutServiceProxy;
import edu.byu.cs.tweeter.model.service.PostStatusServiceProxy;
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

    public LogoutResponse logout(LogoutRequest request) throws IOException, TweeterRemoteException {
        LogoutServiceProxy logoutService = getLogoutService();
        return logoutService.logout(request);
    }

    LogoutServiceProxy getLogoutService() { return new LogoutServiceProxy(); }

    public PostStatusResponse postStatus(PostStatusRequest request) throws IOException, TweeterRemoteException {
        PostStatusServiceProxy postStatusService = getPostStatusService();
        return postStatusService.postStatus(request);
    }

    PostStatusServiceProxy getPostStatusService() { return new PostStatusServiceProxy(); }
}
