package com.example.yangm89.grocerytracker;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_signup_landscape);
        } else {
            setContentView(R.layout.activity_signup_portrait);
        }
        //this leaves the keyboard hidden on load
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    //if the create account button is clicked start a new activity
    public void createAccountActivity(View view){
        Intent intent = new Intent(this, CreateAccountActivity.class);
        //need to add code to send info about account to database
        startActivity(intent);
    }
}
