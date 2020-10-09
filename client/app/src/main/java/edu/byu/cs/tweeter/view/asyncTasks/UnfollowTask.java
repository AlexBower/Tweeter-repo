package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;
import edu.byu.cs.tweeter.presenter.UnfollowPresenter;

public class UnfollowTask extends AsyncTask<UnfollowRequest, Void, UnfollowResponse> {

    private final UnfollowPresenter presenter;
    private final Observer observer;
    private Exception exception;

    public interface Observer {
        void unfollowRetrieved(UnfollowResponse unfollowResponse);
        void handleException(Exception exception);
    }

    public UnfollowTask(UnfollowPresenter presenter, Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected UnfollowResponse doInBackground(UnfollowRequest... unfollowRequests) {

        UnfollowResponse response = null;

        try {
            response = presenter.unfollow(unfollowRequests[0]);
        } catch (IOException ex) {
            exception = ex;
        }

        return response;
    }

    @Override
    protected void onPostExecute(UnfollowResponse unfollowResponse) {
        if(exception != null) {
            observer.handleException(exception);
        } else {
            observer.unfollowRetrieved(unfollowResponse);
        }
    }
}
