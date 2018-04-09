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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    public final Pattern textPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

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
        String username = ((EditText)findViewById(R.id.editText_email)).getText().toString();
        String password = ((EditText)findViewById(R.id.editText_password)).getText().toString();
        String verifyPassword = ((EditText) findViewById(R.id.editText_retype_password)).getText().toString();

        //currently accepting any email to sign up
        if(validateEmail(username)){
            if(password.equals(verifyPassword)){
                if(password.length() >= 6){
                    if(isTextValid(password)){
                        intent.putExtra(Constants.keyEmailName, username);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(this,
                                "Password must contain a capital letter, lowercase letter, and a number",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this,
                            "Password must contain at least 6 characters",
                            Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "Passwords must match", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this,
                    "Email is ivalid.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isTextValid(String textToCheck) {
        return textPattern.matcher(textToCheck).matches();
    }

    public boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

}
