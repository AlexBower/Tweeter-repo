package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.service.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.request.PostQRequest;
import edu.byu.cs.tweeter.server.service.PostStatusServiceImpl;
import edu.byu.cs.tweeter.server.service.TimeFormatter;
import edu.byu.cs.tweeter.server.util.JsonSerializer;

public class PostStatusHandler implements RequestHandler<PostStatusRequest, PostStatusResponse> {
    @Override
    public PostStatusResponse handleRequest(PostStatusRequest request, Context context) {

        if (request.getStatus() == null) {
            throw new RuntimeException("BadRequest: " + "No status");
        }

        LocalDateTime localDateTime = TimeFormatter.getFromString(request.getStatus().getTime());
        long epochSecond = localDateTime.toEpochSecond(ZoneOffset.UTC);
        Status tempStatus = new Status(request.getStatus().getMessage(), epochSecond + "", request.getStatus().getUser());
        String messageBody = JsonSerializer.serialize(new PostQRequest(tempStatus));
        String queueUrl = "https://sqs.us-west-2.amazonaws.com/857989844733/PostsQ";

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(messageBody);

        AmazonSQS sqs = getAmazonSQS();
        sqs.sendMessage(send_msg_request);

        PostStatusServiceImpl service = getPostStatusServiceImpl();
        return service.postStatus(request);
    }

    public AmazonSQS getAmazonSQS() {
        return AmazonSQSClientBuilder.defaultClient();
    }

    public PostStatusServiceImpl getPostStatusServiceImpl() {
        return new PostStatusServiceImpl();
    }
}
