package com.discoverfriend.partybear;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.discoverfriend.partybear.Product.ProductActivity;
import com.discoverfriend.partybear.category.CategoryModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.florent37.viewanimator.ViewAnimator;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    DatabaseReference rootRef;
    String categoryid;
    String categoryname;
    static Context ctx;
    Query myquery;
    private View mView;
    int ACTIVE = 0;
    private RecyclerView mRecycleView;
    private SpinKitView mProgress;
    FirebaseRecyclerAdapter<CategoryModel, CategoryFragment.categoryCardViewHolder> categoryCardRecycler;
    ValueEventListener myQueryListner;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ctx = getActivity();
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        categoryid = (String) getArguments().get("categoryid");
        categoryname = (String) getArguments().get("categoryname");
        this.mView = view;
        mRecycleView = (RecyclerView) mView.findViewById(R.id.fragOneRV_xml);
        mProgress = (SpinKitView) mView.findViewById(R.id.fragmentProgressbarOne);
        rootRef = FirebaseDatabase.getInstance().getReference();
        myquery = rootRef.child("categories").child(categoryid).child("products").orderByPriority();
        myQueryListner = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    mRecycleView.setVisibility(View.GONE);
                    mProgress.setVisibility(View.GONE);
                    LinearLayout error_screen = (LinearLayout) mView.findViewById(R.id.error_screen);
                    error_screen.setVisibility(View.VISIBLE);
                } else {
                    ACTIVE = 1;
                    categoryCardRecycler = new FirebaseRecyclerAdapter<CategoryModel, CategoryFragment.categoryCardViewHolder>(
                            CategoryModel.class,
                            R.layout.categorycardview,
                            CategoryFragment.categoryCardViewHolder.class,
                            myquery
                    ) {

                        @Override
                        protected void populateViewHolder(final CategoryFragment.categoryCardViewHolder viewHolder, CategoryModel model, final int position) {
                            final String post_key = getRef(position).getKey();
                            try {
                                viewHolder.setTitle(model.getName());
                                viewHolder.setPrice(model.getPrice());
                                viewHolder.setEggless(model.getFeature());
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
                            mProgress.setVisibility(View.GONE);
                        }

                        @Override
                        protected void onDataChanged() {
                            super.onDataChanged();
                        }
                    };
                    mRecycleView.setVisibility(View.VISIBLE);
                    ViewAnimator.animate(mRecycleView).alpha(100).duration(300).fadeIn().start();

                    mRecycleView.setAdapter(categoryCardRecycler);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myquery.addValueEventListener(myQueryListner);

        LinearLayoutManager linear = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        mRecycleView.setLayoutManager(linear);
        return mView;

    }

    @Override
    public void onStart() {
        super.onStart();


    }


    /*ViewHolder Class for Category Class*/
    public static class categoryCardViewHolder extends RecyclerView.ViewHolder {
        View mview;

        public categoryCardViewHolder(View itemView) {
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

        public void startCategoryActivity(final String post_key, final String catname) {
            try {
                final Intent productIntent = new Intent(ctx, CategoryActivity.class);
                productIntent.putExtra("categoryid", post_key);
                productIntent.putExtra("categoryname", catname);
                ctx.startActivity(productIntent);
            } catch (Exception e) {
                Log.e("Cat Fragment", "Unable to launch post" + post_key);
                Snackbar.make(mview, "Unable to open item. Please try again!", Snackbar.LENGTH_SHORT).show();
            }
        }

        public void setTitle(String title) {
            TextView product_title = (TextView) mview.findViewById(R.id.categoryCardTitle);
            product_title.setText(title);
        }

        public void setPremium(String title) {
            if (title.equals("yes")) {
                ImageView productimage = (ImageView) mview.findViewById(R.id.premiumImage);
                productimage.setVisibility(View.VISIBLE);
            }
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
                    .showImageOnLoading(R.drawable.loading)
                    .showImageForEmptyUri(R.drawable.loading)
                    .showImageOnFail(R.drawable.loading)
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ACTIVE == 1) {
            categoryCardRecycler.cleanup();
            mRecycleView.removeAllViewsInLayout();
            ctx = null;
            ImageLoader.getInstance().clearMemoryCache();
            myquery.removeEventListener(myQueryListner);
            mProgress = null;
            mView = null;

        }
    }

}
