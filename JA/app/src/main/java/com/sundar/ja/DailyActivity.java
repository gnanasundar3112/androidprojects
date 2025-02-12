package com.sundar.ja;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sundar.ja.Adapter.DailyAdapter;
import com.sundar.ja.Adapter.PartyAdapter;
import com.sundar.ja.Models.DailyModel;
import com.sundar.ja.Models.PartyModel;
import com.sundar.ja.SqlDatabase.SqlDatabaseHelper;

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

public class DailyActivity extends AppCompatActivity {
    private ImageView BACK_PRESS,ADD_BTN,SETTING;
    private TextView APPBAR_TITLE;
    private RecyclerView DAILY_RECYCLER;
    private RecyclerView.LayoutManager DAILY_MANAGER;
    private List<DailyModel> DAILY;
    private DailyAdapter DAILY_ADAPTER;
    SQLiteDatabase db;
    Calendar CALENDAR;
    private TextView DATE,TOL_QTY,TOL_AMT,TOT_RECEIVED,TOL_BAL;
    private TableRow TABLE_ROW;
    String AC_ID;
    private TextInputEditText QTY,AMOUNT,RECEIVED;
    private Spinner PAYMENT;
    private MaterialButton SAVE,CANCEL;
    ArrayAdapter<String> payment_adapter;
    Dialog DIALOG;
    ArrayAdapter<String> adapter;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        ADD_BTN = findViewById(R.id.add_btn);
        SETTING = findViewById(R.id.setting);
        SETTING.setVisibility(View.GONE);
        APPBAR_TITLE = findViewById(R.id.appbarTitle);
        APPBAR_TITLE.setText("Daily Activity");
        //back press activity
        BACK_PRESS = findViewById(R.id.backPress);
        BACK_PRESS.setOnClickListener(view -> DailyActivity.super.onBackPressed());

        // fetch  start
        DAILY_RECYCLER = findViewById(R.id.daily_recyclerView);
        DAILY_MANAGER = new GridLayoutManager(DailyActivity.this, 1);
        DAILY_RECYCLER.setLayoutManager(DAILY_MANAGER);
        DAILY = new ArrayList<>();

        TOL_QTY = findViewById(R.id.tol_qty);
        TOL_AMT = findViewById(R.id.tol_amt);
        TOT_RECEIVED = findViewById(R.id.tol_received);
        TOL_BAL = findViewById(R.id.tol_bal);
        TABLE_ROW = findViewById(R.id.table_row);

        SqlDatabaseHelper dataBaseHelper = new SqlDatabaseHelper(this);
        db = dataBaseHelper.getReadableDatabase();
        db = dataBaseHelper.getWritableDatabase();

        CALENDAR = Calendar.getInstance();

