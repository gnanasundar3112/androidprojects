package com.sundar.i_macbankers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.android.material.textfield.TextInputEditText;
import com.sundar.i_macbankers.Adapter.CateAdapter;
import com.sundar.i_macbankers.Adapter.ProductAdapter;
import com.sundar.i_macbankers.Models.CateModel;
import com.sundar.i_macbankers.Models.ProductModel;
import com.sundar.i_macbankers.internet.NetworkChangeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductMaster extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private ImageView BACK_PRESS,ADD_BTN;
    private TextView APPBAR_TITLE,CATE_NAME;
    private TextInputEditText PRODUCT_NAME;
    private Spinner ACTIVE;
    private ArrayList<String> active_list = new ArrayList<>();
    private ArrayAdapter<String> active_adapter;

    private MaterialButton SAVE,CANCEL;
    private RecyclerView PROD_RECYCLER;
    private RecyclerView.LayoutManager PROD_MANAGER;
    private List<ProductModel> PROD;
    private ProductAdapter PROD_ADAPTER;

    ArrayList<String> CATEGORY_LIST;
    Dialog DIALOG;
    String SELECT_CATE_ID = "";
    ArrayAdapter<String> adapter;

    String cate_fetch_url,insert_url,fetch_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_master);

        cate_fetch_url = Links.prod_cate_fetch;
        insert_url = Links.product_insert;
        fetch_url = Links.prod_fetch;

        ADD_BTN = findViewById(R.id.add_btn);
        APPBAR_TITLE = findViewById(R.id.appbarTitle);
        APPBAR_TITLE.setText("Product Master");

        //back press activity
        BACK_PRESS = findViewById(R.id.backPress);
        BACK_PRESS.setOnClickListener(view -> ProductMaster.super.onBackPressed());

        // fetch  start
        PROD_RECYCLER = findViewById(R.id.prod_recyclerView);
        PROD_MANAGER = new GridLayoutManager(ProductMaster.this, 1);
        PROD_RECYCLER.setLayoutManager(PROD_MANAGER);
        PROD = new ArrayList<>();

        CATEGORY_LIST = new ArrayList<>();
        CATE_NAME = findViewById(R.id.fetch_cate_name);

        CATE_NAME.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryFetch();
            }
        });

        ADD_BTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                String Cate_id = SELECT_CATE_ID;
                if (Cate_id.isEmpty()) {
                    Toast.makeText(ProductMaster.this, "Please Select Category Name", Toast.LENGTH_SHORT).show();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductMaster.this, R.style.AlertDialogTheme);
                    View view = LayoutInflater.from(ProductMaster.this).inflate(R.layout.proddialog,
                            (LinearLayout) findViewById(R.id.prod_dialog));
                    builder.setView(view);

                    final AlertDialog alertDialog = builder.create();

                    ((TextView) view.findViewById(R.id.dialog_title)).setText("Add New Product");

                    PRODUCT_NAME = view.findViewById(R.id.dia_prod_name);
                    ACTIVE = view.findViewById(R.id.dia_prod_active);

                    SAVE = view.findViewById(R.id.dialog_prod_save);
                    CANCEL = view.findViewById(R.id.dialog_prod_cancel);

                    active_adapter = new ArrayAdapter<>(ProductMaster.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.active));
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
            }
        });
    }

    public void categoryFetch(){
        DIALOG = new Dialog(ProductMaster.this);
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

        HashMap<String, String> CATEGORY_NAME = new HashMap<>();

        StringRequest request = new StringRequest(Request.Method.POST, cate_fetch_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                String CATE_ID = object.getString("cate_id");
                                String CATE_NAME = object.getString("cate_name");

                                CATEGORY_NAME.put(CATE_ID, CATE_NAME);
                            }

                            List<String> category = new ArrayList<>(CATEGORY_NAME.values());
                            adapter = new ArrayAdapter<>(ProductMaster.this, android.R.layout.simple_list_item_1, category);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProductMaster.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(ProductMaster.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductMaster.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                String selectedCateName = adapter.getItem(position);

                // Find the key corresponding to the selected value
                String selectedCateId = null;

                for (Map.Entry<String, String> entry : CATEGORY_NAME.entrySet()) {
                    if (entry.getValue().equals(selectedCateName)) {
                        selectedCateId = entry.getKey();
                        break;
                    }
                }

                if (selectedCateId != null) {
                    // Use selectedPartyId and selectedPartyName as neede
                    CATE_NAME.setText(selectedCateName);
                    SELECT_CATE_ID = selectedCateId;
                }

                fetch();

                DIALOG.dismiss();
            }
        });
    }

    public void insert(){
        SharedPreferences sharedPreferences = getSharedPreferences();
        String userName = sharedPreferences.getString("user_name", "");

        String Prod_name = PRODUCT_NAME.getText().toString().trim();
        String Active = ACTIVE.getSelectedItem().toString();

        if (Active.equals("ENABLE")) {
            Active = "1";
        }else {
            Active = "2";
        }

        if (Prod_name.isEmpty()) {
            Toast.makeText(this, "Pleas Enter Product Name", Toast.LENGTH_SHORT).show();
        }else {

            String finalActive = Active;

            StringRequest request = new StringRequest(Request.Method.POST, insert_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("Inserted Successfully")){
                                PRODUCT_NAME.setText("");

                                fetch();

                                Toast.makeText(ProductMaster.this, "Inserted Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(ProductMaster.this, "Inserted Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                        Toast.makeText(ProductMaster.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cate_id", SELECT_CATE_ID);
                    params.put("prod_name", Prod_name.toUpperCase());
                    params.put("active", finalActive);
                    params.put("userName",userName);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(ProductMaster.this);
            requestQueue.add(request);
        }
    }

    public void fetch() {
        StringRequest request = new StringRequest(Request.Method.POST, fetch_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PROD.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                String PROD_ID = object.getString("prod_id");
                                String PROD_NAME = object.getString("prod_name");
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

                                ProductModel productModel = new ProductModel(PROD_ID,PROD_NAME,ACTIVE,CREA_DATE,CREA_TIME,MODI_DATE,MODI_TIME);
                                PROD.add(productModel);
                            }
                            PROD_ADAPTER = new ProductAdapter(ProductMaster.this, PROD);
                            PROD_RECYCLER.setAdapter(PROD_ADAPTER);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProductMaster.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(ProductMaster.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductMaster.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cate_id", SELECT_CATE_ID);
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