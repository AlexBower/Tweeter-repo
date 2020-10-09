package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;
import edu.byu.cs.tweeter.presenter.IsFollowingPresenter;

public class IsFollowingTask extends AsyncTask<IsFollowingRequest, Void, IsFollowingResponse> {

    private final IsFollowingPresenter presenter;
    private final Observer observer;
    private Exception exception;

    public interface Observer {
        void isFollowingRetrieved(IsFollowingResponse isFollowingResponse);
        void handleException(Exception exception);
    }

    public IsFollowingTask(IsFollowingPresenter presenter, Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected IsFollowingResponse doInBackground(IsFollowingRequest... isFollowingRequests) {

        IsFollowingResponse response = null;

        try {
            response = presenter.isFollowing(isFollowingRequests[0]);
        } catch (IOException ex) {
            exception = ex;
        }

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
