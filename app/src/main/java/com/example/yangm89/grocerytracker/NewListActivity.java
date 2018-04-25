package com.example.yangm89.grocerytracker;


import android.app.LauncherActivity;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NewListActivity extends AppCompatActivity implements
        ListItemFragment.OnFragmentInteractionListener, ListItemSpecificsFragment.OnFragmentInteractionListener {
    private String itemName;
    private ListItemFragment listItemFragment;
    private ListItemFragment listFragment ;
    private ListItemSpecificsFragment itemSpecificsFragment ;

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
        listFragment = new ListItemFragment();
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_list_item_container, listFragment);
        fragmentTransaction.commit();

        //instantiate items arraylist
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    public void listItemActivity(View view){
        //create code for fragment transaction
        itemSpecificsFragment = new ListItemSpecificsFragment() ;
        FragmentTransaction f = getSupportFragmentManager().beginTransaction() ;
        f.replace(R.id.fragment_list_item_container, itemSpecificsFragment) ;
        f.commit() ;
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

    public void backToNewListActivity(View view){
        String item = ((EditText)findViewById(R.id.editText_item)).getText().toString();

        if(item.equals("")){
            Toast.makeText(this, "An item name is required.", Toast.LENGTH_SHORT).show();
        }
        else {
            addItemToList(item);

            listFragment = new ListItemFragment();
            FragmentTransaction f = getSupportFragmentManager().beginTransaction();
            f.replace(R.id.fragment_list_item_container, listFragment);
            f.commit();
        }
    }

    public void addItemToList(String item){
        UniversalTables.items.add(item);
    }


}
