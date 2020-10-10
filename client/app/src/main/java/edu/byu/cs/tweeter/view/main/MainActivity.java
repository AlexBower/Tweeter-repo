package edu.byu.cs.tweeter.view.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowCountRequest;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.service.response.FollowCountResponse;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.model.service.response.PostStatusResponse;
import edu.byu.cs.tweeter.presenter.MainPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.PostStatusTask;
import edu.byu.cs.tweeter.view.login.LoginActivity;
import edu.byu.cs.tweeter.view.asyncTasks.GetFollowCountTask;
import edu.byu.cs.tweeter.view.asyncTasks.LogoutTask;
import edu.byu.cs.tweeter.view.util.ImageUtils;

/**
 * The main activity for the application. Contains tabs for feed, story, following, and followers.
 */
public class MainActivity extends AppCompatActivity implements
        MainPresenter.View, GetFollowCountTask.Observer, LogoutTask.Observer, PostStatusTask.Observer {

    private static final String LOG_TAG = "MainActivity";

    public static final String CURRENT_USER_KEY = "CurrentUser";
    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    private MainPresenter presenter;
    public static User loggedInUser;
    private AuthToken authToken;
    private SectionsPagerAdapter sectionsPagerAdapter;

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

        presenter = new MainPresenter(this);

        loggedInUser = (User) getIntent().getSerializableExtra(CURRENT_USER_KEY);
        if(loggedInUser == null) {
            throw new RuntimeException("User not passed to activity");
        }

        authToken = (AuthToken) getIntent().getSerializableExtra(AUTH_TOKEN_KEY);

        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), loggedInUser, authToken);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);

        // We should use a Java 8 lambda function for the listener (and all other listeners), but
        // they would be unfamiliar to many students who use this code.
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.post_dialog, null);
                builder.setView(dialogView);

                ImageView imageView = dialogView.findViewById(R.id.userImagePost);
                imageView.setImageDrawable(ImageUtils.drawableFromByteArray(loggedInUser.getImageBytes()));
                TextView userNamePost = dialogView.findViewById(R.id.userNamePost);
                userNamePost.setText(loggedInUser.getFirstName() + " " + loggedInUser.getLastName());
                TextView userAliasPost = dialogView.findViewById(R.id.userAliasPost);
                userAliasPost.setText(loggedInUser.getAlias());
                EditText input = dialogView.findViewById(R.id.messagePost);

                builder.setPositiveButton(R.string.postStatus, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newPostText = input.getText().toString();
                        PostStatusTask postStatusTask = new PostStatusTask(presenter, MainActivity.this, sectionsPagerAdapter);
                        postStatusTask.execute(new PostStatusRequest(
                                new Status(newPostText, LocalDateTime.now(), loggedInUser)));
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutTask logoutTask = new LogoutTask(presenter, MainActivity.this);
                logoutTask.execute(new LogoutRequest(loggedInUser, authToken));
            }
        });

        TextView userName = findViewById(R.id.userName);
        userName.setText(loggedInUser.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(loggedInUser.getAlias());

        ImageView userImageView = findViewById(R.id.userImage);
        userImageView.setImageDrawable(ImageUtils.drawableFromByteArray(loggedInUser.getImageBytes()));

        TextView followeeCount = findViewById(R.id.followeeCount);
        followeeCount.setText("Following: ");

        TextView followerCount = findViewById(R.id.followerCount);
        followerCount.setText("Followers: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetFollowCountTask getFollowCountTask = new GetFollowCountTask(presenter, this);
        getFollowCountTask.execute(new FollowCountRequest(loggedInUser));
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
    public void postStatusRetrieved(PostStatusResponse postStatusResponse) {

    }

    @Override
    public void handleException(Exception exception) {
        Log.e(LOG_TAG, exception.getMessage(), exception);
        Toast.makeText(this, "Failed to complete action because of exception: " + exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}