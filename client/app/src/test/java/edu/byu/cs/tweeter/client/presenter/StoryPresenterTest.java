package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

import edu.byu.cs.tweeter.TestWithAuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.StatusServiceProxy;
import edu.byu.cs.tweeter.model.service.TimeFormatter;
import edu.byu.cs.tweeter.model.service.request.StatusRequest;
import edu.byu.cs.tweeter.model.service.response.StatusResponse;
import edu.byu.cs.tweeter.presenter.StoryPresenter;

public class StoryPresenterTest extends TestWithAuthToken {

    private StatusRequest request;
    private StatusResponse response;
    private StatusServiceProxy mMockStatusServiceProxy;
    private StoryPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        Status resultStatus1 = new Status("Test Message1", TimeFormatter.format(LocalDateTime.now()), resultUser1);
        Status resultStatus2 = new Status("Test Message2", TimeFormatter.format(LocalDateTime.now()), resultUser2);
        Status resultStatus3 = new Status("Test Message3", TimeFormatter.format(LocalDateTime.now()), resultUser3);

        request = new StatusRequest(currentUser, 3, null, authToken);
        response = new StatusResponse(Arrays.asList(resultStatus1, resultStatus2, resultStatus3), false);

        mMockStatusServiceProxy = Mockito.mock(StatusServiceProxy.class);
        Mockito.when(mMockStatusServiceProxy.getStory(request)).thenReturn(response);

        presenter = Mockito.spy(new StoryPresenter(new StoryPresenter.View() {}));
        Mockito.when(presenter.getStatusService()).thenReturn(mMockStatusServiceProxy);
    }

    @Test
    public void testGetStory_returnsServiceResult() throws IOException, TweeterRemoteException {
        // Assert that the presenter returns the same response as the service
        Assertions.assertEquals(response, presenter.getStory(request));
    }

    @Test
    public void testGetStory_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mMockStatusServiceProxy.getStory(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getStory(request);
        });
    }
}