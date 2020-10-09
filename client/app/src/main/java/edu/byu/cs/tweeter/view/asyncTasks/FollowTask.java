package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.presenter.FollowPresenter;

public class FollowTask extends AsyncTask<FollowRequest, Void, FollowResponse> {

    private final FollowPresenter presenter;
    private final Observer[] observers;
    private Exception exception;

    public interface Observer {
        void followRetrieved(FollowResponse followResponse);
        void handleException(Exception exception);
    }

    public FollowTask(FollowPresenter presenter, Observer... observers) {
        if(observers == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observers = observers;
    }

    @Override
    protected FollowResponse doInBackground(FollowRequest... followRequests) {

        FollowResponse response = null;

        response = presenter.follow(followRequests[0]);

        return response;
    }

    @Override
    protected void onPostExecute(FollowResponse followResponse) {
        if(exception != null) {
            for (Observer observer : observers) {
                observer.handleException(exception);
            }
        } else {
            for (Observer observer : observers) {
                observer.followRetrieved(followResponse);
            }
        }
    }
}
