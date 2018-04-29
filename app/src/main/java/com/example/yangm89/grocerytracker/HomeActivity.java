package com.example.yangm89.grocerytracker;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {
    private String username ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_home_landscape);
        } else {
            setContentView(R.layout.activity_home_portrait);
        }
        //this leaves the keyboard hidden on load
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Intent intent = getIntent();
        username = intent.getStringExtra(Constants.keyUsername) ;

        final LinearLayout layout = ((LinearLayout) findViewById(R.id.linearLayout_prev_list_container));

        if(getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            String url = Constants.root_url + "get_user_prev_lists.php?username=" + username;
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //do something
                            if (response.length() > 0) {
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        JSONObject list = response.getJSONObject(i);
                                        final String listName = list.getString("ListName");
                                        final String store = list.getString("Store");
                                        final String budget = list.getString("Budget");
                                        final String date = list.getString("Date");
                                        TextView t = new TextView(HomeActivity.this);
                                        t.setText(listName + ": " + store);
                                        t.setTextSize(18);
                                        layout.addView(t);
                                        t.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                //call some method to go to the list
                                                // Toast.makeText(PreviousListsActivity.this, "Hello!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(HomeActivity.this, UpdatePrevListUsageActivity.class);
                                                intent.putExtra(Constants.keyUsername, username);
                                                intent.putExtra(Constants.keyForPrevListName, listName);
                                                intent.putExtra(Constants.keyForPrevListStore, store);
                                                intent.putExtra(Constants.keyForPrevListBudget, budget);
                                                intent.putExtra(Constants.keyforPrevListDate, date);
                                                startActivity(intent);
                                            }
                                        });

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                Toast.makeText(HomeActivity.this, "No previous lists are available.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(HomeActivity.this, "There was an error connecting to the database.", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            queue.add(jsonArrayRequest);
        }

    }



    public void optionsActivity(View view){
        Intent intent = new Intent(this, OptionsActivity.class);
        intent.putExtra(Constants.keyUsername, username) ;
        startActivity(intent);
    }

    public void viewPrevListsActivity(View view){
        Intent intent = new Intent(this, PreviousListsActivity.class);
        intent.putExtra(Constants.keyUsername, username) ;
        startActivity(intent);
    }

    public void newListActivity(View view){
        Intent intent = new Intent(this, NewListActivity.class);
        intent.putExtra(Constants.keyUsername, username) ;
        startActivity(intent);
    }

    public void viewStatsActivity(View view){
        Intent intent = new Intent(this, MoreStatsActivity.class);
        intent.putExtra(Constants.keyUsername, username) ;
        startActivity(intent);
    }
/*
    public void iterateThroughJSObject(JSONArray jsonArray) throws JSONException {
        LinearLayout layout = ((LinearLayout) findViewById(R.id.linearLayout_prev_list_container));

        if(jsonArray.length() > 0)
        {
            for(int i = 0; i < jsonArray.length() ; i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i) ;
                String list = jsonObject.getString("ListName") ;
                TextView textView  = new TextView(this) ;
                textView.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
                textView.setText(list);
                layout.addView(textView) ;
            }
        }
    }
    */


}
