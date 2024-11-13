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
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.sundar.i_macbankers.Adapter.PartyAdapter;
import com.sundar.i_macbankers.Adapter.ProductAdapter;
import com.sundar.i_macbankers.Models.PartyModel;
import com.sundar.i_macbankers.Models.ProductModel;
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

public class PartyMaster extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private ImageView BACK_PRESS,ADD_BTN;
    private TextView APPBAR_TITLE;
    private MaterialTextView AC_ID;
    private TextInputEditText AC_NAME,ADDRESS,PLACE,MOBILE,AADHAR_NO;
    AutoCompleteTextView STATE;
    private Spinner ACTIVE;
    ArrayList<String> active_list = new ArrayList<>();
    ArrayAdapter<String> active_adapter;
    private MaterialButton SAVE,CANCEL;
    private RecyclerView PARTY_RECYCLER;
    private RecyclerView.LayoutManager PARTY_MANAGER;
    private List<PartyModel> PARTY;
    private PartyAdapter PARTY_ADAPTER;

    String insert_url,fetch_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_master);

        insert_url = Links.party_insert;
        fetch_url = Links.party_fetch;

        ADD_BTN = findViewById(R.id.add_btn);
        APPBAR_TITLE = findViewById(R.id.appbarTitle);
        APPBAR_TITLE.setText("Party Master");
        //back press activity
        BACK_PRESS = findViewById(R.id.backPress);
        BACK_PRESS.setOnClickListener(view -> PartyMaster.super.onBackPressed());

        // fetch  start
        PARTY_RECYCLER = findViewById(R.id.party_recyclerView);
        PARTY_MANAGER = new GridLayoutManager(PartyMaster.this, 1);
        PARTY_RECYCLER.setLayoutManager(PARTY_MANAGER);
        PARTY = new ArrayList<>();

        ADD_BTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PartyMaster.this, R.style.AlertDialogTheme);
                View view = LayoutInflater.from(PartyMaster.this).inflate(R.layout.partydialog,
                        (LinearLayout)findViewById(R.id.party_dialog));
                builder.setView(view);

                final AlertDialog alertDialog = builder.create();

                ((TextView) view.findViewById(R.id.dialog_title)).setText("Add New Party Detail");

                AC_NAME = view.findViewById(R.id.dia_ac_name);
                ADDRESS = view.findViewById(R.id.dia_address);
                PLACE = view.findViewById(R.id.dia_place);
                STATE = view.findViewById(R.id.dia_state);
                MOBILE = view.findViewById(R.id.dia_mobile);
                AADHAR_NO = view.findViewById(R.id.dia_aadhar_no);
                ACTIVE = view.findViewById(R.id.dia_active);

                SAVE = view.findViewById(R.id.dialog_save);
                CANCEL = view.findViewById(R.id.dialog_cancel);

                active_adapter = new ArrayAdapter<>(PartyMaster.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.active));
                active_adapter.setDropDownViewResource(R.layout.item_drop_down);
                ACTIVE.setAdapter(active_adapter);

                // Assuming this code is inside an activity or a context
                String[] statesArray = getResources().getStringArray(R.array.states_array);
                for (int i = 0; i < statesArray.length; i++) {
                    statesArray[i] = statesArray[i].toUpperCase();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(PartyMaster.this, android.R.layout.simple_list_item_1, statesArray);
                STATE.setAdapter(adapter);
                STATE.setOnItemClickListener((parent, view1, position, id) -> {
                    String selectedItem = (String) parent.getItemAtPosition(position);
                    Toast.makeText(PartyMaster.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
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


    public void insert(){
        SharedPreferences sharedPreferences = getSharedPreferences();
        String userName = sharedPreferences.getString("user_name", "");

        String Ac_name = AC_NAME.getText().toString().trim();
        String Address = ADDRESS.getText().toString().trim();
        String Place = PLACE.getText().toString().trim();
        String State = STATE.getText().toString().trim();
        String Mobile = MOBILE.getText().toString().trim();
        String Aadhar_no = AADHAR_NO.getText().toString().trim();
        String Active = ACTIVE.getSelectedItem().toString();

        if (Active.equals("ENABLE")) {
            Active = "1";
        }else {
            Active = "2";
        }

        if (Ac_name.isEmpty()){
            Toast.makeText(this, "Please enter Party name", Toast.LENGTH_SHORT).show();
        }else {
            String finalActive = Active;

            StringRequest request = new StringRequest(Request.Method.POST, insert_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("Inserted Successfully")) {
                                AC_NAME.setText("");
                                ADDRESS.setText("");
                                PLACE.setText("");
                                STATE.setText("");
                                MOBILE.setText("");

                                fetch();

                                Toast.makeText(PartyMaster.this, "Inserted Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PartyMaster.this, "Inserted Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                        Toast.makeText(PartyMaster.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("ac_name",Ac_name.toUpperCase());
                    params.put("address",Address.toUpperCase());
                    params.put("place",Place.toUpperCase());
                    params.put("state",State.toUpperCase());
                    params.put("mobile",Mobile);
                    params.put("aadhar_no",Aadhar_no);
                    params.put("active",finalActive);
                    params.put("crea_user",userName);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(PartyMaster.this);
            requestQueue.add(request);

        }
    }

    public void fetch() {
        StringRequest request = new StringRequest(Request.Method.POST, fetch_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PARTY.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                String ACID = object.getString("ac_id");
                                String AC_NAME = object.getString("ac_name");
                                String ADDRESS = object.getString("address");
                                String PLACE = object.getString("place");
                                String STATE = object.getString("state");
                                String MOBILE = object.getString("mobile");
                                String AADHAR_NO = object.getString("aadhar_no");
                                String ACTIVE = object.getString("active");

                                if (ACTIVE.equalsIgnoreCase("1")){
                                    ACTIVE="ENABLE";
                                }else {
                                    ACTIVE = "DISABLE";
                                }

                                PartyModel partyModel = new PartyModel(ACID,AC_NAME,ADDRESS,PLACE,STATE,MOBILE,AADHAR_NO,ACTIVE);
                                PARTY.add(partyModel);
                            }
                            PARTY_ADAPTER = new PartyAdapter(PartyMaster.this, PARTY);
                            PARTY_RECYCLER.setAdapter(PARTY_ADAPTER);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PartyMaster.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(PartyMaster.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PartyMaster.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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