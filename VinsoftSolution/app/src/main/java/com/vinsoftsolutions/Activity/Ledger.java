package com.vinsoftsolutions.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.vinsoftsolutions.Internet.NetworkChangeListener;
import com.vinsoftsolutions.R;
import com.vinsoftsolutions.Service.CustomAlertDialog;
import com.vinsoftsolutions.Service.DateAndTime;
import com.vinsoftsolutions.Service.FindYear;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ledger extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private ImageView BACK_PRESS;
    private String ID;
    private TextView APPBAR_TITLE,FIRM_NAME,AC_NAME,FROM_DATE,TO_DATE,TOTAL_CREDIT,TOTAL_DEBIT;
    private MaterialButton PROCESS;
    private Calendar CALENDAR;
    private RecyclerView LEDGER_RECYCLERVIEW;
    private RecyclerView.LayoutManager LEDGER_MANAGER;
    private Handler handler = new Handler();
    int dateIsClick = 0;
    private Dialog DIALOG;
    private ArrayAdapter<String> adapter;
    public static HashMap<String, String> firm_lists = new HashMap<>();
    public static HashMap<String, String> acname_lists = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger);

        BACK_PRESS = findViewById(R.id.backPress);
        APPBAR_TITLE = findViewById(R.id.appbarTitle);
        FIRM_NAME = findViewById(R.id.firm_name);
        AC_NAME = findViewById(R.id.ac_name);
        FROM_DATE = findViewById(R.id.from_date);
        TO_DATE = findViewById(R.id.to_date);
        PROCESS = findViewById(R.id.process);
        TOTAL_CREDIT = findViewById(R.id.credit_total);
        TOTAL_DEBIT = findViewById(R.id.debit_total);
        LEDGER_RECYCLERVIEW = findViewById(R.id.ledger_recycler);

        APPBAR_TITLE.setText(getApplicationContext().getString(R.string.ledger));
        BACK_PRESS.setOnClickListener(view -> Ledger.super.onBackPressed());

        LEDGER_MANAGER = new GridLayoutManager(Ledger.this,1);
        LEDGER_RECYCLERVIEW.setLayoutManager(LEDGER_MANAGER);

        CALENDAR = Calendar.getInstance();

        FIRM_NAME.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchDialogBox(0,"Select Firm");
            }
        });
        AC_NAME.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchDialogBox(1,"Select Ac Name");
            }
        });

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
                new DatePickerDialog(Ledger.this, from_date, CALENDAR.get(Calendar.YEAR),
                        CALENDAR.get(Calendar.MONTH),
                        CALENDAR.get(Calendar.DAY_OF_MONTH)).show();
                dateIsClick = 0;
            }
        });

        TO_DATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Ledger.this, from_date, CALENDAR.get(Calendar.YEAR),
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
                    Toast.makeText(Ledger.this, "Please Select From Date", Toast.LENGTH_SHORT).show();
                }else if (to_date.equals("")){
                    Toast.makeText(Ledger.this, "Please Select To Date", Toast.LENGTH_SHORT).show();
                }else {
                    CustomAlertDialog alertDialog = new CustomAlertDialog(Ledger.this);
                    AlertDialog progressDialog = alertDialog.pleaseWaitDialog();
                    progressDialog.show();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            String year = FindYear.acYear(from_date);
                            Toast.makeText(Ledger.this,year , Toast.LENGTH_SHORT).show();
                        }
                    }, 3000);
                }
            }
        });
    }

    public void SearchDialogBox(int list,String name){
        DIALOG = new Dialog(Ledger.this);
        DIALOG.setContentView(R.layout.dialog_search_spinner);

        // Set dialog width to match parent
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(DIALOG.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = 1000;
        DIALOG.getWindow().setAttributes(layoutParams);

        DIALOG.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DIALOG.show();

        TextView Tittle = DIALOG.findViewById(R.id.dialog_spinner_title);
        Tittle.setText(name);
        EditText editText = DIALOG.findViewById(R.id.spinner_search);
        ListView listView = DIALOG.findViewById(R.id.spinner_list);

        // Initialize the list outside the loop
        if (list == 0) {
            List<String> firmlists = new ArrayList<>(firm_lists.values());
            adapter = new ArrayAdapter<>(Ledger.this, android.R.layout.simple_list_item_1, firmlists);
        }else {
            List<String> acnamelists = new ArrayList<>(acname_lists.values());
            adapter = new ArrayAdapter<>(Ledger.this, android.R.layout.simple_list_item_1, acnamelists);
        }
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedName = adapter.getItem(position);

                if (list == 0) {
                    setFirmlists(selectedName);
                }else {
                    setAcnamelists(selectedName);
                }

                DIALOG.dismiss();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Lists();
    }

    public void Lists(){
        firm_lists.clear();
        acname_lists.clear();

        firm_lists.put("1","hello world");
        firm_lists.put("2","hello world1");
        firm_lists.put("3","hello world2");
        firm_lists.put("4","hello world3");
        firm_lists.put("5","hello world4");
        firm_lists.put("6","hello world5");

        acname_lists.put("7","world");
        acname_lists.put("8","world1");
        acname_lists.put("9","world2");
        acname_lists.put("10","world3");
        acname_lists.put("11","world4");
        acname_lists.put("12","world5");

    }

    public void setFirmlists(String selectedName){
        String selectedId = null;

        for (Map.Entry<String, String> entry : firm_lists.entrySet()) {
            if (entry.getValue().equals(selectedName)) {
                selectedId = entry.getKey();
                break;
            }
        }

        if (selectedId != null) {
            FIRM_NAME.setText(selectedName);
            ID = selectedId;
        }
    }
    public void setAcnamelists(String selectedName){
        String selectedId = null;

        for (Map.Entry<String, String> entry : acname_lists.entrySet()) {
            if (entry.getValue().equals(selectedName)) {
                selectedId = entry.getKey();
                break;
            }
        }

        if (selectedId != null) {
            AC_NAME.setText(selectedName);
            ID = selectedId;
        }
    }
}