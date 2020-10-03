package edu.byu.cs.tweeter.view.main.fragments;

import androidx.fragment.app.Fragment;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

abstract public class PaginatedFragment extends Fragment {
    protected static final String USER_KEY = "UserKey";

    protected User user;

    protected static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    protected static final int LOADING_DATA_VIEW = 0;
    protected static final int ITEM_VIEW = 1;

    protected static final int PAGE_SIZE = 10;

    protected AuthToken authToken;
}
