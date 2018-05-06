package com.example.yangm89.grocerytracker;


import android.app.DownloadManager;
import android.app.LauncherActivity;
import android.app.usage.ConfigurationStats;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class NewListActivity extends AppCompatActivity implements
        ListItemFragment.OnFragmentInteractionListener, ListItemSpecificsFragment.OnFragmentInteractionListener {
    private ListItemFragment listItemFragment;
    private ListItemFragment listFragment ;
    private ListItemSpecificsFragment itemSpecificsFragment ;
    private HashMap<String, ItemSpec> itemMap = new HashMap<>() ;
    private String username, itemName, itemPrice, itemQuantity, itemProtein, itemFat, itemCarbs,
            itemOther, listDate, listStore, listBudget, listRemainingBudget, list, budgetCalculation;
    private boolean listItemsVisible ;
    private double quantAndPrice ;
    private Spinner spinner ;
    int selectedSpinner ;
    String calculatedBudget, initialBudget ;
    int firstAdd ;

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

        calculatedBudget = "" ;
        initialBudget = "" ;
        quantAndPrice = 0 ;
        firstAdd = 0;

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

        EditText budgetText = findViewById(R.id.number_budget) ;
        budgetText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                initialBudget = ((EditText)findViewById(R.id.number_budget)).getText().toString() ;
                double iBudget = Double.parseDouble(initialBudget) ;
                double updateBudget = iBudget - quantAndPrice ;
                calculatedBudget = updateBudget + "" ;
                ((TextView) findViewById(R.id.textView_remaining_budget)).setText(updateBudget + "");
            }
        });
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
        outState.putInt(Constants.keyForCounterForAdding, firstAdd);

        if(!listItemsVisible)
        {
            selectedSpinner = spinner.getSelectedItemPosition() ;
            outState.putInt(Constants.keyForItemCategoryNewList, selectedSpinner) ;
            itemName = ((EditText) findViewById(R.id.editText_item)).getText().toString() ;
            itemPrice = ((EditText) findViewById(R.id.number_price)).getText().toString() ;
            itemQuantity = ((EditText) findViewById(R.id.editText_quantity)).getText().toString() ;
            itemProtein = ((EditText) findViewById(R.id.editText_protein)).getText().toString() ;
            itemFat = ((EditText) findViewById(R.id.editText_fat)).getText().toString() ;
            itemCarbs = ((EditText) findViewById(R.id.editText_carbs)).getText().toString() ;
            itemOther = ((EditText) findViewById(R.id.editText_carbs)).getText().toString() ;


            outState.putString(Constants.keyForItemNameNewList, itemName) ;
            outState.putString(Constants.keyforItemPriceNewList, itemPrice);
            outState.putString(Constants.keyForItemQuantity, itemQuantity) ;
            outState.putString(Constants.keyForItemProteinInfo, itemProtein) ;
            outState.putString(Constants.keyForItemFatInfo, itemFat);
            outState.putString(Constants.keyForITemCarbs, itemCarbs);
            outState.putString(Constants.keyForItemOtherInfo, itemOther);
        }

        outState.putString(Constants.keyListDate, listDate);
        outState.putString(Constants.keyForStoreNameInNewList, listStore);
        outState.putString(Constants.keyBudgetListItem, listBudget);
        outState.putString(Constants.keyRemaingingBudgetItem, listRemainingBudget);
        outState.putString(Constants.keyforCalculatedBudget, calculatedBudget);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        listItemsVisible = savedInstanceState.getBoolean(Constants.listItemsVisibility) ;
        itemMap = (HashMap<String,ItemSpec>) (savedInstanceState.getSerializable(Constants.keyHashMapItemMap)) ;
        selectedSpinner = savedInstanceState.getInt(Constants.keyForItemCategoryNewList) ;
        itemName = savedInstanceState.getString(Constants.keyForItemNameNewList) ;
        itemPrice = savedInstanceState.getString(Constants.keyforItemPriceNewList) ;
        itemQuantity = savedInstanceState.getString(Constants.keyForItemQuantity) ;
        itemProtein = savedInstanceState.getString(Constants.keyForItemProteinInfo) ;
        itemFat = savedInstanceState.getString(Constants.keyForItemFatInfo) ;
        itemCarbs = savedInstanceState.getString(Constants.keyForITemCarbs) ;
        itemOther  = savedInstanceState.getString(Constants.keyForItemOtherInfo) ;
        listDate = savedInstanceState.getString(Constants.keyListDate) ;
        listBudget = savedInstanceState.getString(Constants.keyBudgetListItem) ;
        listRemainingBudget = savedInstanceState.getString(Constants.keyRemaingingBudgetItem) ;
        calculatedBudget = savedInstanceState.getString(Constants.keyforCalculatedBudget) ;
        ((TextView) findViewById(R.id.textView_remaining_budget)).setText(calculatedBudget);
        firstAdd = savedInstanceState.getInt(Constants.keyForCounterForAdding) ;

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

    //adds fragment with list to page
    public void cancel(View view){
        //clear all the names
        itemName = "" ;
        itemPrice = "";
        itemQuantity = "";
        itemProtein = "" ;
        itemFat = "";
        itemCarbs = "" ;
        itemOther = "" ;
        selectedSpinner = 0;

        //create code for fragment transaction
        listFragment = new ListItemFragment();
        FragmentTransaction f = getSupportFragmentManager().beginTransaction();
        f.replace(R.id.fragment_list_item_container, listFragment);
        f.commit();

        listItemsVisible = true ;
    }

    public String getItemName()
    {
        return itemName ;
    }

    public String getItemPrice ()
    {
        return itemPrice ;
    }

    public String getItemQuantity()
    {
        return itemQuantity ;
    }

    public String getItemProtein()
    {
        return itemProtein ;
    }

    public String getItemCarbs() {
        return itemCarbs;
    }

    public String getItemFat() {
        return itemFat;
    }

    public String getItemOther() {
        return itemOther;
    }

    public int getSelectedSpinner()
    {
        return selectedSpinner ;
    }

    //home button
    public void backToHomeActivity(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(Constants.keyUsername, username) ;
        startActivity(intent);
        finish();
    }

    //linked to new list specs fragment and takes back to new list activity with list item info
    public void backToNewListActivity(View view){

        //do something here
        String item = ((EditText) findViewById(R.id.editText_item)).getText().toString() ;
        itemName = "" ;
        String price = ((EditText) findViewById(R.id.number_price)).getText().toString() ;
        itemPrice = "" ;
        String quantity = ((EditText) findViewById(R.id.editText_quantity)).getText().toString() ;
        itemQuantity = "" ;
        String category = ((Spinner) findViewById(R.id.spinner_category)).getSelectedItem().toString() ;
        selectedSpinner = 0 ;
        String protein = ((EditText) findViewById(R.id.editText_protein)).getText().toString() ;
        itemProtein = "" ;
        String fat = ((EditText) findViewById(R.id.editText_fat)).getText().toString() ;
        itemFat = "" ;
        String carbs = ((EditText) findViewById(R.id.editText_carbs)).getText().toString() ;
        itemCarbs = "" ;
        String other = ((EditText) findViewById(R.id.editText_other)).getText().toString() ;
        itemOther = "" ;
        if(item.equals(""))
        {
            Toast.makeText(this, "Item name is required.", Toast.LENGTH_SHORT).show();
        }
        else if(price.equals(""))
        {
            Toast.makeText(this, "Item price is required.", Toast.LENGTH_SHORT).show();
        }
        else if(quantity.equals(""))
        {
            Toast.makeText(this, "Item quantity is required.", Toast.LENGTH_SHORT).show();
        }
        else {
            ItemSpec i = new ItemSpec(item, price, quantity, category, protein, fat, carbs, other);
            calculatePriceXQuantity(price, quantity);
            addItemToList(item, i);
            listFragment = new ListItemFragment();
            FragmentTransaction f = getSupportFragmentManager().beginTransaction();
            f.replace(R.id.fragment_list_item_container, listFragment);
            f.commit();

            listItemsVisible = true;
        }


    }

    public void calculatePriceXQuantity(String itemPrice, String itemQuantity)
    {
        double quant = Double.parseDouble(itemQuantity)  ;
        double costOfItem = Double.parseDouble(itemPrice) ;
        quantAndPrice = quant * costOfItem ;
    }

    public void addItemToList(String item, ItemSpec itemSpec){
        if(itemMap.containsKey(item))
        {
            Toast.makeText(this, "Item names must be unique.", Toast.LENGTH_SHORT).show() ;
        }
        else {
            calculatePriceXQuantity(itemSpec.getPrice(), itemSpec.getQuantity());
            if(initialBudget.equals(""))
            {
                double updatedBudget = 0 - quantAndPrice ;
                calculatedBudget = updatedBudget + "" ;
                ((TextView) findViewById(R.id.textView_remaining_budget)).setText(updatedBudget + "");
                firstAdd++ ;
            }
            else if(firstAdd == 0)
            {
                double iBudget = Double.parseDouble(initialBudget) ;
                double updateBudget = iBudget - quantAndPrice ;
                calculatedBudget = updateBudget + "" ;
                ((TextView) findViewById(R.id.textView_remaining_budget)).setText(updateBudget + "");
                firstAdd++ ;
            }
            else
            {
                double updatedBudget = Double.parseDouble(calculatedBudget) - quantAndPrice ;
                calculatedBudget = updatedBudget + "" ;
                ((TextView) findViewById(R.id.textView_remaining_budget)).setText(updatedBudget + "");
            }
            itemMap.put(item, itemSpec);
        }
    }

    public void saveList(View view)
    {
        MyDBContract.MyDbHelper mdbh = new MyDBContract.MyDbHelper(getApplicationContext());
        final SQLiteDatabase db = mdbh.getWritableDatabase();
        SQLiteDatabase rdb = mdbh.getReadableDatabase();
        final ContentValues values = new ContentValues();

        final String listName = ((EditText) findViewById(R.id.editText_list_name)).getText().toString().trim() ;
        list = listName ;
        String storeName = ((EditText) findViewById(R.id.editText_store_info)).getText().toString() ;
        listStore = storeName ;
        String date = ((EditText) findViewById(R.id.number_date)).getText().toString() ;
        listDate = date ;
        String budget = ((EditText) findViewById(R.id.number_budget)).getText().toString() ;
        listBudget = budget ;
        String sqlDate ;
        RequestQueue queue = Volley.newRequestQueue(this) ;

        if(!listName.equals("") && !storeName.equals("") && !date.equals("") && !budget.equals(""))
        {
            //check local database if listname exists
            String selection = MyDBContract.DBEntry.COLUMN_NAME_LISTS + " GLOB ? " ;
            String[] selectionArgs = { list } ;
            String[] projection = { MyDBContract.DBEntry.COLUMN_NAME_LISTS, MyDBContract.DBEntry.COLUMN_NAME_USER_ID } ;
            String sortOrder = MyDBContract.DBEntry.COLUMN_NAME_LISTS + " DESC" ;
            Cursor cursor = rdb.query(MyDBContract.DBEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder) ;

            if( cursor.getCount() == 0 ) {
                if (checkDateFormat(date)) {
                    String[] dateArr = date.split("/");
                    sqlDate = dateArr[2] + "-" + dateArr[0] + "-" + dateArr[1];
                    String url = Constants.root_url + "save_list.php?username=" + username + "&listname=" + listName
                            + "&store=" + storeName + "&date=" + sqlDate + "&budget=" + budget;
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.equals("Successfully inserted into database.")) {
                                        // Toast.makeText(NewListActivity.this, response, Toast.LENGTH_SHORT).show() ;
                                        //put list into local database
                                        values.put(MyDBContract.DBEntry.COLUMN_NAME_LISTS, list) ;
                                        values.put(MyDBContract.DBEntry.COLUMN_NAME_USER_ID, username) ;
                                        //create a row in the database
                                        long rowId = db.insert(MyDBContract.DBEntry.TABLE_NAME, null, values) ;
                                        //save list to remote database
                                        new SaveListItemsToDB().execute();
                                        Intent intent = new Intent(NewListActivity.this, HomeActivity.class);
                                        intent.putExtra(Constants.keyUsername, username);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(NewListActivity.this, "Error inserting the list into the databse.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //do something
                            Toast.makeText(NewListActivity.this, "There was an error connecting to the database.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    queue.add(stringRequest);
                } else {
                    Toast.makeText(this, "Dates need to be in the format : MM/DD/YYYY", Toast.LENGTH_SHORT).show();
                    ;
                }
            }
            else {
                Toast.makeText(this, "The list name " + listName + " already exists. List names must be unique.", Toast.LENGTH_SHORT).show()  ;
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
            else if(budget.equals(""))
            {
                Toast.makeText(this, "Budget is required.", Toast.LENGTH_SHORT).show() ;
            }
            else if(date.equals(""))
            {
                Toast.makeText(this, "Date0 is required.", Toast.LENGTH_SHORT).show() ;
            }
        }



    }

    public void setSpinner(Spinner s)
    {
        spinner = s;
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
            String date = ((EditText) findViewById(R.id.number_date)).getText().toString() ;

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
                            + "&itemname=" + item + "&price=" + price + "&quantity=" + quantity + "&protein=" + protein
                            + "&carbs=" + carbs + "&other=" + other + "&fat=" + fat + "&category=" + category + "&date=" + date;

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
