package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.GetUserService;
import edu.byu.cs.tweeter.model.service.request.GetUserRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserResponse;

public class GetUserPresenter {
    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    public GetUserPresenter(View view) {
        this.view = view;
    }

    public GetUserResponse getUser(GetUserRequest getUserRequest) throws IOException {
        GetUserService getUserService = new GetUserService();
        return getUserService.getUser(getUserRequest);
    }
}