package com.example.yangm89.grocerytracker;


import android.app.DownloadManager;
import android.app.LauncherActivity;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class NewListActivity extends AppCompatActivity implements
        ListItemFragment.OnFragmentInteractionListener, ListItemSpecificsFragment.OnFragmentInteractionListener {
    private String itemName;
    private ListItemFragment listItemFragment;
    private ListItemFragment listFragment ;
    private ListItemSpecificsFragment itemSpecificsFragment ;
    private HashMap<String, ItemSpec> itemMap = new HashMap<>() ;
    private String username ;
    private boolean listItemsVisible ;

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

        Intent intent = getIntent();
        username = intent.getStringExtra(Constants.keyUsername) ;

        if(savedInstanceState == null) {
            //adds item fragment
            listFragment = new ListItemFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_list_item_container, listFragment);
            fragmentTransaction.commit();

            listItemsVisible = true;
        }
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.listItemsVisibility, listItemsVisible);
        outState.putSerializable(Constants.keyHashMapItemMap, itemMap);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        listItemsVisible = savedInstanceState.getBoolean(Constants.listItemsVisibility) ;
        itemMap = (HashMap<String,ItemSpec>) (savedInstanceState.getSerializable(Constants.keyHashMapItemMap)) ;

        if(listItemsVisible)
        {
            listFragment = new ListItemFragment();
            FragmentTransaction f = getSupportFragmentManager().beginTransaction();
            f.replace(R.id.fragment_list_item_container, listFragment);
            f.commit();
        }
        else
        {
            itemSpecificsFragment = new ListItemSpecificsFragment() ;
            FragmentTransaction f = getSupportFragmentManager().beginTransaction() ;
            f.replace(R.id.fragment_list_item_container, itemSpecificsFragment) ;
            f.commit() ;
        }
    }

    //adds fragment with item specs to activity
    public void listItemActivity(View view){
        //create code for fragment transaction
        itemSpecificsFragment = new ListItemSpecificsFragment() ;
        FragmentTransaction f = getSupportFragmentManager().beginTransaction() ;
        f.replace(R.id.fragment_list_item_container, itemSpecificsFragment) ;
        f.commit() ;

        listItemsVisible = false ;
    }

    //home button
    public void backToHomeActivity(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(Constants.keyUsername, username) ;
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

    //linked to new list specs fragment and takes back to new list activity with list item info
    public void backToNewListActivity(View view){

        //do something here
        String item = ((EditText) findViewById(R.id.editText_item)).getText().toString() ;
        String price = ((EditText) findViewById(R.id.number_price)).getText().toString() ;
        String quantity = ((EditText) findViewById(R.id.editText_quantity)).getText().toString() ;
        String category = ((Spinner) findViewById(R.id.spinner_category)).getSelectedItem().toString() ;
        String protein = ((EditText) findViewById(R.id.editText_protein)).getText().toString() ;
        String fat = ((EditText) findViewById(R.id.editText_fat)).getText().toString() ;
        String carbs = ((EditText) findViewById(R.id.editText_carbs)).getText().toString() ;
        String other = ((EditText) findViewById(R.id.editText_other)).getText().toString() ;
        ItemSpec i = new ItemSpec(item, price, quantity, category, protein, fat, carbs, other ) ;
        addItemToList(item, i);

        listFragment = new ListItemFragment();
        FragmentTransaction f = getSupportFragmentManager().beginTransaction();
        f.replace(R.id.fragment_list_item_container, listFragment);
        f.commit();

        listItemsVisible = true ;
    }

    public void addItemToList(String item, ItemSpec itemSpec){
        if(itemMap.containsKey(item))
        {
            Toast.makeText(this, "Item names must be unique.", Toast.LENGTH_SHORT).show() ;
        }
        else {
            itemMap.put(item, itemSpec);
        }
    }

    public void saveList(View view)
    {
        String listName = ((EditText) findViewById(R.id.editText_list_name)).getText().toString() ;
        String storeName = ((EditText) findViewById(R.id.editText_store_info)).getText().toString() ;
        String date = ((EditText) findViewById(R.id.number_date)).getText().toString() ;
        String budget = ((EditText) findViewById(R.id.number_budget)).getText().toString() ;
        String sqlDate ;
        RequestQueue queue = Volley.newRequestQueue(this) ;

        if(!listName.equals("") && !storeName.equals("") && !date.equals(""))
        {
            if(checkDateFormat(date))
            {
                String[] dateArr = date.split("/") ;
                sqlDate = dateArr[2] + "-" + dateArr[0] +"-" + dateArr[1] ;
                String url = Constants.root_url + "save_list.php?username=" + username + "&listname=" + listName
                        + "&store=" + storeName + "&date=" + sqlDate + "&budget=" + budget;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("Successfully inserted into database."))
                                {
                                    Toast.makeText(NewListActivity.this, response, Toast.LENGTH_SHORT).show() ;
                                    new SaveListItemsToDB().execute();
                                    Intent intent = new Intent(NewListActivity.this, HomeActivity.class) ;
                                    intent.putExtra(Constants.keyUsername, username) ;
                                    startActivity(intent) ;
                                    finish() ;
                                }
                                else
                                {
                                    Toast.makeText(NewListActivity.this, "Error inserting the list into the databse.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //do something
                        Toast.makeText(NewListActivity.this, "There was an error connecting to the database.", Toast.LENGTH_SHORT).show() ;
                    }
                }) ;

                queue.add(stringRequest) ;
            }
            else {
                Toast.makeText(this, "Dates need to be in the format : MM/DD/YYYY",Toast.LENGTH_SHORT).show(); ;
            }
        }
        else
        {
            if(listName.equals(""))
            {
                Toast.makeText(this, "All lists must have a unique name.", Toast.LENGTH_SHORT).show() ;
            }
            else if(storeName.equals(""))
            {
                Toast.makeText(this, "Store name is required.", Toast.LENGTH_SHORT).show();
            }
        }



    }

    public boolean checkDateFormat(String date)
    {
        if (date.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")) {
            return true;
        }
        else {
            return false;
        }
    }

    public HashMap<String, ItemSpec> getItemMap()
    {
        return itemMap ;
    }

    public void printListItems()
    {
        LinearLayout layout = ((LinearLayout) findViewById(R.id.linear_layout_items_container)) ;
        for(String key : itemMap.keySet())
        {
            ItemSpec i = itemMap.get(key) ;
            TextView t = new TextView(this) ;
            t.setText(i.getName()) ;
            layout.addView(t) ;
        }
    }


    //run background service to add the items to the database
    private class SaveListItemsToDB extends AsyncTask<HashMap<String, ItemSpec>, Void, Integer>
    {
        @Override
        protected Integer doInBackground(HashMap... hashMaps) {
            String listName = ((EditText) findViewById(R.id.editText_list_name)).getText().toString() ;
            String store = ((EditText) findViewById(R.id.editText_store_info)).getText().toString() ;

            if(!listName.equals("") && !store.equals(""))
            {
                for(String key : itemMap.keySet())
                {
                    Log.d("KEY" , key) ;
                    ItemSpec itemSpec = itemMap.get(key) ;
                    String item = itemSpec.getName() ;
                    String protein = itemSpec.getProtein() ;
                    String fat = itemSpec.getFat() ;
                    String carbs = itemSpec.getCarbs() ;
                    String other = itemSpec.getOther() ;
                    String price = itemSpec.getPrice() ;
                    String quantity = itemSpec.getQuantity() ;
                    String category = itemSpec.getCategory() ;

                    RequestQueue queue = Volley.newRequestQueue(NewListActivity.this) ;
                    String url = Constants.root_url + "save_individual_items.php?username=" + username + "&listname=" + listName
                            + "&itemname=" + itemName + "&price=" + price + "&quantity=" + quantity + "&protein=" + protein
                            + "&carbs=" + carbs + "&other=" + other + "&fat=" + fat + "&category=" + category ;

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(!response.equals("Successfully inserted into database."))
                            {
                                Toast.makeText(NewListActivity.this, response, Toast.LENGTH_SHORT).show() ;
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) ;

                    queue.add(request) ;
                }
            }
            //change this
            return 1 ;
        }


        protected  void onPostExecute(Void result)
        {
            Toast.makeText(NewListActivity.this, "Successfully added all items to database.", Toast.LENGTH_SHORT).show() ;
        }
    }

}
