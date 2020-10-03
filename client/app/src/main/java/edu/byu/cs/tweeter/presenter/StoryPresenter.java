package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.StatusService;
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

    public StatusResponse getStory(StatusRequest request) throws IOException {
        StatusService statusService = getStatusService();
        return statusService.getStory(request);
    }

    StatusService getStatusService() {
        return new StatusService();
    }
}
