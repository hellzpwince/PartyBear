package com.discoverfriend.partybear.addons;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.discoverfriend.partybear.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class addonFragment extends Fragment {
    private View mView;
    private RecyclerView addonRCView;
    Context ctx;
    String position = "", path = "";
    TextView fragmentTitleTextview;
    DatabaseReference addonRoot;
    Query query;
    ValueEventListener queryListener;

    public addonFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addon, container, false);
        ctx = getActivity();
        basicSetup();
        this.mView = view;
        if (!getArguments().isEmpty()) {
            position = getArguments().getString("Position");
            path = getArguments().getString("path");
            fragmentTitleTextview.setText(position);
            query = addonRoot.child(path);
            queryListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.e("Data",dataSnapshot+"");44
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            query.addValueEventListener(queryListener);
        }


        return mView;
    }

    public void basicSetup() {
        fragmentTitleTextview = (TextView) mView.findViewById(R.id.fragmentaddonTextview);
        addonRCView = (RecyclerView) mView.findViewById(R.id.addon_RCView);
        GridLayoutManager recyclerGridLayout = new GridLayoutManager(ctx, 3);
        addonRCView.setLayoutManager(recyclerGridLayout);
        addonRoot = FirebaseDatabase.getInstance().getReference("addons");
    }


}
