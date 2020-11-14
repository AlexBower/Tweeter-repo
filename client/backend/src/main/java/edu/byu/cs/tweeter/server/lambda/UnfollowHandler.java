package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.service.UnfollowServiceImpl;

public class UnfollowHandler implements RequestHandler<UnfollowRequest, UnfollowResponse> {
    @Override
    public UnfollowResponse handleRequest(UnfollowRequest request, Context context) {
        UnfollowServiceImpl unfollowService = new UnfollowServiceImpl();
        return unfollowService.unfollow(request);
    }
}
