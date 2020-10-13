package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.StatusService;
import edu.byu.cs.tweeter.model.service.request.StatusRequest;
import edu.byu.cs.tweeter.model.service.response.StatusResponse;

public class StoryPresenterTest {

    private StatusRequest request;
    private StatusResponse response;
    private StatusService mockStatusService;
    private StoryPresenter presenter;

    @BeforeEach
    public void setup() throws IOException {
        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        Status resultStatus1 = new Status("Test Message1", LocalDateTime.now(), resultUser1);
        Status resultStatus2 = new Status("Test Message2", LocalDateTime.now(), resultUser2);
        Status resultStatus3 = new Status("Test Message3", LocalDateTime.now(), resultUser3);

        request = new StatusRequest(currentUser, 3, null);
        response = new StatusResponse(Arrays.asList(resultStatus1, resultStatus2, resultStatus3), false);

        mockStatusService = Mockito.mock(StatusService.class);
        Mockito.when(mockStatusService.getStory(request)).thenReturn(response);

        presenter = Mockito.spy(new StoryPresenter(new StoryPresenter.View() {}));
        Mockito.when(presenter.getStatusService()).thenReturn(mockStatusService);
    }

    @Test
    public void testGetStory_returnsServiceResult() throws IOException {
        // Assert that the presenter returns the same response as the service
        Assertions.assertEquals(response, presenter.getStory(request));
    }

    @Test
    public void testGetStory_serviceThrowsIOException_presenterThrowsIOException() throws IOException {
        Mockito.when(mockStatusService.getStory(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getStory(request);
        });
    }
}