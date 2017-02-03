package com.discoverfriend.partybear.cart;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.discoverfriend.partybear.Product.ProductActivity;
import com.discoverfriend.partybear.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * Created by mukesh on 01/02/17.
 */

public class CartViewHolder extends RecyclerView.ViewHolder {
    View mview;

    public CartViewHolder(View itemView) {
        super(itemView);
        mview = itemView;
    }


    public void setName(String title) {
        TextView product_title = (TextView) mview.findViewById(R.id.cart_item_name);
        product_title.setText(title);
    }

    public void setProductType(String type) {
        if (type != null) {
            TextView product_type = (TextView) mview.findViewById(R.id.cart_item_type);
            product_type.setVisibility(View.VISIBLE);
            product_type.setText(type);
        }

    }

    public void setProductRemove(final String uid, String id) {
        final String user_id = uid;
        final String item_id = id;
        if (id != null) {
            TextView remove_item = (TextView) mview.findViewById(R.id.cart_remove_item);
            remove_item.setVisibility(View.VISIBLE);
            remove_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference cart = FirebaseDatabase.getInstance().getReference("cart").child(user_id);
                    cart.child(item_id).removeValue();
                }
            });
        }

    }

    public void setProductWishlishRemove(final String uid, String id) {
        final String user_id = uid;
        final String item_id = id;
        if (id != null) {
            TextView remove_item = (TextView) mview.findViewById(R.id.cart_remove_item);
            remove_item.setVisibility(View.VISIBLE);
            remove_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference cart = FirebaseDatabase.getInstance().getReference("wishlist").child(user_id);
                    cart.child(item_id).removeValue();
                }
            });
        }

    }

    public void SetProductLink(final Context ctx, final String post_key) {
        mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent productIntent;
                productIntent = new Intent(ctx, ProductActivity.class);
                productIntent.putExtra("post_key", post_key);
                ctx.startActivity(productIntent);
            }
        });
    }

    public void setPrice(long price) {
        TextView product_price = (TextView) mview.findViewById(R.id.cart_item_price);
        product_price.setText("Rs " + price + "/-");
    }


    public void setImage(Context ctx, String image) {
        ImageView category_image = (ImageView) mview.findViewById(R.id.cart_item_image);
        Picasso.with(ctx).load(image).placeholder(R.drawable.loading_100).into(category_image);
    }
}
