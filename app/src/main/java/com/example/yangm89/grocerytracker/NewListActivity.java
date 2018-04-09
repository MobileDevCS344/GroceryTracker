package com.example.yangm89.grocerytracker;


import android.provider.ContactsContract;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NewListActivity extends AppCompatActivity implements
        ListItemFragment.OnFragmentInteractionListener {
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

        //adds item fragment
        ListItemFragment listFragment = new ListItemFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_list_item_container, listFragment);
        fragmentTransaction.commit();

        //check if previous activity is ListItemActivity
        Intent intent = getIntent();
        String previousActivity = intent.getStringExtra(Constants.keyListItemActivity);

        if(previousActivity != null){
            if(!previousActivity.equals("")){
                String itemName = intent.getStringExtra(Constants.keyItemNewListName);
                UniversalTables.items.add(itemName);
                updateItemList();

            }
        }


    }

    @Override
    public void onStart(){
        super.onStart();
    }

    public void listItemActivity(View view){
        Intent intent = new Intent(this, ListItemActivity.class);
        startActivity(intent);

    }

    public void backToHomeActivity(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public String updateItemList() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < UniversalTables.items.size(); i++){
            sb.append(UniversalTables.items.get(i) + "\n");
        }

        return sb.toString();
    }
}
