package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.model.service.request.StatusRequest;
import edu.byu.cs.tweeter.model.service.response.StatusResponse;
import edu.byu.cs.tweeter.client.presenter.FeedPresenter;

public class GetFeedTask extends AsyncTask<StatusRequest, Void, StatusResponse> {

    private final FeedPresenter presenter;
    private final Observer observer;
    private Exception exception;

    public interface Observer {
        void statusesRetrieved(StatusResponse statusResponse);
        void handleException(Exception exception);
    }

    public GetFeedTask(FeedPresenter presenter, Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected StatusResponse doInBackground(StatusRequest... statusRequests) {

        StatusResponse response = null;

        try {
            response = presenter.getFeed(statusRequests[0]);
        } catch (Exception ex) {
            exception = ex;
        }

        return response;
    }

    @Override
    protected void onPostExecute(StatusResponse statusResponse) {
        if(exception != null) {
            observer.handleException(exception);
        } else {
            observer.statusesRetrieved(statusResponse);
        }
    }
}
