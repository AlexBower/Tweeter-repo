package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;
import edu.byu.cs.tweeter.presenter.UserPresenter;

public class IsFollowingTask extends AsyncTask<IsFollowingRequest, Void, IsFollowingResponse> {

    private final UserPresenter presenter;
    private final Observer observer;
    private Exception exception;

    public interface Observer {
        void isFollowingRetrieved(IsFollowingResponse isFollowingResponse);
        void handleException(Exception exception);
    }

    public IsFollowingTask(UserPresenter presenter, Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected IsFollowingResponse doInBackground(IsFollowingRequest... isFollowingRequests) {
        IsFollowingResponse response = null;
        response = presenter.isFollowing(isFollowingRequests[0]);
        return response;
    }

    @Override
    protected void onPostExecute(IsFollowingResponse isFollowingResponse) {
        if(exception != null) {
            observer.handleException(exception);
        } else {
            observer.isFollowingRetrieved(isFollowingResponse);
        }
    }
}
