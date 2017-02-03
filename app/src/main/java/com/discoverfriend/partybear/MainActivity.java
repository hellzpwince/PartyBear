package com.discoverfriend.partybear;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.discoverfriend.partybear.Product.ProductActivity;
import com.discoverfriend.partybear.Seperate_list.ListActivity;
import com.discoverfriend.partybear.order_processing.OrderLayoutActivity;
import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.onurciner.toastox.ToastOX;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    SliderLayout sliderShow;
    DatabaseReference rootRef;
    Query flower;
    Query category;
    Query giftQuery;
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
    private RecyclerView flowerRecycleView;
    private RecyclerView giftRecycleView;
    private ProgressBar mCakeProgressbar;
    private ProgressBar mFlowerProgressbar;
    private ProgressBar mGiftProgressbar;
    private NestedScrollView scrollview;
    private ImageView homeBanner2;
    private TextView profileName;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private View hView;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ProgressBar homeProgress = (ProgressBar) findViewById(R.id.homepage_progressbar);
        homeProgress.setVisibility(View.VISIBLE);
        rootRef = FirebaseDatabase.getInstance().getReference();
        scrollview = (NestedScrollView) findViewById(R.id.scroll_view);
        sliderShow = (SliderLayout) findViewById(R.id.slider);
        /*Fetching homepage layout details*/
        DatabaseReference homepageRef = rootRef.child("homepage");
        homepageRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                sliderShow.addSlider(new ImageSlider(getApplicationContext()).image((String) slide.child("imageurl").getValue()).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(BaseSliderView slider) {

                                        if (slide.hasChild("link")) {
                                            String type_check = String.valueOf(slide.child("type").getValue());
                                            Log.e("Link", "Has Link");
                                            if (type_check.equals("product")) {
                                                Log.e("Link", "is product");
                                                String productlink = String.valueOf(slide.child("link").getValue());
                                                Intent activity = new Intent(MainActivity.this, ProductActivity.class);
                                                activity.putExtra("post_key", productlink);
                                                startActivity(activity);

                                            } else if (type_check.equals("category")) {
                                                String productid = String.valueOf(slide.child("type").getValue());
                                                String productname = String.valueOf(slide.child("name").getValue());
                                                String productlink = String.valueOf(slide.child("link").getValue());
                                                Intent activity = new Intent(MainActivity.this, ListActivity.class);
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
                            Picasso.with(getApplicationContext()).load((String) child.getValue()).placeholder(R.drawable.loading_450).into(homeBanner2);
                            homeBanner2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ToastOX.muted(getApplicationContext(), (String) dataSnapshot.child("bannerlink").getValue());
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

                String category1 = (String) homepageLayout.get("category1name");
                String category2 = (String) homepageLayout.get("category2name");
                String category3 = (String) homepageLayout.get("category3name");
                categoryHolder1.setText(category1);
                categoryHolder2.setText(category2);
                categoryHolder3.setText(category3);
                category = rootRef.child("categories").orderByChild("type").equalTo((String) homepageLayout.get("category1type")).limitToFirst(9);
                flower = rootRef.child("categories").orderByChild("type").equalTo((String) homepageLayout.get("category2type")).limitToFirst(9);
                giftQuery = rootRef.child("categories").orderByChild("type").equalTo((String) homepageLayout.get("category3type")).limitToFirst(6);
                homeProgress.setVisibility(View.GONE);
                scrollview.setVisibility(View.VISIBLE);
                try {
        /*Ocassional Cake Recycler View*/
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                    LinearLayoutManager flowerLayoutManager
                            = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                    LinearLayoutManager giftLayoutManager
                            = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

                    myRecycleView.setLayoutManager(layoutManager);
                    flowerRecycleView.setLayoutManager(flowerLayoutManager);
                    giftRecycleView.setLayoutManager(giftLayoutManager);
                    myRecycleView.setNestedScrollingEnabled(false);
                    flowerRecycleView.setNestedScrollingEnabled(false);
                    giftRecycleView.setNestedScrollingEnabled(false);
                } catch (Exception e) {
                    Log.e("Homepage", "Unable to create RecyclerView" + e.getMessage());
                    View v = findViewById(R.id.category_ItemImage);
                    Snackbar.make(v, "Opps! Something Went Wrong", Snackbar.LENGTH_SHORT).setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            recreate();
                        }
                    });
                }

                FirebaseRecyclerAdapter<CakeItemModel, categoryViewHolder> firebaseRecycler = new FirebaseRecyclerAdapter<CakeItemModel, categoryViewHolder>(
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
                        viewHolder.setImage(getApplicationContext(), model.getImageurl());
                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent categoryIntent = new Intent(MainActivity.this, CategoryActivity.class);
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
                        mCakeProgressbar.setVisibility(View.GONE);
                    }
                };


                FirebaseRecyclerAdapter<CakeItemModel, categoryViewHolder> flowerFirebaseRecycler = new FirebaseRecyclerAdapter<CakeItemModel, categoryViewHolder>(
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
                        viewHolder.setImage(getApplicationContext(), model.getImageurl());
                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent categoryIntent = new Intent(MainActivity.this, CategoryActivity.class);
                                categoryIntent.putExtra("categoryid", post_key);
                                categoryIntent.putExtra("categoryname", post_name);
                                startActivity(categoryIntent);
                            }
                        });

                    }

                    @Override
                    protected void onDataChanged() {
                        super.onDataChanged();
                        mFlowerProgressbar.setVisibility(View.GONE);
                    }
                };
                FirebaseRecyclerAdapter<CakeItemModel, categoryViewHolder> giftfirebaseRecycler = new FirebaseRecyclerAdapter<CakeItemModel, categoryViewHolder>(
                        CakeItemModel.class,
                        R.layout.cake_item_layout,
                        categoryViewHolder.class,
                        giftQuery
                ) {
                    @Override
                    protected void onCancelled(DatabaseError error) {
                        mGiftProgressbar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Cancelled By Users Error", Toast.LENGTH_SHORT).show();
                        super.onCancelled(error);
                    }

                    @Override
                    public boolean onFailedToRecycleView(categoryViewHolder holder) {
                        Toast.makeText(MainActivity.this, "Failed To load Data", Toast.LENGTH_SHORT).show();
                        mGiftProgressbar.setVisibility(View.GONE);
                        return super.onFailedToRecycleView(holder);

                    }

                    @Override
                    protected void populateViewHolder(categoryViewHolder viewHolder, CakeItemModel model, int position) {

                        final String post_key = getRef(position).getKey();
                        final String post_name = model.getName();
                        viewHolder.setTitle(model.getName());
                        viewHolder.setOffer(model.getOffer());
                        viewHolder.setImage(getApplicationContext(), model.getImageurl());
                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                                                   @Override
                                                                   public void onClick(View v) {
                                                                       Intent categoryIntent = new Intent(MainActivity.this, CategoryActivity.class);
                                                                       categoryIntent.putExtra("categoryid", post_key);
                                                                       categoryIntent.putExtra("categoryname", post_name);
                                                                       startActivity(categoryIntent);

                                                                   }
                                                               }

                        );

                    }


                    @Override
                    protected void onDataChanged() {
                        super.onDataChanged();
                        mGiftProgressbar.setVisibility(View.GONE);
                    }
                };

                myRecycleView.setAdapter(firebaseRecycler);
                flowerRecycleView.setAdapter(flowerFirebaseRecycler);
                giftRecycleView.setAdapter(giftfirebaseRecycler);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                        Picasso.with(getApplicationContext()).load(user.getPhotoUrl()).into(profileImage);
                    }
                    item.setVisible(true);
                    headLoginbtn.setVisibility(View.GONE);

                } else {
                    // User is signed out
                }
            }
        };

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
                            Toast.makeText(MainActivity.this, "Buy A Cake", Toast.LENGTH_SHORT).show();
                        }
                        if (id == R.id.menu_buyCakes) {
                            Toast.makeText(MainActivity.this, "Buy A Cake", Toast.LENGTH_SHORT).show();
                        }
                        if (id == R.id.menu_buyGifts) {
                            Toast.makeText(MainActivity.this, "Buy A Gift", Toast.LENGTH_SHORT).show();
                        }
                        if (id == R.id.menu_flowers) {
                            Toast.makeText(MainActivity.this, "Buy Flowers", Toast.LENGTH_SHORT).show();
                        }
                        if (id == R.id.menu_bowerbird) {
                            Toast.makeText(MainActivity.this, "Bowerbird", Toast.LENGTH_SHORT).show();
                        }
                        if (id == R.id.menu_wishList) {
                            if (mAuth.getCurrentUser() != null) {
                                Intent wishlist = new Intent(MainActivity.this, OrderLayoutActivity.class);
                                wishlist.putExtra("position", 1);
                                startActivity(wishlist);
                            } else {
                                Snackbar.make(getCurrentFocus(), "Login to access your Wishlist!", Snackbar.LENGTH_SHORT).show();
                            }

                        }
                        if (id == R.id.menu_myOrders) {
                            if (mAuth.getCurrentUser() != null) {
                                startActivity(new Intent(MainActivity.this, OrderLayoutActivity.class));
                            } else {
                                Snackbar.make(getCurrentFocus(), "Login to access your Cart!", Snackbar.LENGTH_SHORT).show();
                            }


                        }
                        if (id == R.id.menu_delivery) {
                            if (mAuth.getCurrentUser() != null) {
                                Intent wishlist = new Intent(MainActivity.this, DeliveryActivity.class);
                                startActivity(wishlist);
                            } else {
                                Snackbar.make(getCurrentFocus(), "Login to access Delivery Options", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                        if (id == R.id.menu_help) {
                            Toast.makeText(MainActivity.this, "Help and Support", Toast.LENGTH_SHORT).show();
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
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            });
        }


        mCakeProgressbar = (ProgressBar) findViewById(R.id.ocassion_progressbar);
        mFlowerProgressbar = (ProgressBar) findViewById(R.id.flowers_progressbar);
        mGiftProgressbar = (ProgressBar) findViewById(R.id.gift_progressbar);

        myRecycleView = (RecyclerView) findViewById(R.id.cake_recycleView);
        flowerRecycleView = (RecyclerView) findViewById(R.id.flowers_recycleView);
        giftRecycleView = (RecyclerView) findViewById(R.id.gift_recycleView);


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
                Picasso.with(getApplicationContext()).load(R.drawable.bgx).into(profileImage);
            }
            item.setVisible(false);
            headLoginbtn.setVisibility(View.VISIBLE);
            ToastOX.ok(getApplicationContext(), "You Logged Out Sucessfully", Toast.LENGTH_SHORT);

        } else {
            ToastOX.warning(getApplicationContext(), "You are not Signed In", Toast.LENGTH_SHORT);
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
                startActivity(new Intent(MainActivity.this, MyCart.class));
            } else {
                Snackbar.make(getCurrentFocus(), "Login to access your Cart!", Snackbar.LENGTH_SHORT).show();
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
            ImageView category_image = (ImageView) mview.findViewById(R.id.category_ItemImage);
            Picasso.with(ctx).load(image).resize(400, 400).placeholder(R.drawable.loading_100).into(category_image);
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
}