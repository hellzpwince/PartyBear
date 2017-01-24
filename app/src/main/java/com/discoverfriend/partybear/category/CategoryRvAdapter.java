package com.discoverfriend.partybear.category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.discoverfriend.partybear.CakeItemModel;

import java.util.ArrayList;

/**
 * Created by mukesh on 21/01/17.
 */

public class CategoryRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<CakeItemModel> marrayList;

    public CategoryRvAdapter(Context ctx, ArrayList<CakeItemModel> arrayList) {
        this.context = ctx;
        this.marrayList = arrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
