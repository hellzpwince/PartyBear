package com.discoverfriend.partybear.addons;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.discoverfriend.partybear.R;
import com.gigamole.navigationtabstrip.NavigationTabStrip;

public class Addons extends AppCompatActivity {
    Toolbar toolbar;
    addonFragmentAdapter mFragmentAdapter;
    ViewPager mViewpager;
    NavigationTabStrip navigationTabStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addons);
        setupToolbar();
        setupNavigationStrip();
        setupViewpager();
    }

    public void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Addons");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.addonscroll_view);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setFillViewport(true);
    }

    public void setupViewpager() {
        mFragmentAdapter = new addonFragmentAdapter(getSupportFragmentManager());
        mViewpager = (ViewPager) findViewById(R.id.addonViewpager);
        mViewpager.setAdapter(mFragmentAdapter);
        mViewpager.setCurrentItem(0);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                navigationTabStrip.setTabIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setupNavigationStrip() {
        navigationTabStrip = (NavigationTabStrip) findViewById(R.id.addonTabStrip);
        navigationTabStrip.setTitles("Basic", "Flower", "Cake", "Chocolate", "Teddy", "Other");
        navigationTabStrip.setTabIndex(0, true);
        navigationTabStrip.setTitleSize(20);
        navigationTabStrip.setStripColor(Color.RED);
        navigationTabStrip.setStripWeight(6);
        navigationTabStrip.setStripFactor(2);
        navigationTabStrip.setStripType(NavigationTabStrip.StripType.LINE);
        navigationTabStrip.setStripGravity(NavigationTabStrip.StripGravity.BOTTOM);
        navigationTabStrip.setCornersRadius(3);
        navigationTabStrip.setAnimationDuration(300);
        navigationTabStrip.setInactiveColor(Color.GRAY);
        navigationTabStrip.setActiveColor(Color.RED);
        navigationTabStrip.setOnTabStripSelectedIndexListener(new NavigationTabStrip.OnTabStripSelectedIndexListener() {
            @Override
            public void onStartTabSelected(String title, int index) {
                mViewpager.setCurrentItem(index);
            }

            @Override
            public void onEndTabSelected(String title, int index) {

            }
        });
    }

}
