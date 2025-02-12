package com.sundar.ja;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sundar.ja.Adapter.PartyAdapter;
import com.sundar.ja.Models.PartyModel;
import com.sundar.ja.SqlDatabase.SqlDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PartyMaster extends AppCompatActivity {

    private ImageView BACK_PRESS,ADD_BTN,SETTING;
    private TextInputEditText SEARCHVIEW;
    private TextView APPBAR_TITLE;
    private RecyclerView PARTY_RECYCLER;
    private RecyclerView.LayoutManager PARTY_MANAGER;
    private List<PartyModel> PARTY;
    private PartyAdapter PARTY_ADAPTER;
    SQLiteDatabase db;
    private TextInputEditText AC_NAME,ADDRESS,MOBILE;
    private Spinner ACTIVE;
    ArrayAdapter<String> active_adapter;
    private MaterialButton SAVE,CANCEL;
    AlertDialog alertDialog;
    private boolean isDialogShowing = false;
    private boolean isExitConfirmed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_master);

        ADD_BTN = findViewById(R.id.add_btn);
        SETTING = findViewById(R.id.setting);
        SEARCHVIEW = findViewById(R.id.searchView);
        ADD_BTN.setPadding(0,0,40,0);
        APPBAR_TITLE = findViewById(R.id.appbarTitle);
        APPBAR_TITLE.setText("JA Water");
        APPBAR_TITLE.setPadding(60,0,0,0);
        //back press activity
        BACK_PRESS = findViewById(R.id.backPress);
        BACK_PRESS.setOnClickListener(view -> PartyMaster.super.onBackPressed());
        BACK_PRESS.setVisibility(View.GONE);

        // fetch  start
        PARTY_RECYCLER = findViewById(R.id.party_recyclerView);
        PARTY_MANAGER = new GridLayoutManager(PartyMaster.this, 1);
        PARTY_RECYCLER.setLayoutManager(PARTY_MANAGER);
        PARTY = new ArrayList<>();

        SqlDatabaseHelper dataBaseHelper = new SqlDatabaseHelper(this);
        db = dataBaseHelper.getReadableDatabase();
        db = dataBaseHelper.getWritableDatabase();

        SETTING.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PartyMaster.this,MainActivity.class);
                startActivity(intent);
            }
        });

        ADD_BTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PartyMaster.this, R.style.AlertDialogTheme);
                View view = LayoutInflater.from(PartyMaster.this).inflate(R.layout.partydialog,
                        (LinearLayout)findViewById(R.id.party_dialog));
                builder.setView(view);

                alertDialog = builder.create();

                ((TextView) view.findViewById(R.id.dialog_title)).setText("Add New Party Detail");

                AC_NAME = view.findViewById(R.id.dia_ac_name);
                ADDRESS = view.findViewById(R.id.dia_address);
                MOBILE = view.findViewById(R.id.dia_mobile);
                ACTIVE = view.findViewById(R.id.dia_active);

                SAVE = view.findViewById(R.id.dialog_save);
                CANCEL = view.findViewById(R.id.dialog_cancel);

                active_adapter = new ArrayAdapter<>(PartyMaster.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.active));
                active_adapter.setDropDownViewResource(R.layout.item_drop_down);
                ACTIVE.setAdapter(active_adapter);


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
        /* filter from search bar start*/
        SEARCHVIEW.clearFocus();
        SEARCHVIEW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this example
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fileList(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    // Disable the cursor pointer when the search view is empty
                    SEARCHVIEW.setCursorVisible(false);
                    // Close the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(SEARCHVIEW.getWindowToken(), 0);
                } else {
                    // Enable the cursor pointer when there is text in the search view
                    SEARCHVIEW.setCursorVisible(true);
                }
            }
            private void fileList(String text) {
                List<PartyModel> filteredList = new ArrayList<>();
                for (PartyModel item : PARTY) {
                    if (item.getParty_name().toLowerCase().contains(text.toLowerCase()) || item.getParty_name().toLowerCase().contains(text.toLowerCase())){
                        filteredList.add(item);
                    }
                }
                if (filteredList.isEmpty()) {
                    Toast.makeText(PartyMaster.this, "No data found", Toast.LENGTH_SHORT).show();
                } else {
                    PARTY_ADAPTER.setFilteredList(filteredList);
                }
            }
        });
        /* filter from search bar End*/
    }

    @SuppressLint("Range")
    public void insert(){
        String Ac_name = AC_NAME.getText().toString().trim();
        String Address = ADDRESS.getText().toString().trim();
        String Mobile = MOBILE.getText().toString().trim();
        String Active = ACTIVE.getSelectedItem().toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String Date = dateFormat.format(new Date());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String time = timeFormat.format(new Date());

        if (Active.equals("ENABLE")) {
            Active = "1";
        }else {
            Active = "2";
        }

        if (Ac_name.isEmpty()){
            Toast.makeText(this, "Please Enter Party Name", Toast.LENGTH_SHORT).show();
        }else {
            ContentValues values = new ContentValues();
            values.put("ac_name", Ac_name.toUpperCase());
            values.put("address", Address.toUpperCase());
            values.put("phone", Mobile);
            values.put("active", Active);
            values.put("crea_date", Date);
            values.put("crea_time", time);
            values.put("modi_date", Date);
            values.put("modi_time", time);

            long tableRow = db.insert("accountmaster", null, values);

            if (tableRow != -1) {
                Toast.makeText(this, "Inserted Successfully", Toast.LENGTH_SHORT).show();
                AC_NAME.setText("");
                ADDRESS.setText("");
                MOBILE.setText("");
                AC_NAME.requestFocus();
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

        Cursor c = db.rawQuery("SELECT ac_id, ac_name, address, phone, CASE WHEN active = 1 THEN 'ENABLE' ELSE 'DISABLE' END as active " +
                "FROM accountmaster", null);
        PARTY.clear();

        while (c.moveToNext()) {
            String FET_ACID = c.getString(c.getColumnIndex("ac_id"));
            String FET_AC_NAME = c.getString(c.getColumnIndex("ac_name"));
            String FET_ADDRESS = c.getString(c.getColumnIndex("address"));
            String FET_MOBILE = c.getString(c.getColumnIndex("phone"));
            String FET_ACTIVE = c.getString(c.getColumnIndex("active"));

            PartyModel partyModel = new PartyModel(FET_ACID,FET_AC_NAME,FET_ADDRESS,FET_MOBILE,FET_ACTIVE);
            PARTY.add(partyModel);
        }
        PARTY_ADAPTER = new PartyAdapter(PartyMaster.this,PARTY);
        PARTY_RECYCLER.setAdapter(PARTY_ADAPTER);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetch();
    }
    @Override
    public void onBackPressed() {
        if (!isDialogShowing && !isExitConfirmed) {
            isDialogShowing = true;

            // Create an AlertDialog to confirm exit
            androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(this);
            adb.setTitle("Confirm Exit");
            adb.setMessage("Are you sure you want to exit?");
            adb.setCancelable(false);
            adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    isDialogShowing = false;
                    isExitConfirmed = true; // Set the flag to indicate exit is confirmed
                    finish();
                }
            });
            adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    isDialogShowing = false;
                }
            });
            androidx.appcompat.app.AlertDialog alertDialog = adb.create();
            alertDialog.show();
        }
    }
}