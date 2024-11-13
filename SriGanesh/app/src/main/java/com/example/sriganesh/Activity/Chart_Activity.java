package com.example.sriganesh.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sriganesh.Adapter.BankAdapter;
import com.example.sriganesh.Model.BankModel;
import com.example.sriganesh.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chart_Activity extends AppCompatActivity {
    BarChart barChart;
    private List<BankModel> models;
    TextView textView;
    private List<BankModel> bankModel = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        models = new ArrayList<>();
        textView = (TextView) findViewById(R.id.text);
        barChart = (BarChart) findViewById(R.id.barchart);
        fetchDetails();

       /*ArrayList<BarEntry> information = new ArrayList<>();
        information.add(new BarEntry(2010,1000));
        information.add(new BarEntry(2011,1200));
        information.add(new BarEntry(2012,1400));
        information.add(new BarEntry(2013,1700));
        information.add(new BarEntry(2014,1300));
        information.add(new BarEntry(2015,1100));
        information.add(new BarEntry(2016,1000));
        information.add(new BarEntry(2017,1700));
        information.add(new BarEntry(2018,1900));
        information.add(new BarEntry(2019,2200));

        BarDataSet dataSet = new BarDataSet(information,"Report");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(10f);

        BarData barData = new BarData(dataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Bar Report");
        barChart.animateY(20);*/

    }
    private void fetchDetails() {

        StringRequest request = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/admin/getcartproducts.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String Bank_Type = jsonObject.getString("id");
                        String Bank_Name = jsonObject.getString("eng_name");
                        double Bank_Amount = jsonObject.getDouble("amount");

                        BankModel bankModel = new BankModel(Bank_Type,Bank_Name,Bank_Amount);
                        models.add(bankModel);

                        textView.setText(Bank_Name);

                        ArrayList<BarEntry> information = new ArrayList<>();
                        information.add(new BarEntry(2010,100));
                        information.add(new BarEntry(2011,1200));
                        information.add(new BarEntry(2012,1400));
                        information.add(new BarEntry(2013,1700));
                        information.add(new BarEntry(2014,1300));
                        information.add(new BarEntry(2015,1100));
                        information.add(new BarEntry(2016,1000));
                        information.add(new BarEntry(2017,1700));
                        information.add(new BarEntry(2018,1900));
                        information.add(new BarEntry(2019,2200));

                        BarDataSet dataSet = new BarDataSet(information,"Report");
                        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        dataSet.setValueTextColor(Color.BLACK);
                        dataSet.setValueTextSize(10f);

                        BarData barData = new BarData(dataSet);

                        barChart.setFitBars(true);
                        barChart.setData(barData);
                        barChart.getDescription().setText("Bar Report");
                        barChart.animateY(20);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Chart_Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}