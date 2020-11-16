package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.TestWithAuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.GetUserServiceProxy;
import edu.byu.cs.tweeter.model.service.request.GetUserRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserResponse;
import edu.byu.cs.tweeter.presenter.PaginatedPresenter;

public class PaginatedPresenterTest extends TestWithAuthToken {

    private GetUserRequest request;
    private GetUserResponse response;
    private GetUserServiceProxy mMockGetUserServiceProxy;
    private PaginatedPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User resultUser = new User("FirstName", "LastName", "@Test",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        request = new GetUserRequest("@Test", authToken);
        response = new GetUserResponse(resultUser);

        mMockGetUserServiceProxy = Mockito.mock(GetUserServiceProxy.class);
        Mockito.when(mMockGetUserServiceProxy.getUser(request)).thenReturn(response);

        presenter = Mockito.spy(new PaginatedPresenter(new PaginatedPresenter.View() {}));
        Mockito.when(presenter.getGetUserService()).thenReturn(mMockGetUserServiceProxy);
    }

    @Test
    public void testGetUser_returnsServiceResult() throws IOException, TweeterRemoteException {
        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.getUser(request));
    }

    @Test
    public void testLogin_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mMockGetUserServiceProxy.getUser(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getUser(request);
        });
    }
}