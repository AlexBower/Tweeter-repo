package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDateTime;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.LogoutService;
import edu.byu.cs.tweeter.model.service.PostStatusService;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.model.service.response.PostStatusResponse;

public class MainPresenterTest {

    private PostStatusRequest postStatusRequest;
    private PostStatusResponse postStatusResponse;
    private PostStatusService mockPostStatusService;

    private LogoutRequest logoutRequest;
    private LogoutResponse logoutResponse;
    private LogoutService mockLogoutService;

    private MainPresenter presenter;

    @BeforeEach
    public void setup() throws IOException {
        User user = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        Status status = new Status("Test Message", LocalDateTime.now(), user);
        AuthToken authToken = new AuthToken();

        postStatusRequest = new PostStatusRequest(status);
        postStatusResponse = new PostStatusResponse(true);
        mockPostStatusService = Mockito.mock(PostStatusService.class);
        Mockito.when(mockPostStatusService.postStatus(postStatusRequest)).thenReturn(postStatusResponse);

        logoutRequest = new LogoutRequest(user, authToken);
        logoutResponse = new LogoutResponse(true);
        mockLogoutService = Mockito.mock(LogoutService.class);
        Mockito.when(mockLogoutService.logout(logoutRequest)).thenReturn(logoutResponse);

        presenter = Mockito.spy(new MainPresenter(new MainPresenter.View() {}));
        Mockito.when(presenter.getPostStatusService()).thenReturn(mockPostStatusService);
        Mockito.when(presenter.getLogoutService()).thenReturn(mockLogoutService);
    }

    @Test
    public void testPostStatus_returnsServiceResult() {
        // Assert that the presenter returns the same response as the service
        Assertions.assertEquals(postStatusResponse, presenter.postStatus(postStatusRequest));
    }

    @Test
    public void testLogout_returnsServiceResult() {
        // Assert that the presenter returns the same response as the service
        Assertions.assertEquals(logoutResponse, presenter.logout(logoutRequest));
    }
}