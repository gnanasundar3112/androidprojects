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
import com.example.sriganesh.Adapter.BankAdapter;
import com.example.sriganesh.Adapter.OutstandingAdapter;
import com.example.sriganesh.Model.BankModel;
import com.example.sriganesh.Model.OutstandingModel;
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

public class Outstanding extends AppCompatActivity {

    private RecyclerView recyclerViewOutstanding;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager manager;
    private List<OutstandingModel> models;

    private ProgressBar progressBar;

    private EditText OUTSTANDING_FROM_DATE,OUTSTANDING_TO_DATE;
    private Button OUTSTANDING_PROCESS;
    private FloatingActionButton backPress;
    private TextView appbarTitle,OUTSTANDING_TOTAL;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstanding);

        progressBar = findViewById(R.id.ProgressBar1);
        recyclerViewOutstanding = findViewById(R.id.outstanding_recycler);
        manager = new GridLayoutManager(Outstanding.this,1);
        recyclerViewOutstanding.setLayoutManager(manager);
        models = new ArrayList<>();

        OUTSTANDING_FROM_DATE = findViewById(R.id.outstanding_from_date);
        OUTSTANDING_TO_DATE = findViewById(R.id.outstanding_to_date);
        OUTSTANDING_PROCESS = findViewById(R.id.outstanding_process);
        OUTSTANDING_TOTAL = findViewById(R.id.outstanding_total);
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

        OUTSTANDING_FROM_DATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Outstanding.this,date,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        OUTSTANDING_TO_DATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Outstanding.this,date1,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        OUTSTANDING_PROCESS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        appbarTitle.setText("Outstanding");
        //back press activity
        backPress.setOnClickListener(view -> Outstanding.super.onBackPressed());
    }

    private void getFromDate() {
        String DateFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat, Locale.US);
        OUTSTANDING_FROM_DATE.setText(dateFormat.format(calendar.getTime()));
    }
    private void getToDate() {
        String DateFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat, Locale.US);
        OUTSTANDING_TO_DATE.setText(dateFormat.format(calendar.getTime()));
    }

    private void fetchDetails() {
        String from_date = OUTSTANDING_FROM_DATE.getText().toString().trim();
        String to_date = OUTSTANDING_TO_DATE.getText().toString().trim();

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

                        String Outstanding_Type = jsonObject.getString("id");
                        String Outstanding_Name = jsonObject.getString("eng_name");
                        double Outstanding_Amount = jsonObject.getDouble("amount");

                        OutstandingModel outstandingModel = new OutstandingModel(Outstanding_Type,Outstanding_Name,Outstanding_Amount);
                        models.add(outstandingModel);

                        item_total += Outstanding_Amount;
                        OUTSTANDING_TOTAL.setText("₹"+ item_total);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                mAdapter = new OutstandingAdapter(Outstanding.this, models);
                recyclerViewOutstanding.setAdapter(mAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Outstanding.this, error.toString(), Toast.LENGTH_SHORT).show();
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
