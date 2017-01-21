package com.discoverfriend.partybear;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class CategoryActivity extends AppCompatActivity {
    /*initialize objects here*/
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Assign views here*/
        setContentView(R.layout.activity_category);
        String category = getIntent().getExtras().getString("category");
        /*Assigning Toolbar*/
        toolbar = (Toolbar) findViewById(R.id.category_app_bar);
        toolbar.setTitle(category);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*wrtie code here */


    }
}
