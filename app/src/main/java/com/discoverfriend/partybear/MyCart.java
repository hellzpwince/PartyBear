package com.discoverfriend.partybear;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.models.Image;
import com.discoverfriend.partybear.cart.CartModel;
import com.discoverfriend.partybear.cart.CartViewHolder;
import com.discoverfriend.partybear.checkout.CheckoutAcitivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.florent37.viewanimator.ViewAnimator;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.instamojo.android.Instamojo;
import com.instamojo.android.activities.PaymentDetailsActivity;
import com.instamojo.android.callbacks.OrderRequestCallBack;
import com.instamojo.android.helpers.Constants;
import com.instamojo.android.models.Errors;
import com.instamojo.android.models.Order;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.onurciner.toastox.ToastOX;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyCart extends AppCompatActivity {
    Context ctx = MyCart.this;
    Toolbar toolbar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private RecyclerView rc_view;
    DatabaseReference root;
    private long total = 0, cod = 0;
    private long realtotal = 0, percent = 0, couponType = 0;
    TextView totalview;
    private ProgressDialog dialog;
    Button btn_checkout;
    String mPhone = null;
    String mPurpose = "", couponCodeString, specialnote = "";
    String accessToken;
    String transactionID = randomString(50);
    private int Transaction_status = 0, COUPON_APPLIED = 0, HAS_IMAGES = 0, REQUIRE_IMAGES = 0;
    String time;
    Query myquery;
    String useremail = "pooja@partybear.in";
    SpinKitView cart_progress;
    NestedScrollView nestedview;
    String username = "";
    String postpaymentid;
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();
    String postPaymentStatus;
    String dAddress = "";
    LinearLayoutManager linear;
    ValueEventListener myQueryListener;
    ChildEventListener myQueryListener2;
    FirebaseAnalytics mFirebaseAnalytics;
    TextView coupontext;
    ButtonBarLayout btnbarlayout;
    FirebaseStorage storage;
    LinearLayout addimagebtn;
    TextView addimagestring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = FirebaseStorage.getInstance();
        setContentView(R.layout.activity_my_cart);
        btnbarlayout = (ButtonBarLayout) findViewById(R.id.cart_bottom_bar);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        cart_progress = (SpinKitView) findViewById(R.id.cart_progress);
        nestedview = (NestedScrollView) findViewById(R.id.categoryscroll_view);
        coupontext = (TextView) findViewById(R.id.coupontext);
        addimagestring = (TextView) findViewById(R.id.addImagestring);
        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        final FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.action_a);
        final FloatingActionButton actionB = (FloatingActionButton) findViewById(R.id.action_b);
        final LinearLayout specialnotebtn = (LinearLayout) findViewById(R.id.specialNotebtnlayout);
        addimagebtn = (LinearLayout) findViewById(R.id.addImagebtnlayout);
        addimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, AlbumSelectActivity.class);
                intent.putExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_LIMIT, 20);
                startActivityForResult(intent, com.darsh.multipleimageselect.helpers.Constants.REQUEST_CODE);
            }
        });
        specialnotebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup vg = (ViewGroup) getLayoutInflater().inflate(R.layout.viewgroup, null);
                final View specialnoteview = getLayoutInflater().inflate(R.layout.special_note, vg);
                final LovelyCustomDialog mDialog = new LovelyCustomDialog(ctx);
                mDialog.setView(specialnoteview)
                        .setTopColorRes(R.color.colorPrimaryDark)
                        .setTopTitle("Special Note")
                        .setTopTitleColor(ContextCompat.getColor(ctx, R.color.colorWhite))
                        .setCancelable(true)
                        .setListener(R.id.couponCodeBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EditText couponcode = (EditText) specialnoteview.findViewById(R.id.couponCode);
                                if (!TextUtils.isEmpty(couponcode.getText())) {
                                    specialnote = couponcode.getText().toString();
                                    TextView specialnotestring = (TextView) findViewById(R.id.specialnotestring);
                                    specialnotestring.setText(specialnote);
                                    specialnotebtn.setBackground(ContextCompat.getDrawable(ctx, R.drawable.cart_updated_border_bg));

                                } else {
                                    Toast.makeText(ctx, "Special Note can't be empty", Toast.LENGTH_SHORT).show();
                                }
                                mDialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        dialog = new ProgressDialog(ctx);
        dialog.setIndeterminate(true);
        mAuth = FirebaseAuth.getInstance();


        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null) {
                    Intent addressUpdate = new Intent(ctx, DeliveryActivity.class);
                    addressUpdate.putExtra("from", "cart");
                    startActivity(addressUpdate);
                    finish();
                } else {
                    Toast.makeText(ctx, "Please Login to set Delivery Address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null) {
                    Intent addressUpdate = new Intent(ctx, Feedback.class);
                    startActivity(addressUpdate);
                    finish();
                } else {
                    Toast.makeText(ctx, "Please Login to send Feedback", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button actioncoupon = (Button) findViewById(R.id.action_coupon);
        actioncoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup vg = (ViewGroup) getLayoutInflater().inflate(R.layout.viewgroup, null);
                final View cakeOptions = getLayoutInflater().inflate(R.layout.coupon, vg);
                final LovelyCustomDialog mDialog = new LovelyCustomDialog(ctx);
                mDialog.setView(cakeOptions)
                        .setTopColorRes(R.color.colorPrimaryDark)
                        .setTopTitle("Apply Coupon ")
                        .setTopTitleColor(ContextCompat.getColor(ctx, R.color.colorWhite))
                        .setCancelable(true)
                        .setListener(R.id.couponCodeBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EditText couponcode = (EditText) cakeOptions.findViewById(R.id.couponCode);
                                if (!TextUtils.isEmpty(couponcode.getText())) {
                                    ValidateCoupon(couponcode.getText().toString());
                                } else {
                                    Toast.makeText(ctx, "Enter Valid Coupon Code", Toast.LENGTH_SHORT).show();
                                }
                                mDialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        btn_checkout = (Button) findViewById(R.id.btn_payment);
        setupToolbar();
        if (mAuth.getCurrentUser() != null) {
            username = mAuth.getCurrentUser().getDisplayName();
            if (mAuth.getCurrentUser().getEmail() == null) {
                useremail = "partybear@gmail.com";
            }

        }
        totalview = (TextView) findViewById(R.id.cart_total);
        totalview.setText("\u20B9" + String.valueOf(total) + "/-");
        root = null;

        rc_view = (RecyclerView) findViewById(R.id.rc_cart);
        if (mAuth.getCurrentUser() != null) {
            root = FirebaseDatabase.getInstance().getReference("cart").child(mAuth.getCurrentUser().getUid());
            myquery = root.orderByKey();
            fetchPhone(mAuth.getCurrentUser().getUid());

            setupAdapter(myquery);
        } else {
            setContentView(R.layout.not_signed_in);

        }
        linear = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
        rc_view.setLayoutManager(linear);
        btn_checkout.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (mAuth.getCurrentUser() != null) {
                                                    if (REQUIRE_IMAGES == 0 || (REQUIRE_IMAGES > 0 && HAS_IMAGES == 1)) {
                                                        new BottomSheet.Builder(MyCart.this).title("Choose Payment Method").sheet(R.menu.checkout_menu).listener(new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog1, int which) {
                                                                switch (which) {
                                                                    case R.id.cod:
                                                                        cashOnDelivery();
                                                                        break;
                                                                    case R.id.online:
                                                                        dialog.show();
                                                                        deliveryAddressAvailable();
                                                                        break;
                                                                }
                                                            }
                                                        }).show();
                                                    } else {
                                                        Toast.makeText(ctx, "Upload images to complete your order.", Toast.LENGTH_SHORT).show();
                                                        addimagebtn.setBackground(ContextCompat.getDrawable(ctx, R.color.colorPrimary));
                                                        addimagestring.setTextColor(ContextCompat.getColor(ctx, R.color.colorWhite));
                                                        ViewAnimator.animate(addimagestring).flash().duration(200).start();
                                                    }
                                                }
                                            }
                                        }

        );
        mAuthListener = new FirebaseAuth.AuthStateListener()

        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mAuth.getCurrentUser() != null) {
                    username = mAuth.getCurrentUser().getDisplayName();
                    if (mAuth.getCurrentUser().getEmail() != null) {
                        useremail = mAuth.getCurrentUser().getEmail();
                    } else {
                        useremail = "partybear@gmail.com";
                    }
                } else {

                }
            }
        }

        ;

    }

    public void setupAdapter(final Query myquery) {
        if (mAuth.getCurrentUser() != null) {
            myQueryListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        FirebaseRecyclerAdapter<CartModel, CartViewHolder> myCartAdapter = new FirebaseRecyclerAdapter<CartModel, CartViewHolder>(
                                CartModel.class,
                                R.layout.cart_list_item,
                                CartViewHolder.class,
                                myquery
                        ) {

                            @Override
                            protected void populateViewHolder(final CartViewHolder viewHolder, CartModel model, final int position) {
                                final String post_key = getRef(position).getKey();
                                final String user_id = mAuth.getCurrentUser().getUid();
                                try {
                                    mPurpose += model.getName() + "";
                                    if (model.getImage_required() == 1) {
                                        REQUIRE_IMAGES++;
                                    }
                                    viewHolder.setName(model.getName());
                                    viewHolder.setPrice(model.getTotal());
                                    viewHolder.setDeliveryPrice(model.getTime_charge());
                                    viewHolder.setProductBasePrice(model.getPrice());
                                    viewHolder.setEgglessPrice(model.getType_charge());
                                    viewHolder.setProductType(model.getProducttype());
                                    viewHolder.setProductRemove(user_id, post_key);
                                    viewHolder.swipeRemove(user_id, post_key);
                                    viewHolder.setImage(ctx, model.getImageurl());

                                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            viewHolder.startProductActivity(ctx, post_key);

                                        }
                                    });

                                } catch (Exception e) {
                                    Log.e("Try Error", "Grid Category Error Logged.");
                                }
                            }

                            @Override
                            protected void onDataChanged() {
                                super.onDataChanged();


                            }
                        };
                        rc_view.setAdapter(myCartAdapter);
                    } else {
                        try {
                            setContentView(R.layout.nothing_found);
                            setupToolbar();
                        } catch (Exception e) {
                            FirebaseCrash.log(e.getMessage());
                        }

                    }
                    cart_progress.setVisibility(View.GONE);
                    nestedview.setVisibility(View.VISIBLE);
                    ViewAnimator.animate(btnbarlayout).translationY(0).duration(200).start();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            myquery.addValueEventListener(myQueryListener);
            myQueryListener2 = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.hasChild("total")) {
                        total = total + Long.parseLong(dataSnapshot.child("total").getValue().toString());
                        realtotal = realtotal + Long.parseLong(dataSnapshot.child("total").getValue().toString());
                        totalview.setText("\u20B9" + String.valueOf(total));
                        if (COUPON_APPLIED == 1) {
                            long discount = (percent * realtotal) / 100;
                            total = realtotal - discount;
                            totalview.setText("\u20B9" + String.valueOf(total));
                            coupontext.setText("Coupon Code :" + String.valueOf(couponCodeString) + " Applied. Real Price \u20B9:" + String.valueOf(realtotal));
                        }
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    COUPON_APPLIED = 1;
                    if (dataSnapshot.hasChild("image_required")) {
                        REQUIRE_IMAGES--;
                    }
                    if (dataSnapshot.hasChild("total")) {
                        total = total - Long.parseLong(dataSnapshot.child("total").getValue().toString());
                        realtotal = realtotal - Long.parseLong(dataSnapshot.child("total").getValue().toString());
                        totalview.setText("\u20B9" + String.valueOf(total));
                        if (COUPON_APPLIED == 1) {
                            if (couponType == 1) {
                                long discount = (percent * realtotal) / 100;
                                total = realtotal - discount;
                                totalview.setText("\u20B9" + String.valueOf(total));
                                TextView coupontext = (TextView) findViewById(R.id.coupontext);
                                coupontext.setText("Coupon Code :" + String.valueOf(couponCodeString) + " Applied. Real Price :" + String.valueOf(realtotal));
                            } else if (couponType == 2) {
                                total = realtotal - percent;
                                totalview.setText("\u20B9" + String.valueOf(total));
                                Toast.makeText(ctx, "Coupon Applied", Toast.LENGTH_SHORT).show();
                                TextView coupontext = (TextView) findViewById(R.id.coupontext);
                                coupontext.setText("Coupon Code :" + String.valueOf(couponCodeString) + " Applied. Real Price :" + String.valueOf(realtotal));
                            }
                        }
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            };
            myquery.addChildEventListener(myQueryListener2);
        } else {
            Toast.makeText(ctx, "Please Login", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);


    }

    public void fetchPhone(String uid) {

        if (uid != null) {
            DatabaseReference address = FirebaseDatabase.getInstance().getReference("deliveryaddress").child(mAuth.getCurrentUser().getUid());
            address.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()
                            ) {
                        dAddress += " " + child.getValue().toString();
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            DatabaseReference myref = FirebaseDatabase.getInstance().getReference("deliveryaddress").child(uid);
            myref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("mobile")) {
                        mPhone = (String) dataSnapshot.child("mobile").getValue();
                    } else {
                        mPhone = "8006444711";
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void generateTransactionID(String username, String useremail, String phone, String purpose, Long Amount) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
        final Map transaction = new HashMap();
        transaction.put("username", username);
        transaction.put("email", useremail);
        transaction.put("phone", phone);
        transaction.put("purpose", purpose);
        transaction.put("amount", Amount);
        transaction.put("status", "Initiated");
        transaction.put("transactionid", transactionID);
        transaction.put("deliveryaddress", dAddress);
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference("transactions");
        myref.child(mAuth.getCurrentUser().getUid()).child(transactionID).updateChildren(transaction);
    }

    public void updateTransactionID(String status, String paymentid, String OrderId) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
        Map transaction = new HashMap();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date rightnow = new Date();
        String date = df.format(rightnow);

        transaction.put("status", status);
        transaction.put("transactionid", transactionID);
        transaction.put("orderid", OrderId);
        transaction.put("paymentid", paymentid);
        transaction.put("date", date);
        time = date;
        postpaymentid = paymentid;
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference("transactions");
        myref.child(mAuth.getCurrentUser().getUid()).child(transactionID).updateChildren(transaction);
    }

    public void removeTransactionID(String oldID) {
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference("transactions");
        myref.child(mAuth.getCurrentUser().getUid()).child(oldID).removeValue();
    }

    public void requestOrder() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
        String phone = String.valueOf(mPhone);

        String totalamount = String.valueOf(total);
        //Create the Order

        Order order = new Order(accessToken, transactionID, username, useremail, phone, totalamount, mPurpose);
        //set webhook
        order.setWebhook("http://partybear.in/api/webhooks/storedata.php");

        //Validate the Order
        try {
            if (!order.isValid()) {
                //oops order validation failed. Pinpoint the issue(s).

                if (!order.isValidName()) {
                    Log.e("order", order.getBuyerName());
                    Log.e("order", "Buyer name is invalid");
                }

                if (!order.isValidEmail()) {
                    Log.e("order", order.getBuyerEmail());
                    Log.e("order", "Buyer email is invalid");
                }

                if (!order.isValidPhone()) {
                    Log.e("order", order.getBuyerPhone());
                    Log.e("order", "Buyer phone is invalid");
                }

                if (!order.isValidAmount()) {
                    Log.e("order", order.getAmount());
                    Log.e("order", "Amount is invalid or has more than two decimal places");
                }

                if (!order.isValidDescription()) {
                    Log.e("order", order.getDescription());
                    Log.e("order", "Description is invalid");
                }

                if (!order.isValidTransactionID()) {
                    Log.e("order", order.getTransactionID());
                    Log.e("order", "Transaction is Invalid");
                }

                if (!order.isValidRedirectURL()) {
                    Log.e("order", order.getRedirectionUrl());
                    Log.e("order", "Redirection URL is invalid");
                }

                if (!order.isValidWebhook()) {
                    Log.e("order", order.getWebhook());
                    Log.e("order", "Webhook URL is invalid");
                }

                return;
            }
        } catch (Exception e) {

            Log.e("Handling", order.getBuyerEmail() + e.getMessage());
        }
        //Validation is successful. Proceed

        com.instamojo.android.network.Request request = new com.instamojo.android.network.Request(order, new OrderRequestCallBack() {
            @Override
            public void onFinish(final Order order, final Exception error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        if (error != null) {
                            if (error instanceof Errors.ConnectionError) {
                                showToast("No internet connection");
                            } else if (error instanceof Errors.ServerError) {
                                showToast("Server Error. Try again");
                            } else if (error instanceof Errors.AuthenticationError) {
                                showToast("Access token is invalid or expired. Please Update the token!!");
                            } else if (error instanceof Errors.ValidationError) {
                                // Cast object to validation to pinpoint the issue
                                Errors.ValidationError validationError = (Errors.ValidationError) error;

                                if (!validationError.isValidTransactionID()) {
                                    showToast("Transaction ID is not Unique");
                                    return;
                                }

                                if (!validationError.isValidRedirectURL()) {
                                    showToast("Redirect url is invalid");
                                    return;
                                }

                                if (!validationError.isValidWebhook()) {
                                    showToast("Webhook url is invalid");
                                    return;
                                }

                                if (!validationError.isValidPhone()) {
                                    Log.e("order", "Buyer's Phone Number is invalid/empty");
                                    return;
                                }

                                if (!validationError.isValidEmail()) {
                                    Log.e("order", "Buyer's Email is invalid/empty");
                                    return;
                                }

                                if (!validationError.isValidAmount()) {
                                    Log.e("order", "Amount is either less than \u20B99 or has more than two decimal places");
                                    return;
                                }

                                if (!validationError.isValidName()) {
                                    Log.e("order", "Buyer's Name is required");
                                    return;
                                }
                            } else {
                                showToast(error.getMessage());
                            }
                            return;
                        }

                        startPreCreatedUI(order);
                    }
                });
            }
        });

        request.execute();

    }

    private void startPreCreatedUI(Order order) {
        Intent intent = new Intent(getBaseContext(), PaymentDetailsActivity.class);
        intent.putExtra(Constants.ORDER, order);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    public void requestToken() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://partybear.in/api/payment_request/token.php")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        showToast("Failed to fetch the Order Tokens");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString;
                String errorMessage = null;
                responseString = response.body().string();
                response.body().close();
                try {
                    JSONObject responseObject = new JSONObject(responseString);
                    if (responseObject.has("error")) {
                        errorMessage = responseObject.getString("error");
                    } else {
                        accessToken = responseObject.getString("access_token");
                    }
                } catch (JSONException e) {
                    errorMessage = "Failed to fetch Order tokens";
                }

                final String finalErrorMessage = errorMessage;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (finalErrorMessage != null) {
                            showToast(finalErrorMessage);
                            return;
                        }
                        requestOrder();

                    }
                });

            }
        });
    }

    String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE && data != null) {
            String orderID = data.getStringExtra(Constants.ORDER_ID);
            String mtransactionID = data.getStringExtra(Constants.TRANSACTION_ID);
            String paymentID = data.getStringExtra(Constants.PAYMENT_ID);

            // Check transactionID, orderID, and orderID for null before using them to check the Payment status.
            if (mtransactionID != null || paymentID != null) {
                checkPaymentStatus(mtransactionID, orderID);
            } else {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                showToast("Oops!! Payment was cancelled");
                removeTransactionID(transactionID);
                transactionID = randomString(50);

            }
        }

        if (requestCode == com.darsh.multipleimageselect.helpers.Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            final ProgressDialog loadimageProgress = new ProgressDialog(ctx);
            loadimageProgress.setMessage("Uploading Images");
            loadimageProgress.setTitle("Please Wait");
            loadimageProgress.show();

            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_IMAGES);
            StorageReference storageRef = storage.getReference();
            final DatabaseReference orderimages = FirebaseDatabase.getInstance().getReference("order_images").child(transactionID);
            for (Image child : images) {
                loadimageProgress.setMessage("Uploading " + images.size() + " Image");
                Uri file = Uri.fromFile(new File(child.path));
                StorageReference riversRef = storageRef.child("orders/" + transactionID + "/" + randomString(5) + file.getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(file);
                Bundle bundle = new Bundle();
                bundle.putLong(FirebaseAnalytics.Param.VALUE, total);
                bundle.putLong(FirebaseAnalytics.Param.TAX, 0);
                bundle.putString(FirebaseAnalytics.Param.COUPON, String.valueOf(couponCodeString));
                bundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID, transactionID);
                bundle.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, bundle);
// Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(ctx, "Failed to Load Image ", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        orderimages.push().setValue(downloadUrl.toString());
                        loadimageProgress.dismiss();
                    }
                });
            }
            addimagestring.setText(images.size() + " Images Uploaded");
            addimagestring.setTextColor(ContextCompat.getColor(ctx, R.color.colorDarkBlack));
            addimagebtn.setBackground(ContextCompat.getDrawable(ctx, R.drawable.cart_updated_border_bg));
            HAS_IMAGES = 1;

        }
    }

    private void checkPaymentStatus(final String transactionID, final String orderID) {
        if (accessToken == null || (transactionID == null && orderID == null)) {
            return;
        }

        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://partybear.in/api/payment_request/request.php?payment_id=" + orderID)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        showToast("Failed to fetch the Transaction status");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                response.body().close();
                String status = null;
                String paymentID = null;
                String amount = null;
                String errorMessage = null;
                try {
                    JSONObject responseObject = new JSONObject(responseString);
                    JSONObject payment = responseObject.getJSONObject("payment_request").getJSONArray("payments").getJSONObject(0);
                    status = payment.getString("status");
                    postPaymentStatus = status;
                    paymentID = payment.getString("payment_id");
                    amount = payment.getString("amount");
                    updateTransactionID(status, paymentID, orderID);
                    DatabaseReference frompath = FirebaseDatabase.getInstance().getReference("cart").child(mAuth.getCurrentUser().getUid());
                    String order_id = FirebaseDatabase.getInstance().getReference("orders").child(mAuth.getCurrentUser().getUid()).push().getKey();
                    DatabaseReference topath = FirebaseDatabase.getInstance().getReference("orders").child(mAuth.getCurrentUser().getUid()).child(order_id);
                    try {
                        NotifyChef(mPhone, 2, total, username, orderID);
                        moveFirebaseRecord(frompath, topath);
                        Intent success = new Intent(ctx, CheckoutAcitivity.class);
                        success.putExtra("success", "yes");
                        success.putExtra("trans_id", amount);
                        startActivity(success);
                        finish();


                    } catch (Exception e) {
                        Log.e("Move Data", e.getMessage());
                        ToastOX.error(getApplicationContext(), "Ops! We something bumped up!. Your order is safe with us.", Toast.LENGTH_SHORT);
                    }

                } catch (JSONException e) {
                    Log.e("error", e.getMessage());
                    errorMessage = "Failed to fetch the Transaction status";
                }

                final String finalStatus = status;
                final String finalErrorMessage = errorMessage;
                final String finalPaymentID = paymentID;
                final String finalAmount = amount;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (finalStatus == null) {
                            showToast(finalErrorMessage);
                            return;
                        }

                        if (!finalStatus.equalsIgnoreCase("Credit")) {
                            showToast("Transaction still pending");
                            return;
                        }
                    }
                });
            }
        });

    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void moveFirebaseRecord(final DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        String ordername = mPurpose;
                        if (mPurpose.length() > 30) {
                            ordername = mPurpose.substring(0, 29);
                        }
                        final Map data = new HashMap();
                        data.put("paymentid", postpaymentid);
                        data.put("total", total);
                        data.put("uid", mAuth.getCurrentUser().getUid());
                        if (cod == 1) {
                            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                            Date rightnow = new Date();
                            String date = df.format(rightnow);
                            data.put("date", date);
                            data.put("paymentstatus", "Cash On Delivery");
                            data.put("paymentmethod", "COD");
                        } else {
                            data.put("date", time);
                            data.put("paymentstatus", postPaymentStatus);
                            data.put("paymentmethod", "Online");
                        }
                        if (HAS_IMAGES == 1) {
                            data.put("order_images", transactionID);
                        } else {
                            data.put("order_images", "No Images");
                        }
                        data.put("order_status", "Pending");
                        if (!specialnote.isEmpty()) {
                            data.put("special_note", specialnote);
                        }
                        if (COUPON_APPLIED == 1) {
                            data.put("coupon_code", couponCodeString);
                            data.put("gross_total", realtotal);
                        }
                        data.put("ordername", ordername);
                        data.put("deliveryaddress", dAddress);
                        toPath.updateChildren(data);
                        toPath.updateChildren(data, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                Log.v("Completed", "yes");
                                fromPath.removeValue();
                                if (COUPON_APPLIED == 1) {
                                    final DatabaseReference removeCode = FirebaseDatabase.getInstance().getReference("coupons");
                                    try {
                                        removeCode.child(couponCodeString).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Map values = new HashMap();
                                                long currentvalue;
                                                if (dataSnapshot.hasChild("qty")) {
                                                    if (Integer.valueOf(String.valueOf(dataSnapshot.child("qty").getValue())) > 0) {
                                                        currentvalue = (long) dataSnapshot.child("qty").getValue();
                                                        currentvalue--;
                                                        values.put("qty", currentvalue);
                                                        removeCode.child(couponCodeString).updateChildren(values);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    } catch (DatabaseException e) {
                                        FirebaseCrash.log(e.getMessage());
                                    }

                                }
                            }
                        });

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void deliveryAddressAvailable() {
        DatabaseReference deliveryadd = FirebaseDatabase.getInstance().getReference("deliveryaddress");
        deliveryadd.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())) {
                    Instamojo.initialize(ctx);
                    Instamojo.setBaseUrl("https://api.instamojo.com/");
                    generateTransactionID(mAuth.getCurrentUser().getDisplayName(), mAuth.getCurrentUser().getEmail(), mPhone, mPurpose, total);
                    requestToken();
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    ToastOX.warning(ctx, "Specify Delivery Address First!");
                    Intent addressUpdate = new Intent(ctx, DeliveryActivity.class);
                    addressUpdate.putExtra("from", "cart");
                    startActivity(addressUpdate);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void ValidateCoupon(String Coupon) {
        final String couponCode = Coupon.toUpperCase();
        DatabaseReference couponref = FirebaseDatabase.getInstance().getReference("coupons");
        if (mAuth.getCurrentUser() != null) {
            couponref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                         @Override
                                                         public void onDataChange(DataSnapshot dataSnapshot) {
                                                             if (dataSnapshot.hasChild(couponCode)) {
                                                                 ProgressDialog maDialog = new ProgressDialog(ctx);
                                                                 maDialog.setMessage("Validating Coupon..");
                                                                 maDialog.setCancelable(false);
                                                                 maDialog.show();
                                                                 DataSnapshot child = dataSnapshot.child(couponCode);
                                                                 if (child.hasChildren()) {
                                                                     try {
                                                                         if (Integer.valueOf(String.valueOf(child.child("qty").getValue())) > 0) {
                                                                             if (total > Long.valueOf(String.valueOf(child.child("min").getValue()))) {
                                                                                 SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                                                                 Date date1 = sdf.parse(String.valueOf(child.child("validity").getValue()));
                                                                                 Date date2 = new Date();
                                                                                 if (date1.compareTo(date2) >= 0) {
                                                                                     if (Integer.valueOf(String.valueOf(child.child("type").getValue())) == 1) {
                                                                                         couponType = 1;
                                                                                         long discount = (Long.valueOf(String.valueOf(child.child("amount").getValue())) * realtotal) / 100;
                                                                                         percent = Long.valueOf(String.valueOf(child.child("amount").getValue()));
                                                                                         couponCodeString = couponCode;
                                                                                         total = realtotal - discount;
                                                                                         totalview.setText("\u20B9" + String.valueOf(total));
                                                                                         Toast.makeText(ctx, "Coupon Applied", Toast.LENGTH_SHORT).show();
                                                                                         TextView coupontext = (TextView) findViewById(R.id.coupontext);
                                                                                         coupontext.setText("Coupon Code :" + String.valueOf(couponCode) + " Applied. Real Price :" + String.valueOf(realtotal));
                                                                                         COUPON_APPLIED = 1;
                                                                                     } else if (Integer.valueOf(String.valueOf(child.child("type").getValue())) == 2) {
                                                                                         couponType = 2;
                                                                                         percent = Long.valueOf(String.valueOf(child.child("amount").getValue()));
                                                                                         couponCodeString = couponCode;
                                                                                         total = realtotal - percent;
                                                                                         totalview.setText("\u20B9" + String.valueOf(total));
                                                                                         Toast.makeText(ctx, "Coupon Applied", Toast.LENGTH_SHORT).show();
                                                                                         TextView coupontext = (TextView) findViewById(R.id.coupontext);
                                                                                         coupontext.setText("Coupon Code :" + String.valueOf(couponCode) + " Applied. Real Price :" + String.valueOf(realtotal));
                                                                                         COUPON_APPLIED = 1;
                                                                                     }
                                                                                 } else {
                                                                                     Toast.makeText(ctx, "Expired", Toast.LENGTH_SHORT).show();

                                                                                 }

                                                                             } else {
                                                                                 Toast.makeText(ctx, "Min Purchase of \u20B9" + String.valueOf(child.child("min").getValue()) + " is required", Toast.LENGTH_SHORT).show();

                                                                             }
                                                                         } else {
                                                                             Toast.makeText(ctx, "Coupon Limit Reached", Toast.LENGTH_SHORT).show();
                                                                         }
                                                                     } catch (Exception e) {
                                                                         FirebaseCrash.log(e.getMessage());
                                                                     }
                                                                 }


                                                                 maDialog.dismiss();
                                                             } else {
                                                                 Toast.makeText(ctx, "Invalid Coupon", Toast.LENGTH_SHORT).show();
                                                             }

                                                         }

                                                         @Override
                                                         public void onCancelled(DatabaseError databaseError) {

                                                         }
                                                     }

            );
        } else {
            Toast.makeText(ctx, "Login to Apply Coupon", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rc_view.removeAllViewsInLayout();
        ImageLoader.getInstance().clearMemoryCache();
        myquery.removeEventListener(myQueryListener);
        myquery.removeEventListener(myQueryListener2);
        linear = null;
        dialog = null;
        ctx = null;


    }

    public void cashOnDelivery() {
        cod = 1;

        DatabaseReference deliveryadd = FirebaseDatabase.getInstance().getReference("deliveryaddress");
        deliveryadd.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())) {
                    fetchPhone(mAuth.getCurrentUser().getUid());
                    if (!mPhone.equals(" ")) {
                        generateTransactionID(mAuth.getCurrentUser().getDisplayName(), mAuth.getCurrentUser().getEmail(), mPhone, mPurpose, total);
                        DatabaseReference frompath = FirebaseDatabase.getInstance().getReference("cart").child(mAuth.getCurrentUser().getUid());
                        String order_id = FirebaseDatabase.getInstance().getReference("orders").child(mAuth.getCurrentUser().getUid()).push().getKey();
                        DatabaseReference topath = FirebaseDatabase.getInstance().getReference("orders").child(mAuth.getCurrentUser().getUid()).child(order_id);
                        moveFirebaseRecord(frompath, topath);
                        NotifyChef(mPhone, 1, total, username, order_id);
                        Bundle bundle = new Bundle();
                        bundle.putLong(FirebaseAnalytics.Param.VALUE, total);
                        bundle.putLong(FirebaseAnalytics.Param.TAX, 0);
                        bundle.putString(FirebaseAnalytics.Param.COUPON, String.valueOf(couponCodeString));
                        bundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID, transactionID);
                        bundle.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, bundle);
                        Intent success = new Intent(ctx, CheckoutAcitivity.class);
                        success.putExtra("success", "yes");
                        success.putExtra("trans_id", total);
                        startActivity(success);
                        finish();
                    } else {
                        Toast.makeText(ctx, "Enter Valid Mobile Number for COD", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    ToastOX.warning(ctx, "Specify Delivery Address First!");
                    Intent addressUpdate = new Intent(ctx, DeliveryActivity.class);
                    addressUpdate.putExtra("from", "cart");
                    startActivity(addressUpdate);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void NotifyChef(String Phone, int msg, long total, String username, String order_id) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://partybear.in/api/notify/request.php?mobile=" + Phone + "&msg=" + msg + "&total=" + total + "&name=" + username + "&orderid=" + order_id)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
            }
        });
    }


}



