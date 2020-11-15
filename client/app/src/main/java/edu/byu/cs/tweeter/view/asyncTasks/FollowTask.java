package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.client.presenter.UserPresenter;

public class FollowTask extends AsyncTask<FollowRequest, Void, FollowResponse> {

    private final UserPresenter presenter;
    private final Observer[] observers;
    private Exception exception;

    public interface Observer {
        void followRetrieved(FollowResponse followResponse);
        void handleException(Exception exception);
    }

    public FollowTask(UserPresenter presenter, Observer... observers) {
        if(observers == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observers = observers;
    }

    @Override
    protected FollowResponse doInBackground(FollowRequest... followRequests) {

        FollowResponse response = null;

        try {
            response = presenter.follow(followRequests[0]);
        } catch (Exception ex) {
            exception = ex;
        }

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
