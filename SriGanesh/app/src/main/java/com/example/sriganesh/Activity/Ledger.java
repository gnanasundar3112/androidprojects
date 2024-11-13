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
import com.example.sriganesh.Adapter.LedgerAdapter;
import com.example.sriganesh.Model.LedgerModel;
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

public class Ledger extends AppCompatActivity {
    EditText FROM_DATE,TO_DATE;
    Calendar calendar;

    private FloatingActionButton backPress;
    private TextView appbarTitle,DEBIT_TOTAL,CREDIT_TOTAL;

    private RecyclerView recyclerViewLedger;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager manager;
    private List<LedgerModel> ledgerModelList;

    private ProgressBar progressBar;
    private Button PROCESS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger);

        FROM_DATE = findViewById(R.id.from_date);
        TO_DATE = findViewById(R.id.to_date);
        DEBIT_TOTAL =findViewById(R.id.ledger_debit_total);
        CREDIT_TOTAL =findViewById(R.id.ledger_credit_total);
        PROCESS = findViewById(R.id.ledger_process);

        backPress = findViewById(R.id.fab_backPress);
        appbarTitle = findViewById(R.id.txt_appbarTitle);

        calendar = Calendar.getInstance();

        progressBar = findViewById(R.id.ProgressBar1);
        recyclerViewLedger = findViewById(R.id.ledger_recycler);
        manager = new GridLayoutManager(Ledger.this,1);
        recyclerViewLedger.setLayoutManager(manager);
        ledgerModelList = new ArrayList<>();

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
                new DatePickerDialog(Ledger.this,date,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        TO_DATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Ledger.this,date1,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        PROCESS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchLedger();
            }
        });
        appbarTitle.setText("Ledger");
        //back press activity
        backPress.setOnClickListener(view -> Ledger.super.onBackPressed());
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


    private void fetchLedger() {
        String from_date = FROM_DATE.getText().toString().trim();
        String to_date = TO_DATE.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/admin/getcartproducts.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    double Debit_item_total = 0;
                    double Credit_item_total = 0;

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String FirmName = jsonObject.getString("id");
                        String VouDate = jsonObject.getString("eng_name");
                        String Naration = jsonObject.getString("tam_name");
                        double Credit = jsonObject.getDouble("amount");
                        double Debit = jsonObject.getDouble("amount");
                        double Closing = jsonObject.getDouble("amount");

                        LedgerModel ledgerModel = new LedgerModel(FirmName,VouDate,Naration,Credit,Debit,Closing);
                        ledgerModelList.add(ledgerModel);

                        Debit_item_total += Debit;
                        Credit_item_total += Credit;

                        DEBIT_TOTAL.setText(""+Debit_item_total);
                        CREDIT_TOTAL.setText(""+Credit_item_total);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                mAdapter = new LedgerAdapter(Ledger.this,ledgerModelList);
                recyclerViewLedger.setAdapter(mAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Ledger.this, error.toString(), Toast.LENGTH_SHORT).show();
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