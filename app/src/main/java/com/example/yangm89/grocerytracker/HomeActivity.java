package com.example.yangm89.grocerytracker;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.androidplot.util.PixelUtils;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {
    private String username;
    private int beef , chicken, bread, dairy , fish, fruits , lamb , pork ,
            ready , sauces , snacks , veal , vegetable , otherMeat , other  ;
    public PieChart pie ;
    private ArrayList<Double> budget;
    private ArrayList<String> dateArr ;

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
        beef = 0;
        chicken = 0;
        bread = 0;
        dairy = 0;
        fish = 0;
        fruits = 0;
        lamb = 0;
        pork = 0;
        ready = 0;
        sauces = 0;
        snacks = 0;
        veal = 0;
        vegetable = 0;
        otherMeat = 0;
        other = 0 ;
        budget = new ArrayList<>() ;
        dateArr = new ArrayList<>() ;

        final LinearLayout layout = findViewById(R.id.linearLayout_prev_list_container);

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

    @Override
    public void onStart()
    {
        super.onStart();
        generateMostRecentListPieGraph(username) ;
        generateBudgetlineGraph(username);
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

    public void generateMostRecentListPieGraph(String username)
    {

        String url = Constants.root_url + "get_pie_chart_data.php?username=" + username;
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
                                    String category = list.getString("Category").trim() ;
                                  //  Toast.makeText(HomeActivity.this, " i : " + i + " " , Toast.LENGTH_SHORT).show();
                                  //  Toast.makeText(HomeActivity.this, " category : " + category , Toast.LENGTH_SHORT).show();
                                  //  mostRecentListName = list.getString("ListName") ;
                                    if(category.equals("Beef"))
                                    {
                                        beef++ ;
                                    }
                                    if(category.equals("Chicken"))
                                    {
                                        chicken++ ;
                                    }
                                    if(category.equals("Bread"))
                                    {
                                        bread++ ;
                                    }
                                    if(category.equals("Dairy"))
                                    {
                                        dairy++ ;
                                    }
                                    if(category.equals("Fish"))
                                    {
                                        fish++ ;
                                    }
                                    if(category.equals("Fruits"))
                                    {
                                        fruits++ ;
                                    }
                                    if(category.equals("Lamb"))
                                    {
                                        lamb++ ;
                                    }
                                    if(category.equals("Pork"))
                                    {
                                        pork++ ;
                                    }
                                    if(category.equals("Ready"))
                                    {
                                        ready++ ;
                                    }
                                    if(category.equals("Sauces"))
                                    {
                                        sauces++;
                                    }
                                    if(category.equals("Snacks"))
                                    {
                                        snacks++ ;
                                    }
                                    if(category.equals("Veal"))
                                    {
                                        veal++ ;
                                    }
                                    if(category.equals("Vegetables"))
                                    {
                                        vegetable++ ;
                                    }
                                    if(category.equals("Meat-Other"))
                                    {
                                        otherMeat++ ;
                                    }
                                    if(category.equals("Other"))
                                    {
                                        other++ ;
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            generateGraph();
                        } else {
                            Toast.makeText(HomeActivity.this, "No previous lists are available.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(HomeActivity.this, error + "", Toast.LENGTH_SHORT).show();

                    }
                }
        );

        queue.add(jsonArrayRequest);

    }

    public void generateGraph()
    {
      //  Toast.makeText(this, "In generate graph", Toast.LENGTH_SHORT).show();
     //   Toast.makeText(this, "beef: " + beef, Toast.LENGTH_SHORT).show();
      //  Toast.makeText(this, "fruits: " + fruits, Toast.LENGTH_SHORT).show();
        pie = findViewById(R.id.graphView_pie_graph);
        pie.getLegend().setVisible(false);
     //   Toast.makeText(this, "list name" + mostRecentListName, Toast.LENGTH_SHORT).show();
       // pie.setTitle(mostRecentListName);
        final float padding = PixelUtils.dpToPix(30) ;
        pie.getPie().setPadding(padding, padding, padding, padding);
        EmbossMaskFilter emf = new EmbossMaskFilter(
                new float[] {1, 1, 1} , 0.4f, 10, 8.2f
        ) ;

        if(beef > 0 ){
            Segment beefSegment = new Segment("Beef", beef) ;
            SegmentFormatter beefFormat = new SegmentFormatter(R.color.beefColor) ;
            beefFormat.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
            beefFormat.getFillPaint().setMaskFilter(emf);
            pie.addSegment(beefSegment, beefFormat);
        }

        if(chicken > 0)
        {
            Segment chickenSegment = new Segment("Chicken", chicken) ;
            SegmentFormatter chickenFormat = new SegmentFormatter(R.color.chickenColor) ;
            chickenFormat.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
            chickenFormat.getFillPaint().setMaskFilter(emf);
            pie.addSegment(chickenSegment, chickenFormat);

        }

        if(bread > 0)
        {
            Segment breadSegment = new Segment("Bread & Bakery", bread) ;
            SegmentFormatter breadFormat = new SegmentFormatter(R.color.breadColor) ;
            breadFormat.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
            breadFormat.getFillPaint().setMaskFilter(emf);
            pie.addSegment(breadSegment, breadFormat);
        }

        if(dairy > 0)
        {
            Segment dairySegment = new Segment("Dairy & Eggs", dairy) ;
            SegmentFormatter dairyFormat = new SegmentFormatter(R.color.dairyColor) ;
            dairyFormat.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
            dairyFormat.getFillPaint().setMaskFilter(emf);
            pie.addSegment(dairySegment, dairyFormat);

        }

        if(fish > 0)
        {
            Segment fishSegment = new Segment("Fish & Shellfish", fish) ;
            SegmentFormatter fishFormat = new SegmentFormatter(R.color.fishColor) ;
            fishFormat.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
            fishFormat.getFillPaint().setMaskFilter(emf);
            pie.addSegment(fishSegment, fishFormat);
        }

        if(fruits > 0 )
        {
            Segment fruitsSegment = new Segment("Fruits & Nuts", fruits) ;
            SegmentFormatter fruitFormat = new SegmentFormatter(R.color.fruitsColor) ;
            fruitFormat.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
            fruitFormat.getFillPaint().setMaskFilter(emf);
            pie.addSegment(fruitsSegment, fruitFormat);

        }

        if(lamb > 0)
        {
            Segment lambSegment = new Segment("Lamb", lamb) ;
            SegmentFormatter lambFormat = new SegmentFormatter(R.color.lambColor) ;
            lambFormat.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
            lambFormat.getFillPaint().setMaskFilter(emf);
            pie.addSegment(lambSegment, lambFormat);
        }

        if(pork > 0)
        {
            Segment porkSegment = new Segment("Pork", pork) ;
            SegmentFormatter porkFormat = new SegmentFormatter(R.color.porkColor) ;
            porkFormat.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
            porkFormat.getFillPaint().setMaskFilter(emf);
            pie.addSegment(porkSegment, porkFormat);

        }

        if(ready > 0)
        {
            Segment readySegment = new Segment ("Ready Meals", ready) ;
            SegmentFormatter readyFormat = new SegmentFormatter(R.color.readyColor) ;
            readyFormat.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
            readyFormat.getFillPaint().setMaskFilter(emf);
            pie.addSegment(readySegment, readyFormat);
        }

        if(sauces > 0 )
        {
            Segment sauceSegment = new Segment("Sauces & Soup", sauces) ;
            SegmentFormatter sauceFormat = new SegmentFormatter(R.color.saucesColor) ;
            sauceFormat.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
            sauceFormat.getFillPaint().setMaskFilter(emf);
            pie.addSegment(sauceSegment, sauceFormat);
        }

        if(snacks > 0)
        {
            Segment snackSegment = new Segment("Snacks, Candy, & Dessert", snacks) ;
            SegmentFormatter snackFormat = new SegmentFormatter(R.color.snacksColor) ;
            snackFormat.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
            snackFormat.getFillPaint().setMaskFilter(emf);
            pie.addSegment(snackSegment, snackFormat);

        }

        if(veal  > 0)
        {
            Segment vealSegment = new Segment("Veal", veal) ;
            SegmentFormatter vealFormat = new SegmentFormatter(R.color.vealColor) ;
            vealFormat.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
            vealFormat.getFillPaint().setMaskFilter(emf);
            pie.addSegment(vealSegment, vealFormat);

        }

        if(vegetable > 0)
        {
            Segment vegeSegment = new Segment("Vegetables", vegetable) ;
            SegmentFormatter vegeFormat = new SegmentFormatter(R.color.vegColor) ;
            vegeFormat.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
            vegeFormat.getFillPaint().setMaskFilter(emf);
            pie.addSegment(vegeSegment, vegeFormat);

        }

        if(otherMeat > 0)
        {
            Segment meatOtherSegment = new Segment("Meat-Other", otherMeat) ;
            SegmentFormatter meatOtherFormat = new SegmentFormatter(R.color.meatOtherColor) ;
            meatOtherFormat.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
            meatOtherFormat.getFillPaint().setMaskFilter(emf);
            pie.addSegment(meatOtherSegment, meatOtherFormat);

        }

        if(other > 0)
        {
            Segment otherSegment = new Segment("Other", other);
            SegmentFormatter otherFormat = new SegmentFormatter(R.color.otherColor) ;
            otherFormat.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
            otherFormat.getFillPaint().setMaskFilter(emf);
            pie.addSegment(otherSegment, otherFormat);
        }

        pie.getBorderPaint().setColor(Color.TRANSPARENT);
        pie.getBackgroundPaint().setColor(Color.TRANSPARENT);

        if(beef > 0 || chicken > 0  ||  bread > 0 || dairy > 0 || fish > 0 || fruits > 0 ||
                lamb > 0 || pork > 0 || ready > 0 || sauces > 0 || snacks > 0 || veal > 0 ||
                vegetable > 0 || otherMeat > 0 || other > 0){
            PieRenderer pieRenderer = pie.getRenderer(PieRenderer.class) ;
            pieRenderer.setDonutSize((float) 0.5, PieRenderer.DonutMode.PERCENT);
            pie.redraw();
        }
        else
        {
            Toast.makeText(this, "You have not created any lists yet.", Toast.LENGTH_LONG).show();
        }
    }

    public void generateBudgetlineGraph(String username)
    {

        String url = Constants.root_url + "get_line_graph_data.php?username=" + username;
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
                                    String strBudget = list.getString("Budget") ;
                                    String date = list.getString("Date") ;
                                    double b = Double.parseDouble(strBudget) ;
                                    budget.add(b) ;
                                    dateArr.add(date) ;


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            generateLinePoints();
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

    public void generateLinePoints()
    {
        GraphView lineGraph = findViewById(R.id.graphView_line_graph) ;
        DataPoint[] dp = new DataPoint[budget.size()] ;
        for(int i  = 0; i < budget.size(); i++)
        {
            String[] datePieces = dateArr.get(i).split("-") ;
            Date d = generateDate(Integer.parseInt(datePieces[1]), Integer.parseInt(datePieces[2]), Integer.parseInt(datePieces[0])) ;
            DataPoint dataPoint = new DataPoint(d, budget.get(i)) ;
            dp[i] = dataPoint ;
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp) ;
        series.setTitle("Budget") ;
        lineGraph.addSeries(series);
    }

    public Date generateDate(int month, int day, int year)
    {
        int m ;
        switch(month)
        {
            case (1) : m = Calendar.JANUARY  ;
                        break ;
            case (2) : m = Calendar.FEBRUARY ;
                        break ;
            case (3) : m = Calendar.MARCH ;
                        break ;
            case (4) : m = Calendar.APRIL ;
                        break ;
            case (5) : m = Calendar.MAY ;
                        break ;
            case (6) : m = Calendar.JUNE ;
                        break ;
            case (7) : m = Calendar.JULY ;
                        break ;
            case (8) : m = Calendar.AUGUST ;
                        break ;
            case (9) : m = Calendar.SEPTEMBER ;
                        break ;
            case (10) : m = Calendar.OCTOBER ;
                        break ;
            case (11) : m = Calendar.NOVEMBER ;
                        break ;
            case (12) : m = Calendar.DECEMBER ;
                        break ;
            default : m = 0;
                        break ;
        }
        Date d = new GregorianCalendar(year, m, day).getTime() ;
        return d ;
    }
}
