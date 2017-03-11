package com.discoverfriend.partybear.checkout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.discoverfriend.partybear.MainActivity;
import com.discoverfriend.partybear.R;

public class CheckoutAcitivity extends AppCompatActivity {
    TextView textidview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_acitivity);
        Bundle bundle = getIntent().getExtras();
        String id = bundle.get("trans_id").toString();
        String success = bundle.get("success").toString();
        textidview = (TextView) findViewById(R.id.transID);
        if (id != null) {
            textidview.setText("Rs " + id.toString());
        }
        Button btn = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CheckoutAcitivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(CheckoutAcitivity.this, MainActivity.class));
        finish();
    }
}
