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

import com.google.android.material.button.MaterialButton;
import com.vinsoftsolutions.Database.ReceiptDatabase;
import com.vinsoftsolutions.Internet.NetworkChangeListener;
import com.vinsoftsolutions.R;
import com.vinsoftsolutions.Service.CustomAlertDialog;
import com.vinsoftsolutions.Service.DateAndTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Receipt extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private ImageView BACK_PRESS;
    private TextView APPBAR_TITLE,FROM_DATE,TO_DATE,TOTAL;
    private MaterialButton PROCESS;
    private Calendar CALENDAR;
    private RecyclerView RECEIPT_RECYCLERVIEW;
    private RecyclerView.LayoutManager RECEIPT_MANAGER;
    private Handler handler = new Handler();
    int dateIsClick = 0;
    private ReceiptDatabase receiptDatabase = new ReceiptDatabase(Receipt.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        BACK_PRESS = findViewById(R.id.backPress);
        APPBAR_TITLE = findViewById(R.id.appbarTitle);
        FROM_DATE = findViewById(R.id.from_date);
        TO_DATE = findViewById(R.id.to_date);
        PROCESS = findViewById(R.id.process);
        TOTAL = findViewById(R.id.receipt_total);
        RECEIPT_RECYCLERVIEW = findViewById(R.id.receipt_recycler);

        APPBAR_TITLE.setText(getApplicationContext().getString(R.string.receipt));
        BACK_PRESS.setOnClickListener(view -> Receipt.super.onBackPressed());

        RECEIPT_MANAGER = new GridLayoutManager(Receipt.this,1);
        RECEIPT_RECYCLERVIEW.setLayoutManager(RECEIPT_MANAGER);

        CALENDAR = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener from_date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                CALENDAR.set(Calendar.YEAR,year);
                CALENDAR.set(Calendar.MONTH,month);
                CALENDAR.set(Calendar.DAY_OF_MONTH,day);
                SimpleDateFormat date = DateAndTime.getDate(CALENDAR);
                if (dateIsClick==0) {
                    FROM_DATE.setText(date.format(CALENDAR.getTime()));
                }else {
                    TO_DATE.setText(date.format(CALENDAR.getTime()));
                }
            }
        };

        FROM_DATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Receipt.this, from_date, CALENDAR.get(Calendar.YEAR),
                        CALENDAR.get(Calendar.MONTH),
                        CALENDAR.get(Calendar.DAY_OF_MONTH)).show();
                dateIsClick = 0;
            }
        });

        TO_DATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Receipt.this, from_date, CALENDAR.get(Calendar.YEAR),
                        CALENDAR.get(Calendar.MONTH),
                        CALENDAR.get(Calendar.DAY_OF_MONTH)).show();
                dateIsClick = 1;
            }
        });


        PROCESS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from_date = FROM_DATE.getText().toString().trim();
                String to_date = TO_DATE.getText().toString().trim();

                if (from_date.equals("")){
                    Toast.makeText(Receipt.this, "Please Select From Date", Toast.LENGTH_SHORT).show();
                }else if (to_date.equals("")){
                    Toast.makeText(Receipt.this, "Please Select To Date", Toast.LENGTH_SHORT).show();
                }else {
                    CustomAlertDialog alertDialog = new CustomAlertDialog(Receipt.this);
                    AlertDialog progressDialog = alertDialog.pleaseWaitDialog();
                    progressDialog.show();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            receiptDatabase.fetch(RECEIPT_RECYCLERVIEW,from_date,to_date,TOTAL);
                        }
                    }, 3000);
                }
            }
        });
    }
}