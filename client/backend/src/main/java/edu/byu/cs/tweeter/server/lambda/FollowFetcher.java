package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.request.JobQRequest;
import edu.byu.cs.tweeter.server.request.PostQRequest;
import edu.byu.cs.tweeter.server.util.JsonSerializer;

public class FollowFetcher implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            PostQRequest postQRequest = JsonSerializer.deserialize(
                    msg.getBody(),
                    PostQRequest.class);
            FollowDAO followDAO = getFollowDAO();

            List<String> allFollowers = followDAO.getAllFollowers(postQRequest.getAlias());
            List<String> jobList = new ArrayList<>();
            for (String follower : allFollowers) {
                if (jobList.size() < 25) {
                    jobList.add(follower);
                } else {
                    sendJob(jobList, postQRequest);
                    jobList = new ArrayList<>();
                    jobList.add(follower);
                }
            }
            if (!jobList.isEmpty()) {
                sendJob(jobList, postQRequest);
            }
        }
        return null;
    }

    private void sendJob(List<String> jobList, PostQRequest postQRequest) {
        String messageBody = JsonSerializer.serialize(new JobQRequest(jobList, postQRequest));
        String queueUrl = "https://sqs.us-west-2.amazonaws.com/857989844733/JobsQ";

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(messageBody);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        sqs.sendMessage(send_msg_request);
    }

    public FollowDAO getFollowDAO() {
        return new FollowDAO();
    }
}
