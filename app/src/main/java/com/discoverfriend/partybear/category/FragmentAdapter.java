package com.discoverfriend.partybear.category;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.discoverfriend.partybear.CategoryFragment;

/**
 * Created by mukesh on 21/01/17.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    Fragment f = null;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            f = new CategoryFragment();

        }
        if (position == 1) {
            f = new CategoryFragment();
        }
        if (position == 2) {
            f = new CategoryFragment();
        }
        return f;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
