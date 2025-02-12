package com.example.gifts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gifts.Adapter.IncomeAdapter;
import com.example.gifts.Models.IncomeModel;
import com.example.gifts.SqlLight.SqlDataBaseHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class IncomOutActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    NavigationView navigationView;
    DrawerLayout drawer;
    public FloatingActionButton BURGER_ICON,ADD_BTN;
    public TextView APPBAR_TITLE,INCOME_TOTAL,OUT_TOTAL,OTHER_TOTAL;
    public TextInputEditText SEARCHVIEW,id,function,party_name,village,income,remarks,payable,other_amt;
    public ProgressBar progressBar;
    private RecyclerView INCOM_RECYCLER;
    private RecyclerView.LayoutManager INCOM_MANAGER;
    private List<IncomeModel> INCOMES;
    private IncomeAdapter Income_Adapter;
    private boolean isDialogShowing = false;
    private boolean isExitConfirmed = false;

    SQLiteDatabase db;
    private boolean isDataInserted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incom_out);

        ADD_BTN = findViewById(R.id.add_btn);
        BURGER_ICON = findViewById(R.id.burgericon);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);
        SEARCHVIEW = findViewById(R.id.searchView);
        INCOME_TOTAL = findViewById(R.id.in_total);
        OUT_TOTAL = findViewById(R.id.out_total);
        OTHER_TOTAL = findViewById(R.id.other_total);
        progressBar = findViewById(R.id.progressbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.side_navigation);
        navigationView.setNavigationItemSelectedListener(IncomOutActivity.this);

        APPBAR_TITLE.setText("FUNCTIONS GIFTS");
        //back press activity

        // products fetch  start
        INCOM_RECYCLER = findViewById(R.id.recyclerView);
        INCOM_MANAGER = new GridLayoutManager(IncomOutActivity.this, 1);
        INCOM_RECYCLER.setLayoutManager(INCOM_MANAGER);
        INCOMES = new ArrayList<>();

        SqlDataBaseHelper dataBaseHelper = new SqlDataBaseHelper(this);
        db = dataBaseHelper.getReadableDatabase();
        db = dataBaseHelper.getWritableDatabase();

        BURGER_ICON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
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
                List<IncomeModel> filteredList = new ArrayList<>();
                for (IncomeModel item : INCOMES) {
                    if (item.getParty_name().toLowerCase().contains(text.toLowerCase()) || item.getArea_name().toLowerCase().contains(text.toLowerCase())){
                        filteredList.add(item);
                    }
                }
                if (filteredList.isEmpty()) {
                    Toast.makeText(IncomOutActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                } else {
                    Income_Adapter.setFilteredList(filteredList);
                }
            }
        });
        /* filter from search bar End*/

        ADD_BTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(IncomOutActivity.this, R.style.AlertDialogTheme);
                View view1 = LayoutInflater.from(IncomOutActivity.this).inflate(R.layout.incomadddialog,
                        (LinearLayout)findViewById(R.id.income_dialog));
                builder.setView(view1);

                final AlertDialog alertDialog = builder.create();

                ((TextView) view1.findViewById(R.id.dialog_title)).setText("Add New Party");

                //final TextInputEditText id = view1.findViewById(R.id.add_dialog_name);
                function = view1.findViewById(R.id.dia_fun_name);
                party_name = view1.findViewById(R.id.dia_ac_name);
                village = view1.findViewById(R.id.dia_area_name);
                income = view1.findViewById(R.id.dia_in_amt);
                remarks = view1.findViewById(R.id.dia_remarks);
                payable = view1.findViewById(R.id.dia_out_amt);
                other_amt = view1.findViewById(R.id.dia_other_amt);

                view1.findViewById(R.id.dialog_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MaterialButton SAVE = (MaterialButton) view1.findViewById(R.id.dialog_save);

                        String PARTY_NAME = party_name.getText().toString();

                        if(PARTY_NAME.isEmpty()){
                            Toast.makeText(IncomOutActivity.this, "Please Enter the Person Name", Toast.LENGTH_SHORT).show();
                        }else {
                            insert();
                        }
                    }
                });

                view1.findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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

    @SuppressLint("Range")
    public void insert(){
        String FUNCTION = function.getText().toString();
        String PARTY_NAME = party_name.getText().toString();
        String VILLAGE = village.getText().toString();
        String INCOME = income.getText().toString();
        String REMARKS = remarks.getText().toString();
        String PAYABLE = payable.getText().toString();
        String OTHER_AMT =other_amt.getText().toString();

        if (INCOME.isEmpty()) {
            INCOME = "0.00";
        }
        if (PAYABLE.isEmpty()) {
            PAYABLE = "0.00";
        }
        if (OTHER_AMT.isEmpty()) {
            OTHER_AMT = "0.00";
        }
        String finalINCOME = INCOME;
        String finalPAYABLE = PAYABLE;
        String finalOTHER_AMT = OTHER_AMT;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String Date = dateFormat.format(new Date());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String time = timeFormat.format(new Date());

        if(PARTY_NAME.isEmpty()){
            Toast.makeText(IncomOutActivity.this, "Please Enter the Person Name", Toast.LENGTH_SHORT).show();
        }else {
            ContentValues values = new ContentValues();

            values.put("fun_id", generateFunId());
            values.put("fun_name", FUNCTION);
            values.put("party_name", PARTY_NAME);
            values.put("area_name", VILLAGE);
            values.put("in_amt", finalINCOME);
            values.put("remarks", REMARKS);
            values.put("out_amt", finalPAYABLE);
            values.put("other_amt", finalOTHER_AMT);
            values.put("crea_user", "");
            values.put("crea_date", Date);
            values.put("crea_time", time);
            values.put("crea_stat", "");
            values.put("modi_user", "");
            values.put("modi_date", Date);
            values.put("modi_time", time);
            values.put("modi_stat", "");

            long tableRow = db.insert("accounts", null, values);

            if (tableRow != -1) {
                Toast.makeText(this, "Inserted Successfully", Toast.LENGTH_SHORT).show();
                party_name.requestFocus();
                party_name.setText("");
                village.setText("");
                income.setText("");
                remarks.setText("");
                payable.setText("");
                other_amt.setText("");
                fetch();
            } else {
                Toast.makeText(this, "Inserted Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("Range")
    public void fetch() {
        Cursor c = db.rawQuery("SELECT fun_id,fun_name,party_name,area_name,in_amt,remarks,out_amt,other_amt FROM accounts", null);

        double incom_total = 0;
        double out_total = 0;
        double other_total=0;

        INCOMES.clear();
        while (c.moveToNext()) {
            String Fun_id = c.getString(c.getColumnIndex("fun_id"));
            String Fun_name = c.getString(c.getColumnIndex("fun_name")).toUpperCase();
            String Party_name = c.getString(c.getColumnIndex("party_name")).toUpperCase();
            String Area_name = c.getString(c.getColumnIndex("area_name")).toUpperCase();
            double In_amt = c.getDouble(c.getColumnIndex("in_amt"));
            String Remarks = c.getString(c.getColumnIndex("remarks")).toUpperCase();
            double Out_amt = c.getDouble(c.getColumnIndex("out_amt"));
            double Other_amt = c.getDouble(c.getColumnIndex("other_amt"));

            incom_total += In_amt;
            out_total += Out_amt;
            other_total += Other_amt;

            double total = incom_total;
            INCOME_TOTAL.setText("₹ "+String.format("%.2f",total));

            double Out_total = out_total;
            OUT_TOTAL.setText("₹ "+String.format("%.2f",Out_total));

            double Other_total = other_total;
            OTHER_TOTAL.setText("₹ "+String.format("%.2f",Other_total));

            IncomeModel incomeModels =new IncomeModel(Fun_id,Fun_name,Party_name,Area_name,Remarks,In_amt,Out_amt,Other_amt);
            INCOMES.add(incomeModels);
        }
        Income_Adapter = new IncomeAdapter(IncomOutActivity.this,INCOMES);
        INCOM_RECYCLER.setAdapter(Income_Adapter);
    }

    @SuppressLint("Range")
    public String generateFunId() {
        String mfunid = "00000001";
        String query = "SELECT fun_id FROM accounts ORDER BY fun_id DESC LIMIT 1";

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                String lastFunId = cursor.getString(cursor.getColumnIndex("fun_id"));
                int nextFunId = Integer.parseInt(lastFunId) + 1;
                mfunid = String.format("%08d", nextFunId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return mfunid;  // Return the generated fun_id
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_setting) {
            startActivity(new Intent(IncomOutActivity.this, Backup.class));
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onStart() {
        fetch();
        super.onStart();
    }
}