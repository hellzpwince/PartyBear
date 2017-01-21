package com.discoverfriend.partybear;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.discoverfriend.partybear.category.CategoryModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.roughike.bottombar.BottomBar;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    DatabaseReference rootRef;
    DatabaseReference databaseRef;
    Query query;
    private View mView;
    private RecyclerView mRecycleView;
    private ProgressBar mProgress;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        this.mView = view;
        mRecycleView = (RecyclerView) mView.findViewById(R.id.fragOneRV_xml);
        mProgress = (ProgressBar) mView.findViewById(R.id.fragmentProgressbarOne);
        rootRef = FirebaseDatabase.getInstance().getReference();
        databaseRef = rootRef.child("categories").child("cakes");
        query = databaseRef;
        GridLayoutManager grid = new GridLayoutManager(getActivity(), 2);
        mRecycleView.setLayoutManager(grid);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<CategoryModel, CategoryFragment.categoryCardViewHolder> categoryCardRecycler = new FirebaseRecyclerAdapter<CategoryModel, CategoryFragment.categoryCardViewHolder>(
                CategoryModel.class,
                R.layout.categorycardview,
                CategoryFragment.categoryCardViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(CategoryFragment.categoryCardViewHolder viewHolder, CategoryModel model, int position) {
                final String post_key = getRef(position).getKey();
                viewHolder.setTitle(model.getCategoryName());
                viewHolder.setImage(getActivity(), model.getCategoryImage());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Hello", Toast.LENGTH_SHORT).show();
                    }
                });
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
            TextView category_title = (TextView) mview.findViewById(R.id.categoryCardTitle);
            category_title.setText(title);
        }

        public void setImage(Context ctx, String image) {
            ImageView category_image = (ImageView) mview.findViewById(R.id.categoryCardImageView);
            Picasso.with(ctx).load(image).into(category_image);
        }
    }
}
