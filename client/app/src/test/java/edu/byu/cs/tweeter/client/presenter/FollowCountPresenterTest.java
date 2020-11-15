package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.TestWithAuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FollowCountServiceProxy;
import edu.byu.cs.tweeter.model.service.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;

public class FollowCountPresenterTest extends TestWithAuthToken {

    private FollowCountRequest request;
    private FollowCountResponse response;
    private FollowCountServiceProxy mMockFollowCountServiceProxy;
    private FollowCountPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", null);

        request = new FollowCountRequest(currentUser, authToken);
        response = new FollowCountResponse(100, 27);

        mMockFollowCountServiceProxy = Mockito.mock(FollowCountServiceProxy.class);
        Mockito.when(mMockFollowCountServiceProxy.getFollowCount(request)).thenReturn(response);

        presenter = Mockito.spy(new FollowCountPresenter(new FollowCountPresenter.View() {
        }));
        Mockito.when(presenter.getFollowCountService()).thenReturn(mMockFollowCountServiceProxy);
    }

    @Test
    public void testGetFollowCount_returnsServiceResult() throws IOException, TweeterRemoteException {
        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.getFollowCount(request));
    }
}
