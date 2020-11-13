package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FollowCountServiceProxy;
import edu.byu.cs.tweeter.model.service.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;

public class FollowCountPresenter {

    private final View view;

    protected FollowCountPresenter(View view) {
        this.view = view;
    }

    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    public FollowCountResponse getFollowCount(FollowCountRequest request) throws IOException, TweeterRemoteException {
        FollowCountServiceProxy followCountService = getFollowCountService();
        return followCountService.getFollowCount(request);
    }

    FollowCountServiceProxy getFollowCountService() { return new FollowCountServiceProxy(); }
}
