package in.vinsoftsolutions.jayagrocery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import in.vinsoftsolutions.jayagrocery.Adapter.AddressAdapter;
import in.vinsoftsolutions.jayagrocery.Models.AddressModel;

import in.vinsoftsolutions.jayagrocery.internet.NetworkChangeListener;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Address extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private FloatingActionButton BACK_PRESS,ADD_BTN;
    TextView APPBAR_TITLE;
    ImageView EMPTY_IMG;
    TextInputEditText add_name, add_mobileNo, add_address, add_state, add_city,add_pinCode;
    CheckBox add_primary;
    RecyclerView recyclerView_address;
    private RecyclerView.Adapter address_adapter;
    private RecyclerView.LayoutManager address_manager;
    private List<AddressModel> addressModels;
    private ProgressBar progressBar;
    LinearLayout LINEARLAYOUT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        ADD_BTN = findViewById(R.id.add_address);
        BACK_PRESS = findViewById(R.id.fab_backPress);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);

        APPBAR_TITLE.setText("Address");
        //back press activity
        BACK_PRESS.setOnClickListener(view -> Address.super.onBackPressed());

        // address fetch  start
        recyclerView_address = findViewById(R.id.address_recycler);
        address_manager = new GridLayoutManager(Address.this,1);
        recyclerView_address.setLayoutManager(address_manager);
        addressModels = new ArrayList<>();
        // address fetch  end

        LINEARLAYOUT = findViewById(R.id.linearLayout);
        EMPTY_IMG = findViewById(R.id.add_empty_img);

        ADD_BTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Address.this, R.style.AlertDialogTheme);
                View view1 = LayoutInflater.from(Address.this).inflate(R.layout.add_adress_dialog,
                        (LinearLayout)findViewById(R.id.add_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.add_dialog_title)).setText("Add New Address");
                ((Button) view1.findViewById(R.id.add_dialog_no)).setText("NO");
                ((Button) view1.findViewById(R.id.add_dialog_yes)).setText("YES");

                ((TextInputEditText) view1.findViewById(R.id.add_dialog_name)).setText("");
                ((TextInputEditText) view1.findViewById(R.id.add_dialog_mobile)).setText("");
                ((TextInputEditText) view1.findViewById(R.id.add_dialog_address)).setText("");
                ((TextInputEditText) view1.findViewById(R.id.add_dialog_city)).setText("");
                ((TextInputEditText) view1.findViewById(R.id.add_dialog_state)).setText("");
                ((TextInputEditText) view1.findViewById(R.id.add_dialog_pincode)).setText("");

                add_name = view1.findViewById(R.id.add_dialog_name);
                add_mobileNo = view1.findViewById(R.id.add_dialog_mobile);
                add_address = view1.findViewById(R.id.add_dialog_address);
                add_state = view1.findViewById(R.id.add_dialog_city);
                add_city = view1.findViewById(R.id.add_dialog_state);
                add_pinCode = view1.findViewById(R.id.add_dialog_pincode);
                add_primary = view1.findViewById(R.id.add_dialog_primary);

                final AlertDialog alertDialog = builder.create();

                view1.findViewById(R.id.add_dialog_no).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });


                view1.findViewById(R.id.add_dialog_yes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view ) {
                        String name_add = add_name.getText().toString();
                        String mobileNo_add = add_mobileNo.getText().toString();
                        String address_add = add_address.getText().toString();
                        String state_add = add_state.getText().toString();
                        String city_add = add_city.getText().toString();
                        String pinCode_add = add_pinCode.getText().toString();

                        if (name_add.isEmpty()) {
                            add_name.setError("Name is empty");
                        } else if (mobileNo_add.isEmpty()){
                            add_mobileNo.setError("MobileNo is empty");
                        } else if (mobileNo_add.length() < 10) {
                            add_mobileNo.setError("Please enter the correct mobile no");
                        } else if (address_add.isEmpty()) {
                            add_address.setError("Address is empty");
                        } else if (state_add.isEmpty()) {
                            add_state.setError("State is empty");
                        } else if (city_add.isEmpty()) {
                            add_city.setError("City is empty");
                        } else if (pinCode_add.isEmpty()) {
                            add_pinCode.setError("Pincode is empty");
                        } else if (pinCode_add.length() < 6) {
                            add_pinCode.setError("Please check correct pincode");
                        } else {

                            final ProgressDialog progressDialog1 = new ProgressDialog(Address.this);
                            progressDialog1.setMessage("Please wait...");

                            progressDialog1.show();

                            String Add_name = add_name.getText().toString();
                            String Add_mobileNo = add_mobileNo.getText().toString();
                            String Add_address = add_address.getText().toString();
                            String Add_state = add_state.getText().toString();
                            String Add_city = add_city.getText().toString();
                            String Add_pinCode = add_pinCode.getText().toString();

                            StringBuilder result = new StringBuilder();
                            if (add_primary.isChecked()){
                                result.append("1");
                            }else {
                                result.append("0");
                            }
                            String PRIMARY = String.valueOf(result);


                            SharedPreferences sharedPreferences = getSharedPreferences("user_send_data", MODE_PRIVATE);
                            String USER_ID = sharedPreferences.getString("USER_ID", "");
                            StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/in_address.php",
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            progressDialog1.dismiss();
                                            if (response.equalsIgnoreCase("Address Add successfully")) {
                                                alertDialog.dismiss();
                                                fetchAddress();
                                                Toast.makeText(Address.this, "Insert successfully", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(Address.this, response, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog1.dismiss();
                                    if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                                        Toast.makeText(Address.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            ) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("add_name", Add_name);
                                    params.put("add_mobile", Add_mobileNo);
                                    params.put("add_address", Add_address);
                                    params.put("add_state", Add_state);
                                    params.put("add_city", Add_city);
                                    params.put("add_pincode", Add_pinCode);
                                    params.put("add_select", PRIMARY);
                                    params.put("cust_id", USER_ID);
                                    return params;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(Address.this);
                            requestQueue.add(request);
                        }
                    }
                });

                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();

            }
        });
    }

    // Address fetch  start*/
    public void fetchAddress(){
        progressBar = findViewById(R.id.address_ProgressBar);

        SharedPreferences sharedPreferences = getSharedPreferences("user_send_data", MODE_PRIVATE);
        String USER_ID = sharedPreferences.getString("USER_ID", "");

        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST,"https://vinsoftsolutions.in/jaya-android/address_fetch.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        addressModels.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String cust_id = jsonObject.getString("cust_id");
                                String add_id = jsonObject.getString("add_id");
                                String add_name = jsonObject.getString("add_name");
                                String add_mobile = jsonObject.getString("add_mobile");
                                String add_address = jsonObject.getString("add_address");
                                String add_city = jsonObject.getString("add_state");
                                String add_state = jsonObject.getString("add_city");
                                String add_pinCode = jsonObject.getString("add_pincode");
                                String add_active = jsonObject.getString("active");
                                String add_select = jsonObject.getString("add_select");

                                AddressModel addressModel = new AddressModel(cust_id,add_id,add_name,add_mobile,add_address,add_city,add_state,add_pinCode,add_active,add_select);
                                addressModels.add(addressModel);
                            }


                            if (jsonArray.length() > 0) {
                                LINEARLAYOUT.setVisibility(View.VISIBLE);
                                EMPTY_IMG.setVisibility(View.GONE);
                            } else {
                                LINEARLAYOUT.setVisibility(View.GONE);
                                EMPTY_IMG.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        address_adapter = new AddressAdapter(Address.this, addressModels);
                        recyclerView_address.setAdapter(address_adapter);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Address.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("cust_id", USER_ID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Address.this);
        requestQueue.add(request);
    }
    // Address fetch  End

    // this fetch for recycler item delete to fetch
    public void updateAddress() {
        fetchAddress();
        String ADDRESS_ID = getIntent().getStringExtra("add_id");

        if (ADDRESS_ID != null && ADDRESS_ID.equalsIgnoreCase("1")) {
            Intent intent = new Intent(Address.this, Order_Confirm.class);
            startActivity(intent);
            finish();
        }
    }

    // network offline filter start
    @Override
    protected void onStart() {
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);

        fetchAddress();
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
    // network offline filter End
}