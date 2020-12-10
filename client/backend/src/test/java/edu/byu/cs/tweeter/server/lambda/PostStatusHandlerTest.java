package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.service.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.TestWithAuthToken;
import edu.byu.cs.tweeter.server.request.PostQRequest;
import edu.byu.cs.tweeter.server.service.PostStatusServiceImpl;
import edu.byu.cs.tweeter.server.service.TimeFormatter;
import edu.byu.cs.tweeter.server.util.JsonSerializer;

public class PostStatusHandlerTest extends TestWithAuthToken {
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

    private final User user = new User("Testing", "User", "@test", MALE_IMAGE_URL);

    private final Status status = new Status("Hey, 1 "
            + user.getAlias()
            + " check out this really cool url: "
            + "http://google.com",
            TimeFormatter.format(LocalDateTime.now()), user);

    private PostStatusRequest request;
    private PostStatusResponse expectedResponse;
    private PostStatusServiceImpl mockPostStatusServiceImpl;
    private AmazonSQS mockAmazonSQS;
    private PostStatusHandler postStatusHandlerSpy;

    @BeforeEach
    public void setup() {

        // Setup a request object to use in the tests
        request = new PostStatusRequest(status, authToken);

        LocalDateTime localDateTime = TimeFormatter.getFromString(request.getStatus().getTime());
        long epochSecond = localDateTime.toEpochSecond(ZoneOffset.UTC);
        Status tempStatus = new Status(request.getStatus().getMessage(), epochSecond + "", request.getStatus().getUser());
        String messageBody = JsonSerializer.serialize(new PostQRequest(tempStatus));
        String queueUrl = "https://sqs.us-west-2.amazonaws.com/857989844733/PostsQ";

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(messageBody);

        expectedResponse = new PostStatusResponse(true);
        mockPostStatusServiceImpl = Mockito.mock(PostStatusServiceImpl.class);
        Mockito.when(mockPostStatusServiceImpl.postStatus(request)).thenReturn(expectedResponse);

        mockAmazonSQS = Mockito.mock(AmazonSQS.class);
        Mockito.when(mockAmazonSQS.sendMessage(send_msg_request)).thenReturn(new SendMessageResult());

        postStatusHandlerSpy = Mockito.spy(PostStatusHandler.class);
        Mockito.when(postStatusHandlerSpy.getAmazonSQS()).thenReturn(mockAmazonSQS);
        Mockito.when(postStatusHandlerSpy.getPostStatusServiceImpl()).thenReturn(mockPostStatusServiceImpl);
    }

    @Test
    public void testPostStatus_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        PostStatusResponse response = postStatusHandlerSpy.handleRequest(request, null);
        Assertions.assertEquals(expectedResponse.isSuccess(), response.isSuccess());
    }

    @Test
    public void testPostStatus_invalidRequest_throwsError() {
        PostStatusRequest invalidRequest = new PostStatusRequest(null, null);

        String failureResponse = "BadRequest: " + "No status";
        try {
            Assertions.assertThrows(RuntimeException.class
                    , (Executable) postStatusHandlerSpy.handleRequest(invalidRequest, null)
                    , failureResponse);
        } catch (Exception e) {
            Assertions.assertEquals(failureResponse, e.getMessage());
        }
    }
}
