package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.FollowCountService;
import edu.byu.cs.tweeter.model.service.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;

public class FollowCountPresenterTest {

    private FollowCountRequest request;
    private FollowCountResponse response;
    private FollowCountService mockFollowCountService;
    private FollowCountPresenter presenter;

    @BeforeEach
    public void setup() throws IOException {
        User currentUser = new User("FirstName", "LastName", null);

        request = new FollowCountRequest(currentUser);
        response = new FollowCountResponse(100,27);

        mockFollowCountService = Mockito.mock(FollowCountService.class);
        Mockito.when(mockFollowCountService.getFollowCount(request)).thenReturn(response);

        presenter = Mockito.spy(new FollowCountPresenter(new FollowCountPresenter.View() {}));
        Mockito.when(presenter.getFollowCountService()).thenReturn(mockFollowCountService);
    }

    @Test
    public void testGetFollowCount_returnsServiceResult() throws IOException {
        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.getFollowCount(request));
    }
}
