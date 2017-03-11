package com.discoverfriend.partybear;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.discoverfriend.partybear.Product.ProductActivity;
import com.discoverfriend.partybear.Seperate_list.ListActivity;
import com.discoverfriend.partybear.addons.Addons;
import com.discoverfriend.partybear.category.CategoryModel;
import com.discoverfriend.partybear.order_processing.OrderLayoutActivity;
import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.florent37.viewanimator.ViewAnimator;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.onurciner.toastox.ToastOX;
import com.squareup.picasso.Picasso;
import com.webianks.easy_feedback.EasyFeedback;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    SliderLayout sliderShow;
    DatabaseReference rootRef;
    Query flower;
    Query category;
    Query giftQuery, myquery, categoryQuery, dealQuery, recentQuery, celebrateQuery;
    FirebaseUser user;
    Menu hmenu;
    MenuItem item;
    Button headLoginbtn;
    Map<String, Object> homepageLayout = new HashMap<String, Object>();
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView nav_view;
    private DrawerLayout mdrawerLayout;
    private RecyclerView myRecycleView;
    private RecyclerView recentRecycleView;
    private RecyclerView flowerRecycleView;
    private RecyclerView giftRecycleView;
    private RecyclerView categoryRecycleView;
    private RecyclerView dealRecycleView;
    private RecyclerView celebrateRecycleView;
    private NestedScrollView scrollview;
    private ImageView homeBanner2;
    private TextView profileName;
    private FirebaseAuth mAuth;
    RecyclerView mRecycleView;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private View hView;
    private ImageView profileImage;
    FloatingSearchView mSearchView;
    FirebaseRecyclerAdapter<CategoryModel, chefViewHolder> categoryCardRecycler;
    FirebaseRecyclerAdapter<RecentModel, RecentViewHolder> recentCardRecycler;
    FirebaseRecyclerAdapter<CategoryModel, CategoryFragment.categoryCardViewHolder> home_categoryCardRecycler;
    FirebaseRecyclerAdapter<CategoryModel, CategoryFragment.categoryCardViewHolder> dealCardRecycler;
    FirebaseRecyclerAdapter<CakeItemModel, categoryViewHolder> firebaseRecycler;
    FirebaseRecyclerAdapter<CakeItemModel, categoryViewHolder> flowerFirebaseRecycler;
    FirebaseRecyclerAdapter<CakeItemModel, categoryViewHolder> giftfirebaseRecycler;
    FirebaseRecyclerAdapter<CakeItemModel, categoryViewHolder> celebrateFirebaseRecycler;
    ValueEventListener homepageListner;
    ValueEventListener recentQueryListner;
    ValueEventListener myQueryListner;
    ValueEventListener flowerQueryListner;
    ValueEventListener giftQueryListner;
    ValueEventListener categoryQueryListner;
    ValueEventListener dealQueryListner;
    ValueEventListener celebrateQueryListner;
    FloatingSearchView.OnSearchListener mSearchViewListner;
    DatabaseReference homepageRef;
    Context ctx = this;
    LinearLayoutManager layoutManager;
    LinearLayoutManager recentLayoutManager;
    LinearLayoutManager flowerLayoutManager;
    LinearLayoutManager giftLayoutManager;
    LinearLayoutManager CategoryLayoutManager;
    LinearLayoutManager celebrateLayoutManager;
    RelativeLayout HandpickedLayout;
    RelativeLayout recentLayout;
    GridLayoutManager grid;
    GoogleApiClient mGoogleApiClient;
    URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Build GoogleApiClient with AppInvite API for receiving deep links
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(AppInvite.API)
                .build();

        // Check if this app was launched from a deep link. Setting autoLaunchDeepLink to true
        // would automatically launch the deep link if one is found.
        boolean autoLaunchDeepLink = false;
        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, autoLaunchDeepLink)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(@NonNull AppInviteInvitationResult result) {
                                if (result.getStatus().isSuccess()) {
                                    // Extract deep link from Intent
                                    Intent intent = result.getInvitationIntent();
                                    String deepLink = AppInviteReferral.getDeepLink(intent);
                                    try {
                                        url = new URL(deepLink);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }
                                    Map<String, String> parameters = null;
                                    try {
                                        parameters = splitQuery(url);
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    if (parameters.containsKey("product")) {
                                        Intent activity = new Intent(ctx, ProductActivity.class);
                                        activity.putExtra("post_key", parameters.get("product"));
                                        startActivity(activity);
                                    }
                                } else {

                                }
                            }
                        });


        layoutManager
                = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);
        flowerLayoutManager
                = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);
        giftLayoutManager
                = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);
        recentLayoutManager
                = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);
        celebrateLayoutManager
                = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);
        FoldingCube foldingCube = new FoldingCube();
        final SpinKitView homeProgress = (SpinKitView) findViewById(R.id.mainpage_spin_kit);
        final SpinKitView mProgress = (SpinKitView) findViewById(R.id.handpicked_progressbar);
        final SpinKitView dealProgressbar = (SpinKitView) findViewById(R.id.deal_progressbar);
        final SpinKitView mCategoryProgressbar = (SpinKitView) findViewById(R.id.category_progressbar);
        final SpinKitView mRecentProgressbar = (SpinKitView) findViewById(R.id.recent_progressbar);
        final SpinKitView mCakeProgressbar = (SpinKitView) findViewById(R.id.ocassion_progressbar);
        final SpinKitView mFlowerProgressbar = (SpinKitView) findViewById(R.id.flower_progressbar);
        final SpinKitView mGiftProgressbar = (SpinKitView) findViewById(R.id.gift_progressbar);

        //homeProgress.setVisibility(View.VISIBLE);
        mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        mSearchViewListner = new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
            }

            @Override
            public void onSearchAction(String currentQuery) {
                String message = currentQuery;
                mSearchView.setSearchText("");
                mSearchView.setSearchHint("Search Product");
                Intent search = new Intent(ctx, SearchActivity.class);
                search.putExtra("string", currentQuery);
                startActivity(search);
            }
        };
        mSearchView.setOnSearchListener(mSearchViewListner);

        celebrateRecycleView = (RecyclerView) findViewById(R.id.celebrate_recycleView);
        mRecycleView = (RecyclerView) findViewById(R.id.handpicked_recycleView);
        recentRecycleView = (RecyclerView) findViewById(R.id.recent_recycleView);
        dealRecycleView = (RecyclerView) findViewById(R.id.deal_recycleView);
        categoryRecycleView = (RecyclerView) findViewById(R.id.category_recycleView);
        HandpickedLayout = (RelativeLayout) findViewById(R.id.handpicked_layout);
        recentLayout = (RelativeLayout) findViewById(R.id.recent_layout);
        ViewAnimator.animate(HandpickedLayout).alpha(100).start();
        ViewAnimator.animate(recentLayout).alpha(100).startDelay(200).slideBottom().decelerate().start();
        rootRef = FirebaseDatabase.getInstance().getReference();
        myquery = rootRef.child("categories").child("handpicked").child("products").orderByKey();
        myQueryListner = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    HandpickedLayout.setVisibility(View.GONE);
                    mRecycleView.setVisibility(View.GONE);
                    mProgress.setVisibility(View.GONE);
                } else {

                    categoryCardRecycler = new FirebaseRecyclerAdapter<CategoryModel, chefViewHolder>(
                            CategoryModel.class,
                            R.layout.home_grid_cardview,
                            chefViewHolder.class,
                            myquery
                    ) {

                        @Override
                        protected void populateViewHolder(final chefViewHolder viewHolder, CategoryModel model, final int position) {
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
                            } catch (Exception e) {
                                Log.e("Try Error", "Grid Category Error Logged.");
                            }
                        }

                        @Override
                        protected void onDataChanged() {
                            super.onDataChanged();
                        }
                    };

                    mRecycleView.setAdapter(categoryCardRecycler);
                    grid = new GridLayoutManager(ctx, 2);
                    mProgress.setVisibility(View.GONE);
                    ViewAnimator.animate(mRecycleView).alpha(100).fadeIn().duration(300).start();
                    mRecycleView.setLayoutManager(grid);


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myquery.addListenerForSingleValueEvent(myQueryListner);
        categoryQuery = rootRef.child("categories").child("kids").child("products").orderByKey().limitToFirst(10);
        categoryQueryListner = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    categoryRecycleView.setVisibility(View.GONE);
                    mCategoryProgressbar.setVisibility(View.GONE);
                    RelativeLayout error_screen = (RelativeLayout) findViewById(R.id.handpicked_layout);
                    error_screen.setVisibility(View.GONE);
                } else {

                    home_categoryCardRecycler = new FirebaseRecyclerAdapter<CategoryModel, CategoryFragment.categoryCardViewHolder>(
                            CategoryModel.class,
                            R.layout.home_category_cardview,
                            CategoryFragment.categoryCardViewHolder.class,
                            categoryQuery
                    ) {

                        @Override
                        protected void populateViewHolder(final CategoryFragment.categoryCardViewHolder viewHolder, CategoryModel model, final int position) {
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
                            } catch (Exception e) {
                                Log.e("Try Error", "Grid Category Error Logged.");
                            }
                        }

                        @Override
                        protected void onDataChanged() {
                            super.onDataChanged();
                        }
                    };

                    categoryRecycleView.setAdapter(home_categoryCardRecycler);
                    CategoryLayoutManager
                            = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);
                    mCategoryProgressbar.setVisibility(View.GONE);
                    ViewAnimator.animate(categoryRecycleView).alpha(100).fadeIn().duration(300).start();
                    categoryRecycleView.setLayoutManager(CategoryLayoutManager);


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        categoryQuery.addListenerForSingleValueEvent(categoryQueryListner);
        scrollview = (NestedScrollView) findViewById(R.id.scroll_view);
        sliderShow = (SliderLayout) findViewById(R.id.slider);
        /*Fetching homepage layout details*/
        homepageRef = rootRef.child("homepage");
        homepageListner = new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    try {
                        for (DataSnapshot subchild : child.getChildren()) {
                            homepageLayout.put(child.getKey() + subchild.getKey(), subchild.getValue());
                        }

                    } catch (DatabaseException e) {
                        Log.e("Homepage Layout", "Error Caused while fetching homepage layout data");
                    }
                    if (child.getKey().equals("heroslider")) {
                        for (final DataSnapshot slide : child.getChildren()) {
                            try {
                                sliderShow.addSlider(new ImageSlider(ctx).image((String) slide.child("imageurl").getValue()).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(BaseSliderView slider) {

                                        if (slide.hasChild("link")) {
                                            String type_check = String.valueOf(slide.child("type").getValue());
                                            if (type_check.equals("product")) {
                                                Log.e("Link", "is product");
                                                String productlink = String.valueOf(slide.child("link").getValue());
                                                Intent activity = new Intent(ctx, ProductActivity.class);
                                                activity.putExtra("post_key", productlink);
                                                startActivity(activity);

                                            } else if (type_check.equals("category")) {
                                                String productid = String.valueOf(slide.child("type").getValue());
                                                String productname = String.valueOf(slide.child("name").getValue());
                                                String productlink = String.valueOf(slide.child("link").getValue());
                                                Intent activity = new Intent(ctx, ListActivity.class);
                                                activity.putExtra("type", productid);
                                                activity.putExtra("name", productname);
                                                activity.putExtra("link", productlink);
                                                startActivity(activity);
                                            } else {
                                                Log.e("Hero Slider Link", type_check + " Not found");
                                                Snackbar.make(getCurrentFocus(), "Sorry! This Item is not available right now!", Snackbar.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }));
                            } catch (RuntimeExecutionException e) {
                                Log.e("Error", "Failed to generate hero Slides.");
                            }
                        }
                    }
                    if (child.getKey().equals("banner")) {
                        try {
                            homeBanner2 = (ImageView) findViewById(R.id.xml_homeBanner2);
                            Picasso.with(ctx).load((String) child.getValue()).placeholder(R.drawable.loading_450).into(homeBanner2);
                            homeBanner2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Snackbar.make(getCurrentFocus(), "Coming Soon", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e) {
                            Log.e("Home Banner Error", e.getMessage());
                        }

                    }
                }
                TextView categoryHolder1 = (TextView) findViewById(R.id.ocassion_textview);
                TextView categoryHolder2 = (TextView) findViewById(R.id.flowers_textview);
                TextView categoryHolder3 = (TextView) findViewById(R.id.gift_textview);
                TextView categoryHolder4 = (TextView) findViewById(R.id.category_textview);
                TextView dealholder = (TextView) findViewById(R.id.deal_textview);
                final String category1 = (String) homepageLayout.get("category1name");
                final String category2 = (String) homepageLayout.get("category2name");
                final String category3 = (String) homepageLayout.get("category3name");
                final String category4 = (String) homepageLayout.get("category4name");
                final String deal = (String) homepageLayout.get("categorytopname");
                TextView viewall_1 = (TextView) findViewById(R.id.cat_view_all);
                TextView viewall_2 = (TextView) findViewById(R.id.cat2_view_all);
                TextView viewall_3 = (TextView) findViewById(R.id.cat3_view_all);
                TextView viewall_4 = (TextView) findViewById(R.id.category_view_all);
                viewall_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent activity = new Intent(ctx, ViewAllActivity.class);
                        activity.putExtra("type", (String) homepageLayout.get("category1type"));
                        activity.putExtra("name", category1);
                        activity.putExtra("viewall", category1);
                        activity.putExtra("link", (String) homepageLayout.get("category1type"));
                        startActivity(activity);
                    }
                });
                viewall_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent activity = new Intent(ctx, ViewAllActivity.class);
                        activity.putExtra("type", (String) homepageLayout.get("category2type"));
                        activity.putExtra("name", category2);
                        activity.putExtra("viewall", category2);
                        activity.putExtra("link", (String) homepageLayout.get("category2type"));
                        startActivity(activity);
                    }
                });
                viewall_3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent activity = new Intent(ctx, ViewAllActivity.class);
                        activity.putExtra("type", (String) homepageLayout.get("category3type"));
                        activity.putExtra("name", category3);
                        activity.putExtra("viewall", category3);
                        activity.putExtra("link", (String) homepageLayout.get("category3type"));
                        startActivity(activity);
                    }
                });
                viewall_4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent activity = new Intent(ctx, CategoryActivity.class);
                        activity.putExtra("categoryid", (String) homepageLayout.get("category4type"));
                        activity.putExtra("categoryname", category4);
                        startActivity(activity);
                    }
                });
                categoryHolder1.setText(category1);
                categoryHolder2.setText(category2);
                categoryHolder3.setText(category3);
                categoryHolder4.setText(category4);
                dealholder.setText(deal);
                category = rootRef.child("categories").orderByChild("type").equalTo((String) homepageLayout.get("category1type")).limitToFirst(9);
                flower = rootRef.child("categories").orderByChild("type").equalTo((String) homepageLayout.get("category2type")).limitToFirst(9);
                giftQuery = rootRef.child("categories").orderByChild("type").equalTo((String) homepageLayout.get("category3type")).limitToFirst(9);
                celebrateQuery = rootRef.child("categories").orderByChild("type").equalTo((String) homepageLayout.get("categoryoccasiontype")).limitToFirst(9);
                dealQuery = rootRef.child("categories").child((String) homepageLayout.get("categorytoptype")).child("products").orderByKey().limitToFirst(15);
                homeProgress.setVisibility(View.GONE);
                ViewAnimator.animate(scrollview).alpha(100).duration(200).fadeIn().start();
                myRecycleView.setLayoutManager(layoutManager);
                flowerRecycleView.setLayoutManager(flowerLayoutManager);
                giftRecycleView.setLayoutManager(giftLayoutManager);
                celebrateRecycleView.setLayoutManager(celebrateLayoutManager);
                myRecycleView.setNestedScrollingEnabled(false);
                flowerRecycleView.setNestedScrollingEnabled(false);
                giftRecycleView.setNestedScrollingEnabled(false);
                categoryRecycleView.setNestedScrollingEnabled(false);
                dealRecycleView.setNestedScrollingEnabled(false);
                celebrateRecycleView.setNestedScrollingEnabled(false);
                mRecycleView.setNestedScrollingEnabled(false);
                dealRecycleView.setNestedScrollingEnabled(false);

                dealQueryListner = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.hasChildren()) {
                            dealRecycleView.setVisibility(View.GONE);
                            dealProgressbar.setVisibility(View.GONE);
                            RelativeLayout error_screen = (RelativeLayout) findViewById(R.id.handpicked_layout);
                            error_screen.setVisibility(View.GONE);
                        } else {
                            dealCardRecycler = new FirebaseRecyclerAdapter<CategoryModel, CategoryFragment.categoryCardViewHolder>(
                                    CategoryModel.class,
                                    R.layout.deal_cardview,
                                    CategoryFragment.categoryCardViewHolder.class,
                                    dealQuery
                            ) {

                                @Override
                                protected void populateViewHolder(final CategoryFragment.categoryCardViewHolder viewHolder, CategoryModel model, final int position) {
                                    final String post_key = getRef(position).getKey();
                                    try {

                                        viewHolder.setTitle(model.getName());
                                        viewHolder.setPrice(model.getPrice());
                                        viewHolder.setImage(model.getImageurl());
                                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                viewHolder.startProductActivity(ctx, post_key);
                                            }
                                        });
                                    } catch (Exception e) {
                                        Log.e("Try Error", "Grid Category Error Logged.");
                                    }
                                    dealProgressbar.setVisibility(View.GONE);
                                }

                                @Override
                                protected void onDataChanged() {
                                    super.onDataChanged();
                                }
                            };

                            dealRecycleView.setAdapter(dealCardRecycler);
                            LinearLayoutManager dealLayoutManager
                                    = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);

                            ViewAnimator.animate(dealRecycleView).alpha(100).fadeIn().duration(300).start();
                            dealRecycleView.setLayoutManager(dealLayoutManager);


                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                dealQuery.addListenerForSingleValueEvent(dealQueryListner);

                categoryQueryListner = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            firebaseRecycler = new FirebaseRecyclerAdapter<CakeItemModel, categoryViewHolder>(
                                    CakeItemModel.class,
                                    R.layout.cake_item_layout,
                                    categoryViewHolder.class,
                                    category
                            ) {
                                @Override
                                protected void populateViewHolder(categoryViewHolder viewHolder, CakeItemModel model, int position) {
                                    final String post_key = getRef(position).getKey();
                                    final String post_name = model.getName();
                                    viewHolder.setTitle(model.getName());
                                    viewHolder.setOffer(model.getOffer());
                                    viewHolder.setImage(ctx, model.getImageurl());
                                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            try {
                                                Intent categoryIntent = new Intent(ctx, CategoryActivity.class);
                                                categoryIntent.putExtra("categoryid", post_key);
                                                categoryIntent.putExtra("categoryname", post_name);
                                                startActivity(categoryIntent);
                                            } catch (DatabaseException e) {
                                                Log.e("DB ERROR", e.getMessage());
                                            }

                                        }
                                    });
                                    mCakeProgressbar.setVisibility(View.GONE);
                                }

                                @Override
                                protected void onDataChanged() {
                                    super.onDataChanged();

                                }
                            };

                            ViewAnimator.animate(myRecycleView).alpha(100).duration(300).newsPaper().start();
                            myRecycleView.setAdapter(firebaseRecycler);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                category.addListenerForSingleValueEvent(categoryQueryListner);

                celebrateQueryListner = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            celebrateFirebaseRecycler = new FirebaseRecyclerAdapter<CakeItemModel, categoryViewHolder>(
                                    CakeItemModel.class,
                                    R.layout.celebrate_itemview,
                                    categoryViewHolder.class,
                                    celebrateQuery
                            ) {
                                @Override
                                protected void populateViewHolder(categoryViewHolder viewHolder, CakeItemModel model, int position) {
                                    final String post_key = getRef(position).getKey();
                                    final String post_name = model.getName();
                                    viewHolder.setTitle(model.getName());
                                    viewHolder.setImage(ctx, model.getImageurl());
                                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            try {
                                                Intent categoryIntent = new Intent(ctx, CategoryActivity.class);
                                                categoryIntent.putExtra("categoryid", post_key);
                                                categoryIntent.putExtra("categoryname", post_name);
                                                startActivity(categoryIntent);
                                            } catch (DatabaseException e) {
                                                Log.e("DB ERROR", e.getMessage());
                                            }
                                        }
                                    });
                                }

                                @Override
                                protected void onDataChanged() {
                                    super.onDataChanged();

                                }
                            };
                            ViewAnimator.animate(celebrateRecycleView).alpha(100).duration(300).newsPaper().start();
                            celebrateRecycleView.setAdapter(celebrateFirebaseRecycler);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                celebrateQuery.addListenerForSingleValueEvent(celebrateQueryListner);


                flowerQueryListner = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {

                            flowerFirebaseRecycler = new FirebaseRecyclerAdapter<CakeItemModel, categoryViewHolder>(
                                    CakeItemModel.class,
                                    R.layout.single_item_layout,
                                    categoryViewHolder.class,
                                    flower
                            ) {
                                @Override
                                protected void populateViewHolder(categoryViewHolder viewHolder, CakeItemModel model, int position) {

                                    final String post_key = getRef(position).getKey();
                                    final String post_name = model.getName();
                                    viewHolder.setTitle(model.getName());
                                    viewHolder.setOffer(model.getOffer());
                                    View v;
                                    viewHolder.setImage(ctx, model.getImageurl());
                                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent categoryIntent = new Intent(ctx, CategoryActivity.class);
                                            categoryIntent.putExtra("categoryid", post_key);
                                            categoryIntent.putExtra("categoryname", post_name);
                                            startActivity(categoryIntent);
                                        }
                                    });

                                }

                                @Override
                                protected void onDataChanged() {
                                    super.onDataChanged();

                                }
                            };
                            mFlowerProgressbar.setVisibility(View.GONE);
                            ViewAnimator.animate(flowerRecycleView).alpha(100).duration(300).newsPaper().start();
                            flowerRecycleView.setAdapter(flowerFirebaseRecycler);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                flower.addListenerForSingleValueEvent(flowerQueryListner);
                giftQueryListner = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            giftfirebaseRecycler = new FirebaseRecyclerAdapter<CakeItemModel, categoryViewHolder>(
                                    CakeItemModel.class,
                                    R.layout.cake_item_layout,
                                    categoryViewHolder.class,
                                    giftQuery
                            ) {
                                @Override
                                protected void onCancelled(DatabaseError error) {
                                    mGiftProgressbar.setVisibility(View.GONE);
                                    Toast.makeText(ctx, "Cancelled By Users Error", Toast.LENGTH_SHORT).show();
                                    super.onCancelled(error);
                                }

                                @Override
                                public boolean onFailedToRecycleView(categoryViewHolder holder) {
                                    Toast.makeText(ctx, "Failed To load Data", Toast.LENGTH_SHORT).show();
                                    mGiftProgressbar.setVisibility(View.GONE);
                                    return super.onFailedToRecycleView(holder);

                                }

                                @Override
                                protected void populateViewHolder(categoryViewHolder viewHolder, CakeItemModel model, int position) {

                                    final String post_key = getRef(position).getKey();
                                    final String post_name = model.getName();
                                    viewHolder.setTitle(model.getName());
                                    viewHolder.setOffer(model.getOffer());
                                    viewHolder.setImage(ctx, model.getImageurl());
                                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                                                               @Override
                                                                               public void onClick(View v) {
                                                                                   Intent categoryIntent = new Intent(ctx, CategoryActivity.class);
                                                                                   categoryIntent.putExtra("categoryid", post_key);
                                                                                   categoryIntent.putExtra("categoryname", post_name);
                                                                                   startActivity(categoryIntent);

                                                                               }
                                                                           }

                                    );
                                    mGiftProgressbar.setVisibility(View.GONE);
                                }


                                @Override
                                protected void onDataChanged() {
                                    super.onDataChanged();

                                }
                            };

                            ViewAnimator.animate(giftRecycleView).alpha(100).duration(300).newsPaper().start();
                            giftRecycleView.setAdapter(giftfirebaseRecycler);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                giftQuery.addListenerForSingleValueEvent(giftQueryListner);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        homepageRef.addListenerForSingleValueEvent(homepageListner);

        nav_view = (NavigationView) findViewById(R.id.navi_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        profileName = (TextView) findViewById(R.id.ocassion_textview);
        hmenu = nav_view.getMenu();
        item = hmenu.findItem(R.id.menu_logout);
        headLoginbtn = (Button) nav_view.getHeaderView(0).findViewById(R.id.header_loginbtn);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    hView = nav_view.getHeaderView(0);
                    profileName = (TextView) hView.findViewById(R.id.xml_bearName);
                    if (profileName != null && (user.getDisplayName() != null)) {
                        profileName.setText(user.getDisplayName());

                    }
                    profileImage = (ImageView) hView.findViewById(R.id.xml_profileImageImageView);

                    if (profileImage != null && (user.getPhotoUrl() != null)) {
                        Picasso.with(ctx).load(user.getPhotoUrl()).into(profileImage);
                    }
                    item.setVisible(true);
                    headLoginbtn.setVisibility(View.GONE);

                } else {
                    // User is signed out
                }
            }
        };


        if (mAuth.getCurrentUser() != null) {
            recentQuery = rootRef.child("recently_viewed").child(mAuth.getCurrentUser().getUid()).orderByChild("timestamp").limitToFirst(12);
            recentQueryListner = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChildren()) {
                        recentLayout.setVisibility(View.GONE);
                    } else {

                        recentCardRecycler = new FirebaseRecyclerAdapter<RecentModel, RecentViewHolder>(
                                RecentModel.class,
                                R.layout.home_recently_viewed,
                                RecentViewHolder.class,
                                recentQuery
                        ) {
                            @Override
                            protected void populateViewHolder(final RecentViewHolder viewHolder, RecentModel model, final int position) {
                                final String post_key = getRef(position).getKey();
                                try {
                                    viewHolder.setImage(ctx, model.getImageurl());
                                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            viewHolder.startProductActivity(ctx, post_key);
                                        }
                                    });
                                } catch (Exception e) {
                                    Log.e("Try Error", "Grid Category Error Logged.");
                                }
                                mRecentProgressbar.setVisibility(View.GONE);
                            }

                            @Override
                            protected void onDataChanged() {
                                super.onDataChanged();
                            }
                        };

                        recentRecycleView.setAdapter(recentCardRecycler);
                        recentRecycleView.setNestedScrollingEnabled(false);
                        ViewAnimator.animate(recentRecycleView).alpha(100).fadeIn().duration(300).start();
                        recentRecycleView.setLayoutManager(recentLayoutManager);


                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            recentQuery.addListenerForSingleValueEvent(recentQueryListner);
        } else {
            recentLayout.setVisibility(View.GONE);
            mRecentProgressbar.setVisibility(View.GONE);
        }


        //Setting Toolbar as Actionbar and Enabling Home Button
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
        setup((DrawerLayout) findViewById(R.id.xml_drawer), toolbar);
        scrollview.setVerticalScrollBarEnabled(false);
        //Hiding Scrollbar from navigation Drawer
        NavigationMenuView navigationMenuView = (NavigationMenuView) nav_view.getChildAt(0);
        if (navigationMenuView != null) {
            navigationMenuView.setVerticalScrollBarEnabled(false);
        }
        nav_view.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.action_shopping_cart) {
                            Toast.makeText(ctx, "Buy A Cake", Toast.LENGTH_SHORT).show();
                        }
                        if (id == R.id.menu_buyGifts) {
                            String productid = "gift";
                            String productname = "Gifts";
                            String productlink = "gift";
                            Intent activity = new Intent(ctx, ProductTypeActivity.class);
                            activity.putExtra("type", productid);
                            activity.putExtra("name", productname);
                            activity.putExtra("link", productlink);
                            startActivity(activity);
                        }
                        if (id == R.id.menu_bowerbird) {
                            String productid = "bowerbird";
                            String productname = "Bowerbird";
                            String productlink = "bowerbird";
                            Intent activity = new Intent(ctx, ProductTypeActivity.class);
                            activity.putExtra("type", productid);
                            activity.putExtra("name", productname);
                            activity.putExtra("link", productlink);
                            startActivity(activity);

                        }
                        if (id == R.id.menu_wishList) {
                            if (mAuth.getCurrentUser() != null) {
                                Intent wishlist = new Intent(ctx, OrderLayoutActivity.class);
                                wishlist.putExtra("position", 1);
                                startActivity(wishlist);
                            } else {
                                Snackbar.make(getCurrentFocus(), "Log In to access your Wishlist!", Snackbar.LENGTH_SHORT).setAction("Login", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(ctx, LoginActivity.class));
                                    }
                                }).setActionTextColor(ContextCompat.getColor(ctx, R.color.colorPrimary)).show();
                            }

                        }
                        if (id == R.id.menu_myOrders) {
                            if (mAuth.getCurrentUser() != null) {
                                startActivity(new Intent(ctx, OrderLayoutActivity.class));
                            } else {
                                Snackbar.make(getCurrentFocus(), "Log In to access your Orders", Snackbar.LENGTH_SHORT).setAction("Login", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(ctx, LoginActivity.class));
                                    }
                                }).setActionTextColor(ContextCompat.getColor(ctx, R.color.colorPrimary)).show();
                            }


                        }
                        if (id == R.id.menu_delivery) {
                            if (mAuth.getCurrentUser() != null) {
                                Intent wishlist = new Intent(ctx, DeliveryActivity.class);
                                startActivity(wishlist);
                            } else {
                                Snackbar.make(getCurrentFocus(), "Log In to access your Delivery Options", Snackbar.LENGTH_SHORT).setAction("Login", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(ctx, LoginActivity.class));
                                    }
                                }).setActionTextColor(ContextCompat.getColor(ctx, R.color.colorPrimary)).show();
                            }
                        }
                        if (id == R.id.menu_help) {
                            if (mAuth.getCurrentUser() != null) {
                                Intent wishlist = new Intent(ctx, Feedback.class);
                                startActivity(wishlist);
                            } else {
                                Snackbar.make(getCurrentFocus(), "Log In to access Feedback!", Snackbar.LENGTH_SHORT).setAction("Login", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(ctx, LoginActivity.class));
                                    }
                                }).setActionTextColor(ContextCompat.getColor(ctx, R.color.colorPrimary)).show();
                            }
                        }
                        if (id == R.id.menu_issue) {
                            if (mAuth.getCurrentUser() != null) {
                                new EasyFeedback.Builder(ctx)
                                        .withEmail("support@partybear.in")
                                        .withSystemInfo()
                                        .build()
                                        .start();
                            } else {
                                Snackbar.make(getCurrentFocus(), "Log In to Report Issue!", Snackbar.LENGTH_SHORT).setAction("Login", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(ctx, LoginActivity.class));
                                    }
                                }).setActionTextColor(ContextCompat.getColor(ctx, R.color.colorPrimary)).show();
                            }
                        }

                        if (id == R.id.menu_terms) {
                            //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.partybear.in/terms"));
                            //startActivity(browserIntent);
                            Intent wishlist = new Intent(ctx, Addons.class);
                            startActivity(wishlist);
                        }
                        if (id == R.id.menu_sell) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.partybear.in/sell_with_us"));
                            startActivity(browserIntent);
                        }
                        if (id == R.id.menu_logout) {
                            if (user != null)
                                signOutUser(user.getUid());
                        }
                        mdrawerLayout.closeDrawer(GravityCompat.START);
                        return false;
                    }
                });
        if (headLoginbtn != null) {

            headLoginbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ctx, LoginActivity.class));
                }
            });
        }


        myRecycleView = (RecyclerView) findViewById(R.id.cake_recycleView);
        flowerRecycleView = (RecyclerView) findViewById(R.id.flowers_recycleView);
        giftRecycleView = (RecyclerView) findViewById(R.id.gift_recycleView);
        myRecycleView.setNestedScrollingEnabled(false);
        flowerRecycleView.setNestedScrollingEnabled(false);
        giftRecycleView.setNestedScrollingEnabled(false);


    }

    @Override
    protected void onStop() {
        sliderShow.stopAutoCycle();
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);


    }

    private void setup(DrawerLayout drawerlayout, Toolbar toolbar) {
        mdrawerLayout = drawerlayout;
        drawerToggle = new ActionBarDrawerToggle(this, mdrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mdrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private void signOutUser(String uid) {
        if (user != null) {
            mAuth.signOut();
            LoginManager.getInstance().logOut();

            if (profileName != null && (user.getDisplayName() != null)) {
                profileName.setText(R.string.bear_name);

            }

            if (profileImage != null) {
                Picasso.with(ctx).load(R.drawable.bgx).into(profileImage);
            }
            item.setVisible(false);
            headLoginbtn.setVisibility(View.VISIBLE);
            ToastOX.ok(ctx, "You Logged Out Sucessfully", Toast.LENGTH_SHORT);

        } else {
            ToastOX.warning(ctx, "You are not Signed In", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_shopping_cart) {
            if (mAuth.getCurrentUser() != null) {
                startActivity(new Intent(ctx, MyCart.class));
            } else {
                Snackbar.make(getCurrentFocus(), "Log In to access your Cart!", Snackbar.LENGTH_SHORT).setAction("Login", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ctx, LoginActivity.class));
                    }
                }).setActionTextColor(ContextCompat.getColor(ctx, R.color.colorPrimary)).show();
            }
        }
        if (id == R.id.action_top_wishlist) {
            if (mAuth.getCurrentUser() != null) {
                Intent wishlist = new Intent(ctx, OrderLayoutActivity.class);
                wishlist.putExtra("position", 1);
                startActivity(wishlist);
            } else {
                Snackbar.make(getCurrentFocus(), "Log In to access your Wishlist!", Snackbar.LENGTH_SHORT).setAction("Login", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ctx, LoginActivity.class));
                    }
                }).setActionTextColor(ContextCompat.getColor(ctx, R.color.colorPrimary)).show();
            }
        }
        return super.onOptionsItemSelected(item);

    }

    /*ViewHolder Class for Category Class*/
    public static class categoryViewHolder extends RecyclerView.ViewHolder {
        View mview;

        public categoryViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
        }

        public void setTitle(String title) {
            TextView category_title = (TextView) mview.findViewById(R.id.category_itemName);
            category_title.setText(title);
        }

        public void setImage(Context ctx, String image) {
            DisplayImageOptions options;
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.loading)
                    .showImageForEmptyUri(R.drawable.loading)
                    .showImageOnFail(R.drawable.loading)
                    .cacheInMemory(false)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new RoundedBitmapDisplayer(0))
                    .build();
            ImageView category_image = (ImageView) mview.findViewById(R.id.category_ItemImage);

            ImageLoader.getInstance().displayImage(image, category_image, options);

            //ImageView category_image = (ImageView) mview.findViewById(R.id.categoryCardImageView);
            //Picasso.with(ctx).load(image).fit().placeholder(R.drawable.loading_100).into(category_image);
        }

        public void setOffer(String offer) {
            TextView category_offer = (TextView) mview.findViewById(R.id.category_itemPrice);
            if (offer != null) {
                category_offer.setText(offer);
            } else {
                category_offer.setBackground(null);
            }
        }
    }

    public static class RecentViewHolder extends RecyclerView.ViewHolder {
        View mview;

        public RecentViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
        }

        public void startProductActivity(Context ctx1, final String post_key) {
            try {
                final Intent productIntent = new Intent(ctx1, ProductActivity.class);
                productIntent.putExtra("post_key", post_key);
                ctx1.startActivity(productIntent);
            } catch (Exception e) {
                Log.e("Cat Fragment", "Unable to launch post " + post_key);
                Snackbar.make(mview, "Unable to open item. Please try again!", Snackbar.LENGTH_SHORT).show();
            }
        }

        public void setImage(Context ctx, String image) {
            DisplayImageOptions options;
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.loading)
                    .showImageForEmptyUri(R.drawable.loading)
                    .showImageOnFail(R.drawable.loading)
                    .cacheInMemory(false)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageView category_image = (ImageView) mview.findViewById(R.id.category_ItemImage);

            ImageLoader.getInstance().displayImage(image, category_image, options);
        }

    }

    public static class chefViewHolder extends RecyclerView.ViewHolder {
        View mview;

        public chefViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
        }

        public void startProductActivity(Context ctx1, final String post_key) {
            try {
                final Intent productIntent = new Intent(ctx1, ProductActivity.class);
                productIntent.putExtra("post_key", post_key);
                ctx1.startActivity(productIntent);
            } catch (Exception e) {
                Log.e("Cat Fragment", "Unable to launch post " + post_key);
                Snackbar.make(mview, "Unable to open item. Please try again!", Snackbar.LENGTH_SHORT).show();
            }
        }

        public void setTitle(String title) {
            TextView product_title = (TextView) mview.findViewById(R.id.categoryCardTitle);
            product_title.setText(title);
        }

        public void setEggless(String feature) {
            if (feature != null) {
                TextView product_title = (TextView) mview.findViewById(R.id.eggstatus);
                product_title.setVisibility(View.VISIBLE);
                product_title.setText(feature);
            }

        }

        public void setBasePrice(int price) {
            if (price != 0) {
                TextView product_price = (TextView) mview.findViewById(R.id.categoryCardBasePrice);
                product_price.setPaintFlags(product_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                product_price.setText("\u20B9" + price);
                product_price.setVisibility(View.VISIBLE);
            }
        }

        public void setPrice(int price) {
            TextView product_price = (TextView) mview.findViewById(R.id.categoryCardPrice);
            product_price.setVisibility(View.VISIBLE);
            product_price.setText("\u20B9" + price);
        }

        public void setImage(String image) {
            DisplayImageOptions options;
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.loadingwhite)
                    .showImageForEmptyUri(R.drawable.loadingwhite)
                    .showImageOnFail(R.drawable.loadingwhite)
                    .cacheInMemory(false)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();

            ImageView category_image = (ImageView) mview.findViewById(R.id.categoryCardImageView);
            ImageLoader.getInstance().displayImage(image, category_image, options);
            //Picasso.with(ctx).load(image).fit().placeholder(R.drawable.loading_450).into(category_image);
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            myRecycleView.removeAllViewsInLayout();
            flowerRecycleView.removeAllViewsInLayout();
            if (mAuth.getCurrentUser() != null) {
                recentRecycleView.removeAllViewsInLayout();
                recentCardRecycler.cleanup();
                recentRecycleView.setAdapter(null);
            }
            giftRecycleView.removeAllViewsInLayout();
            categoryRecycleView.removeAllViewsInLayout();
            dealRecycleView.removeAllViewsInLayout();
            dealRecycleView.setAdapter(null);
            myRecycleView.setAdapter(null);
            flowerRecycleView.setAdapter(null);
            giftRecycleView.setAdapter(null);
            categoryRecycleView.setAdapter(null);
            categoryCardRecycler.cleanup();
            home_categoryCardRecycler.cleanup();
            dealCardRecycler.cleanup();
            firebaseRecycler.cleanup();
            flowerFirebaseRecycler.cleanup();
            celebrateFirebaseRecycler.cleanup();
            sliderShow.removeAllSliders();
            ImageLoader.getInstance().clearMemoryCache();
            homepageLayout.clear();
            ctx = null;
        } catch (Exception e) {
            FirebaseCrash.log(e.getMessage());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (ctx == null) {
            ctx = MainActivity.this;
        }
    }

    public static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String query = url.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }
}

