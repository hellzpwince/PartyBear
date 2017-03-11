package com.discoverfriend.partybear;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.discoverfriend.partybear.category.CategoryModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.florent37.viewanimator.ViewAnimator;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
public class SearchActivity extends AppCompatActivity {
    FloatingSearchView searchbar;
    Toolbar toolbar;
    Query mQuery;
    DatabaseReference mroot;
    SpinKitView progressBar;
    Context ctx;
    RecyclerView resultRCV;
    ProgressDialog dialog;
    FirebaseRecyclerAdapter<CategoryModel, MainActivity.chefViewHolder> categoryCardRecycler;
    FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        ctx = SearchActivity.this;
        dialog = new ProgressDialog(ctx);
        dialog.setMessage("Searching..");
        setupToolbar();
        Bundle bundle = getIntent().getExtras();
        progressBar = (SpinKitView) findViewById(R.id.search_progress_bars);
        resultRCV = (RecyclerView) findViewById(R.id.search_RCView);
        resultRCV.setNestedScrollingEnabled(false);
        GridLayoutManager grid = new GridLayoutManager(ctx, 2);
        resultRCV.setLayoutManager(grid);
        searchbar = (FloatingSearchView) findViewById(R.id.floating_search_view);
        if (bundle.containsKey("string")) {
            searchbar.setSearchText(bundle.get("string").toString());
            generateSearchResult(toTitleCase(bundle.get("string").toString()));
        }
        searchbar.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                dialog.show();
                generateSearchResult(toTitleCase(currentQuery));
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, currentQuery);
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);
            }
        });

    }


    public void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }


    public void generateSearchResult(String Query) {
        progressBar.setVisibility(View.VISIBLE);
        mroot = FirebaseDatabase.getInstance().getReference();
        mQuery = mroot.child("products").orderByChild("name").startAt(Query).limitToFirst(30);
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    Toast.makeText(SearchActivity.this, "Nothing Found", Toast.LENGTH_SHORT).show();
                } else {
                    categoryCardRecycler = new FirebaseRecyclerAdapter<CategoryModel, MainActivity.chefViewHolder>(
                            CategoryModel.class,
                            R.layout.home_grid_cardview,
                            MainActivity.chefViewHolder.class,
                            mQuery
                    ) {

                        @Override
                        protected void populateViewHolder(final MainActivity.chefViewHolder viewHolder, CategoryModel model, final int position) {
                            final String post_key = getRef(position).getKey();
                            try {
                                viewHolder.setTitle(model.getName());
                                viewHolder.setPrice(model.getPrice());
                                if (model.getBaseprice() != 0) {
                                    viewHolder.setBasePrice(model.getBaseprice());
                                }
                                viewHolder.setImage(model.getImageurl());
                                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        viewHolder.startProductActivity(ctx, post_key);
                                    }
                                });
                                dialog.dismiss();
                            } catch (DatabaseException e) {
                                Log.e("Try Error", "Grid Category Error Logged.");
                            }
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        protected void onDataChanged() {
                            super.onDataChanged();
                        }
                    };
                    resultRCV.setAdapter(categoryCardRecycler);


                    ViewAnimator.animate(resultRCV).alpha(100).fadeIn().duration(300).start();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageLoader.getInstance().clearMemoryCache();
        ctx = null;
    }
}
