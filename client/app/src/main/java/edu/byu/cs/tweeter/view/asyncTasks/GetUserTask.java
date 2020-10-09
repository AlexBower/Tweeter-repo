package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.GetUserRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserResponse;
import edu.byu.cs.tweeter.presenter.GetUserPresenter;
import edu.byu.cs.tweeter.util.ByteArrayUtils;

public class GetUserTask extends AsyncTask<GetUserRequest, Void, GetUserResponse> {

    private final GetUserPresenter presenter;
    private final Observer observer;
    private Exception exception;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface Observer {
        void getUserSuccessful(GetUserResponse getUserResponse);
        void getUserUnsuccessful(GetUserResponse getUserResponse);
        void handleException(Exception ex);
    }

    public GetUserTask(GetUserPresenter presenter, Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected GetUserResponse doInBackground(GetUserRequest... getUserRequests) {
        GetUserResponse getUserResponse = null;

        try {
            getUserResponse = presenter.getUser(getUserRequests[0]);

            if(getUserResponse.isSuccess()) {
                loadImage(getUserResponse.getUser());
            }
        } catch (IOException ex) {
            exception = ex;
        }

        return getUserResponse;
    }

    private void loadImage(User user) {
        try {
            byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
            user.setImageBytes(bytes);
        } catch (IOException e) {
            Log.e(this.getClass().getName(), e.toString(), e);
        }
    }

    @Override
    protected void onPostExecute(GetUserResponse getUserResponse) {
        if(exception != null) {
            observer.handleException(exception);
        } else if(getUserResponse.isSuccess()) {
            observer.getUserSuccessful(getUserResponse);
        } else {
            observer.getUserUnsuccessful(getUserResponse);
        }
    }
}