package com.example.adminpanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.adminpanel.Adapter.ReportsAdapter;
import com.example.adminpanel.Adapter.UserAdapter;
import com.example.adminpanel.Model.ReportsModel;
import com.example.adminpanel.Model.UserModel;
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

public class Report extends AppCompatActivity {

    private FloatingActionButton BACK;
    private Spinner STATUS;
    private TextView APPBAR_TITLE,TOTAL_AMOUNT;
    private EditText FROM_DATE,TO_DATE;
    Calendar calendar;

    private RecyclerView RECYCLERVIEW_REPORTS;
    private RecyclerView.LayoutManager REPORTS_MANAGER;
    private List<ReportsModel> reportsModel;
    private ReportsAdapter reportsAdapter;
    private Button PROCESS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        RECYCLERVIEW_REPORTS = findViewById(R.id.report_recycler);
        REPORTS_MANAGER = new GridLayoutManager(Report.this, 1);
        RECYCLERVIEW_REPORTS.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        reportsModel = new ArrayList<>();

        BACK = findViewById(R.id.fab_backPress);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);
        STATUS = findViewById(R.id.delivery_status);
        FROM_DATE = findViewById(R.id.from_date);
        TO_DATE = findViewById(R.id.to_date);
        PROCESS = findViewById(R.id.report_process);
        TOTAL_AMOUNT = findViewById(R.id.total_amt);

        FROM_DATE.setText(getCurrentDate());
        TO_DATE.setText(getCurrentDate());

        APPBAR_TITLE.setText("Reports");
        BACK.setOnClickListener(view -> Report.super.onBackPressed());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Report.this,R.array.Status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_size);
        STATUS.setAdapter(adapter);

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
        FROM_DATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Report.this,date,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        TO_DATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Report.this,date1,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        PROCESS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchReports();
            }
        });
    }
    private String getCurrentDate(){
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }
    private void fetchReports() {
        final ProgressDialog progressDialog = new ProgressDialog(Report.this);
        progressDialog.setMessage("Please wait...");

        progressDialog.show();
        String From_Date = FROM_DATE.getText().toString().trim();
        String To_Date = TO_DATE.getText().toString().trim();
        String Status = STATUS.getSelectedItem().toString().trim();

        reportsModel.clear();
        StringRequest request = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/adminpanel/order_reports.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            double item_total = 0;

                            for (int i = 0; i<jsonArray.length();i++){

                                JSONObject object = jsonArray.getJSONObject(i);

                                String Report_order_no = object.getString("order_no");
                                String Report_order_date = object.getString("order_date");
                                String Report_mobile_no = object.getString("cust_phone");
                                String Report_status = object.getString("status");
                                String Report_qty = object.getString("qty");
                                double Report_amount = object.getDouble("amount");

                                ReportsModel reportsModels =new ReportsModel (Report_order_no,Report_order_date,Report_mobile_no,Report_status,Report_qty,Report_amount);
                                reportsModel.add(reportsModels);

                                item_total += Report_amount;
                                TOTAL_AMOUNT.setText("₹"+ item_total);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        reportsAdapter = new ReportsAdapter(Report.this,reportsModel);
                        RECYCLERVIEW_REPORTS.setAdapter(reportsAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Report.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("from_date",From_Date);
                params.put("to_date",To_Date);
                params.put("status",Status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void getFromDate() {
        String DateFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat, Locale.US);
        FROM_DATE.setText(dateFormat.format(calendar.getTime()));
    }
    private void getToDate() {
        String DateFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat, Locale.US);
        TO_DATE.setText(dateFormat.format(calendar.getTime()));
    }
}