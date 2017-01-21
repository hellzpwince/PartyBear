package com.discoverfriend.partybear;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.onurciner.toastox.ToastOX;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    SliderLayout sliderShow;
    DatabaseReference rootRef;
    DatabaseReference databaseRef;
    DatabaseReference flowerDatabaseRef;
    DatabaseReference giftDatabaseRef;
    Query flower;
    Query category;
    Query giftQuery;
    FirebaseUser user;
    Menu hmenu;
    MenuItem item;
    Button headLoginbtn;
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
    private ScrollView scrollview;
    private ImageView homeBanner2;
    private TextView profileName;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nav_view = (NavigationView) findViewById(R.id.navi_drawer);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        profileName = (TextView) findViewById(R.id.ocassion_textview);
        hmenu = nav_view.getMenu();
        item = hmenu.findItem(R.id.menu_logout);
        headLoginbtn = (Button) nav_view.getHeaderView(0).findViewById(R.id.header_loginbtn);
        rootRef = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    View hView = nav_view.getHeaderView(0);
                    profileName = (TextView) hView.findViewById(R.id.xml_bearName);
                    if (profileName != null && (user.getDisplayName() != null)) {
                        profileName.setText(user.getDisplayName());

                    }
                    ImageView profileImage = (ImageView) hView.findViewById(R.id.xml_profileImageImageView);

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


        scrollview = (ScrollView) findViewById(R.id.scroll_view);
        homeBanner2 = (ImageView) findViewById(R.id.xml_homeBanner2);


        databaseRef = rootRef.child("categories").child("birthdaycakes");
        flowerDatabaseRef = rootRef.child("categories").child("birthdaycakes");
        giftDatabaseRef = rootRef.child("categories").child("birthdaycakes");

        flower = flowerDatabaseRef.limitToFirst(6);
        category = databaseRef.limitToFirst(6);
        giftQuery = giftDatabaseRef.limitToFirst(6);

        mCakeProgressbar = (ProgressBar) findViewById(R.id.ocassion_progressbar);
        mFlowerProgressbar = (ProgressBar) findViewById(R.id.flowers_progressbar);
        mGiftProgressbar = (ProgressBar) findViewById(R.id.gift_progressbar);

        myRecycleView = (RecyclerView) findViewById(R.id.cake_recycleView);
        flowerRecycleView = (RecyclerView) findViewById(R.id.flowers_recycleView);
        giftRecycleView = (RecyclerView) findViewById(R.id.gift_recycleView);

        //Setting Toolbar as Actionbar and Enabling Home Button
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
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
                        if (id == R.id.menu_account) {
                            Toast.makeText(MainActivity.this, "Account", Toast.LENGTH_SHORT).show();
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
                        if (id == R.id.menu_Home) {
                            Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        }
                        if (id == R.id.menu_bowerbird) {
                            Toast.makeText(MainActivity.this, "Bowerbird", Toast.LENGTH_SHORT).show();
                        }
                        if (id == R.id.menu_wallet) {
                            Toast.makeText(MainActivity.this, "My Wallet", Toast.LENGTH_SHORT).show();
                        }
                        if (id == R.id.menu_wishList) {
                            Toast.makeText(MainActivity.this, "My Wishlist", Toast.LENGTH_SHORT).show();
                        }
                        if (id == R.id.menu_myOrders) {
                            Toast.makeText(MainActivity.this, "My Orders", Toast.LENGTH_SHORT).show();
                        }
                        if (id == R.id.menu_delivery) {
                            Toast.makeText(MainActivity.this, "Delivery", Toast.LENGTH_SHORT).show();
                        }
                        if (id == R.id.menu_help) {
                            Toast.makeText(MainActivity.this, "Help and Support", Toast.LENGTH_SHORT).show();
                        }
                        if (id == R.id.menu_rateus) {
                            Toast.makeText(MainActivity.this, "Rate Our App", Toast.LENGTH_SHORT).show();
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
        } else {
            Toast.makeText(this, "Null", Toast.LENGTH_SHORT).show();
        }
        Picasso.with(getApplicationContext()).load("http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg").into(homeBanner2);

        /*Setting Up Home Image Slider*/
        sliderShow = (SliderLayout) findViewById(R.id.slider);
        sliderShow.addSlider(new ImageSlider(this).image("http://www.taste.com.au/images/recipes/sfi/2014/09/pinata-party-cake-31164_l.jpeg").setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
                ToastOX.info(getApplicationContext(), sliderShow.getCurrentPosition() + " is Position", Toast.LENGTH_SHORT);
            }
        }));
        sliderShow.addSlider(new ImageSlider(this).image("http://www.primrose-bakery.co.uk/shop/content/images/thumbs/0000362_chocolate-layer-cake.jpeg").setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
                ToastOX.info(getApplicationContext(), sliderShow.getCurrentPosition() + " is Position", Toast.LENGTH_SHORT);
            }
        }));
        sliderShow.addSlider(new ImageSlider(this).image("https://i.ytimg.com/vi/dsJtgmAhFF4/maxresdefault.jpg").setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
                ToastOX.info(getApplicationContext(), sliderShow.getCurrentPosition() + " is Position", Toast.LENGTH_SHORT);
            }
        }));
        sliderShow.addSlider(new ImageSlider(this).image("https://static01.nyt.com/images/2015/12/21/dining/21COOKING_DEVILSFOODCAKE/21COOKING_DEVILSFOODCAKE-superJumbo.jpg").setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
                ToastOX.info(getApplicationContext(), sliderShow.getCurrentPosition() + " is Position", Toast.LENGTH_SHORT);
            }
        }));
        sliderShow.addSlider(new ImageSlider(this).image("http://www.taste.com.au/images/recipes/sfi/2014/09/pinata-party-cake-31164_l.jpeg").setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
                ToastOX.info(getApplicationContext(), sliderShow.getCurrentPosition() + " is Position", Toast.LENGTH_SHORT);
            }
        }));

        /*Ocassional Cake Recycler View*/
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager flowerLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager giftLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        myRecycleView.setLayoutManager(layoutManager);
        flowerRecycleView.setLayoutManager(flowerLayoutManager);
        giftRecycleView.setLayoutManager(giftLayoutManager);

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

        FirebaseRecyclerAdapter<CakeItemModel, categoryViewHolder> firebaseRecycler = new FirebaseRecyclerAdapter<CakeItemModel, categoryViewHolder>(
                CakeItemModel.class,
                R.layout.cake_item_layout,
                categoryViewHolder.class,
                category
        ) {
            @Override
            protected void populateViewHolder(categoryViewHolder viewHolder, CakeItemModel model, int position) {
                final String post_key = getRef(position).getKey();
                viewHolder.setTitle(model.getCategoryItem());
                viewHolder.setOffer(model.getCategoryOffer());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent categoryIntent = new Intent(MainActivity.this, CategoryActivity.class);
                        categoryIntent.putExtra("category", post_key);
                        startActivity(categoryIntent);
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
                viewHolder.setTitle(model.getCategoryItem());
                viewHolder.setOffer(model.getCategoryOffer());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent categoryIntent = new Intent(MainActivity.this, CategoryActivity.class);
                        categoryIntent.putExtra("category", post_key);
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
            protected void populateViewHolder(categoryViewHolder viewHolder, CakeItemModel model, int position) {
                final String post_key = getRef(position).getKey();
                viewHolder.setTitle(model.getCategoryItem());
                viewHolder.setOffer(model.getCategoryOffer());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent categoryIntent = new Intent(MainActivity.this, CategoryActivity.class);
                        categoryIntent.putExtra("category", post_key);
                        startActivity(categoryIntent);
                    }
                });
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
            Picasso.with(ctx).load(image).into(category_image);
        }

        public void setOffer(String offer) {
            TextView category_offer = (TextView) mview.findViewById(R.id.category_itemPrice);
            category_offer.setText(offer);
        }
    }
}
