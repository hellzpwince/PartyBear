package com.discoverfriend.partybear;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity {
    Toolbar toolbar;
    EditText subject, mobile, feedback;
    Button feedbackbtn;
    DatabaseReference myref;
    FirebaseAuth mAuth;
    int updated = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        myref = FirebaseDatabase.getInstance().getReference("Feedback");
        mAuth = FirebaseAuth.getInstance();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarSetup();
        subject = (EditText) findViewById(R.id.subject_editview);
        mobile = (EditText) findViewById(R.id.mobile_text);
        feedback = (EditText) findViewById(R.id.feedback_msg);
        feedbackbtn = (Button) findViewById(R.id.btn_feedback);
        feedbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LovelyStandardDialog(Feedback.this)
                        .setTopColorRes(R.color.colorPrimary)
                        .setButtonsColorRes(R.color.colorDarkBlack)
                        .setIcon(R.drawable.ic_message_white_24dp)
                        .setTitle("Confirm Feedback")
                        .setMessage(feedback.getText().toString())
                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (updated == 0) {
                                    saveData(subject.getText().toString(), mobile.getText().toString(), feedback.getText().toString());
                                    updated = 1;
                                }
                                startActivity(new Intent(Feedback.this, MainActivity.class));
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();

            }
        });

    }

    public void toolbarSetup() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    public void saveData(String subject, String mobile, String feedback) {
        Map data = new HashMap();
        data.put("subject", subject);
        data.put("mobile", mobile);
        data.put("feedback", feedback);
        try {
            String feedbackid = myref.child(mAuth.getCurrentUser().getUid()).push().getKey();
            myref.child(mAuth.getCurrentUser().getUid()).child(feedbackid).updateChildren(data);
        } catch (Exception e) {
            Log.e("FeedbackError", mAuth.getCurrentUser().getUid() + " " + e.getMessage());
        }
    }
}
