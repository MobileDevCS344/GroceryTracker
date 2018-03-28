package com.example.yangm89.grocerytracker;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_create_account_landscape);
        } else {
            setContentView(R.layout.activity_create_account_portrait);
        }
        //this leaves the keyboard hidden on load
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    //start a new activity when login button is pressed
    public void goToLoginActivity(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
