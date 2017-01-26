package com.discoverfriend.partybear.category;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.discoverfriend.partybear.CategoryFragment;
import com.discoverfriend.partybear.GridCategoryFragment;

/**
 * Created by mukesh on 21/01/17.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    Fragment f = null;
    Bundle mbundle;

    public FragmentAdapter(FragmentManager fm, Bundle bundle) {
        super(fm);
        this.mbundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            f = new GridCategoryFragment();
            f.setArguments(this.mbundle);

        }
        if (position == 1) {
            f = new CategoryFragment();
            f.setArguments(this.mbundle);
        }
        return f;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
