package com.example.adminpanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.adminpanel.Adapter.Prod_Size_Adapter;
import com.example.adminpanel.Model.Prod_Size_Model;
import com.example.adminpanel.Model.Public_Id_Model;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProductSize extends AppCompatActivity {

    private FloatingActionButton BACK, ADD_PROD_SIZE;
    private TextView APPBAR_TITLE,PRODUCT_SPINNER,SIZE_SPINNER,SIZE_ID;
    private TextInputEditText DATE;
    private Button SIZE_INSERT;

    private RecyclerView RECYCLERVIEW_SIZE;
    private RecyclerView.LayoutManager SIZE_MANAGER;
    private List<Prod_Size_Model> prod_size_models;
    private Prod_Size_Adapter prod_size_adapter;
    private ProgressBar PROGRASSBAR;
    RequestQueue requestQueue;
    ArrayList<String> size_list = new ArrayList<>();
    ArrayAdapter<String> size_adapter;
    ArrayList<String> prod_list = new ArrayList<>();
    ArrayAdapter<String> prod_adapter;
    Dialog dialog;
    private SearchView SEARCHVIEW;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_size);

        RECYCLERVIEW_SIZE = findViewById(R.id.size_recycler);
        SIZE_MANAGER = new GridLayoutManager(ProductSize.this, 1);
        RECYCLERVIEW_SIZE.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        prod_size_models = new ArrayList<>();
        fetchSize();
        BACK = findViewById(R.id.fab_backPress);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);
        SIZE_INSERT = findViewById(R.id.size_insert);
        ADD_PROD_SIZE = findViewById(R.id.floating_prod_size);
        APPBAR_TITLE.setText("Product Size");
        BACK.setOnClickListener(view -> ProductSize.super.onBackPressed());

        SEARCHVIEW = findViewById(R.id.searchView_size);
        SEARCHVIEW.clearFocus();
        SEARCHVIEW.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fileList(newText);
                return true;
            }

            private void fileList(String text) {
                List<Prod_Size_Model> filteredList = new ArrayList<>();
                for (Prod_Size_Model item : prod_size_models){
                    if (item.getSize_prod_name().toLowerCase().contains(text.toLowerCase()) ){
                        filteredList.add(item);
                    }
                }
                if (filteredList.isEmpty()){
                    Toast.makeText(ProductSize.this, "No data found", Toast.LENGTH_SHORT).show();
                }else {
                    prod_size_adapter.setFilteredList(filteredList);
                }
            }
        });
        /* filter from search bar End*/

        ADD_PROD_SIZE.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProductSize.this, R.style.LogoutDialogThe);
                View view1 = LayoutInflater.from(ProductSize.this).inflate(R.layout.prod_size_dialog,
                        (LinearLayout) findViewById(R.id.prod_size_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.user_title)).setText("Product Size");
                ((MaterialButton) view1.findViewById(R.id.size_cancel)).setText("Cancel");
                ((MaterialButton) view1.findViewById(R.id.size_insert)).setText("Insert");

                DATE = view1.findViewById(R.id.eff_date);
                PRODUCT_SPINNER = view1.findViewById(R.id.product_name_spinner);
                SIZE_SPINNER = view1.findViewById(R.id.size_spinner);
                SIZE_ID = ((TextView) view1.findViewById(R.id.size_prod_id));
                DATE.setText(getCurrentDate());

                final android.app.AlertDialog alertDialog = builder.create();

                view1.findViewById(R.id.size_insert).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String product_name_error = ((TextView) view1.findViewById(R.id.product_name_spinner)).getText().toString();
                        String size_error = ((TextView) view1.findViewById(R.id.size_spinner)).getText().toString();

                        if (product_name_error.isEmpty()) {
                            ((TextView) view1.findViewById(R.id.product_name_spinner)).setError("Produc name is empty");
                        } else {
                            if (size_error.isEmpty()) {
                                ((TextView) view1.findViewById(R.id.size_spinner)).setError("Size is empty");
                            } else {

                                String eff_Date = ((TextInputEditText) view1.findViewById(R.id.eff_date)).getText().toString();
                                String prod_Id = ((TextView) view1.findViewById(R.id.size_prod_id)).getText().toString();
                                String size_Id = ((TextView) view1.findViewById(R.id.size_size_id)).getText().toString();

                                final ProgressDialog progressDialog1 = new ProgressDialog(ProductSize.this);
                                progressDialog1.setMessage("Please wait...");

                                progressDialog1.show();

                                StringRequest request = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/adminpanel/prod_size_insert.php",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                progressDialog1.dismiss();
                                                if (response.equalsIgnoreCase("success")) {
                                                    Toast.makeText(ProductSize.this, "Insert Successfully ", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(ProductSize.this, response, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog1.dismiss();
                                        Toast.makeText(ProductSize.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                ) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {

                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("eff_date", eff_Date);
                                        params.put("prod_id", prod_Id);
                                        params.put("size_id", size_Id);

                                        return params;
                                    }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(ProductSize.this);
                                requestQueue.add(request);
                            }
                        }

                    }
                });
                view1.findViewById(R.id.size_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                    }
                });
                view1.findViewById(R.id.product_name_spinner).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog = new Dialog(ProductSize.this);

                        dialog.setContentView(R.layout.searchable_spinner);

                        dialog.getWindow().setLayout(900,800);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                        EditText editText = dialog.findViewById(R.id.search_spinner);
                        ListView listView = dialog.findViewById(R.id.list_spinner);
                        ImageButton cancel = dialog.findViewById(R.id.dialog_cancel);

                        prod_list.clear();
                        requestQueue = Volley.newRequestQueue(ProductSize.this);
                        String url1 = "https://caustic-rinses.000webhostapp.com/adminpanel/prod_name_spinner.php";
                        JsonObjectRequest jsonObject= new JsonObjectRequest(Request.Method.POST, url1, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray jsonArray = response.getJSONArray("productmaster");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        String sp_prod_name = object.getString("prod_name");
                                        String sp_prod_id = object.getString("prod_id");
                                        ((TextView) view1.findViewById(R.id.size_prod_id)).setText(sp_prod_id);
                                        prod_list.add(sp_prod_name);
                                        prod_adapter = new ArrayAdapter<>(ProductSize.this, android.R.layout.simple_list_item_1, prod_list);
                                        prod_adapter.setDropDownViewResource(R.layout.spinner_dropdown_size);
                                        listView.setAdapter(prod_adapter);

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                        requestQueue.add(jsonObject);


                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                prod_adapter.getFilter().filter(charSequence);
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                PRODUCT_SPINNER.setText(prod_adapter.getItem(i));
                                dialog.dismiss();
                                String prod_Name = ((TextView)view1.findViewById(R.id.product_name_spinner)).getText().toString();
                                StringRequest request = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/adminpanel/id_get_formet/get_prod_id.php",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONArray jsonArray = new JSONArray(response);

                                                    for (int i = 0; i < jsonArray.length(); i++) {

                                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                                        String public_id = jsonObject.getString("prod_id");

                                                        Public_Id_Model publicId_model = new Public_Id_Model(public_id);

                                                        ((TextView)view1.findViewById(R.id.size_prod_id)).setText(publicId_model.getPublic_id());

                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(ProductSize.this, error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {

                                        Map<String, String> params = new HashMap<String, String>();

                                        params.put("prod_name", prod_Name);

                                        return params;
                                    }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(ProductSize.this);
                                requestQueue.add(request);
                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                    }
                });
                view1.findViewById(R.id.size_spinner).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog = new Dialog(ProductSize.this);

                        dialog.setContentView(R.layout.searchable_spinner);

                        dialog.getWindow().setLayout(900,800);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                        EditText editText = dialog.findViewById(R.id.search_spinner);
                        ListView listView = dialog.findViewById(R.id.list_spinner);
                        ImageButton cancel = dialog.findViewById(R.id.dialog_cancel);

                        size_list.clear();
                        requestQueue = Volley.newRequestQueue(ProductSize.this);
                        String url = "https://caustic-rinses.000webhostapp.com/adminpanel/size_spinner_data.php";
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray jsonArray = response.getJSONArray("sizemaster");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        String sp_size_name = object.getString("size_name");
                                        String sp_size_id = object.getString("size_id");

                                        ((TextView) view1.findViewById(R.id.size_size_id)).setText(sp_size_id);
                                        size_list.add(sp_size_name);
                                        size_adapter = new ArrayAdapter<>(ProductSize.this, android.R.layout.simple_list_item_1, size_list);
                                        size_adapter.setDropDownViewResource(R.layout.spinner_dropdown_size);
                                        listView.setAdapter(size_adapter);

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                        requestQueue.add(jsonObjectRequest);

                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                size_adapter.getFilter().filter(charSequence);
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                SIZE_SPINNER.setText(size_adapter.getItem(i));
                                dialog.dismiss();

                                String size_Name = ((TextView)view1.findViewById(R.id.size_spinner)).getText().toString();
                                StringRequest request = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/adminpanel/id_get_formet/get_size_id.php",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONArray jsonArray = new JSONArray(response);

                                                    for (int i = 0; i < jsonArray.length(); i++) {

                                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                                        String public_id = jsonObject.getString("size_id");

                                                        Public_Id_Model publicId_model = new Public_Id_Model(public_id);

                                                        ((TextView)view1.findViewById(R.id.size_size_id)).setText(publicId_model.getPublic_id());

                                                    }


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(ProductSize.this, error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {

                                        Map<String, String> params = new HashMap<String, String>();

                                        params.put("size_name", size_Name);

                                        return params;
                                    }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(ProductSize.this);
                                requestQueue.add(request);
                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });


                    }
                });

                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();

            }
        });
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }

    public void fetchSize() {
        PROGRASSBAR = findViewById(R.id.size_ProgressBar);
        PROGRASSBAR.setVisibility(View.VISIBLE);
        prod_size_models.clear();
        StringRequest request = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/adminpanel/product_size_fetch.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PROGRASSBAR.setVisibility(View.GONE);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                String eff_date = object.getString("eff_date");
                                String size_prod_id= object.getString("prod_id");
                                String size_prod_name = object.getString("prod_name");
                                String prod_size_id= object.getString("size_id");
                                String prod_size = object.getString("size_name");

                                Prod_Size_Model prod_size_model = new Prod_Size_Model(eff_date,size_prod_id,size_prod_name,prod_size_id,prod_size);
                                prod_size_models.add(prod_size_model);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        prod_size_adapter = new Prod_Size_Adapter(ProductSize.this, prod_size_models);
                        RECYCLERVIEW_SIZE.setAdapter(prod_size_adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PROGRASSBAR.setVisibility(View.GONE);
                Toast.makeText(ProductSize.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


}