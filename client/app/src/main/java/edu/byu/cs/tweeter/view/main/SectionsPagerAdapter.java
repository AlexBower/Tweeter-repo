package edu.byu.cs.tweeter.view.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.view.main.follow.FollowFragment;
import edu.byu.cs.tweeter.view.main.follow.FollowerFragment;
import edu.byu.cs.tweeter.view.main.follow.FollowingFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to one of the sections/tabs/pages
 * of the Main Activity.
 */
class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final int FOLLOWING_FRAGMENT_POSITION = 2;
    private static final int FOLLOWER_FRAGMENT_POSITION = 3;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.feedTabTitle, R.string.storyTabTitle, R.string.followingTabTitle, R.string.followersTabTitle};
    private final Context mContext;
    private final User user;
    private final AuthToken authToken;

    public SectionsPagerAdapter(Context context, FragmentManager fm, User user, AuthToken authToken) {
        super(fm);
        mContext = context;
        this.user = user;
        this.authToken = authToken;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == FOLLOWING_FRAGMENT_POSITION) {
            FollowingFragment followingFragment = FollowingFragment.newInstance(user, authToken);
            return followingFragment;
        } else if (position == FOLLOWER_FRAGMENT_POSITION) {
            FollowerFragment followerFragment = FollowerFragment.newInstance(user, authToken);
            return followerFragment;
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
        // Show 4 total pages.
        return 4;
    }
}