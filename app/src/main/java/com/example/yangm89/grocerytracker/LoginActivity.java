package com.example.yangm89.grocerytracker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_login_landscape);
        } else {
            setContentView(R.layout.activity_login_portrait);
        }
        //this leaves the keyboard hidden on load
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    //start a new activity when login button is clicked
    public void homeActivity(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        String username = ((EditText)findViewById(R.id.edittext_username)).getText().toString();
        String password = ((EditText)findViewById(R.id.edittext_password)).getText().toString();

        //current default email : test@test.com
        //current password: Test1
        if(username.equals("test@test.com")){
            if(password.equals("Test1")){
                intent.putExtra(Constants.keyEmailName, username);
                startActivity(intent);
            }
            else {
                Toast.makeText(this,
                        "Incorrect username and password",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this,
                    "Incorrect username and password",
                    Toast.LENGTH_SHORT).show();
        }
    }

}

