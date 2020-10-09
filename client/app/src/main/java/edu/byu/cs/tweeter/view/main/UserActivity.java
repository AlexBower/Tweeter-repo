package edu.byu.cs.tweeter.view.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;
import edu.byu.cs.tweeter.presenter.FollowCountPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.GetFollowCountTask;
import edu.byu.cs.tweeter.view.util.ImageUtils;

public class UserActivity extends AppCompatActivity
        implements FollowCountPresenter.View, GetFollowCountTask.Observer {

    private static final String LOG_TAG = "UserActivity";

    public static final String CURRENT_USER_KEY = "CurrentUser";
    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    private FollowCountPresenter followCountPresenter;
    private User user;
    private AuthToken authToken;

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

        followCountPresenter = new FollowCountPresenter(this);

        user = (User) getIntent().getSerializableExtra(CURRENT_USER_KEY);
        if(user == null) {
            throw new RuntimeException("User not passed to activity");
        }

        authToken = (AuthToken) getIntent().getSerializableExtra(AUTH_TOKEN_KEY);

        UserPagerAdapter userPagerAdapter = new UserPagerAdapter(this, getSupportFragmentManager(), user, authToken);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetFollowCountTask getFollowCountTask = new GetFollowCountTask(followCountPresenter, this);
        getFollowCountTask.execute(new FollowCountRequest(user));
    }

    @Override
    public void followCountRetrieved(FollowCountResponse followCountResponse) {
        TextView followeeCount = findViewById(R.id.followeeCount);
        followeeCount.setText("Following: " + followCountResponse.getFollowingCount());

        TextView followerCount = findViewById(R.id.followerCount);
        followerCount.setText("Followers: " + followCountResponse.getFollowersCount());
    }

    @Override
    public void handleException(Exception exception) {
        Log.e(LOG_TAG, exception.getMessage(), exception);
        Toast.makeText(this, "Failed to complete action because of exception: " + exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}