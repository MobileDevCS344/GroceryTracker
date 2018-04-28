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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

    //check username and password and login
    public void login(View view) {
        final String username = ((EditText)findViewById(R.id.edittext_username)).getText().toString();
        final String password = ((EditText)findViewById(R.id.edittext_password)).getText().toString();

        if(!username.equals("") && !password.equals("")) {
            RequestQueue queue = Volley.newRequestQueue(this);

            String url = Constants.root_url + "login_query.php?username=" + username + "&password=" + password;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("The username does not exist.")) {
                                Toast.makeText(LoginActivity.this, "The username " + username + " does not exist. " +
                                        "Please register first.", Toast.LENGTH_SHORT).show();
                            } else if (response.equals("Successfully logged in.")) {
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                intent.putExtra(Constants.keyUsername, username);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(LoginActivity.this, "There was an error connecting to the database. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
            );

            queue.add(stringRequest);
        }
        else {
            Toast.makeText(this, "Username and password are required.", Toast.LENGTH_SHORT).show() ;
        }
    }

}

