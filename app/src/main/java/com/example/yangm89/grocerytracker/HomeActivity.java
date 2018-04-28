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




        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = Constants.root_url + "get_user_prev_lists.php?username=" + username;
            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                    Request.Method.GET, url, null, new
                    Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //access the jsonobject
                            try {
                                iterateThroughJSObject(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //do something here
                }
            }
            ) ;

            queue.add(jsonObjectRequest) ;
        }
    }



    public void optionsActivity(View view){
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }

    public void viewPrevListsActivity(View view){
        Intent intent = new Intent(this, PreviousListsActivity.class);
        startActivity(intent);
    }

    public void newListActivity(View view){
        Intent intent = new Intent(this, NewListActivity.class);
        startActivity(intent);
    }

    public void viewStatsActivity(View view){
        Intent intent = new Intent(this, MoreStatsActivity.class);
        startActivity(intent);
    }

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


}
