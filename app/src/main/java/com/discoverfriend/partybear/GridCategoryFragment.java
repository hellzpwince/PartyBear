package com.discoverfriend.partybear;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.discoverfriend.partybear.Product.ProductActivity;
import com.discoverfriend.partybear.category.CategoryModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

/**
 * Created by mukesh on 25/01/17.
 */

public class GridCategoryFragment extends Fragment {

    DatabaseReference rootRef;
    String categoryid;
    String categoryname;
    Query myquery;
    private View mView;
    private RecyclerView mRecycleView;
    private ProgressBar mProgress;

    public GridCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        categoryid = (String) getArguments().get("categoryid");
        categoryname = (String) getArguments().get("categoryname");
        this.mView = view;
        mRecycleView = (RecyclerView) mView.findViewById(R.id.fragOneRV_xml);
        mProgress = (ProgressBar) mView.findViewById(R.id.fragmentProgressbarOne);
        rootRef = FirebaseDatabase.getInstance().getReference();
        myquery = rootRef.child("categories").child(categoryid).child("products").orderByPriority();


        GridLayoutManager grid = new GridLayoutManager(getActivity().getApplicationContext(), 2);

        mRecycleView.setLayoutManager(grid);
        return mView;

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<CategoryModel, CategoryFragment.categoryCardViewHolder> categoryCardRecycler = new FirebaseRecyclerAdapter<CategoryModel, CategoryFragment.categoryCardViewHolder>(
                CategoryModel.class,
                R.layout.grid_cardview,
                CategoryFragment.categoryCardViewHolder.class,
                myquery
        ) {

            @Override
            protected void populateViewHolder(final CategoryFragment.categoryCardViewHolder viewHolder, CategoryModel model, final int position) {
                final String post_key = getRef(position).getKey();
                try {

                    viewHolder.setTitle(model.getName());
                    viewHolder.setPrice(model.getPrice());
                    viewHolder.setImage(getActivity(), model.getImageurl());
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewHolder.startProductActivity(getActivity(), post_key);
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

    }

    /*ViewHolder Class for Category Class*/
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

        public void startProductActivity(Context ctx, String post_key) {
            Intent productIntent = new Intent(ctx, ProductActivity.class);
            productIntent.putExtra("post_key", post_key);
            ctx.startActivity(productIntent);

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
