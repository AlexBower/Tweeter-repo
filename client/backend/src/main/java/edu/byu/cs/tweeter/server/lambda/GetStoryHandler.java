package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.StatusRequest;
import edu.byu.cs.tweeter.model.service.response.StatusResponse;
import edu.byu.cs.tweeter.server.service.StatusServiceImpl;

public class GetStoryHandler implements RequestHandler<StatusRequest, StatusResponse> {

    @Override
    public StatusResponse handleRequest(StatusRequest request, Context context) {
        System.out.println(request.getUser().getImageBytes());
        StatusServiceImpl service = new StatusServiceImpl();
        return service.getStory(request);
    }
}
