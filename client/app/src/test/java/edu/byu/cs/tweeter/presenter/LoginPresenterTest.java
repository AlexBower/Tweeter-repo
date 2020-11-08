package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.LoginServiceProxy;

public class LoginPresenterTest {

    private LoginRequest request;
    private LoginResponse response;
    private LoginServiceProxy mMockLoginServiceProxy;
    private LoginPresenter presenter;

    @BeforeEach
    public void setup() throws IOException {
        User resultUser = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        AuthToken resultAuthToken = new AuthToken();

        request = new LoginRequest("@Test", "password");
        response = new LoginResponse(resultUser, resultAuthToken);

        mMockLoginServiceProxy = Mockito.mock(LoginServiceProxy.class);
        Mockito.when(mMockLoginServiceProxy.login(request)).thenReturn(response);

        presenter = Mockito.spy(new LoginPresenter(new LoginPresenter.View() {}));
        Mockito.when(presenter.getLoginService()).thenReturn(mMockLoginServiceProxy);
    }

    @Test
    public void testLogin_returnsServiceResult() throws IOException {
        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.login(request));
    }

    @Test
    public void testLogin_serviceThrowsIOException_presenterThrowsIOException() throws IOException {
        Mockito.when(mMockLoginServiceProxy.login(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.login(request);
        });
    }
}