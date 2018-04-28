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
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    public final Pattern textPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
    //public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
        //    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

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

    public void register(View view){
        final String username = ((EditText) findViewById(R.id.editText_username)).getText().toString() ;
        final String password = ((EditText) findViewById(R.id.editText_password)).getText().toString() ;
        final String confirmPassword = ((EditText) findViewById(R.id.editText_retype_password)).getText().toString() ;
        final String fullName = ((EditText) findViewById(R.id.editText_full_name)).getText().toString() ;


        if(!username.equals("") && !password.equals("") && !confirmPassword.equals("") && !fullName.equals("")) {
            if(username.length() >= 4)
            {
                if(password.length() >= 6)
                {
                    if(isTextValid(password))
                    {
                        if(password.equals(confirmPassword)) {
                            RequestQueue queue = Volley.newRequestQueue(this);

                            String url = Constants.root_url + "register_query.php?username=" + username + "&password=" + password + "&fullname=" + fullName ;
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if (response.equals("Username already exists.")) {
                                                Toast.makeText(RegisterActivity.this, "The username " + username + " already exists. Please register with a new username.", Toast.LENGTH_SHORT).show();
                                            } else if (response.equals("Successfully registered and signed in.")) {
                                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                                intent.putExtra(Constants.keyUsername, username);
                                                startActivity(intent);
                                                finish();
                                            } else if (response.equals("Error registering and signing in.")) {
                                                Toast.makeText(RegisterActivity.this, "Please check your username and password.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(RegisterActivity.this, "There was an error connecting to the database. Please try again later.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            );

                            queue.add(stringRequest);
                        }
                        else {
                            Toast.makeText(this, "Passwords must match.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(this, "Password must contains a capital letter," +
                                " lowercase letter, and a number.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this, "Password must be at least 6 characters in length.", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "Username must be at least 4 characters in length.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if(username.equals(""))
            {
                Toast.makeText(this, "Username is required.", Toast.LENGTH_SHORT).show();
            }

            else if(password.equals(""))
            {
                Toast.makeText(this, "Password is required.", Toast.LENGTH_SHORT).show();
            }

            else if(confirmPassword.equals(""))
            {
                Toast.makeText(this, "Retyped Password is required. Retyped password and password must match.", Toast.LENGTH_SHORT).show();
            }

            else if(fullName.equals("")) {
                Toast.makeText(this, "Full name is required.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public boolean isTextValid(String textToCheck) {
        return textPattern.matcher(textToCheck).matches();
    }


}
