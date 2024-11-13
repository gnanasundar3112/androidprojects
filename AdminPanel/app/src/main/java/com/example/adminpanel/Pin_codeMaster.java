package com.example.adminpanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.example.adminpanel.Adapter.Pin_codeAdapter;
import com.example.adminpanel.Adapter.Prod_Size_Adapter;
import com.example.adminpanel.Model.Pin_codeModel;
import com.example.adminpanel.Model.Prod_Size_Model;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pin_codeMaster extends AppCompatActivity {
    private FloatingActionButton BACK, ADD_PINCODE;
    private TextView APPBAR_TITLE;
    private RecyclerView RECYCLERVIEW_PINCODE;
    private RecyclerView.LayoutManager PINCODE_MANAGER;
    private List<Pin_codeModel> pincode_models;
    private Pin_codeAdapter pincode_adapter;
    private ProgressBar PROGRASSBAR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code_master);

        RECYCLERVIEW_PINCODE = findViewById(R.id.pincode_recycler);
        PINCODE_MANAGER = new GridLayoutManager(Pin_codeMaster.this, 1);
        RECYCLERVIEW_PINCODE.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        pincode_models = new ArrayList<>();
        fetchPin_code();
        BACK = findViewById(R.id.fab_backPress);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);
        APPBAR_TITLE.setText("Pincode Master");
        BACK.setOnClickListener(view -> Pin_codeMaster.super.onBackPressed());
        ADD_PINCODE = findViewById(R.id.floating_pincode_master);


        ADD_PINCODE.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View view) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Pin_codeMaster.this, R.style.LogoutDialogThe);
                View view1 = LayoutInflater.from(Pin_codeMaster.this).inflate(R.layout.pincode_dialog,
                        (LinearLayout) findViewById(R.id.pincode_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.user_title)).setText("Pincode Master");
                ((MaterialButton) view1.findViewById(R.id.cate_cancel)).setText("Cancel");
                ((MaterialButton) view1.findViewById(R.id.cate_insert)).setText("Insert");


                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Pin_codeMaster.this, R.array.Active, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_size);
                ((Spinner)view1.findViewById(R.id.pin_active)).setAdapter(adapter);

                final android.app.AlertDialog alertDialog = builder.create();


                view1.findViewById(R.id.cate_insert).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String pincode_error = ((TextInputEditText) view1.findViewById(R.id.pin_name)).getText().toString();

                        if (pincode_error.length() < 6) {
                            ((TextInputEditText) view1.findViewById(R.id.pin_name)).setError("Pincod length is short");
                        }else {
                            if (pincode_error.isEmpty()) {
                                ((TextInputEditText) view1.findViewById(R.id.pin_name)).setError("Pincode is empty");
                            } else {
                                final ProgressDialog progressDialog1 = new ProgressDialog(Pin_codeMaster.this);
                                progressDialog1.setMessage("Please wait...");

                                progressDialog1.show();

                                String pincode = ((TextInputEditText) view1.findViewById(R.id.pin_name)).getText().toString();
                                String active = ((Spinner) view1.findViewById(R.id.pin_active)).getSelectedItem().toString().trim();


                                StringRequest request = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/adminpanel/pincode_master.php",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                progressDialog1.dismiss();
                                                if (response.equals("success")) {
                                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                                    fetchPin_code();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog1.dismiss();
                                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                ) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {

                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("pincode", pincode);
                                        params.put("active", active);
                                        return params;
                                    }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                requestQueue.add(request);
                            }
                        }
                    }
                });

                view1.findViewById(R.id.cate_cancel).setOnClickListener(new View.OnClickListener() {
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
    public void fetchPin_code() {
        PROGRASSBAR = findViewById(R.id.pincode_ProgressBar);
        PROGRASSBAR.setVisibility(View.VISIBLE);
        pincode_models.clear();
        StringRequest request = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/adminpanel/pincode_fetch.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PROGRASSBAR.setVisibility(View.GONE);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                String Pin_code_name = object.getString("pincode");
                                String Pin_code_active= object.getString("active");

                                Pin_codeModel pin_codeModel = new Pin_codeModel(Pin_code_name,Pin_code_active);
                                pincode_models.add(pin_codeModel);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        pincode_adapter = new Pin_codeAdapter(Pin_codeMaster.this, pincode_models);
                        RECYCLERVIEW_PINCODE.setAdapter(pincode_adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PROGRASSBAR.setVisibility(View.GONE);
                Toast.makeText(Pin_codeMaster.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}