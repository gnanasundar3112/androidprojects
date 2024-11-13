package com.sundar.i_macbankers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
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
import com.sundar.i_macbankers.Adapter.CateAdapter;
import com.sundar.i_macbankers.Models.CateModel;
import com.sundar.i_macbankers.internet.NetworkChangeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CateMaster extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private ImageView BACK_PRESS,ADD_BTN;
    private TextView APPBAR_TITLE;
    private TextInputEditText CATE_NAME;
    private Spinner ACTIVE;
    private ArrayList<String> active_list = new ArrayList<>();
    private ArrayAdapter<String> active_adapter;

    private MaterialButton SAVE,CANCEL;
    private RecyclerView CATE_RECYCLER;
    private RecyclerView.LayoutManager CATE_MANAGER;
    private List<CateModel> CATE;
    private CateAdapter CATE_ADAPTER;
    private LinearLayout APPBAR;
    String insert_url,fetch_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cate_master);

        insert_url = Links.cate_insert;
        fetch_url = Links.cate_fetch;

        ADD_BTN = findViewById(R.id.add_btn);
        APPBAR_TITLE = findViewById(R.id.appbarTitle);
        APPBAR_TITLE.setText("Category Master");
        APPBAR = findViewById(R.id.include);

        //back press activity
        BACK_PRESS = findViewById(R.id.backPress);
        BACK_PRESS.setOnClickListener(view -> CateMaster.super.onBackPressed());

        // fetch  start
        CATE_RECYCLER = findViewById(R.id.cate_recyclerView);
        CATE_MANAGER = new GridLayoutManager(CateMaster.this, 1);
        CATE_RECYCLER.setLayoutManager(CATE_MANAGER);
        CATE = new ArrayList<>();

        ADD_BTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CateMaster.this, R.style.AlertDialogTheme);
                View view = LayoutInflater.from(CateMaster.this).inflate(R.layout.catedialog,
                        (LinearLayout)findViewById(R.id.cate_dialog));
                builder.setView(view);

                final AlertDialog alertDialog = builder.create();

                ((TextView) view.findViewById(R.id.dialog_title)).setText("Add New Category");

                CATE_NAME =view.findViewById(R.id.dia_cate_name);
                ACTIVE = view.findViewById(R.id.dia_cate_active);

                SAVE = view.findViewById(R.id.dialog_cate_save);
                CANCEL = view.findViewById(R.id.dialog_cate_cancel);

                active_adapter = new ArrayAdapter<>(CateMaster.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.active));
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


    public void insert(){
        SharedPreferences sharedPreferences = getSharedPreferences();
        String userName = sharedPreferences.getString("user_name", "");

        String Cate_name = CATE_NAME.getText().toString().trim();
        String Active = ACTIVE.getSelectedItem().toString();

        if (Active.equals("ENABLE")) {
            Active = "1";
        }else {
            Active = "2";
        }
        if (Cate_name.isEmpty()) {
            Toast.makeText(this, "Pleas Enter Category Name", Toast.LENGTH_SHORT).show();
        }else {

            String finalActive = Active;

            StringRequest request = new StringRequest(Request.Method.POST, insert_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("Inserted Successfully")){
                                CATE_NAME.setText("");
                                fetch();
                                Toast.makeText(CateMaster.this, "Inserted Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(CateMaster.this, "Inserted Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                        Toast.makeText(CateMaster.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cate_name", Cate_name.toUpperCase());
                    params.put("active", finalActive);
                    params.put("userName",userName);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(CateMaster.this);
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
                            CATE.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String CATE_ID = object.getString("cate_id");
                                String CATE_NAME = object.getString("cate_name");
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

                                CateModel cateModel = new CateModel(CATE_ID, CATE_NAME, ACTIVE, CREA_DATE, CREA_TIME, MODI_DATE, MODI_TIME);
                                CATE.add(cateModel);
                            }
                            CATE_ADAPTER = new CateAdapter(CateMaster.this, CATE);
                            CATE_RECYCLER.setAdapter(CATE_ADAPTER);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CateMaster.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(CateMaster.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CateMaster.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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