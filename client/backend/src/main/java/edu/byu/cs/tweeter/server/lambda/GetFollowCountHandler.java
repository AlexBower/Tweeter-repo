package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;
import edu.byu.cs.tweeter.server.service.FollowCountServiceImpl;

public class GetFollowCountHandler implements RequestHandler<FollowCountRequest, FollowCountResponse> {

    @Override
    public FollowCountResponse handleRequest(FollowCountRequest request, Context context) {
        FollowCountServiceImpl service = new FollowCountServiceImpl();
        return service.getFollowCount(request);
    }
}