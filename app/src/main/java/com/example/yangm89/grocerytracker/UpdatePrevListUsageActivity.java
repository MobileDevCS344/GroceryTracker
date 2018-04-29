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

public class UpdatePrevListUsageActivity extends AppCompatActivity {
    String listName, store, budget, listDate, username ;
    double remainingQuantity, originalQuantity ;

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
                                    TextView t = new TextView(UpdatePrevListUsageActivity.this) ;
                                    t.setText(itemName);
                                    originalQuantity = Double.parseDouble(quantity) ;
                                    t.setTextSize(18);
                                    listLayout.addView(t);
                                    TextView tQuant = new TextView(UpdatePrevListUsageActivity.this) ;
                                    tQuant.setText(quantity) ;
                                    tQuant.setTextSize(18);
                                    quantityLayout.addView(tQuant) ;
                                    Button b = new Button(UpdatePrevListUsageActivity.this) ;
                                    b.setText(R.string.button_usage) ;
                                    updateLayout.addView(b);
                                    b.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //do something here
                                            //Toast.makeText(UpdatePrevListUsageActivity.this,"Hello!", Toast.LENGTH_SHORT).show() ;

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

    public void updateUsage(String us, String list, String item)
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
                        try {
                            double usage = Double.parseDouble(response) ;
                            calculateUsage(originalQuantity, usage);
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

    public void calculateUsage(double originalQuantity, double usage)
    {
        //calculate remaining quantity
        double quantRemaining = originalQuantity - usage ;
        TextView tRemain = new TextView(UpdatePrevListUsageActivity.this) ;
        tRemain.setText(quantRemaining+"") ;
        tRemain.setTextSize(18);
        //update layout with the usage
    }
}
