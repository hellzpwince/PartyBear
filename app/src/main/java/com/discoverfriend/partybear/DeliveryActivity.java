package com.discoverfriend.partybear;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onurciner.toastox.ToastOX;

import java.util.HashMap;

public class DeliveryActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText name;
    EditText address1;
    EditText address2;
    EditText area;
    EditText pincode;
    EditText email;
    EditText mobile;
    FirebaseAuth mAuth;
    DatabaseReference deliveryref;
    Button btn_delivery;
    String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        mAuth = FirebaseAuth.getInstance();
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            if (!bundle.isEmpty()) {
                if (bundle.containsKey("from")) {
                    from = bundle.get("from").toString();
                }
            }
        }

        name = (EditText) findViewById(R.id.delivery_name);
        email = (EditText) findViewById(R.id.delivery_email);
        mobile = (EditText) findViewById(R.id.delivery_mobile);
        address1 = (EditText) findViewById(R.id.delivery_address1);
        address2 = (EditText) findViewById(R.id.delivery_address2);
        area = (EditText) findViewById(R.id.delivery_address3);
        pincode = (EditText) findViewById(R.id.delivery_pincode);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btn_delivery = (Button) findViewById(R.id.btn_delivery);
        if (mAuth.getCurrentUser() == null) {
            ToastOX.error(getApplicationContext(), "Login to set Delivery Address", Toast.LENGTH_SHORT);
            getParentActivityIntent();
            finish();
        } else {

            deliveryref = FirebaseDatabase.getInstance().getReference("deliveryaddress");
            deliveryref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())) {
                            for (DataSnapshot child : dataSnapshot.child(mAuth.getCurrentUser().getUid()).getChildren()) {
                                Log.e("Location", "" + child);

                                if (child.getKey().equals("name")) {
                                    name.setText((String) child.getValue());
                                }
                                if (child.getKey().equals("email")) {
                                    email.setText((String) child.getValue());
                                }
                                if (child.getKey().equals("mobile")) {
                                    mobile.setText("" + child.getValue());
                                }
                                if (child.getKey().equals("address1")) {
                                    address1.setText((String) child.getValue());
                                }
                                if (child.getKey().equals("address2")) {
                                    address2.setText((String) child.getValue());
                                }
                                if (child.getKey().equals("address3")) {
                                    area.setText((String) child.getValue());
                                }
                                if (child.getKey().equals("pincode")) {
                                    pincode.setText("" + child.getValue());
                                }
                            }
                        }
                    } catch (Exception e) {
                        ToastOX.error(getApplicationContext(), "Opps! An Error Occurred. Please Try Again");
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btn_delivery.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                HashMap address = new HashMap();

                                                if (!TextUtils.isEmpty(name.getText().toString())) {
                                                    if (!TextUtils.isEmpty(email.getText().toString())) {
                                                        if (!TextUtils.isEmpty(mobile.getText().toString())) {
                                                            if (!TextUtils.isEmpty(address1.getText().toString())) {
                                                                if (!TextUtils.isEmpty(address2.getText().toString())) {
                                                                    if (!TextUtils.isEmpty(area.getText().toString())) {
                                                                        if (!TextUtils.isEmpty(pincode.getText().toString())) {
                                                                            try {
                                                                                address.put("name", name.getText().toString());
                                                                                address.put("mobile", mobile.getText().toString());
                                                                                address.put("email", email.getText().toString());
                                                                                address.put("address1", address1.getText().toString());
                                                                                address.put("address2", address2.getText().toString());
                                                                                address.put("address3", area.getText().toString());
                                                                                address.put("pincode", pincode.getText().toString());
                                                                                deliveryref = FirebaseDatabase.getInstance().getReference("deliveryaddress").child(mAuth.getCurrentUser().getUid());
                                                                                deliveryref.updateChildren(address);
                                                                                if (!from.isEmpty()) {
                                                                                    if (from.equals("cart")) {
                                                                                        ToastOX.info(DeliveryActivity.this, "Delivery Address Updated");
                                                                                        startActivity(new Intent(DeliveryActivity.this, MyCart.class));
                                                                                        finish();
                                                                                    }
                                                                                } else {
                                                                                    ToastOX.info(getApplicationContext(), "Delivery Address Updated");
                                                                                    getParentActivityIntent();
                                                                                    finish();
                                                                                }
                                                                            } catch (
                                                                                    Exception e
                                                                                    )

                                                                            {
                                                                                Log.e("Delivery Log", e.getMessage());
                                                                                Snackbar.make(getCurrentFocus(), "Failed to Update Address.Please Try Again", Snackbar.LENGTH_SHORT).show();
                                                                            }
                                                                        } else {
                                                                            pincode.setError("Required");
                                                                        }
                                                                    } else {
                                                                        area.setError("Required");
                                                                    }
                                                                } else {
                                                                    address2.setError("Required");
                                                                }
                                                            } else {
                                                                address1.setError("Required");
                                                            }
                                                        } else {
                                                            mobile.setError("Required");
                                                        }
                                                    } else {
                                                        email.setError("Required");
                                                    }
                                                } else {
                                                    name.setError("Required");
                                                    Snackbar.make(getCurrentFocus(), "All Fields are Mandatory Details", Snackbar.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

        );
    }
}
