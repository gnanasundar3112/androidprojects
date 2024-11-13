package com.sundar.i_macbankers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.google.android.material.textview.MaterialTextView;
import com.sundar.i_macbankers.Adapter.CateAdapter;
import com.sundar.i_macbankers.Adapter.LoanAdapter;
import com.sundar.i_macbankers.Models.CateModel;
import com.sundar.i_macbankers.Models.LoanModels;
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

public class Loan extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private ImageView BACK_PRESS,ADD_BTN;
    private TextView APPBAR_TITLE;
    private TextView SELECT_CUST_NAME;
    private TextView LOAN_DATE;
    private TextView SELECT_PROD_NAME;
    private TextView GRADE_NAME;
    private EditText LOAN_NO,RATE,QTY,WEIGHT,WAST_WEIGHT,NET_WEIGHT,AMOUNT,TOTAL_AMT,SAN_AMT,INT_PER,INT_AMT;
    private MaterialButton T_SAVE,NEXT;
    private Calendar CALENDAR;
    private Dialog DIALOG;
    private String SELECT_CUST_ID  = "",SELECT_PROD_ID = "",GRADE_ID="";
    private ArrayAdapter<String> adapter;
    private MaterialButton SAVE,CANCEL;

    private RecyclerView LOAN_RECYCLER;
    private RecyclerView.LayoutManager LOAN_MANAGER;
    private List<LoanModels> LOAN;
    private LoanAdapter LOAN_ADAPTER;

    private String fetch_loan_no_url,customer_url,product_url,grade_url,grade_rate_url,insert_url,fetch_url,loan_no_to_fetch_url,final_save_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);

        fetch_loan_no_url = Links.loan_No;
        customer_url = Links.customer_fetch;
        product_url = Links.product_fetch;
        grade_url = Links.GradeFetch;
        grade_rate_url = Links.gradeRateFetch;
        insert_url = Links.loan_insert;
        fetch_url = Links.loan_fetch;
        loan_no_to_fetch_url = Links.loan_no_to_fetch;
        final_save_url = Links.final_save;

        ADD_BTN = findViewById(R.id.add_btn);
        APPBAR_TITLE = findViewById(R.id.appbarTitle);
        APPBAR_TITLE.setText("Loan Entry");
        //back press activity
        BACK_PRESS = findViewById(R.id.backPress);
        BACK_PRESS.setOnClickListener(view -> Loan.super.onBackPressed());
        TOTAL_AMT = findViewById(R.id.total_amt);
        SAN_AMT = findViewById(R.id.san_amt);
        INT_PER = findViewById(R.id.int_per);
        INT_AMT = findViewById(R.id.int_amt);

        SELECT_CUST_NAME = findViewById(R.id.loan_cust_name);

        SELECT_CUST_NAME.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerFetch();
            }
        });

        LOAN_NO = findViewById(R.id.loan_no);
        LOAN_DATE = findViewById(R.id.loan_date);
        CALENDAR = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = dateFormat.format(new Date());
        LOAN_DATE.setText(currentDate);

        // fetch  start
        LOAN_RECYCLER = findViewById(R.id.loan_recyclerView);
        LOAN_MANAGER = new GridLayoutManager(Loan.this, 1);
        LOAN_RECYCLER.setLayoutManager(LOAN_MANAGER);
        LOAN = new ArrayList<>();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                CALENDAR.set(Calendar.YEAR,year);
                CALENDAR.set(Calendar.MONTH,month);
                CALENDAR.set(Calendar.DAY_OF_MONTH,day);
                getFromDate();
            }
        };
        LOAN_DATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Loan.this, date, CALENDAR.get(Calendar.YEAR),
                        CALENDAR.get(Calendar.MONTH),
                        CALENDAR.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        LOAN_NO.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loanNumberToFetch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        SAN_AMT.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String san_amt = SAN_AMT.getText().toString().trim();
                if (hasFocus) {
                    if (san_amt.equals("")||san_amt.equals("0.0")) {
                        ((EditText) v).setText("");
                    }
                }
            }
        });

        INT_PER.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String int_per = INT_PER.getText().toString().trim();
                if (hasFocus) {
                    if (int_per.equals("")||int_per.equals("0.0")) {
                        ((EditText) v).setText("");
                    }
                }
            }
        });



        T_SAVE = findViewById(R.id.loan_save);
        NEXT = findViewById(R.id.loan_next);
        T_SAVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalSave();
            }
        });
        NEXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loanNo();
            }
        });
        ADD_BTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                String customer = SELECT_CUST_ID;
                if (customer.isEmpty()) {
                    Toast.makeText(Loan.this, "Please Select Customer Name", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Loan.this, R.style.AlertDialogTheme);
                    View view = LayoutInflater.from(Loan.this).inflate(R.layout.loan_dialog,
                            (LinearLayout) findViewById(R.id.loan_dialog));
                    builder.setView(view);

                    final AlertDialog alertDialog = builder.create();

                    ((TextView) view.findViewById(R.id.loan_title)).setText("Add New Loan");

                    SELECT_PROD_NAME = view.findViewById(R.id.loan_dia_prod_name);
                    GRADE_NAME = view.findViewById(R.id.loan_dia_grade_name);
                    RATE = view.findViewById(R.id.dia_rate);
                    QTY = view.findViewById(R.id.dia_qty);
                    WEIGHT = view.findViewById(R.id.dia_weight);
                    WAST_WEIGHT = view.findViewById(R.id.dia_wast_weight);
                    NET_WEIGHT = view.findViewById(R.id.dia_net_weight);
                    AMOUNT = view.findViewById(R.id.dia_amount);

                    SAVE = view.findViewById(R.id.dialog_loan_save);
                    CANCEL = view.findViewById(R.id.dialog_loan_cancel);

                    WAST_WEIGHT.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            String Wast_Weight = WAST_WEIGHT.getText().toString().trim();
                            if (hasFocus) {
                                if (Wast_Weight.equals("")||Wast_Weight.equals("0")) {
                                    ((EditText) v).setText("");
                                }
                            }
                        }
                    });


                    SELECT_PROD_NAME.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            productFetch();
                        }
                    });
                    GRADE_NAME.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String product = SELECT_PROD_ID;
                            if (product.isEmpty()) {
                                Toast.makeText(Loan.this, "Please Select Product Name", Toast.LENGTH_SHORT).show();
                            } else {
                                GradeFetch();
                            }
                        }
                    });

                    WEIGHT.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            // Not needed
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            double Amount = 0;
                            if (!TextUtils.isEmpty(s)) { // Check if input is not empty
                                try {
                                    double weight = Double.parseDouble(s.toString());
                                    NET_WEIGHT.setText(String.valueOf(weight));


                                    double rate = Double.valueOf(RATE.getText().toString());
                                    Amount = weight * rate;
                                    AMOUNT.setText(String.valueOf(Amount));

                                } catch (NumberFormatException e) {
                                    e.printStackTrace(); // Handle the exception
                                }
                            }else {
                                AMOUNT.setText("");
                                NET_WEIGHT.setText(s.toString());
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            // Not needed
                        }
                    });

                    WAST_WEIGHT.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            // Not needed
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            double netWeight = 0, amount = 0;

                            if (!TextUtils.isEmpty(s)) { // Check if input is not empty
                                try {
                                    double wastWeight = Double.parseDouble(s.toString());
                                    double weight = Double.parseDouble(WEIGHT.getText().toString());
                                    netWeight = weight - wastWeight;

                                    double rate = Double.parseDouble(RATE.getText().toString());
                                    amount = netWeight * rate;
                                } catch (NumberFormatException e) {
                                    e.printStackTrace(); // Handle the exception
                                }
                            } else {
                                try {
                                    double rate = Double.parseDouble(RATE.getText().toString());
                                    double weight = Double.parseDouble(WEIGHT.getText().toString());
                                    amount = weight * rate;
                                    netWeight = weight;
                                } catch (NumberFormatException e) {
                                    e.printStackTrace(); // Handle the exception
                                }
                            }

                            // Format netWeight to two decimal places
                            String formattedNetWeight = String.format("%.2f", netWeight);
                            NET_WEIGHT.setText(formattedNetWeight);

                            AMOUNT.setText(String.format("%.2f", amount));
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            // Not needed
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
        });

        INT_PER.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    double int_per = Double.parseDouble(s.toString());
                    double san_amt = Double.parseDouble(SAN_AMT.getText().toString());
                    double int_amt = san_amt * int_per / 100;
                    INT_AMT.setText(String.valueOf(int_amt));
                } catch (NumberFormatException e) {
                    INT_AMT.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
    private void getFromDate() {
        String DateFormat = "dd-MM-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat, Locale.US);
        LOAN_DATE.setText(dateFormat.format(CALENDAR.getTime()));
    }

    public void loanNo(){
        SELECT_CUST_NAME.setText("");
        SELECT_CUST_ID="";

        StringRequest request = new StringRequest(Request.Method.POST, fetch_loan_no_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("[]")){
                            LOAN_NO.setText("000001");
                        }else {

                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String loan_no = object.getString("loan_no");

                                    LOAN_NO.setText(loan_no);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Loan.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Loan.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Loan.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    public void customerFetch(){
        DIALOG = new Dialog(Loan.this);
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

        HashMap<String, String> CUST_NAME = new HashMap<>();

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

                                CUST_NAME.put(Cust_Id, Cust_Name);
                            }

                            List<String> CUSTOMER = new ArrayList<>(CUST_NAME.values());
                            adapter = new ArrayAdapter<>(Loan.this, android.R.layout.simple_list_item_1, CUSTOMER);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Loan.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Loan.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Loan.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                String selectedCustName = adapter.getItem(position);

                // Find the key corresponding to the selected value
                String selectedCustId = null;

                for (Map.Entry<String, String> entry : CUST_NAME.entrySet()) {
                    if (entry.getValue().equals(selectedCustName)) {
                        selectedCustId = entry.getKey();
                        break;
                    }
                }

                if (selectedCustId != null) {
                    SELECT_CUST_NAME.setText(selectedCustName);
                    SELECT_CUST_ID = selectedCustId;
                }

                DIALOG.dismiss();
            }
        });
    }
    public void productFetch(){
        DIALOG = new Dialog(Loan.this);
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
        Tittle.setText("Select Product");
        EditText editText = DIALOG.findViewById(R.id.spinner_search);
        ListView listView = DIALOG.findViewById(R.id.spinner_list);

        HashMap<String, String> PROD_NAME = new HashMap<>();

        StringRequest request = new StringRequest(Request.Method.POST, product_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                String prod_id = object.getString("prod_id");
                                String prod_name = object.getString("prod_name");

                                PROD_NAME.put(prod_id, prod_name);
                            }

                            List<String> PRODUCT = new ArrayList<>(PROD_NAME.values());
                            adapter = new ArrayAdapter<>(Loan.this, android.R.layout.simple_list_item_1, PRODUCT);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Loan.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Loan.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Loan.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                String selectedProdName = adapter.getItem(position);

                // Find the key corresponding to the selected value
                String selectedProdId = null;

                for (Map.Entry<String, String> entry : PROD_NAME.entrySet()) {
                    if (entry.getValue().equals(selectedProdName)) {
                        selectedProdId = entry.getKey();
                        break;
                    }
                }

                if (selectedProdId != null) {
                    SELECT_PROD_NAME.setText(selectedProdName);
                    SELECT_PROD_ID = selectedProdId;
                }

                DIALOG.dismiss();
            }
        });

    }
    public void GradeFetch(){
        DIALOG = new Dialog(Loan.this);
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
        Tittle.setText("Select Grade");
        EditText editText = DIALOG.findViewById(R.id.spinner_search);
        ListView listView = DIALOG.findViewById(R.id.spinner_list);

        HashMap<String, String> GRADENAME = new HashMap<>();

        StringRequest request = new StringRequest(Request.Method.POST, grade_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                String grade_id = object.getString("grade_id");
                                String grade_name = object.getString("grade_name");

                                GRADENAME.put(grade_id, grade_name);
                            }

                            List<String> category = new ArrayList<>(GRADENAME.values());
                            adapter = new ArrayAdapter<>(Loan.this, android.R.layout.simple_list_item_1, category);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Loan.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Loan.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Loan.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                String selectedGradeName = adapter.getItem(position);

                // Find the key corresponding to the selected value
                String selectedGradeId = null;

                for (Map.Entry<String, String> entry : GRADENAME.entrySet()) {
                    if (entry.getValue().equals(selectedGradeName)) {
                        selectedGradeId = entry.getKey();
                        break;
                    }
                }

                if (selectedGradeId != null) {
                    // Use selectedPartyId and selectedPartyName as neede
                    GRADE_NAME.setText(selectedGradeName);
                    GRADE_ID = selectedGradeId;
                }

                gradeRate(GRADE_ID);

                DIALOG.dismiss();
            }
        });
    }
    public void gradeRate(String GRADE_ID) {
        String Loan_Date = LOAN_DATE.getText().toString();
        StringRequest request = new StringRequest(Request.Method.POST, grade_rate_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("[]")) {
                            RATE.setText("0.00");
                        } else {
                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String rate = object.getString("rate").trim();

                                    RATE.setText(rate);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Loan.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Loan.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Loan.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("loan_date", Loan_Date);
                params.put("grade_id",GRADE_ID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    public void insert(){
        SharedPreferences sharedPreferences = getSharedPreferences();
        String userName = sharedPreferences.getString("user_name", "");

        String loan_no = LOAN_NO.getText().toString();
        String loan_date = LOAN_DATE.getText().toString();
        String cust_id = SELECT_CUST_ID;
        String prod_id = SELECT_PROD_ID;
        String grade_id = GRADE_ID;
        String rate = RATE.getText().toString();
        String qty = QTY.getText().toString();
        String weight = WEIGHT.getText().toString();
        String waste_weight = WAST_WEIGHT.getText().toString();
        String net_weight = NET_WEIGHT.getText().toString().trim();
        String amount = AMOUNT.getText().toString();

        if (loan_no.isEmpty()) {
            Toast.makeText(this, "Pleas Enter loan no", Toast.LENGTH_SHORT).show();
        }else if (loan_date.isEmpty()) {
            Toast.makeText(this, "Pleas Enter loan date", Toast.LENGTH_SHORT).show();
        }else if (cust_id.isEmpty()) {
            Toast.makeText(this, "Pleas Enter customer Name", Toast.LENGTH_SHORT).show();
        }else if (prod_id.isEmpty()) {
            Toast.makeText(this, "Pleas Enter product Name", Toast.LENGTH_SHORT).show();
        }else if (grade_id.isEmpty()) {
            Toast.makeText(this, "Pleas Enter grade Name", Toast.LENGTH_SHORT).show();
        }else if (rate.isEmpty()) {
            Toast.makeText(this, "Pleas Enter rate", Toast.LENGTH_SHORT).show();
        }else if (qty.isEmpty()) {
            Toast.makeText(this, "Pleas Enter qty", Toast.LENGTH_SHORT).show();
        }else if (weight.isEmpty()) {
            Toast.makeText(this, "Pleas Enter weight", Toast.LENGTH_SHORT).show();
        }else if (net_weight.isEmpty()) {
            Toast.makeText(this, "Pleas Enter waste weight", Toast.LENGTH_SHORT).show();
        }else if (amount.isEmpty()) {
            Toast.makeText(this, "Pleas Enter amount", Toast.LENGTH_SHORT).show();
        }else {

            StringRequest request = new StringRequest(Request.Method.POST,insert_url ,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("Inserted Successfully")){
                                fetch();
                                SELECT_PROD_ID = "";
                                SELECT_PROD_NAME.setText("");
                                GRADE_ID = "";
                                GRADE_NAME.setText("");
                                RATE.setText("");
                                QTY.setText("");
                                WEIGHT.setText("");
                                WAST_WEIGHT.setText("");
                                NET_WEIGHT.setText("0.00");
                                AMOUNT.setText("0.00");

                                Toast.makeText(Loan.this, "Inserted Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(Loan.this, "Inserted Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                        Toast.makeText(Loan.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("loan_no", loan_no);
                    params.put("loan_date", loan_date);
                    params.put("cust_id", cust_id);
                    params.put("prod_id",prod_id);
                    params.put("grade_id", grade_id);
                    params.put("rate", rate);
                    params.put("qty", qty);
                    params.put("weight",weight);
                    params.put("waste_weight", waste_weight);
                    params.put("net_weight", net_weight);
                    params.put("amount", amount);
                    params.put("userName",userName);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(Loan.this);
            requestQueue.add(request);
        }
    }

    public void fetch() {
        String loan_no = LOAN_NO.getText().toString();
        StringRequest request = new StringRequest(Request.Method.POST, fetch_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            LOAN.clear();

                            double total_amount = 0;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String loan_no = object.getString("loan_no");
                                String loan_date = object.getString("loan_date");
                                String cust_id = object.getString("cust_id");
                                String cust_name = object.getString("cust_name");
                                String serial = object.getString("serial");
                                String prod_id = object.getString("prod_id");
                                String prod_name = object.getString("prod_name");
                                String grade_id = object.getString("grade_id");
                                String grade_name = object.getString("grade_name");
                                double rate = object.getDouble("rate");
                                String qty = object.getString("qty");
                                String weight = object.getString("weight");
                                String wst_wt = object.getString("wst_wt");
                                String net_wt = object.getString("net_wt");
                                double amount = object.getDouble("amount");

                                total_amount += amount;

                                LoanModels loanModels = new LoanModels(loan_no,loan_date,cust_id,cust_name,serial,prod_id,prod_name,grade_id,grade_name,qty,weight,wst_wt,net_wt,rate,amount);
                                LOAN.add(loanModels);
                            }

                            TOTAL_AMT.setText(String.valueOf(total_amount));

                            LOAN_ADAPTER = new LoanAdapter(Loan.this, LOAN);
                            LOAN_RECYCLER.setAdapter(LOAN_ADAPTER);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Loan.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Loan.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Loan.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("loan_no", loan_no);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    public void loanNumberToFetch(String loan_no) {
        StringRequest request = new StringRequest(Request.Method.POST, loan_no_to_fetch_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            LOAN.clear();
                            double total_amount = 0,san_amt = 0,int_per = 0,int_amt = 0;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String loan_no = object.getString("loan_no");
                                String loan_date = object.getString("loan_date");
                                String cust_id = object.getString("cust_id");
                                String cust_name = object.getString("cust_name");
                                String serial = object.getString("serial");
                                String prod_id = object.getString("prod_id");
                                String prod_name = object.getString("prod_name");
                                String grade_id = object.getString("grade_id");
                                String grade_name = object.getString("grade_name");
                                double rate = object.getDouble("rate");
                                String qty = object.getString("qty");
                                String weight = object.getString("weight");
                                String wst_wt = object.getString("wst_wt");
                                String net_wt = object.getString("net_wt");
                                double amount = object.getDouble("amount");
                                double tot_amt = object.getDouble("tot_amt");
                                double sanc_amt = object.getDouble("sanc_amt");
                                double intper = object.getDouble("int_per");
                                double intamt = object.getDouble("int_amt");

                                if (tot_amt == 0.0 || tot_amt == 0.00) {
                                    total_amount += amount;
                                }else {
                                    total_amount = tot_amt;
                                }
                                san_amt = sanc_amt;
                                int_per = intper;
                                int_amt = intamt;

                                LoanModels loanModels = new LoanModels(loan_no,loan_date,cust_id,cust_name,serial,prod_id,prod_name,grade_id,grade_name,qty,weight,wst_wt,net_wt,rate,amount);
                                LOAN.add(loanModels);

                                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                String Loan_Date = null;

                                try {
                                    Date loandate = originalFormat.parse(loan_date);
                                    SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                    Loan_Date = newFormat.format(loandate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                SELECT_CUST_ID = cust_id;
                                SELECT_CUST_NAME.setText(cust_name);
                                LOAN_DATE.setText(Loan_Date);
                            }
                            TOTAL_AMT.setText(String.valueOf(total_amount));
                            SAN_AMT.setText(String.valueOf(san_amt));
                            INT_PER.setText(String.valueOf(int_per));
                            INT_AMT.setText(String.valueOf(int_amt));

                            LOAN_ADAPTER = new LoanAdapter(Loan.this, LOAN);
                            LOAN_RECYCLER.setAdapter(LOAN_ADAPTER);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Loan.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Loan.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Loan.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("loan_no", loan_no);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    public void finalSave(){
        String loan_no = LOAN_NO.getText().toString();
        String total_amt = TOTAL_AMT.getText().toString();
        String san_amt = SAN_AMT.getText().toString();
        String int_per = INT_PER.getText().toString();
        String int_amt = INT_AMT.getText().toString();

        if (loan_no.isEmpty()) {
            Toast.makeText(this, "Pleas Enter loan no", Toast.LENGTH_SHORT).show();
        }else if (total_amt.isEmpty()) {
            Toast.makeText(this, "Pleas Enter total amount", Toast.LENGTH_SHORT).show();
        }else if (san_amt.isEmpty()) {
            Toast.makeText(this, "Pleas Enter sanction amount", Toast.LENGTH_SHORT).show();
        }if (int_per.isEmpty()) {
            Toast.makeText(this, "Pleas Enter Interest %", Toast.LENGTH_SHORT).show();
        }if (int_amt.isEmpty()) {
            Toast.makeText(this, "Pleas Enter Interest amount", Toast.LENGTH_SHORT).show();
        }else {

            StringRequest request = new StringRequest(Request.Method.POST, final_save_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("Updated Successfully")){
                                loanNo();
                                fetch();
                                SELECT_CUST_NAME.setText("");
                                SELECT_CUST_ID="";
                                INT_PER.setText("");
                                INT_AMT.setText("");
                                Toast.makeText(Loan.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(Loan.this, "Update Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                        Toast.makeText(Loan.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("loan_no", loan_no);
                    params.put("total_amt", total_amt);
                    params.put("san_amt", san_amt);
                    params.put("int_per", int_per);
                    params.put("int_amt", int_amt);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(Loan.this);
            requestQueue.add(request);
        }

    }

    public SharedPreferences getSharedPreferences() {
        // Retrieve SharedPreferences instance
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MainActivity.MODE_PRIVATE);
        // Return SharedPreferences instance
        return sharedPreferences;
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);

        loanNo();
        fetch();
    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }

}