package com.example.yangm89.grocerytracker;


import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Spinner;

import java.util.ArrayList;

public class NewListActivity extends AppCompatActivity {
    private ArrayList<String> items;
    private String itemName;
    private ListItemFragment listItemFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_new_list_landscape);
        } else {
            setContentView(R.layout.activity_new_list_portrait);
        }
        //this leaves the keyboard hidden on load
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        items = new ArrayList();


        //adds item fragment

    }

    @Override
    public void onStart(){
        super.onStart();
    }

    public void listItemActivity(View view){
        Intent intent = new Intent(this, ListItemActivity.class);
        startActivity(intent);
    }
}
