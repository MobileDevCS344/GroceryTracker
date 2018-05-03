package com.example.yangm89.grocerytracker;

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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListStatistics extends AppCompatActivity{
    String username ;
    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_stats_landscape);
        } else {
            setContentView(R.layout.activity_stats_portrait);
        }
        //this leaves the keyboard hidden on load
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Intent intent = getIntent();
        username = intent.getStringExtra(Constants.keyUsername) ;


        String url = Constants.root_url + "get_user_prev_lists.php?username=" + username;
        final LinearLayout layout = findViewById(R.id.linearLayout_prevList) ;

    }

    //home button
    public void home(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(Constants.keyUsername, username) ;
        startActivity(intent);
        finish();
    }
}
