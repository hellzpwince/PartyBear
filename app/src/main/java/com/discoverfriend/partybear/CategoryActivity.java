package com.discoverfriend.partybear;

import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.discoverfriend.partybear.category.FragmentAdapter;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import static android.R.attr.fragment;
import static android.R.attr.value;

public class CategoryActivity extends AppCompatActivity {
    Bundle bundle;
    /*initialize objects here*/
    private String categoryid;
    private String categoryname;
    private Toolbar toolbar;
    private ViewPager mViewpager;
    private FragmentAdapter mFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Assign views here*/
        setContentView(R.layout.activity_category);
        /*Assigning Toolbar*/
        try {
            categoryid = getIntent().getExtras().getString("categoryid");
            categoryname = getIntent().getExtras().getString("categoryname");
            bundle = new Bundle();
            bundle.putString("categoryid", categoryid);
            bundle.putString("categoryname", categoryname);
            toolbar = (Toolbar) findViewById(R.id.app_bar);
            toolbar.setTitle(categoryname);
        } catch (Exception e) {
            Log.e("CatActiveError", e.getMessage());
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.categoryscroll_view);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setFillViewport(true);
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), bundle);
        mViewpager = (ViewPager) findViewById(R.id.categoryViewPager);
        mViewpager.setAdapter(mFragmentAdapter);
        mViewpager.setCurrentItem(0);
        final BottomBar bottomBar = (BottomBar) findViewById(R.id.categorybottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_favorites) {
                    mViewpager.setCurrentItem(1);
                }
                if (tabId == R.id.tab_bakecake) {
                    mViewpager.setCurrentItem(0);
                }
                mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        int page = position;
                        if (page == 0) {
                            bottomBar.selectTabAtPosition(0);

                        }
                        if (page == 1) {
                            bottomBar.selectTabAtPosition(1);
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
        /*wrtie code here */


            }
        });
    }
}
