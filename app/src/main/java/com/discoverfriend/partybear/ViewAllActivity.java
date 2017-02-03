package com.discoverfriend.partybear;

import android.content.Context;
import android.content.Intent;
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

import com.discoverfriend.partybear.category.CategoryModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewAllActivity extends AppCompatActivity {
    String itemtype, itemlink, itemname;
    DatabaseReference rootRef;
    Query myquery;
    private View mView;
    private RecyclerView mRecycleView;
    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemlink = extras.getString("link");
            itemname = extras.getString("name");
            itemtype = extras.getString("type");
        }
        setupToolbar(itemname);

        mRecycleView = (RecyclerView) findViewById(R.id.fragOneRV_xml);
        mProgress = (ProgressBar) findViewById(R.id.progress_listactivity);
        rootRef = FirebaseDatabase.getInstance().getReference();
        myquery = rootRef.child("categories").orderByChild("type").equalTo(itemlink);
        myquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    mRecycleView.setVisibility(View.GONE);
                    mProgress.setVisibility(View.GONE);
                    setContentView(R.layout.nothing_found);
                    setupToolbar(itemname);

                } else {
                    FirebaseRecyclerAdapter<CategoryModel, ViewAllActivity.categoryCardViewHolder> categoryCardRecycler = new FirebaseRecyclerAdapter<CategoryModel, ViewAllActivity.categoryCardViewHolder>(
                            CategoryModel.class,
                            R.layout.grid_cardview,
                            ViewAllActivity.categoryCardViewHolder.class,
                            myquery
                    ) {

                        @Override
                        protected void populateViewHolder(final ViewAllActivity.categoryCardViewHolder viewHolder, final CategoryModel model, final int position) {
                            final String post_key = getRef(position).getKey();
                            try {

                                viewHolder.setTitle(model.getName());
                                viewHolder.setImage(ViewAllActivity.this, model.getImageurl());
                                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        viewHolder.startProductActivity(ViewAllActivity.this, post_key, model.getName());
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
        });
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

        public void startProductActivity(Context ctx, String post_key, String name) {
            try {
                final Intent productIntent = new Intent(ctx, CategoryActivity.class);
                productIntent.putExtra("categoryid", post_key);
                productIntent.putExtra("categoryname", name);
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
            ImageView category_image = (ImageView) mview.findViewById(R.id.categoryCardImageView);
            Picasso.with(ctx).load(image).resize(400, 400).placeholder(R.drawable.loading_100).into(category_image);
        }
    }
}
