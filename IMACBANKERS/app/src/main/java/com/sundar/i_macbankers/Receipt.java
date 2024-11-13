package com.sundar.i_macbankers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
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
import com.google.android.material.textfield.TextInputEditText;
import com.sundar.i_macbankers.Adapter.CateAdapter;
import com.sundar.i_macbankers.Adapter.ReceiptAdapter;
import com.sundar.i_macbankers.Models.CateModel;
import com.sundar.i_macbankers.Models.ReceiptModel;
import com.sundar.i_macbankers.Models.selectBoxModel;
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

public class Receipt extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private ImageView BACK_PRESS,ADD_BTN,MORE;
    private TextView APPBAR_TITLE,SELECT_CUST_NAME,LOAN_NO;
    private TextInputEditText RECEIPT_DATE,INTEREST_AMT,BALANCE_AMT;
    private String INT_AMT;
    private Dialog DIALOG;
    private String SELECT_CUST_ID  = "",STATUS = "";
    private ArrayAdapter<selectBoxModel> adapter,loan_adapter;

    private MaterialButton SAVE,CANCEL;
    private Calendar CALENDAR;

    private RecyclerView RECEIPT_RECYCLER;
    private RecyclerView.LayoutManager RECEIPT_MANAGER;
    private List<ReceiptModel> RECEIPT;
    private ReceiptAdapter RECEIPT_ADAPTER;

    String customer_url,fetch_loan_no_url,insert_url,fetch_url,loan_close;

    private  AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        customer_url = Links.cust_fetch;
        fetch_loan_no_url = Links.fetch_loan_no;
        insert_url = Links.receipt_insert;
        fetch_url = Links.receipt_fetch;
        loan_close = Links.loan_close;

        CALENDAR = Calendar.getInstance();

        MORE = findViewById(R.id.more);
        ADD_BTN = findViewById(R.id.add_btn);
        APPBAR_TITLE = findViewById(R.id.appbarTitle);
        APPBAR_TITLE.setText("Receipt Entry");
        //back press activity
        BACK_PRESS = findViewById(R.id.backPress);
        BACK_PRESS.setOnClickListener(view -> Receipt.super.onBackPressed());


        MORE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = STATUS;
                if (status.equals("0")) {
                    Toast.makeText(Receipt.this, "Loan no already closed", Toast.LENGTH_SHORT).show();
                } else {
                    showPopupMenu(v);
                }
            }
        });

        // fetch  start
        RECEIPT_RECYCLER = findViewById(R.id.receipt_recyclerView);
        RECEIPT_MANAGER = new GridLayoutManager(Receipt.this, 1);
        RECEIPT_RECYCLER.setLayoutManager(RECEIPT_MANAGER);
        RECEIPT = new ArrayList<>();

        SELECT_CUST_NAME = findViewById(R.id.rece_cust_name);
        LOAN_NO = findViewById(R.id.rece_loan_no);

        SELECT_CUST_NAME.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerFetch();
            }
        });

        LOAN_NO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loanNoFetch();
            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                CALENDAR.set(Calendar.YEAR,year);
                CALENDAR.set(Calendar.MONTH,month);
                CALENDAR.set(Calendar.DAY_OF_MONTH,day);
                getFromDate();
            }
        };

        ADD_BTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                String status = STATUS;
                if (status.equals("0")) {
                    Toast.makeText(Receipt.this, "Loan no already closed", Toast.LENGTH_SHORT).show();
                } else {
                    String customer = SELECT_CUST_ID;
                    String loan_no = LOAN_NO.getText().toString();
                    if (customer.isEmpty()) {
                        Toast.makeText(Receipt.this, "Please Select Customer Name", Toast.LENGTH_SHORT).show();
                    } else if (loan_no.isEmpty()) {
                        Toast.makeText(Receipt.this, "Please Select Loan No", Toast.LENGTH_SHORT).show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Receipt.this, R.style.AlertDialogTheme);
                        View view = LayoutInflater.from(Receipt.this).inflate(R.layout.receipt_dialog,
                                (LinearLayout) findViewById(R.id.receipt_dialog));
                        builder.setView(view);

                        alertDialog = builder.create();

                        ((TextView) view.findViewById(R.id.dialog_title)).setText("Add New Receipt");

                        RECEIPT_DATE = view.findViewById(R.id.dia_receipt_date);
                        INTEREST_AMT = view.findViewById(R.id.dia_receipt_interest_amt);

                        INTEREST_AMT.setText(INT_AMT);

                        SAVE = view.findViewById(R.id.dialog_receipt_save);
                        CANCEL = view.findViewById(R.id.dialog_receipt_cancel);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        String currentDate = dateFormat.format(new Date());
                        RECEIPT_DATE.setText(currentDate);

                        RECEIPT_DATE.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new DatePickerDialog(Receipt.this, date, CALENDAR.get(Calendar.YEAR),
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
                }
            }
        });
    }
    public void customerFetch(){
        DIALOG = new Dialog(Receipt.this);
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
        Tittle.setText("Select Customer");
        EditText editText = DIALOG.findViewById(R.id.spinner_search);
        ListView listView = DIALOG.findViewById(R.id.spinner_list);

        List<selectBoxModel> customerList = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.POST, customer_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String Cust_Id = object.getString("ac_id");
                                String Cust_Name = object.getString("ac_name");

                                customerList.add(new selectBoxModel(Cust_Id, Cust_Name));
                            }

                            LOAN_NO.setText("");
                            adapter = new ArrayAdapter<>(Receipt.this, android.R.layout.simple_list_item_1, customerList);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Receipt.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Receipt.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Receipt.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("Range")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectBoxModel selectedCustomer = adapter.getItem(position);

                if (selectedCustomer != null) {
                    SELECT_CUST_NAME.setText(selectedCustomer.getName());
                    SELECT_CUST_ID = selectedCustomer.getId();
                }
                fetch();
                DIALOG.dismiss();
            }
        });
    }
    public void loanNoFetch(){

        DIALOG = new Dialog(Receipt.this);
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
        Tittle.setText("Select Customer");
        EditText editText = DIALOG.findViewById(R.id.spinner_search);
        ListView listView = DIALOG.findViewById(R.id.spinner_list);

        List<selectBoxModel> loanList = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.POST, fetch_loan_no_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                String loan_no = object.getString("loan_no");
                                String status = object.getString("status");
                                String int_amt = object.getString("int_amt");

                                INT_AMT = int_amt;
                                loanList.add(new selectBoxModel(status,loan_no));

                            }
                            loan_adapter = new ArrayAdapter<>(Receipt.this, android.R.layout.simple_list_item_1, loanList);
                            listView.setAdapter(loan_adapter);
                            loan_adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Receipt.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Receipt.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Receipt.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cust_id", SELECT_CUST_ID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loan_adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("Range")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectBoxModel loan = loan_adapter.getItem(position);

                if (loan != null) {
                    LOAN_NO.setText(loan.getName());
                    STATUS = loan.getId();
                }
                fetch();

                DIALOG.dismiss();
            }
        });
    }

    public void insert(){
        SharedPreferences sharedPreferences = getSharedPreferences();
        String userName = sharedPreferences.getString("user_name", "");

        String cust_id = SELECT_CUST_ID;
        String loan_no = LOAN_NO.getText().toString();
        String Receipt_date = RECEIPT_DATE.getText().toString().trim();
        String Interest_amt = INTEREST_AMT.getText().toString().trim();

        if (Interest_amt.isEmpty()) {
            Toast.makeText(this, "Pleas Enter Interest amount", Toast.LENGTH_SHORT).show();
        }else {

            StringRequest request = new StringRequest(Request.Method.POST, insert_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("Inserted Successfully")){
                                INTEREST_AMT.setText("");
                                fetch();
                                alertDialog.dismiss();
                                Toast.makeText(Receipt.this, "Inserted Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(Receipt.this, "Inserted Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                        Toast.makeText(Receipt.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cust_id", cust_id);
                    params.put("loan_no", loan_no);
                    params.put("recep_date", Receipt_date);
                    params.put("inter_amt", Interest_amt);
                    params.put("userName",userName);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(Receipt.this);
            requestQueue.add(request);
        }
    }

    public void fetch() {
        String cust_id = SELECT_CUST_ID;
        String loan_no = LOAN_NO.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST, fetch_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            RECEIPT.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String cust_id = object.getString("cust_id");
                                String loan_no = object.getString("loan_no");
                                String serial = object.getString("serial");
                                String recep_date = object.getString("recep_date");
                                double inter_amt = object.getDouble("inter_amt");

                                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                String updateDate = null;

                                try {
                                    Date date = originalFormat.parse(recep_date);
                                    SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                    updateDate = newFormat.format(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                ReceiptModel receiptModel = new ReceiptModel(cust_id,loan_no,serial,updateDate,inter_amt);
                                RECEIPT.add(receiptModel);
                            }
                            RECEIPT_ADAPTER = new ReceiptAdapter(Receipt.this, RECEIPT);
                            RECEIPT_RECYCLER.setAdapter(RECEIPT_ADAPTER);

                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(Receipt.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Receipt.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Receipt.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("cust_id", cust_id);
                params.put("loan_no", loan_no);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


    public SharedPreferences getSharedPreferences() {
        // Retrieve SharedPreferences instance
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MainActivity.MODE_PRIVATE);
        // Return SharedPreferences instance
        return sharedPreferences;
    }
    private void getFromDate() {
        String DateFormat = "dd-MM-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat, Locale.US);
        RECEIPT_DATE.setText(dateFormat.format(CALENDAR.getTime()));
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.option_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return handleMenuItemClick(item);
            }
        });
        popup.show();
    }

    @SuppressLint("NonConstantResourceId")
    private boolean handleMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.opt_loan_close) {
            loanClose();
            return true;
        } else {
            return false;
        }
    }

    public void loanClose(){
        SharedPreferences sharedPreferences = getSharedPreferences();
        String userName = sharedPreferences.getString("user_name", "");

        String loan_no = LOAN_NO.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST, loan_close,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("Updated Successfully")){
                            Toast.makeText(Receipt.this, "Loan closed Successfully", Toast.LENGTH_SHORT).show();
                            customerFetch();
                            fetch();

                        }
                        else {
                            Toast.makeText(Receipt.this, "Loan closed Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Receipt.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("loan_no", loan_no);
                params.put("userName",userName);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Receipt.this);
        requestQueue.add(request);
    }
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);

        fetch();
    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }

}