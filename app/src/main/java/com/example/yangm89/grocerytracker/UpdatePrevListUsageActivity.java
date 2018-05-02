package com.example.yangm89.grocerytracker;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class UpdatePrevListUsageActivity extends AppCompatActivity {
    String listName, store, budget, listDate, username ;
    double remainingQuantity, originalQuantity ;
    ArrayList<TextView> textViewArrayList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_items);

        Intent intent = getIntent() ;
        username = intent.getStringExtra(Constants.keyUsername) ;
        listName = intent.getStringExtra(Constants.keyForPrevListName) ;
        store = intent.getStringExtra(Constants.keyForPrevListStore) ;
        budget = intent.getStringExtra(Constants.keyForPrevListBudget) ;
        listDate = intent.getStringExtra(Constants.keyforPrevListDate) ;

        ((TextView) findViewById(R.id.textView_listName)).setText(listName) ;
        ((TextView) findViewById(R.id.textView_storeName)).setText(store) ;
        ((TextView) findViewById(R.id.textView_date)).setText(listDate);
        ((TextView) findViewById(R.id.textView_Budget)).setText(budget) ;

        final LinearLayout listLayout = findViewById(R.id.linearLayout_items) ;
        final LinearLayout quantityLayout = findViewById(R.id.linearLayout_quantity) ;
        final LinearLayout updateLayout = findViewById(R.id.linearLayout_update) ;
        final LinearLayout remainingLayout = findViewById(R.id.linearLayout_remaining);
        textViewArrayList = new ArrayList<>() ;

        String url = Constants.root_url + "get_list_items.php?username=" + username + "&listname=" + listName ;

        RequestQueue queue = Volley.newRequestQueue(this) ;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //do something here
                        if(response.length() > 0)
                        {
                            for(int i = 0; i < response.length(); i++)
                            {
                                try {
                                    JSONObject object = response.getJSONObject(i) ;
                                    final String itemName = object.getString("ItemName") ;
                                    final String quantity = object.getString("Quantity") ;
                                    //textview for the item name
                                    TextView t = new TextView(UpdatePrevListUsageActivity.this) ;
                                    t.setText(itemName);
                                    originalQuantity = Double.parseDouble(quantity) ;
                                   // Toast.makeText(UpdatePrevListUsageActivity.this, originalQuantity + "", Toast.LENGTH_SHORT).show();
                                    t.setTextSize(20);
                                    t.setPadding(25, 25, 25, 25);
                                    listLayout.addView(t);

                                    //textview for the initial quantity
                                    TextView tQuant = new TextView(UpdatePrevListUsageActivity.this) ;
                                    tQuant.setText(quantity) ;
                                    tQuant.setTextSize(20);
                                    tQuant.setPadding(25, 25, 25, 25);
                                    quantityLayout.addView(tQuant) ;

                                    //textview for the updated quantity
                                    final TextView remainingQuant = new TextView(UpdatePrevListUsageActivity.this) ;
                                    remainingQuant.setTextSize(20);
                                    remainingQuant.setPadding(25, 25, 25, 25);
                                    getInitialCount(username, listName, itemName, remainingQuant);
                                    remainingLayout.addView(remainingQuant) ;

                                    //listener for button
                                    Button b = new Button(UpdatePrevListUsageActivity.this, null, android.R.attr.buttonBarNegativeButtonStyle) ;
                                    b.setHeight(12);
                                    b.setText(R.string.button_usage) ;
                                    updateLayout.addView(b);
                                    b.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //do something here
                                            //  Toast.makeText(UpdatePrevListUsageActivity.this,"Hello!", Toast.LENGTH_SHORT).show() ;
                                            updateUsage(username, listName, itemName, remainingQuant);
                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(UpdatePrevListUsageActivity.this,"No grocery items were added to this list.", Toast.LENGTH_SHORT).show() ;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //do something here
                        Toast.makeText(UpdatePrevListUsageActivity.this, "There was an error connecting to the database.", Toast.LENGTH_SHORT).show();
                    }
                }
        ) ;
        queue.add(jsonArrayRequest) ;
    }

    public void updateUsage(String us, String list, String item, final TextView t)
    {
        String url = Constants.root_url + "update_usage.php?username=" + us + "&listname=" + list +
                "&item=" + item ;
        RequestQueue queue = Volley.newRequestQueue(this) ;
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.equals("The remaining quantity is zero.") && !response.equals("Error.") ) {
                            t.setText(response);
                        }
                        else
                        {
                            Toast.makeText(UpdatePrevListUsageActivity.this, "The remaining quantity is already 0.", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //do something here
                    }
                }
        ) ;

        queue.add(stringRequest) ;
    }


    public void getInitialCount(String user, String list, String item, final TextView t)
    {

        String url = Constants.root_url + "get_usage_count.php?username=" + user + "&listname=" + list +
                "&item=" + item ;
        RequestQueue queue = Volley.newRequestQueue(this) ;
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Toast.makeText(UpdatePrevListUsageActivity.this, response + "", Toast.LENGTH_SHORT).show();
                           t.setText(response) ;
                        }
                        catch(Exception e)
                        {
                            Toast.makeText(UpdatePrevListUsageActivity.this, response , Toast.LENGTH_SHORT).show() ;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //do something here
                    }
                }
        ) ;

        queue.add(stringRequest) ;
    }
}
