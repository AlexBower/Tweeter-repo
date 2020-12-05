package edu.byu.cs.tweeter.view.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;
import edu.byu.cs.tweeter.presenter.UserPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.FollowTask;
import edu.byu.cs.tweeter.view.asyncTasks.GetFollowCountTask;
import edu.byu.cs.tweeter.view.asyncTasks.IsFollowingTask;
import edu.byu.cs.tweeter.view.asyncTasks.UnfollowTask;
import edu.byu.cs.tweeter.view.util.ImageUtils;

public class UserActivity extends AppCompatActivity implements
        UserPresenter.View, GetFollowCountTask.Observer, IsFollowingTask.Observer, FollowTask.Observer, UnfollowTask.Observer {

    private static final String LOG_TAG = "UserActivity";

    public static final String CURRENT_USER_KEY = "CurrentUser";
    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    private User user;
    private AuthToken authToken;
    private ToggleButton followButton;
    private UserPagerAdapter userPagerAdapter;

    private UserPresenter presenter;

    public static Intent newIntent(Context packageContext, User user, AuthToken authToken) {
        Intent intent = new Intent(packageContext, UserActivity.class);

        intent.putExtra(CURRENT_USER_KEY, user);
        intent.putExtra(AUTH_TOKEN_KEY, authToken);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        presenter = new UserPresenter(this);

        user = (User) getIntent().getSerializableExtra(CURRENT_USER_KEY);
        if(user == null) {
            throw new RuntimeException("User not passed to activity");
        }

        authToken = (AuthToken) getIntent().getSerializableExtra(AUTH_TOKEN_KEY);

        userPagerAdapter = new UserPagerAdapter(this, getSupportFragmentManager(), user, authToken);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(userPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        TextView userName = findViewById(R.id.userName);
        userName.setText(user.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(user.getAlias());

        ImageView userImageView = findViewById(R.id.userImage);
        userImageView.setImageDrawable(ImageUtils.drawableFromByteArray(user.getImageBytes()));

        TextView followeeCount = findViewById(R.id.followeeCount);
        followeeCount.setText("Following: ");

        TextView followerCount = findViewById(R.id.followerCount);
        followerCount.setText("Followers: ");

        followButton = findViewById(R.id.followButton);
        followButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!followButton.isEnabled()) {
                    return;
                }
                followButton.setEnabled(false);
                if (isChecked) {
                    // The toggle is checked, follow user
                    FollowTask followTask = new FollowTask(presenter,
                            UserActivity.this,
                            userPagerAdapter);
                    followTask.execute(new FollowRequest(MainActivity.loggedInUser, user, authToken));
                } else {
                    // The toggle is checked, unfollow user
                    UnfollowTask unfollowTask = new UnfollowTask(presenter,
                            UserActivity.this,
                            userPagerAdapter);
                    unfollowTask.execute(new UnfollowRequest(MainActivity.loggedInUser, user, authToken));
                }
            }
        });
        followButton.setEnabled(false);
        updateFollowButton();
    }

    private void updateFollowButton() {
        IsFollowingTask isFollowingTask = new IsFollowingTask(presenter, this);
        isFollowingTask.execute(new IsFollowingRequest(MainActivity.loggedInUser, user, authToken));
    }

    private void updateFollowCount() {
        GetFollowCountTask getFollowCountTask = new GetFollowCountTask(presenter, this);
        getFollowCountTask.execute(new FollowCountRequest(user, authToken));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFollowCount();
    }

    @Override
    public void followCountRetrieved(FollowCountResponse followCountResponse) {
        TextView followeeCount = findViewById(R.id.followeeCount);
        followeeCount.setText("Following: " + followCountResponse.getFollowingCount());

        TextView followerCount = findViewById(R.id.followerCount);
        followerCount.setText("Followers: " + followCountResponse.getFollowersCount());
    }

    @Override
    public void followRetrieved(FollowResponse followResponse) {
        updateFollowCount();
        followButton.setEnabled(true);
    }

    @Override
    public void isFollowingRetrieved(IsFollowingResponse isFollowingResponse) {
        followButton.setChecked(isFollowingResponse.isFollowing());
        followButton.setEnabled(true);
    }

    @Override
    public void unfollowRetrieved(UnfollowResponse unfollowResponse) {
        updateFollowCount();
        followButton.setEnabled(true);
    }

    @Override
    public void handleException(Exception exception) {
        Log.e(LOG_TAG, exception.getMessage(), exception);
        Toast.makeText(this, "Failed to complete action because of exception: " + exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}