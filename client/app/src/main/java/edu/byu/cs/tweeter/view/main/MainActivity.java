package edu.byu.cs.tweeter.view.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.presenter.FollowCountPresenter;
import edu.byu.cs.tweeter.presenter.LogoutPresenter;
import edu.byu.cs.tweeter.view.login.LoginActivity;
import edu.byu.cs.tweeter.view.asyncTasks.GetFollowCountTask;
import edu.byu.cs.tweeter.view.asyncTasks.LogoutTask;
import edu.byu.cs.tweeter.view.util.ImageUtils;

/**
 * The main activity for the application. Contains tabs for feed, story, following, and followers.
 */
public class MainActivity
        extends AppCompatActivity
        implements FollowCountPresenter.View, GetFollowCountTask.Observer,
            LogoutPresenter.View, LogoutTask.Observer {

    private static final String LOG_TAG = "MainActivity";

    public static final String CURRENT_USER_KEY = "CurrentUser";
    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    private FollowCountPresenter followCountPresenter;
    private LogoutPresenter logoutPresenter;
    public static User user;
    private AuthToken authToken;

    public static Intent newIntent(Context packageContext, User user, AuthToken authToken) {
        Intent intent = new Intent(packageContext, MainActivity.class);

        intent.putExtra(CURRENT_USER_KEY, user);
        intent.putExtra(AUTH_TOKEN_KEY, authToken);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        followCountPresenter = new FollowCountPresenter(this);
        logoutPresenter = new LogoutPresenter(this);

        user = (User) getIntent().getSerializableExtra(CURRENT_USER_KEY);
        if(user == null) {
            throw new RuntimeException("User not passed to activity");
        }

        authToken = (AuthToken) getIntent().getSerializableExtra(AUTH_TOKEN_KEY);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), user, authToken);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);

        // We should use a Java 8 lambda function for the listener (and all other listeners), but
        // they would be unfamiliar to many students who use this code.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutTask logoutTask = new LogoutTask(logoutPresenter, MainActivity.this);
                logoutTask.execute(new LogoutRequest(user, authToken));
            }
        });

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
    public void logoutRetrieved(LogoutResponse logoutResponse) {
        Toast.makeText(this, "Logout successful!", Toast.LENGTH_LONG).show();
        startActivity(LoginActivity.newIntent(this));
    }

    @Override
    public void handleException(Exception exception) {
        Log.e(LOG_TAG, exception.getMessage(), exception);
        Toast.makeText(this, "Failed to complete action because of exception: " + exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}