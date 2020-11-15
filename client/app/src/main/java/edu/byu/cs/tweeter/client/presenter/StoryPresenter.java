package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.StatusServiceProxy;
import edu.byu.cs.tweeter.model.service.request.StatusRequest;
import edu.byu.cs.tweeter.model.service.response.StatusResponse;

public class StoryPresenter {

    private final View view;

    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    public StoryPresenter(View view) {
        this.view = view;
    }

    public StatusResponse getStory(StatusRequest request) throws IOException, TweeterRemoteException {
        StatusServiceProxy statusService = getStatusService();
        return statusService.getStory(request);
    }

    StatusServiceProxy getStatusService() {
        return new StatusServiceProxy();
    }
}
