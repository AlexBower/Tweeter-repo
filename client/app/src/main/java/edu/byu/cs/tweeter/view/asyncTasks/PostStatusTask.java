package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.service.response.PostStatusResponse;
import edu.byu.cs.tweeter.presenter.PostStatusPresenter;

public class PostStatusTask extends AsyncTask<PostStatusRequest, Void, PostStatusResponse> {

    private final PostStatusPresenter presenter;
    private final Observer[] observers;
    private Exception exception;

    public interface Observer {
        void postStatusRetrieved(PostStatusResponse postStatusResponse);
        void handleException(Exception exception);
    }

    public PostStatusTask(PostStatusPresenter presenter, Observer... observers) {
        if(observers == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observers = observers;
    }

    @Override
    protected PostStatusResponse doInBackground(PostStatusRequest... postStatusRequests) {

        PostStatusResponse response = null;
        response = presenter.postStatus(postStatusRequests[0]);

        return response;
    }

    @Override
    protected void onPostExecute(PostStatusResponse postStatusResponse) {
        if(exception != null) {
            for (Observer observer : observers) {
                observer.handleException(exception);
            }
        } else {
            for (Observer observer : observers) {
                observer.postStatusRetrieved(postStatusResponse);
            }
        }
    }
}
