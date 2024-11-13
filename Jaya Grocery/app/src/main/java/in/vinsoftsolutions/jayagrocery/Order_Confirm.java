package in.vinsoftsolutions.jayagrocery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import in.vinsoftsolutions.jayagrocery.Adapter.Order_add_Adapter;
import in.vinsoftsolutions.jayagrocery.Models.AddressModel;

import in.vinsoftsolutions.jayagrocery.R;

import in.vinsoftsolutions.jayagrocery.internet.NetworkChangeListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order_Confirm extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private FloatingActionButton BACK_PRESS;
    TextView APPBAR_TITLE,Add_id,DELI_TYPE;
    private ProgressBar PROGRESSBAR;
    private RecyclerView RECYCLERVIEW_ADDRESS;
    private RecyclerView.LayoutManager ADDRESS_MANAGER;
    private List<AddressModel> ADDRESS;
    private Order_add_Adapter ADDRESS_ADAPTER;
    MaterialButton CONFIRM_BTN,ADD_BTN;
    MaterialTextView CART_TOTAL,DELI_CHARGE,FINAL_TOTAL,INCI,ACTIVE;
    Dialog DIALOG;
    LinearLayout LINEAR;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);

        Intent serviceIntent = new Intent(this, Order_Confirm.class);
        startService(serviceIntent);


        BACK_PRESS = findViewById(R.id.fab_backPress);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);

        APPBAR_TITLE.setText("Confirm");
        //back press activity
        BACK_PRESS.setOnClickListener(view -> Order_Confirm.super.onBackPressed());

        Add_id = findViewById(R.id.add_id_con);
        PROGRESSBAR = findViewById(R.id.order_add_progress);
        // address fetch  start
        RECYCLERVIEW_ADDRESS = findViewById(R.id.order_add_recycler);
        ADDRESS_MANAGER = new GridLayoutManager(Order_Confirm.this,1);
        RECYCLERVIEW_ADDRESS.setLayoutManager(ADDRESS_MANAGER);
        ADDRESS = new ArrayList<>();
        // address fetch  end

        ACTIVE = findViewById(R.id.pin_active);
        DELI_CHARGE = findViewById(R.id.con_deli_charg);
        CART_TOTAL = findViewById(R.id.item_con_total);
        INCI = findViewById(R.id.con_inci);
        FINAL_TOTAL = findViewById(R.id.final_total);
        LINEAR = findViewById(R.id.con_linear);
        DELI_TYPE = findViewById(R.id.deli_type);

        SharedPreferences sharedPreferences = getSharedPreferences("amount_sent", MODE_PRIVATE);
        String TOTAL_AMOUNT = sharedPreferences.getString("total_amt", "");
        CART_TOTAL.setText(TOTAL_AMOUNT);
        String cart_total = TOTAL_AMOUNT.replaceAll("[^0-9.]", "");


        DELI_TYPE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DIALOG = new Dialog(Order_Confirm.this);
                DIALOG.setContentView(R.layout.custom_spinner);
                DIALOG.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                DIALOG.show();

                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                int screenWidth = displayMetrics.widthPixels;
                int screenHeight = displayMetrics.heightPixels;

                if (screenWidth == 480 && screenHeight == 800) {
                    DIALOG.getWindow().setLayout(400,300);
                }else if (screenWidth == 1440 && screenHeight == 2960) {
                    DIALOG.getWindow().setLayout(1100,900);
                }else {
                    DIALOG.getWindow().setLayout(900,800);
                }

                MaterialTextView DROPDOWN_TITTLE = DIALOG.findViewById(R.id.dropdown_title);
                String TITLE = String.valueOf("Select Payment");
                DROPDOWN_TITTLE.setText(TITLE);

                ListView listView = DIALOG.findViewById(R.id.list_spinner);
                ImageButton cancel = DIALOG.findViewById(R.id.dialog_cancel);

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Order_Confirm.this,R.array.Active, R.layout.item_drop_down);
                adapter.setDropDownViewResource(R.layout.item_drop_down);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String payment = (String) adapter.getItem(i);
                        DELI_TYPE.setText(payment);
                        DIALOG.dismiss();
                    }
                });


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DIALOG.dismiss();
                    }
                });
            }
        });

        CONFIRM_BTN = findViewById(R.id.confirm_order_btn);
        CONFIRM_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Order_Confirm.this, R.style.AlertDialogTheme);
                View view1 = LayoutInflater.from(Order_Confirm.this).inflate(R.layout.delete_dialog,
                        (ConstraintLayout)findViewById(R.id.warning_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.dialog_title)).setText("Order to confirm");
                ((TextView) view1.findViewById(R.id.dialog_message)).setText("Are you sure confirmed to ordered?");
                ((Button) view1.findViewById(R.id.dialog_btn)).setText("NO");
                ((Button) view1.findViewById(R.id.dialog_btn2)).setText("YES");
                ((ImageView) view1.findViewById(R.id.dialog_image)).setImageResource(R.drawable.confirm);

                final AlertDialog alertDialog = builder.create();

                view1.findViewById(R.id.dialog_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                view1.findViewById(R.id.dialog_btn2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sharedPreferences = getSharedPreferences("user_send_data", MODE_PRIVATE);
                        String USER_ID = sharedPreferences.getString("USER_ID", "");

                        String MADD_ID = Add_id.getText().toString();
                        String MACTIVE = ACTIVE.getText().toString();
                        String MINCI = INCI.getText().toString();
                        String MDELI_CHARGE = DELI_CHARGE.getText().toString();
                        String MDELI_TYPE = DELI_TYPE.getText().toString();
                        String status = String.valueOf("1");
                        String PIN_ACTIVE = String.valueOf("1");
                        String MPIN_ACTIVE = String.valueOf("2");

                        String TYPE = "Cash On Delivery";
                        String MDELITYPE;
                        if (MDELI_TYPE.equalsIgnoreCase(TYPE)) {
                            MDELITYPE = "1";
                        } else {
                            MDELITYPE = "2";
                        }

                        if (ADDRESS.isEmpty()){
                            Toast.makeText(Order_Confirm.this, "Please add the delivery address", Toast.LENGTH_SHORT).show();
                        }else if (!MACTIVE.equalsIgnoreCase(PIN_ACTIVE)){
                            Toast.makeText(Order_Confirm.this, "This selected pincode is not available for delivery", Toast.LENGTH_SHORT).show();
                        }else if (MACTIVE.equalsIgnoreCase(MPIN_ACTIVE)){
                            Toast.makeText(Order_Confirm.this, "Please change the valid delivery address", Toast.LENGTH_SHORT).show();
                        }else {

                            progressDialog = ProgressDialog.show(Order_Confirm.this, "Order confirmed for few sec...", null, true, false);

                            Handler handler = new Handler();
                            // Create a new Runnable for the delayed task
                            Runnable delayedTask = new Runnable() {
                                @Override
                                public void run() {
                                    StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/salesorder.php",
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    progressDialog.dismiss();
                                                    if (response.equalsIgnoreCase("Ordered successfully")) {
                                                        Intent intent = new Intent(Order_Confirm.this, Success.class);
                                                        startActivity(intent);
                                                        finish();
                                                        Toast.makeText(Order_Confirm.this, "Ordered successfully", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(Order_Confirm.this, response, Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            progressDialog.dismiss();
                                            if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                                                Toast.makeText(Order_Confirm.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                    ) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {

                                            Map<String, String> params = new HashMap<String, String>();
                                            params.put("cust_id", USER_ID);
                                            params.put("add_id", MADD_ID);
                                            params.put("deli_charg", MDELI_CHARGE);
                                            params.put("inci", MINCI);
                                            params.put("deli_type", MDELITYPE);
                                            params.put("status", status);

                                            return params;
                                        }
                                    };
                                    RequestQueue requestQueue = Volley.newRequestQueue(Order_Confirm.this);
                                    requestQueue.add(request);
                                    handler.removeCallbacks(this);
                                }
                            };
                            handler.postDelayed(delayedTask, 5000);
                        }
                        alertDialog.dismiss();
                    }
                });

                if (alertDialog.getWindow() !=null){
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }
        });

        ADD_BTN = findViewById(R.id.confirm_add_btn);
        ADD_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Order_Confirm.this,Address.class);
                intent.putExtra("add_id", "1");
                startActivity(intent);
                finish();
            }
        });
    }
    public void fetchAddress(){
        SharedPreferences sharedPreferences = getSharedPreferences("user_send_data", MODE_PRIVATE);
        String USER_ID = sharedPreferences.getString("USER_ID", "");

        PROGRESSBAR.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST,"https://vinsoftsolutions.in/jaya-android/order_add_fetch.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PROGRESSBAR.setVisibility(View.GONE);
                        ADDRESS.clear();
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
                                String deli_cahrg = jsonObject.getString("deli_charg");
                                String pin_active = jsonObject.getString("pin_active");

                                AddressModel addressModel = new AddressModel(cust_id,add_id,add_name,add_mobile,add_address,add_city,add_state,add_pinCode,add_active,add_select);
                                ADDRESS.add(addressModel);
                                Add_id.setText(add_id);
                                ACTIVE.setText(pin_active);

                                if (deli_cahrg.isEmpty()){
                                    DELI_CHARGE.setText("0");
                                }else {
                                    DELI_CHARGE.setText(deli_cahrg);
                                }

                                double cart_total = Double.parseDouble(CART_TOTAL.getText().toString().replaceAll("[^0-9.]", ""));
                                double deli_charge = Double.parseDouble(DELI_CHARGE.getText().toString().trim().replaceAll("[^0-9.]", ""));
                                double inci_amt = Double.parseDouble(INCI.getText().toString().replaceAll("[^0-9.]", ""));

                                double item_total = 0;
                                item_total = cart_total + deli_charge;

                                double inci = Math.round((Math.round(item_total)-(Math.round(item_total*100.0)/100.0))*100.0)/100.0;
                                double total = (item_total+inci);

                                INCI.setText("₹ "+inci);
                                FINAL_TOTAL.setText("₹ "+String.format("%.2f", total)); // Format to display 2 decimal places
                            }
                            if (jsonArray.length() > 0) {
                                LINEAR.setVisibility(View.GONE);
                            } else {
                                LINEAR.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ADDRESS_ADAPTER = new Order_add_Adapter(Order_Confirm.this, ADDRESS);
                        RECYCLERVIEW_ADDRESS.setAdapter(ADDRESS_ADAPTER);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PROGRESSBAR.setVisibility(View.GONE);
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Order_Confirm.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Order_Confirm.this);
        requestQueue.add(request);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}