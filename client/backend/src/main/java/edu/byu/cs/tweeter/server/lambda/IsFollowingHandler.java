package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;
import edu.byu.cs.tweeter.server.service.IsFollowingServiceImpl;

public class IsFollowingHandler implements RequestHandler<IsFollowingRequest, IsFollowingResponse> {
    @Override
    public IsFollowingResponse handleRequest(IsFollowingRequest request, Context context) {
        IsFollowingServiceImpl isFollowingService = new IsFollowingServiceImpl();
        return isFollowingService.isFollowing(request);
    }
}
