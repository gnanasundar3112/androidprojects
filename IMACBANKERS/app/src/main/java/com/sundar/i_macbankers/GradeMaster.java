package com.sundar.i_macbankers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.sundar.i_macbankers.Adapter.CateAdapter;
import com.sundar.i_macbankers.Adapter.GradeAdapter;
import com.sundar.i_macbankers.Models.CateModel;
import com.sundar.i_macbankers.Models.GradeModel;
import com.sundar.i_macbankers.internet.NetworkChangeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradeMaster extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private ImageView BACK_PRESS,ADD_BTN;
    private TextView APPBAR_TITLE;
    private TextInputEditText GRADE_NAME;
    private MaterialTextView LABEL_NAME;
    private Spinner ACTIVE;
    private ArrayList<String> active_list = new ArrayList<>();
    private ArrayAdapter<String> active_adapter;

    private MaterialButton SAVE,CANCEL;
    private RecyclerView GRADE_RECYCLER;
    private RecyclerView.LayoutManager GRADE_MANAGER;
    private List<GradeModel> GRADE;
    private GradeAdapter GRADE_ADAPTER;
    private LinearLayout APPBAR;
    String insert_url,fetch_url;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_master);

        insert_url = Links.grade_insert;
        fetch_url = Links.grade_fetch;

        ADD_BTN = findViewById(R.id.add_btn);
        APPBAR_TITLE = findViewById(R.id.appbarTitle);
        APPBAR_TITLE.setText("Grade Master");
        APPBAR = findViewById(R.id.include);

        //back press activity
        BACK_PRESS = findViewById(R.id.backPress);
        BACK_PRESS.setOnClickListener(view -> GradeMaster.super.onBackPressed());

        // fetch  start
        GRADE_RECYCLER = findViewById(R.id.grade_recyclerView);
        GRADE_MANAGER = new GridLayoutManager(GradeMaster.this, 1);
        GRADE_RECYCLER.setLayoutManager(GRADE_MANAGER);
        GRADE = new ArrayList<>();

        ADD_BTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GradeMaster.this, R.style.AlertDialogTheme);
                View view = LayoutInflater.from(GradeMaster.this).inflate(R.layout.catedialog,
                        (LinearLayout)findViewById(R.id.cate_dialog));
                builder.setView(view);

                final AlertDialog alertDialog = builder.create();

                ((TextView) view.findViewById(R.id.dialog_title)).setText("Add New Grade");

                LABEL_NAME = view.findViewById(R.id.name_label);
                LABEL_NAME.setText("Grade Name");


                GRADE_NAME = view.findViewById(R.id.dia_cate_name);
                ACTIVE = view.findViewById(R.id.dia_cate_active);

                SAVE = view.findViewById(R.id.dialog_cate_save);
                CANCEL = view.findViewById(R.id.dialog_cate_cancel);

                active_adapter = new ArrayAdapter<>(GradeMaster.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.active));
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
    }
    @SuppressLint("Range")
    public void insert(){
        SharedPreferences sharedPreferences = getSharedPreferences();
        String userName = sharedPreferences.getString("user_name", "");

        String Grade_name = GRADE_NAME.getText().toString().trim();
        String Active = ACTIVE.getSelectedItem().toString();

        if (Active.equals("ENABLE")) {
            Active = "1";
        }else {
            Active = "2";
        }
        if (Grade_name.isEmpty()) {
            Toast.makeText(this, "Pleas Enter Grade Name", Toast.LENGTH_SHORT).show();
        }else {

            String finalActive = Active;

            StringRequest request = new StringRequest(Request.Method.POST, insert_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("Inserted Successfully")){
                                GRADE_NAME.setText("");
                                fetch();
                                Toast.makeText(GradeMaster.this, "Inserted Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(GradeMaster.this, "Inserted Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                        Toast.makeText(GradeMaster.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("grade_name", Grade_name.toUpperCase());
                    params.put("active", finalActive);
                    params.put("userName",userName);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(GradeMaster.this);
            requestQueue.add(request);
        }
    }

    public void fetch() {
        StringRequest request = new StringRequest(Request.Method.POST, fetch_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            GRADE.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String GRADE_ID = object.getString("grade_id");
                                String GRADE_NAME = object.getString("grade_name");
                                String ACTIVE = object.getString("active");
                                String CREA_DATE = object.getString("crea_date");
                                String CREA_TIME = object.getString("crea_time");
                                String MODI_DATE = object.getString("modi_date");
                                String MODI_TIME = object.getString("modi_time");

                                if (ACTIVE.equalsIgnoreCase("1")){
                                    ACTIVE="ENABLE";
                                }else {
                                    ACTIVE = "DISABLE";
                                }

                                GradeModel gradeModel = new GradeModel(GRADE_ID, GRADE_NAME, ACTIVE, CREA_DATE, CREA_TIME, MODI_DATE, MODI_TIME);
                                GRADE.add(gradeModel);
                            }
                            GRADE_ADAPTER = new GradeAdapter(GradeMaster.this, GRADE);
                            GRADE_RECYCLER.setAdapter(GRADE_ADAPTER);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(GradeMaster.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(GradeMaster.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GradeMaster.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
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