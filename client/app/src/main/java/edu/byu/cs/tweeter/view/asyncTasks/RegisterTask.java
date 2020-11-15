package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.client.presenter.RegisterPresenter;

public class RegisterTask extends AsyncTask<RegisterRequest, Void, RegisterResponse> {

    private final RegisterPresenter presenter;
    private final Observer observer;
    private Exception exception;

    public interface Observer {
        void registerSuccessful(RegisterResponse registerResponse);
        void registerUnsuccessful(RegisterResponse registerResponse);
        void handleException(Exception ex);
    }

    public RegisterTask(RegisterPresenter presenter, Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected RegisterResponse doInBackground(RegisterRequest... registerRequests) {
        RegisterResponse registerResponse = null;

        try {
            registerResponse = presenter.register(registerRequests[0]);
        } catch (Exception ex) {
            exception = ex;
        }

        return registerResponse;
    }

    @Override
    protected void onPostExecute(RegisterResponse registerResponse) {
        if(exception != null) {
            observer.handleException(exception);
        } else if(registerResponse.isSuccess()) {
            observer.registerSuccessful(registerResponse);
        } else {
            observer.registerUnsuccessful(registerResponse);
        }
    }
}
