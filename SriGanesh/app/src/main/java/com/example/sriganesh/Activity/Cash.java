package com.example.sriganesh.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.example.sriganesh.Adapter.CashAdapter;
import com.example.sriganesh.MainActivity;
import com.example.sriganesh.Model.BankModel;
import com.example.sriganesh.Model.CashModel;
import com.example.sriganesh.Model.PaymentModel;
import com.example.sriganesh.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Cash extends AppCompatActivity {

    private RecyclerView recyclerViewCash;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager manager;
    private List<CashModel> models;
    private ProgressBar progressBar;

    private EditText CASH_DATE;
    private Button CASH_PROCESS;
    private FloatingActionButton backPress;
    private TextView appbarTitle, CASH_TOTAL;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);

        progressBar = findViewById(R.id.ProgressBar1);
        recyclerViewCash = findViewById(R.id.cash_recycler);
        manager = new GridLayoutManager(Cash.this, 1);
        recyclerViewCash.setLayoutManager(manager);
        models = new ArrayList<>();

        CASH_DATE = findViewById(R.id.cash_date);
        CASH_PROCESS = findViewById(R.id.cash_process);
        CASH_TOTAL = findViewById(R.id.cash_total);
        backPress = findViewById(R.id.fab_backPress);
        appbarTitle = findViewById(R.id.txt_appbarTitle);

        CASH_DATE.setText(getCurrentDate());
        calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                getDate();
            }
        };


        CASH_DATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Cash.this,date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


        CASH_PROCESS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cash_amount();
                Intent intent =new Intent(Cash.this,Chart_Activity2.class);
                startActivity(intent);
            }
        });

        appbarTitle.setText("Cash");
        //back press activity
        backPress.setOnClickListener(view -> Cash.super.onBackPressed());
    }

    private void getDate() {
        String DateFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat, Locale.US);
        CASH_DATE.setText(dateFormat.format(calendar.getTime()));
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }



    private void cash_amount() {
        CASH_DATE = findViewById(R.id.cash_date);
        String date = CASH_DATE.getText().toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.225.112/php/ganesh/cash.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    double item_total = 0;

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String Cash_Name = jsonObject.getString("Amount");
                        double Cash_Amount = jsonObject.getDouble("Amount");

                        CashModel cashModel = new CashModel(Cash_Name,Cash_Amount);
                        models.add(cashModel);

                        item_total += Cash_Amount;
                        CASH_TOTAL.setText("₹"+ item_total);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                mAdapter = new CashAdapter(Cash.this, models);
                recyclerViewCash.setAdapter(mAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Cash.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                params.put("today_data",date);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
