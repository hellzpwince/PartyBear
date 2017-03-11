package com.discoverfriend.partybear;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.discoverfriend.partybear.Product.ProductActivity;
import com.discoverfriend.partybear.category.CategoryModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
public class ProductTypeActivity extends AppCompatActivity {
    String itemtype, itemlink, itemname;
    DatabaseReference rootRef;
    Query myquery;
    private View mView;
    private RecyclerView mRecycleView;
    private ProgressBar mProgress;
    long items;
    int ACTIVE = 0;
    TextView items_no;
    FirebaseRecyclerAdapter<CategoryModel, ProductTypeActivity.categoryCardViewHolder> categoryCardRecycler;
    ValueEventListener myQueryListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_type);
        items_no = (TextView) findViewById(R.id.items_no);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemname = extras.getString("name");
            itemtype = extras.getString("type");
        }
        setupToolbar(itemname);
        mRecycleView = (RecyclerView) findViewById(R.id.fragOneRV_xml);
        mRecycleView.setNestedScrollingEnabled(false);
        mProgress = (ProgressBar) findViewById(R.id.progress_listactivity);
        rootRef = FirebaseDatabase.getInstance().getReference();
        myquery = rootRef.child("products").orderByChild("producttype").equalTo(itemtype);
        myQueryListner = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    mRecycleView.setVisibility(View.GONE);
                    mProgress.setVisibility(View.GONE);
                    setContentView(R.layout.nothing_found);
                    setupToolbar(itemname);

                } else {
                    ACTIVE = 1;
                    items = dataSnapshot.getChildrenCount();
                    items_no.setText(String.valueOf(items) + " " + itemname);

                    categoryCardRecycler = new FirebaseRecyclerAdapter<CategoryModel, ProductTypeActivity.categoryCardViewHolder>(
                            CategoryModel.class,
                            R.layout.grid_cardview,
                            ProductTypeActivity.categoryCardViewHolder.class,
                            myquery
                    ) {

                        @Override
                        protected void populateViewHolder(final ProductTypeActivity.categoryCardViewHolder viewHolder, CategoryModel model, final int position) {
                            final String post_key = getRef(position).getKey();
                            try {

                                viewHolder.setTitle(model.getName());
                                viewHolder.setPrice(model.getPrice());
                                if (model.getBaseprice() != 0) {
                                    viewHolder.setBasePrice(model.getBaseprice());
                                }
                                viewHolder.setImage(ProductTypeActivity.this, model.getImageurl());
                                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        viewHolder.startProductActivity(ProductTypeActivity.this, post_key);
                                    }
                                });
                            } catch (Exception e) {
                                Log.e("Try Error", "Grid Category Error Logged.");
                            }
                        }

                        @Override
                        protected void onDataChanged() {
                            super.onDataChanged();
                            mProgress.setVisibility(View.GONE);
                            mRecycleView.setVisibility(View.VISIBLE);
                        }
                    };

                    mRecycleView.setAdapter(categoryCardRecycler);
                    GridLayoutManager grid = new GridLayoutManager(getApplicationContext(), 2);

                    mRecycleView.setLayoutManager(grid);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myquery.addValueEventListener(myQueryListner);
    }

    public void setupToolbar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(title);
        }
    }


    public static class categoryCardViewHolder extends RecyclerView.ViewHolder {
        View mview;

        public categoryCardViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
        }

        public void setTitle(String title) {
            TextView product_title = (TextView) mview.findViewById(R.id.categoryCardTitle);
            product_title.setText(title);
        }

        public void setBasePrice(int price) {
            if (price != 0) {
                TextView product_price = (TextView) mview.findViewById(R.id.categoryCardBasePrice);
                product_price.setPaintFlags(product_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                product_price.setText("Rs " + price + "/-");
                product_price.setVisibility(View.VISIBLE);
            }
        }

        public void startProductActivity(Context ctx, String post_key) {
            try {
                final Intent productIntent = new Intent(ctx, ProductActivity.class);
                productIntent.putExtra("post_key", post_key);
                ctx.startActivity(productIntent);
            } catch (Exception e) {
                Log.e("Cat Fragment", "Unable to launch post" + post_key);
            }
        }

        public void setPrice(int price) {
            TextView product_price = (TextView) mview.findViewById(R.id.categoryCardPrice);
            product_price.setText("Rs " + price + "/-");
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

            ImageView category_image = (ImageView) mview.findViewById(R.id.categoryCardImageView);
            ImageLoader.getInstance().displayImage(image, category_image, options);
            //Picasso.with(ctx).load(image).resize(180, 180).placeholder(R.drawable.loading_100).into(category_image);
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ACTIVE == 1) {
            categoryCardRecycler.cleanup();
            mRecycleView.removeAllViewsInLayout();
            ImageLoader.getInstance().clearMemoryCache();
            myquery.removeEventListener(myQueryListner);
        }
    }
}