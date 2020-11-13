package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;
import edu.byu.cs.tweeter.presenter.FollowCountPresenter;
import edu.byu.cs.tweeter.presenter.MainPresenter;

public class GetFollowCountTask extends AsyncTask<FollowCountRequest, Void, FollowCountResponse> {

    private final FollowCountPresenter presenter;
    private final Observer observer;
    private Exception exception;

    public interface Observer {
        void followCountRetrieved(FollowCountResponse followCountResponse);
        void handleException(Exception exception);
    }

    public GetFollowCountTask(FollowCountPresenter presenter, Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected FollowCountResponse doInBackground(FollowCountRequest... followCountRequests) {

        FollowCountResponse response = null;

        try {
            response = presenter.getFollowCount(followCountRequests[0]);
        } catch (Exception ex) {
            exception = ex;
        }

        return response;
    }

    @Override
    protected void onPostExecute(FollowCountResponse followCountResponse) {
        if(exception != null) {
            observer.handleException(exception);
        } else {
            observer.followCountRetrieved(followCountResponse);
        }
    }
}
