package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.RegisterService;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

public class RegisterPresenterTest {

    private RegisterRequest request;
    private RegisterResponse response;
    private RegisterService mockRegisterService;
    private RegisterPresenter presenter;

    @BeforeEach
    public void setup() throws IOException {
        byte[] testBytes= {0, 1, 0, 1};

        User resultUser = new User("FirstName",
                "LastName",
                "@Test",
                testBytes);
        AuthToken resultAuthToken = new AuthToken();

        request = new RegisterRequest("@Test",
                "password",
                "FirstName",
                "LastName",
                testBytes);
        response = new RegisterResponse(resultUser, resultAuthToken);

        mockRegisterService = Mockito.mock(RegisterService.class);
        Mockito.when(mockRegisterService.register(request)).thenReturn(response);

        presenter = Mockito.spy(new RegisterPresenter(new RegisterPresenter.View() {}));
        Mockito.when(presenter.getRegisterService()).thenReturn(mockRegisterService);
    }

    @Test
    public void testRegister_returnsServiceResult() throws IOException {
        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.register(request));
    }

    @Test
    public void testRegister_serviceThrowsIOException_presenterThrowsIOException() throws IOException {
        Mockito.when(mockRegisterService.register(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.register(request);
        });
    }
}