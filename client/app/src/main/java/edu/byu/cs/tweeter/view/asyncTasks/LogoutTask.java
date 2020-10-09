package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.presenter.LogoutPresenter;

public class LogoutTask extends AsyncTask<LogoutRequest, Void, LogoutResponse> {

    private final LogoutPresenter presenter;
    private final Observer observer;
    private Exception exception;

    public interface Observer {
        void logoutRetrieved(LogoutResponse logoutResponse);

        void handleException(Exception exception);
    }

    public LogoutTask(LogoutPresenter presenter, Observer observer) {
        if (observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected LogoutResponse doInBackground(LogoutRequest... logoutRequests) {

        LogoutResponse response = null;

        try {
            response = presenter.logout(logoutRequests[0]);
        } catch (IOException ex) {
            exception = ex;
        }

        return response;
    }

    @Override
    protected void onPostExecute(LogoutResponse logoutResponse) {
        if (exception != null) {
            observer.handleException(exception);
        } else {
            observer.logoutRetrieved(logoutResponse);
        }
    }
}