package com.example.sriganesh.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sriganesh.Adapter.RecieptAdapter;
import com.example.sriganesh.Adapter.SalesAdapter;
import com.example.sriganesh.Model.RecieptModel;
import com.example.sriganesh.Model.SalesModel;
import com.example.sriganesh.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Sales extends AppCompatActivity {

    private RecyclerView recyclerViewSales;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager manager;
    private List<SalesModel> models;

    private ProgressBar progressBar;
    private EditText SALES_FROM_DATE,SALES_TO_DATE;
    private Button SALES_PROCESS;
    private FloatingActionButton backPress;
    private TextView appbarTitle,SALES_TOTAL;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        progressBar = findViewById(R.id.ProgressBar1);
        recyclerViewSales = findViewById(R.id.sales_recycler);
        manager = new GridLayoutManager(Sales.this,1);
        recyclerViewSales.setLayoutManager(manager);
        models = new ArrayList<>();

        SALES_FROM_DATE = findViewById(R.id.sales_from_date);
        SALES_TO_DATE = findViewById(R.id.sales_to_date);
        SALES_PROCESS = findViewById(R.id.sales_process);
        SALES_TOTAL = findViewById(R.id.sales_total);
        backPress = findViewById(R.id.fab_backPress);
        appbarTitle = findViewById(R.id.txt_appbarTitle);

        calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                getFromDate();
            }
        };
        DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                getToDate();
            }
        };

        SALES_FROM_DATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Sales.this,date,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        SALES_TO_DATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Sales.this,date1,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        SALES_PROCESS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        appbarTitle.setText("Sales");
        //back press activity
        backPress.setOnClickListener(view -> Sales.super.onBackPressed());
    }

    private void getFromDate() {
        String DateFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat, Locale.US);
        SALES_FROM_DATE.setText(dateFormat.format(calendar.getTime()));
    }
    private void getToDate() {
        String DateFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat, Locale.US);
        SALES_TO_DATE.setText(dateFormat.format(calendar.getTime()));
    }

    private void fetchDetails() {
        String from_date = SALES_FROM_DATE.getText().toString().trim();
        String to_date = SALES_TO_DATE.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/admin/getcartproducts.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    double item_total = 0;

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String Sales_Type = jsonObject.getString("id");
                        String Sales_Name = jsonObject.getString("eng_name");
                        double Sales_Amount = jsonObject.getDouble("amount");

                        SalesModel salesModel = new SalesModel(Sales_Type,Sales_Name,Sales_Amount);
                        models.add(salesModel);

                        item_total += Sales_Amount;
                        SALES_TOTAL.setText("₹"+ item_total);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                mAdapter = new SalesAdapter(Sales.this, models);
                recyclerViewSales.setAdapter(mAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Sales.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id",from_date);
                params.put("user_id",to_date);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

}
