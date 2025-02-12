package com.vinsoftsolutions.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.vinsoftsolutions.Adapter.CashAdapter;
import com.vinsoftsolutions.Database.CashDatabase;
import com.vinsoftsolutions.Internet.NetworkChangeListener;
import com.vinsoftsolutions.Models.CashModel;
import com.vinsoftsolutions.R;
import com.vinsoftsolutions.Service.CustomAlertDialog;
import com.vinsoftsolutions.Service.DateAndTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Cash extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private ImageView BACK_PRESS;
    private TextView APPBAR_TITLE,DATE,TOTAL;
    private MaterialButton PROCESS;
    private Calendar CALENDAR;
    private RecyclerView CASH_RECYCLERVIEW;
    private RecyclerView.LayoutManager CASH_MANAGER;
    private Handler handler = new Handler();
    private CashDatabase cashDatabase = new CashDatabase(Cash.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);

        BACK_PRESS = findViewById(R.id.backPress);
        APPBAR_TITLE = findViewById(R.id.appbarTitle);
        DATE = findViewById(R.id.date);
        PROCESS = findViewById(R.id.process);
        TOTAL = findViewById(R.id.cash_total);
        CASH_RECYCLERVIEW = findViewById(R.id.cash_recycler);

        APPBAR_TITLE.setText(getApplicationContext().getString(R.string.cash));
        BACK_PRESS.setOnClickListener(view -> Cash.super.onBackPressed());

        CASH_MANAGER = new GridLayoutManager(Cash.this,1);
        CASH_RECYCLERVIEW.setLayoutManager(CASH_MANAGER);

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
                new DatePickerDialog(Cash.this, date, CALENDAR.get(Calendar.YEAR),
                        CALENDAR.get(Calendar.MONTH),
                        CALENDAR.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        PROCESS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = DATE.getText().toString().trim();

                if (date.equals("")){
                    Toast.makeText(Cash.this, "Please Select Date", Toast.LENGTH_SHORT).show();
                }else {
                    CustomAlertDialog alertDialog = new CustomAlertDialog(Cash.this);
                    AlertDialog progressDialog = alertDialog.pleaseWaitDialog();
                    progressDialog.show();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            cashDatabase.fetch(CASH_RECYCLERVIEW,date,TOTAL);
                        }
                    }, 3000);
                }
            }
        });
    }
}