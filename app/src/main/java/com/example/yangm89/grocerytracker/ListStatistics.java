package com.example.yangm89.grocerytracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class ListStatistics extends AppCompatActivity{
    String username ;
    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stats_main);

        //this leaves the keyboard hidden on load
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Intent intent = getIntent();
        username = intent.getStringExtra(Constants.keyUsername);
    }
}
