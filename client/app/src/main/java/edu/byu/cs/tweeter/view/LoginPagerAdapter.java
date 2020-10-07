package edu.byu.cs.tweeter.view;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.view.main.PlaceholderFragment;
import edu.byu.cs.tweeter.view.main.fragments.FeedFragment;
import edu.byu.cs.tweeter.view.main.fragments.FollowerFragment;
import edu.byu.cs.tweeter.view.main.fragments.FollowingFragment;
import edu.byu.cs.tweeter.view.main.fragments.StoryFragment;

public class LoginPagerAdapter extends FragmentPagerAdapter {

    private static final int LOGIN_FRAGMENT_POSITION = 0;
    private static final int REGISTER_FRAGMENT_POSITION = 1;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.loginTabTitle, R.string.registerTabTitle};
    private final Context mContext;

    public LoginPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == LOGIN_FRAGMENT_POSITION) {
            return LoginFragment.newInstance();
        } else if (position == REGISTER_FRAGMENT_POSITION) {
            return RegisterFragment.newInstance();
        } else {
            return PlaceholderFragment.newInstance(position + 1);
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}