package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.request.JobQRequest;
import edu.byu.cs.tweeter.server.request.PostQRequest;
import edu.byu.cs.tweeter.server.util.JsonSerializer;

public class JobHandler implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            JobQRequest jobQRequest = JsonSerializer.deserialize(
                    msg.getBody(),
                    JobQRequest.class);
            FeedDAO feedDAO = getFeedDAO();

            feedDAO.postStatuses(jobQRequest);
        }
        return null;
    }

    public FeedDAO getFeedDAO() {
        return new FeedDAO();
    }
}
