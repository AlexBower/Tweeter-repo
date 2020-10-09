package edu.byu.cs.tweeter.view.main.fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.GetUserRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserResponse;
import edu.byu.cs.tweeter.presenter.GetUserPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.GetUserTask;
import edu.byu.cs.tweeter.view.main.MainActivity;
import edu.byu.cs.tweeter.view.main.UserActivity;

abstract public class PaginatedFragment extends Fragment implements GetUserPresenter.View, GetUserTask.Observer {
    private static final String LOG_TAG = "PaginatedFragment";

    protected static final String USER_KEY = "UserKey";

    protected User user;

    protected static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    protected static final int LOADING_DATA_VIEW = 0;
    protected static final int ITEM_VIEW = 1;

    protected static final int PAGE_SIZE = 10;

    protected AuthToken authToken;

    GetUserPresenter getUserPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserPresenter = new GetUserPresenter(this);
    }

    protected void userClicked(String userAlias) {
        if (userAlias != null) {
            if (userAlias.equals(MainActivity.loggedInUser.getAlias())) {
                startActivity(MainActivity.newIntent(
                        getContext(),
                        MainActivity.loggedInUser,
                        authToken));
                return;
            }
            GetUserTask getUserTask = new GetUserTask(getUserPresenter, this);
            getUserTask.execute(new GetUserRequest(userAlias));
        }
    }

    @Override
    public void getUserSuccessful(GetUserResponse getUserResponse) {
        startActivity(UserActivity.newIntent(getContext(), getUserResponse.getUser(), authToken));
    }

    @Override
    public void getUserUnsuccessful(GetUserResponse getUserResponse) {
        Toast.makeText(getContext(), getUserResponse.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleException(Exception ex) {
        Log.e(LOG_TAG, ex.getMessage(), ex);
        Toast.makeText(getContext(), "Failed to complete action because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
    }
}