        ADD_BTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DailyActivity.this, R.style.AlertDialogTheme);
                View view = LayoutInflater.from(DailyActivity.this).inflate(R.layout.dialog_daily,
                        (LinearLayout)findViewById(R.id.daily_dialog));
                builder.setView(view);

                alertDialog = builder.create();

                ((TextView) view.findViewById(R.id.dialog_title)).setText("Add New Detail");

                DATE = view.findViewById(R.id.date);
                QTY = view.findViewById(R.id.dia_can_qty);
                PAYMENT = view.findViewById(R.id.dia_payment);
                RECEIVED = view.findViewById(R.id.dia_received);
                AMOUNT = view.findViewById(R.id.dia_amount);

                SAVE = view.findViewById(R.id.dialog_save);
                CANCEL = view.findViewById(R.id.dialog_cancel);

                payment_adapter = new ArrayAdapter<>(DailyActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.payment));
                payment_adapter.setDropDownViewResource(R.layout.item_drop_down);
                PAYMENT.setAdapter(payment_adapter);

                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        CALENDAR.set(Calendar.YEAR,year);
                        CALENDAR.set(Calendar.MONTH,month);
                        CALENDAR.set(Calendar.DAY_OF_MONTH,day);
                        getFromDate();
                    }
                };

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String currentDate = dateFormat.format(new Date());
                DATE.setText(currentDate);

                DATE.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DatePickerDialog(DailyActivity.this, date, CALENDAR.get(Calendar.YEAR),
                                CALENDAR.get(Calendar.MONTH),
                                CALENDAR.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                SAVE.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        insert();
                    }
                });

                CANCEL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View V) {
                        alertDialog.dismiss();
                    }
                });

                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }
        });


    }

    private void getFromDate() {
        String DateFormat = "dd-MM-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat, Locale.US);
        DATE.setText(dateFormat.format(CALENDAR.getTime()));
    }
    @SuppressLint("Range")
    public void insert(){

        String day_id = "000001";
        Cursor c = db.rawQuery("SELECT substr(day_id+1000001,2,6) as day_id,day_id+1 as day_id1 FROM `dailyactive` order by day_id1 desc limit 1", null);

        if (c.moveToNext()){
            day_id = c.getString(c.getColumnIndex("day_id"));
        }

        String Date = DATE.getText().toString().trim();
        String Ac_Id = AC_ID;
        String qty = QTY.getText().toString().trim();
        String Payment = PAYMENT.getSelectedItem().toString();
        String Amount = AMOUNT.getText().toString().toString();
        String Received = RECEIVED.getText().toString().trim();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateFormat.format(new Date());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String time = timeFormat.format(new Date());

        if (Payment.equals("CASH")) {
            Payment = "1";
        }else if (Payment.equals("GPAY")){
            Payment = "2";
        }else if (Payment.equals("PAYTM")) {
            Payment = "3";
        }else {
            Payment = "4";
        }

        if (qty.isEmpty()){
            Toast.makeText(this, "Please Enter Qty", Toast.LENGTH_SHORT).show();
        }else if (Payment.isEmpty()){
            Toast.makeText(this, "Please Select Payment Type", Toast.LENGTH_SHORT).show();
        }else if (Amount.isEmpty()){
            Toast.makeText(this, "Please Enter Amount", Toast.LENGTH_SHORT).show();
        }else {

            ContentValues values = new ContentValues();
            values.put("day_id", day_id);
            values.put("date", Date);
            values.put("ac_id", Ac_Id);
            values.put("qty", qty);
            values.put("payment", Payment);
            values.put("received", Received);
            values.put("amount", Amount);
            values.put("crea_date", date);
            values.put("crea_time", time);
            values.put("modi_date", date);
            values.put("modi_time", time);

            long tableRow = db.insert("dailyactive", null, values);

            if (tableRow != -1) {
                Toast.makeText(this, "Inserted Successfully", Toast.LENGTH_SHORT).show();
                QTY.setText("");
                AMOUNT.setText("0");
                RECEIVED.setText("0");
                fetch();
                alertDialog.dismiss();
            } else {
                // Failed insertion
                Toast.makeText(this, "Inserted Failed", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        }
    }

    @SuppressLint("Range")
    public void fetch() {

        Cursor c = db.rawQuery(
                "SELECT a.day_id, a.date, a.ac_id, b.ac_name, a.qty, a.payment, a.amount,a.received " +
                        "FROM dailyactive AS a " +
                        "LEFT JOIN accountmaster AS b ON a.ac_id = b.ac_id " +
                        "WHERE a.ac_id = ? " +
                        "ORDER BY strftime('%Y-%m-%d', substr(a.date, 7, 4) || '-' || substr(a.date, 4, 2) || '-' || substr(a.date, 1, 2)) ASC",
                new String[]{AC_ID}
        );
        DAILY.clear();

        int tot_qty = 0;
        double tot_amt = 0;
        double tot_received = 0;

        while (c.moveToNext()) {
            String day_id = c.getString(c.getColumnIndex("day_id"));
            String date = c.getString(c.getColumnIndex("date"));
            String ac_name = c.getString(c.getColumnIndex("ac_name"));
            int qty = c.getInt(c.getColumnIndex("qty"));
            String payment = c.getString(c.getColumnIndex("payment"));
            double amount = c.getDouble(c.getColumnIndex("amount"));
            double received = c.getDouble(c.getColumnIndex("received"));

            tot_qty += qty;
            tot_amt += amount;
            tot_received += received;

            if (payment.equals("1")){
                payment = "CASH";
            }else if (payment.equals("2")){
                payment = "GPAY";
            }else if (payment.equals("3")){
                payment = "PATYM";
            }else {
                payment = "ACCOUNT";
            }

            DailyModel dailyModel = new DailyModel(day_id,date,ac_name,qty,payment,amount,received);
            DAILY.add(dailyModel);
        }

        if (DAILY.isEmpty()){
            TABLE_ROW.setVisibility(View.GONE);
        }else {
            TABLE_ROW.setVisibility(View.VISIBLE);
        }

        double balance = tot_amt - tot_received;

        TOL_QTY.setText(String.valueOf(tot_qty));
        TOL_AMT.setText(String.format("%.1f", tot_amt));
        TOT_RECEIVED.setText(String.format("%.1f", tot_received));
        TOL_BAL.setText(String.format("₹ %.1f", balance));

        DAILY_ADAPTER = new DailyAdapter(DailyActivity.this,DAILY);
        DAILY_RECYCLER.setAdapter(DAILY_ADAPTER);
    }

    public void getUserDetail() {
        Intent intent = getIntent();
        if (intent != null) {
            String party_id = intent.getStringExtra("party_id");
            String party_name = intent.getStringExtra("party_name");
            AC_ID = party_id;
            APPBAR_TITLE.setText(party_name);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUserDetail();
        fetch();
    }
}