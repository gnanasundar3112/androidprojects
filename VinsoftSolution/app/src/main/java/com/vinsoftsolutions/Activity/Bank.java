package com.vinsoftsolutions.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.vinsoftsolutions.Database.BankDatabase;
import com.vinsoftsolutions.Internet.NetworkChangeListener;
import com.vinsoftsolutions.R;
import com.vinsoftsolutions.Service.CustomAlertDialog;
import com.vinsoftsolutions.Service.DateAndTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Bank extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private ImageView BACK_PRESS;
    private TextView APPBAR_TITLE,DATE,TOTAL;
    private MaterialButton PROCESS;
    private Calendar CALENDAR;
    private RecyclerView BANK_RECYCLERVIEW;
    private RecyclerView.LayoutManager BANK_MANAGER;
    private Handler handler = new Handler();
    private BankDatabase bankDatabase = new BankDatabase(Bank.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        BACK_PRESS = findViewById(R.id.backPress);
        APPBAR_TITLE = findViewById(R.id.appbarTitle);
        DATE = findViewById(R.id.date);
        PROCESS = findViewById(R.id.process);
        TOTAL = findViewById(R.id.bank_total);
        BANK_RECYCLERVIEW = findViewById(R.id.bank_recycler);

        APPBAR_TITLE.setText(getApplicationContext().getString(R.string.bank));
        BACK_PRESS.setOnClickListener(view -> Bank.super.onBackPressed());

        BANK_MANAGER = new GridLayoutManager(Bank.this,1);
        BANK_RECYCLERVIEW.setLayoutManager(BANK_MANAGER);

        CALENDAR = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                CALENDAR.set(Calendar.YEAR,year);
                CALENDAR.set(Calendar.MONTH,month);
                CALENDAR.set(Calendar.DAY_OF_MONTH,day);
                SimpleDateFormat date = DateAndTime.getDate(CALENDAR);
                DATE.setText(date.format(CALENDAR.getTime()));
            }
        };

        DATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Bank.this, date, CALENDAR.get(Calendar.YEAR),
                        CALENDAR.get(Calendar.MONTH),
                        CALENDAR.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        PROCESS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = DATE.getText().toString().trim();

                if (date.equals("")){
                    Toast.makeText(Bank.this, "Please Select Date", Toast.LENGTH_SHORT).show();
                }else {
                    CustomAlertDialog alertDialog = new CustomAlertDialog(Bank.this);
                    AlertDialog progressDialog = alertDialog.pleaseWaitDialog();
                    progressDialog.show();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            bankDatabase.fetch(BANK_RECYCLERVIEW,date,TOTAL);
                        }
                    }, 3000);
                }
            }
        });
    }

}