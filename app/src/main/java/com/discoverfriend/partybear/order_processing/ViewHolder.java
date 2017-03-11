package com.discoverfriend.partybear.order_processing;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.discoverfriend.partybear.R;

/**
 * Created by mukesh on 02/02/17.
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    View mview;

    public ViewHolder(View itemView) {
        super(itemView);
        mview = itemView;
    }

    public void SetDate(String date) {
        TextView date_textview = (TextView) mview.findViewById(R.id.order_name);
        date_textview.setText(String.valueOf(date));
    }

    public void SetName(String name) {
        TextView date_textview = (TextView) mview.findViewById(R.id.order_name1);
        date_textview.setText(String.valueOf(name));
    }

    public void SetOrderStatus(String status) {
        TextView order_status_textview = (TextView) mview.findViewById(R.id.order_status);
        order_status_textview.setText(status);
    }

    public void SetOrderCoupon(String status) {
        LinearLayout couponLayout = (LinearLayout) mview.findViewById(R.id.couponLayout);
        couponLayout.setVisibility(View.VISIBLE);
        TextView order_status_textview = (TextView) mview.findViewById(R.id.couponCode);
        order_status_textview.setText("Coupon " + status + " Applied");
    }

    public void SetOrderId(String status) {
        TextView order_status_textview = (TextView) mview.findViewById(R.id.order_id);
        order_status_textview.setText("Order ID: " + status);
    }

    public void SetTotal(Long orderTotal) {
        TextView order_total = (TextView) mview.findViewById(R.id.order_amount);
        order_total.setText(String.valueOf(orderTotal));
    }
}
