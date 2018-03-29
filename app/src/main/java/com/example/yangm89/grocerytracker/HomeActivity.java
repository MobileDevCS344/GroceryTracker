package com.example.yangm89.grocerytracker;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

    public void optionsActivity(View view){
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }

    public void viewPrevListsActivity(View view){
        Intent intent = new Intent(this, PreviousListsActivity.class);
        startActivity(intent);
    }

    public void newListActivity(View view){
        Intent intent = new Intent(this, NewListActivity.class);
        startActivity(intent);
    }

    public void viewStatsActivity(View view){
        Intent intent = new Intent(this, MoreStatsActivity.class);
        startActivity(intent);
    }
}
