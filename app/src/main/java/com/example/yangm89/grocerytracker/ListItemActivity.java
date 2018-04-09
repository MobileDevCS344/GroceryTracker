package com.example.yangm89.grocerytracker;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.lang.reflect.Array;

public class ListItemActivity extends AppCompatActivity {
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_list_items_landscape);
        } else {
            setContentView(R.layout.activity_list_items_portrait);
        }
        //this leaves the keyboard hidden on load
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        spinner = (Spinner) findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array_category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void backToNewListActivity(View view){
        Intent intent = new Intent(this, NewListActivity.class);
        String category = ((Spinner) findViewById(R.id.spinner_category)).getSelectedItem().toString();
        String itemName = ((EditText) findViewById(R.id.edit_text_item)).getText().toString();
        intent.putExtra(Constants.keyCategoryName, category);
        intent.putExtra(Constants.keyItemNewListName, itemName);
        intent.putExtra(Constants.keyListItemActivity,  "PreviousActivity=ListItemActivity");
        startActivity(intent);
        finish();
    }

    public void backToHomeActivity(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


}
