package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDateTime;

import edu.byu.cs.tweeter.TestWithAuthToken;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.LogoutServiceProxy;
import edu.byu.cs.tweeter.model.service.PostStatusServiceProxy;
import edu.byu.cs.tweeter.model.service.TimeFormatter;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.model.service.response.PostStatusResponse;
import edu.byu.cs.tweeter.presenter.MainPresenter;

public class MainPresenterTest extends TestWithAuthToken {

    private PostStatusRequest postStatusRequest;
    private PostStatusResponse postStatusResponse;
    private PostStatusServiceProxy mMockPostStatusServiceProxy;

    private LogoutRequest logoutRequest;
    private LogoutResponse logoutResponse;
    private LogoutServiceProxy mMockLogoutServiceProxy;

    private MainPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User user = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        Status status = new Status("Test Message", TimeFormatter.format(LocalDateTime.now()), user);
        AuthToken authToken = new AuthToken();

        postStatusRequest = new PostStatusRequest(status, authToken);
        postStatusResponse = new PostStatusResponse(true);
        mMockPostStatusServiceProxy = Mockito.mock(PostStatusServiceProxy.class);
        Mockito.when(mMockPostStatusServiceProxy.postStatus(postStatusRequest)).thenReturn(postStatusResponse);

        logoutRequest = new LogoutRequest(user, authToken);
        logoutResponse = new LogoutResponse(true);
        mMockLogoutServiceProxy = Mockito.mock(LogoutServiceProxy.class);
        Mockito.when(mMockLogoutServiceProxy.logout(logoutRequest)).thenReturn(logoutResponse);

        presenter = Mockito.spy(new MainPresenter(new MainPresenter.View() {}));
        Mockito.when(presenter.getPostStatusService()).thenReturn(mMockPostStatusServiceProxy);
        Mockito.when(presenter.getLogoutService()).thenReturn(mMockLogoutServiceProxy);
    }

    @Test
    public void testPostStatus_returnsServiceResult() throws IOException, TweeterRemoteException {
        // Assert that the presenter returns the same response as the service
        Assertions.assertEquals(postStatusResponse, presenter.postStatus(postStatusRequest));
    }

    @Test
    public void testLogout_returnsServiceResult() throws IOException, TweeterRemoteException {
        // Assert that the presenter returns the same response as the service
        Assertions.assertEquals(logoutResponse, presenter.logout(logoutRequest));
    }
}