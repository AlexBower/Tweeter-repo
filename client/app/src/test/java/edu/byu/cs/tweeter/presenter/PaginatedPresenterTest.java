package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.GetUserService;
import edu.byu.cs.tweeter.model.service.request.GetUserRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserResponse;

public class PaginatedPresenterTest {

    private GetUserRequest request;
    private GetUserResponse response;
    private GetUserService mockGetUserService;
    private PaginatedPresenter presenter;

    @BeforeEach
    public void setup() throws IOException {
        User resultUser = new User("FirstName", "LastName", "@Test",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        request = new GetUserRequest("@Test");
        response = new GetUserResponse(resultUser);

        mockGetUserService = Mockito.mock(GetUserService.class);
        Mockito.when(mockGetUserService.getUser(request)).thenReturn(response);

        presenter = Mockito.spy(new PaginatedPresenter(new PaginatedPresenter.View() {}));
        Mockito.when(presenter.getGetUserService()).thenReturn(mockGetUserService);
    }

    @Test
    public void testGetUser_returnsServiceResult() throws IOException {
        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.getUser(request));
    }

    @Test
    public void testLogin_serviceThrowsIOException_presenterThrowsIOException() throws IOException {
        Mockito.when(mockGetUserService.getUser(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getUser(request);
        });
    }
}