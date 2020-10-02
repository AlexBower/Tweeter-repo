package edu.byu.cs.tweeter.view.main.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.presenter.StoryPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.GetStoryTask;

public class StoryFragment extends StatusFragment implements StoryPresenter.View {

    private static final String LOG_TAG = "StoryFragment";

    private StoryPresenter presenter;

    private StoryRecyclerViewAdapter storyRecyclerViewAdapter;

    /**
     * Creates an instance of the fragment and places the user and auth token in an arguments
     * bundle assigned to the fragment.
     *
     * @param user the logged in user.
     * @param authToken the auth token for this user's session.
     * @return the fragment.
     */
    public static StoryFragment newInstance(User user, AuthToken authToken) {
        StoryFragment fragment = new StoryFragment();

        Bundle args = new Bundle(2);
        args.putSerializable(USER_KEY, user);
        args.putSerializable(AUTH_TOKEN_KEY, authToken);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);

        //noinspection ConstantConditions
        user = (User) getArguments().getSerializable(USER_KEY);
        authToken = (AuthToken) getArguments().getSerializable(AUTH_TOKEN_KEY);

        presenter = new StoryPresenter(this);

        RecyclerView statusRecyclerView = view.findViewById(R.id.statusRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        statusRecyclerView.setLayoutManager(layoutManager);

        storyRecyclerViewAdapter = new StoryFragment.StoryRecyclerViewAdapter();
        statusRecyclerView.setAdapter(storyRecyclerViewAdapter);

        statusRecyclerView.addOnScrollListener(new StatusRecyclerViewPaginationScrollListener(layoutManager));

        return view;
    }

    private class StoryRecyclerViewAdapter extends StoryFragment.StatusRecyclerViewAdapter implements GetStoryTask.Observer {

        void doLoadMoreItems() {
            GetStoryTask getStoryTask = new GetStoryTask(presenter, this);
            StoryRequest request = new StoryRequest(user, PAGE_SIZE, lastStatus);
            getStoryTask.execute(request);
        }

        @Override
        public void statusesRetrieved(StoryResponse storyResponse) {
            List<Status> statuses = storyResponse.getStatuses();

            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() -1) : null;
            hasMorePages = storyResponse.getHasMorePages();

            isLoading = false;
            removeLoadingFooter();
            storyRecyclerViewAdapter.addItems(statuses);
        }


        @Override
        public void handleException(Exception exception) {
            Log.e(LOG_TAG, exception.getMessage(), exception);
            removeLoadingFooter();
            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private class StatusRecyclerViewPaginationScrollListener extends RecyclerView.OnScrollListener {

        private final LinearLayoutManager layoutManager;

        /**
         * Creates a new instance.
         *
         * @param layoutManager the layout manager being used by the RecyclerView.
         */
        StatusRecyclerViewPaginationScrollListener(LinearLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!storyRecyclerViewAdapter.isLoading && storyRecyclerViewAdapter.hasMorePages) {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    storyRecyclerViewAdapter.loadMoreItems();
                }
            }
        }
    }
}
