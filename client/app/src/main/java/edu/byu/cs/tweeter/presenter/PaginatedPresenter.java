package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.GetUserServiceProxy;
import edu.byu.cs.tweeter.model.service.request.GetUserRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserResponse;

public class PaginatedPresenter {
    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    public PaginatedPresenter(View view) {
        this.view = view;
    }

    public GetUserResponse getUser(GetUserRequest getUserRequest) throws IOException, TweeterRemoteException {
        GetUserServiceProxy getUserService = getGetUserService();
        return getUserService.getUser(getUserRequest);
    }

    GetUserServiceProxy getGetUserService() { return new GetUserServiceProxy(); }
}