package com.discoverfriend.partybear.addons;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by mukesh on 10/03/17.
 */

public class addonFragmentAdapter extends FragmentPagerAdapter {
    Fragment f = null;

    public addonFragmentAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            f = new addonFragment();
            Bundle bundle = new Bundle();
            bundle.putString("Position", "Basic");
            bundle.putString("path", "basic");
            f.setArguments(bundle);

        }
        if (position == 1) {
            f = new addonFragment();
            Bundle bundle = new Bundle();
            bundle.putString("Position", "Flower");
            bundle.putString("path", "basic");
            f.setArguments(bundle);
        }
        if (position == 2) {
            f = new addonFragment();
            Bundle bundle = new Bundle();
            bundle.putString("Position", "Cake");
            bundle.putString("path", "basic");
            f.setArguments(bundle);

        }
        if (position == 3) {
            f = new addonFragment();
            Bundle bundle = new Bundle();
            bundle.putString("Position", "Chocolate");
            bundle.putString("path", "basic");
            f.setArguments(bundle);
        }
        if (position == 4) {
            f = new addonFragment();
            Bundle bundle = new Bundle();
            bundle.putString("Position", "Teddy");
            bundle.putString("path", "basic");
            f.setArguments(bundle);

        }
        if (position == 5) {
            f = new addonFragment();
            Bundle bundle = new Bundle();
            bundle.putString("Position", "Other");
            bundle.putString("path", "basic");
            f.setArguments(bundle);

        }
        return f;
    }

    @Override
    public int getCount() {
        return 6;
    }
}
