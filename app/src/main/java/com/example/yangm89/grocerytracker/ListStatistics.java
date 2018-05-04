package com.example.yangm89.grocerytracker;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.androidplot.util.PixelUtils;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ListStatistics extends AppCompatActivity{
    String username, listname, listdate, store, budget ;
    private HashMap<String ,Integer> countArr;
    private ArrayList<String> dateArr ;
    private ArrayList<Date> dates ;
    private ArrayList<String> itemsArrayList ;
    private int beef , chicken, bread, dairy , fish, fruits , lamb , pork ,
            ready , sauces , snacks , veal , vegetable , otherMeat , other  ;
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
        listname = intent.getStringExtra(Constants.keyForStatsListName) ;
        listdate = intent.getStringExtra(Constants.keyForStatsListDate) ;
        store = intent.getStringExtra(Constants.keyForStatsListStore) ;
        budget = intent.getStringExtra(Constants.keyForStatsListBudget) ;
        String textForTextView = listname + ": " + store + " $" + budget + "\n " + listdate  ;
        ((TextView) (findViewById(R.id.textview_list_name))).setText(textForTextView);
        dateArr = new ArrayList<>( ) ;
        countArr = new HashMap<>();
        dates = new ArrayList<>() ;

        itemsArrayList = new ArrayList<>() ;
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

        generateListCategories();

    }

    public void generateListCategories()
    {

        String url = Constants.root_url + "category_stats.php?username=" + username + "&listname="
                + listname ;
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
                                    String itemName = list.getString("ItemName") ;
                                    itemsArrayList.add(itemName) ;
                                    //  Toast.makeText(ListStatistics.this, " item : " + itemName + " " , Toast.LENGTH_SHORT).show();
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

                            generateGraph() ;
                            new GetLineGraph().execute();

                        } else {
                            Toast.makeText(ListStatistics.this, "No data is available.", Toast.LENGTH_SHORT).show();
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
        PieChart pie = findViewById(R.id.graphView_pie_graph);
        pie.getLegend().setVisible(false);
        pie.setTitle("Categories of Most Recent List") ;
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

    //home button
    public void home(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(Constants.keyUsername, username) ;
        startActivity(intent);
        finish();
    }

    public void generateLineGraph()
    {
        //Toast.makeText(ListStatistics.this, "In generateLineGraph", Toast.LENGTH_SHORT).show();
        //Toast.makeText(ListStatistics.this, "items arraylist :" + itemsArrayList.size(), Toast.LENGTH_SHORT).show();
        generateDateCount();
        for(int j = 0; j < itemsArrayList.size() ; j++)
        {
//            Toast.makeText(this, "in generateLineGraph " + itemsArrayList.get(j), Toast.LENGTH_SHORT).show() ;
            requestForLineGraphData(itemsArrayList.get(j));
        }

    }

    public void requestForLineGraphData(final String itemName)
    {
      //  Toast.makeText(this, "In requestForLineGraph", Toast.LENGTH_SHORT).show();
        String url = Constants.root_url + "list_item_usage.php?username=" + username +
                "&listname=" + listname + "&item=" + itemName;
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
                                    String date = list.getString("UsageDate");
                                    dateArr.add(date);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            generateLinePoints(itemName);
                        } else {
                            Toast.makeText(ListStatistics.this, "No previous lists are available.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ListStatistics.this, "There was an error connecting to the database.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        queue.add(jsonArrayRequest);
    }

    public void generateLinePoints(String item)
    {

        Toast.makeText(this, "In generateLinePoints", Toast.LENGTH_SHORT).show();
       // generateDateCount();
        Toast.makeText(this, "dates " + dates.size(), Toast.LENGTH_SHORT).show();
        GraphView lineGraph = findViewById(R.id.graphView_line_graph) ;
        DataPoint[] dp = new DataPoint[countArr.size()] ;
        for(int i  = 0; i < countArr.size(); i++)
        {
            Toast.makeText(this, dates.get(i) + "", Toast.LENGTH_SHORT).show();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd") ;
            String fmtDate = fmt.format(dates.get(i)) ;
            dp[i] = new DataPoint(dates.get(i), countArr.get(fmtDate)) ;

        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp) ;
        lineGraph.addSeries(series);
        series.setTitle(item);
        lineGraph.setTitle("Line Graph of Usage");
        lineGraph.setTitleColor(Color.BLACK);
        lineGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        lineGraph.getGridLabelRenderer().setNumHorizontalLabels(countArr.size() + 1);
        lineGraph.getLegendRenderer().setVisible(true);

    }

    public Date generateDate(String date)
    {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d = sdf.parse(date) ;
            return d;
        }
        catch (ParseException e)
        {

        }

        return null ;

    }

    public void generateDateCount()
    {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd") ;
        for(int i = 0; i < dateArr.size(); i++)
        {
            Date d1 = generateDate(dateArr.get(i)) ;
            String format = fmt.format(d1) ;
            if(countArr.containsKey(format))
            {
                int c = countArr.get(format) ;
                c++ ;
                countArr.put(format, c) ;
            }
            else
            {
                countArr.put(format, 1) ;
                dates.add(d1) ;
            }
        }
    }

    //run background service to add the items to the database
    private class GetLineGraph extends AsyncTask<HashMap<String, ItemSpec>, Void, Integer>
    {
        @Override
        protected Integer doInBackground(HashMap... hashMaps) {
            generateLineGraph();
            //change this
            return 1 ;
        }


        protected  void onPostExecute(Void result)
        {

        }
    }

}
