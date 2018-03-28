package com.example.yangm89.grocerytracker;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;


public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_home_landscape);
        } else {
            setContentView(R.layout.activity_home_portrait);
        }
        //this leaves the keyboard hidden on load
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
}
