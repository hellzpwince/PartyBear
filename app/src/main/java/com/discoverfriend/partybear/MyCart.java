package com.discoverfriend.partybear;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.discoverfriend.partybear.cart.CartModel;
import com.discoverfriend.partybear.cart.CartViewHolder;
import com.discoverfriend.partybear.checkout.CheckoutAcitivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.instamojo.android.Instamojo;
import com.instamojo.android.activities.PaymentDetailsActivity;
import com.instamojo.android.callbacks.OrderRequestCallBack;
import com.instamojo.android.helpers.Constants;
import com.instamojo.android.models.Errors;
import com.instamojo.android.models.Order;
import com.onurciner.toastox.ToastOX;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
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
    private long total = 0;
    TextView totalview;
    private ProgressDialog dialog;
    Button btn_checkout;
    String mPhone = null;
    String mPurpose = "PartyBear ";
    String accessToken;
    String transactionID = randomString(50);
    private int Transaction_status = 0;
    String time;
    String useremail = "";
    ProgressBar cart_progress;
    NestedScrollView nestedview;
    String username = "";
    String postpaymentid;
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();
    String postPaymentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Instamojo.initialize(this);
        Instamojo.setBaseUrl("https://test.instamojo.com/");
        setContentView(R.layout.activity_my_cart);
        cart_progress = (ProgressBar) findViewById(R.id.cart_progress);
        nestedview = (NestedScrollView) findViewById(R.id.categoryscroll_view);
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        mAuth = FirebaseAuth.getInstance();
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        Instamojo.setLogLevel(Log.ERROR);

        btn_checkout = (Button) findViewById(R.id.btn_payment);
        setupToolbar();
        if (mAuth.getCurrentUser() != null) {
            username = mAuth.getCurrentUser().getDisplayName();
            useremail = mAuth.getCurrentUser().getEmail();
        }
        totalview = (TextView) findViewById(R.id.cart_total);
        totalview.setText("Rs " + String.valueOf(total) + "/-");
        root = null;

        rc_view = (RecyclerView) findViewById(R.id.rc_cart);
        if (mAuth.getCurrentUser() != null) {
            root = FirebaseDatabase.getInstance().getReference("cart").child(mAuth.getCurrentUser().getUid());
            Query myquery = root.orderByKey();
            fetchPhone(mAuth.getCurrentUser().getUid());

            setupAdapter(myquery);
            cart_progress.setVisibility(View.GONE);
            nestedview.setVisibility(View.VISIBLE);
        } else {
            setContentView(R.layout.not_signed_in);

        }
        LinearLayoutManager linear = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
        rc_view.setLayoutManager(linear);
        btn_checkout.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (mAuth.getCurrentUser() != null) {
                                                    dialog.show();
                                                    deliveryAddressAvailable();

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
                    useremail = mAuth.getCurrentUser().getEmail();
                } else {

                }
            }
        }

        ;

    }

    public void setupAdapter(final Query myquery) {
        if (mAuth.getCurrentUser() != null) {

            myquery.addValueEventListener(new ValueEventListener() {
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
                                    mPurpose += model.getName() + " ";
                                    viewHolder.setName(model.getName());
                                    viewHolder.setPrice(model.getTotal());
                                    viewHolder.setProductType(model.getProducttype());
                                    viewHolder.setProductRemove(user_id, post_key);
                                    viewHolder.setImage(MyCart.this, model.getImageurl());

                                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

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
                        setContentView(R.layout.nothing_found);
                        setupToolbar();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            myquery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    total = total + Long.parseLong(dataSnapshot.child("total").getValue().toString());
                    totalview.setText("Rs " + String.valueOf(total) + "/-");
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    total = total - Long.parseLong(dataSnapshot.child("total").getValue().toString());
                    totalview.setText("Rs " + String.valueOf(total) + "/-");
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            Log.e("I am ", "Not Logged In");
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
        Map transaction = new HashMap();
        transaction.put("username", username);
        transaction.put("email", useremail);
        transaction.put("phone", phone);
        transaction.put("purpose", purpose);
        transaction.put("amount", Amount);
        transaction.put("status", "Initiated");
        transaction.put("transactionid", transactionID);

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
        // order.setWebhook("http://partybear.in/api/webhooks/storedata.php");

        //Validate the Order
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
                                    Log.e("order", "Amount is either less than Rs.9 or has more than two decimal places");
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
                        moveFirebaseRecord(frompath, topath);
                        Intent success = new Intent(MyCart.this, CheckoutAcitivity.class);
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
                        Map data = new HashMap();
                        data.put("paymentid", postpaymentid);
                        data.put("date", time);
                        data.put("total", total);
                        data.put("uid", mAuth.getCurrentUser().getUid());
                        data.put("paymentstatus", postPaymentStatus);
                        data.put("order_status", "Pending");
                        data.put("ordername", ordername);
                        toPath.updateChildren(data);
                        toPath.updateChildren(data, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                Log.v("Completed", "yes");
                                fromPath.removeValue();
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
                    generateTransactionID(mAuth.getCurrentUser().getDisplayName(), mAuth.getCurrentUser().getEmail(), mPhone, mPurpose, total);
                    requestToken();
                } else {
                    ToastOX.warning(MyCart.this, "Specify Delivery Address First!");
                    Intent addressUpdate = new Intent(MyCart.this, DeliveryActivity.class);
                    addressUpdate.putExtra("from", "cart");
                    startActivity(addressUpdate);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

