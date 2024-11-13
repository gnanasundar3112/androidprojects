package com.sundar.i_macbankers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sundar.i_macbankers.Adapter.ReceiptAdapter;
import com.sundar.i_macbankers.Adapter.ReportsAdapter;
import com.sundar.i_macbankers.Models.ReceiptModel;
import com.sundar.i_macbankers.Models.ReportsModel;
import com.sundar.i_macbankers.internet.NetworkChangeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Reports extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private ImageView BACK_PRESS,ADD_BTN;
    private TextView APPBAR_TITLE;
    private TextView MONTH,YEAR,ALL_LOAN_AMT,ALL_PAID_AMT,ALL_BAL_AMT;
    private final Calendar CALENDAR = Calendar.getInstance();

    private RecyclerView REPORTS_RECYCLER;
    private RecyclerView.LayoutManager REPORTS_MANAGER;

    private List<ReportsModel> REPORTS;
    private ReportsAdapter REPORTS_ADAPTER;
    String month,fetch_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        fetch_url = Links.report_fetch;

        ADD_BTN = findViewById(R.id.add_btn);
        ADD_BTN.setImageResource(android.R.color.transparent);
        APPBAR_TITLE = findViewById(R.id.appbarTitle);
        APPBAR_TITLE.setText("Reports");
        //back press activity
        BACK_PRESS = findViewById(R.id.backPress);
        BACK_PRESS.setOnClickListener(view -> Reports.super.onBackPressed());

        MONTH = findViewById(R.id.reports_month);
        YEAR = findViewById(R.id.reports_year);
        ALL_LOAN_AMT = findViewById(R.id.all_loan_amt);
        ALL_PAID_AMT = findViewById(R.id.all_paid_amt);
        ALL_BAL_AMT = findViewById(R.id.all_bal_amt);

        MONTH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMonthPickerDialog();
            }
        });

        YEAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showYearPickerDialog();
            }
        });

        // fetch  start
        REPORTS_RECYCLER = findViewById(R.id.receipt_recyclerView);
        REPORTS_MANAGER = new GridLayoutManager(Reports.this, 1);
        REPORTS_RECYCLER.setLayoutManager(REPORTS_MANAGER);
        REPORTS = new ArrayList<>();
    }

    private void showMonthPickerDialog() {
        final Dialog dialog = new Dialog(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_year_picker, null);

        final NumberPicker monthPicker = view.findViewById(R.id.yearPicker);
        monthPicker.setMinValue(0);
        monthPicker.setMaxValue(11);
        monthPicker.setDisplayedValues(new String[] {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        });
        monthPicker.setValue(CALENDAR.get(Calendar.MONTH));

        Button buttonOk = view.findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CALENDAR.set(Calendar.MONTH, monthPicker.getValue());
                getMonth();
                dialog.dismiss();
                String month = MONTH.getText().toString();
                String Year = YEAR.getText().toString();
                fetch(month,Year);
            }
        });

        dialog.setContentView(view);
        dialog.show();
    }
    private void getMonth() {
        String DateFormat = "MMMM MM";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat, Locale.US);
        MONTH.setText(dateFormat.format(CALENDAR.getTime()));
    }
    private void showYearPickerDialog() {
        final Dialog dialog = new Dialog(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_year_picker, null);

        final NumberPicker yearPicker = view.findViewById(R.id.yearPicker);
        yearPicker.setMinValue(1900);
        yearPicker.setMaxValue(2100);
        yearPicker.setValue(CALENDAR.get(Calendar.YEAR));

        Button buttonOk = view.findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CALENDAR.set(Calendar.YEAR, yearPicker.getValue());
                getYear();
                dialog.dismiss();

                String month = "";
                String Year = YEAR.getText().toString();
                fetch(month,Year);
            }
        });

        dialog.setContentView(view);
        dialog.show();
    }
    private void getYear() {
        String DateFormat = "yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat, Locale.US);
        YEAR.setText(dateFormat.format(CALENDAR.getTime()));
    }

    public void fetch(String month,String year) {

        StringRequest request = new StringRequest(Request.Method.POST, fetch_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            REPORTS.clear();

                            double bal_int = 0,all_loan_amt = 0,all_paid_amt = 0,all_bal_amt=0;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String loan_date = object.getString("loan_date");
                                String loan_no = object.getString("loan_no");
                                String party_name = object.getString("cust_name");
                                String gram = object.getString("net_wt");
                                String int_per = object.getString("int_per");
                                String status = object.getString("status");
                                String cl_date = object.getString("cl_date");
                                double loan_amt = object.getDouble("sanc_amt");
                                double int_amt = object.getDouble("int_amt");
                                double tol_amt = object.getDouble("tol_int");
                                double tol_paid = object.getDouble("tol_paid");

                                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                String Loan_Date = null;
                                String Close_Date = null;

                                try {
                                    Date loandate = originalFormat.parse(loan_date);
                                    Date close_date = originalFormat.parse(cl_date);
                                    SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                    Loan_Date = newFormat.format(loandate);
                                    Close_Date = newFormat.format(close_date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if (cl_date.equals("0000-00-00")) {
                                    Close_Date = "00-00-0000";
                                }

                                bal_int = tol_amt - tol_paid;

                                if (status.equals("closed")){
                                    bal_int = Double.parseDouble("0.0");
                                }

                                all_paid_amt +=tol_paid;
                                ALL_PAID_AMT.setText(String.valueOf(all_paid_amt));
                                all_loan_amt +=loan_amt;
                                ALL_LOAN_AMT.setText(String.valueOf(all_loan_amt));
                                all_bal_amt += bal_int;
                                ALL_BAL_AMT.setText(String.valueOf(all_bal_amt));


                                ReportsModel reportsModel = new ReportsModel(Loan_Date, loan_no, party_name, gram, int_per, status, Close_Date, loan_amt, int_amt, tol_amt, tol_paid, bal_int);
                                REPORTS.add(reportsModel);
                            }
                            REPORTS_ADAPTER = new ReportsAdapter(Reports.this, REPORTS);
                            REPORTS_RECYCLER.setAdapter(REPORTS_ADAPTER);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Reports.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Reports.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Reports.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("month", month);
                params.put("year", year);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }

}