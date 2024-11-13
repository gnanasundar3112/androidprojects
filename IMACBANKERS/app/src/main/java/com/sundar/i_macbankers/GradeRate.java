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
import com.google.android.material.textview.MaterialTextView;
import com.sundar.i_macbankers.Adapter.CateAdapter;
import com.sundar.i_macbankers.Adapter.GradeRateAdapter;
import com.sundar.i_macbankers.Models.CateModel;
import com.sundar.i_macbankers.Models.GradeModel;
import com.sundar.i_macbankers.Models.GradeRateModel;
import com.sundar.i_macbankers.internet.NetworkChangeListener;

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

public class GradeRate extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private ImageView BACK_PRESS,ADD_BTN;
    private TextView APPBAR_TITLE,EFF_DATE,GRADE_NAME;
    private TextInputEditText RATE;
    private MaterialButton SAVE,CANCEL;
    private Calendar CALENDAR;
    private RecyclerView GRADE_RECYCLER;
    private RecyclerView.LayoutManager GRADE_MANAGER;
    private List<GradeRateModel> GRADE;
    private GradeRateAdapter GRADE_ADAPTER;
    ArrayAdapter<String> adapter;
    Dialog DIALOG;
    private String GRADE_ID;
    private AlertDialog alertDialog;
    String insert_url,fetch_url,fetch_grade_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_rate);

        fetch_grade_url = Links.gradeFetch;
        insert_url = Links.grade_rate_insert;
        fetch_url = Links.grade_rate_fetch;

        ADD_BTN = findViewById(R.id.add_btn);
        APPBAR_TITLE = findViewById(R.id.appbarTitle);
        APPBAR_TITLE.setText("Grade Rate");
        //back press activity
        BACK_PRESS = findViewById(R.id.backPress);
        BACK_PRESS.setOnClickListener(view -> GradeRate.super.onBackPressed());

        CALENDAR = Calendar.getInstance();
        EFF_DATE = findViewById(R.id.eff_date);

        // fetch  start
        GRADE_RECYCLER = findViewById(R.id.grade_recyclerView);
        GRADE_MANAGER = new GridLayoutManager(GradeRate.this, 1);
        GRADE_RECYCLER.setLayoutManager(GRADE_MANAGER);
        GRADE = new ArrayList<>();

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
        EFF_DATE.setText(currentDate);

        EFF_DATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(GradeRate.this, date, CALENDAR.get(Calendar.YEAR),
                        CALENDAR.get(Calendar.MONTH),
                        CALENDAR.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ADD_BTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                String eff_date = EFF_DATE.getText().toString().trim();

                if (eff_date.isEmpty()) {
                    Toast.makeText(GradeRate.this, "Please Select Party Name", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GradeRate.this, R.style.AlertDialogTheme);
                    View view = LayoutInflater.from(GradeRate.this).inflate(R.layout.grade_dialog,
                            (LinearLayout) findViewById(R.id.grade_dialog));
                    builder.setView(view);

                    alertDialog = builder.create();

                    ((TextView) view.findViewById(R.id.dialog_title)).setText("Add New Income Detail");

                    GRADE_NAME = view.findViewById(R.id.dia_grade_name);
                    RATE = view.findViewById(R.id.dia_grade_rate);

                    SAVE = view.findViewById(R.id.dialog_grade_save);
                    CANCEL = view.findViewById(R.id.dialog_grade_cancel);

                    GRADE_NAME.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GradeFetch();
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
    }
    public void GradeFetch(){
        DIALOG = new Dialog(GradeRate.this);
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
        Tittle.setText("Select Category");
        EditText editText = DIALOG.findViewById(R.id.spinner_search);
        ListView listView = DIALOG.findViewById(R.id.spinner_list);

        HashMap<String, String> GRADENAME = new HashMap<>();

        StringRequest request = new StringRequest(Request.Method.POST, fetch_grade_url,
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
                            adapter = new ArrayAdapter<>(GradeRate.this, android.R.layout.simple_list_item_1, category);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(GradeRate.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(GradeRate.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GradeRate.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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

                fetch();

                DIALOG.dismiss();
            }
        });
    }
    public void insert(){
        SharedPreferences sharedPreferences = getSharedPreferences();
        String userName = sharedPreferences.getString("user_name", "");

        String eff_date = EFF_DATE.getText().toString();
        String grade_name = GRADE_NAME.getText().toString().trim();
        String grade_id = GRADE_ID;
        String rate = RATE.getText().toString();

        if (grade_name.isEmpty()) {
            Toast.makeText(this, "Pleas Enter Category Name", Toast.LENGTH_SHORT).show();
        }else {

            StringRequest request = new StringRequest(Request.Method.POST, insert_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("Inserted Successfully")){
                                fetch();
                                Toast.makeText(GradeRate.this, "Inserted Successfully", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                            else {
                                Toast.makeText(GradeRate.this, "Inserted Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                        Toast.makeText(GradeRate.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("eff_date", eff_date);
                    params.put("grade_id", grade_id);
                    params.put("rate", rate);
                    params.put("userName",userName);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(GradeRate.this);
            requestQueue.add(request);
        }
    }
    public void fetch() {
        String eff_date = EFF_DATE.getText().toString();
        StringRequest request = new StringRequest(Request.Method.POST, fetch_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        GRADE.clear();

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String EFF_DATE = object.getString("eff_date");
                                String GRADE_ID = object.getString("grade_id");
                                String GRADE_NAME = object.getString("grade_name");
                                String RATE = object.getString("rate");

                                GradeRateModel gradeRateModel = new GradeRateModel(EFF_DATE,GRADE_ID,GRADE_NAME,RATE);
                                GRADE.add(gradeRateModel);
                            }
                            GRADE_ADAPTER = new GradeRateAdapter(GradeRate.this, GRADE);
                            GRADE_RECYCLER.setAdapter(GRADE_ADAPTER);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(GradeRate.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(GradeRate.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GradeRate.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("eff_date", eff_date);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void getFromDate() {
        String DateFormat = "dd-MM-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat, Locale.US);
        EFF_DATE.setText(dateFormat.format(CALENDAR.getTime()));
        fetch();
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

        fetch();
    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }
}