package in.vinsoftsolutions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Orders_Status_Detail extends AppCompatActivity {

    private TextView DELIVERY_BOY_ID,DELIVERY_BOY_NAME,CUSTOMER_ORDER_NO,CUSTOMER_ORDER_DATE,CUSTOMER_ORDER_NAME,CUSTOMER_ORDER_PHONE,CUSTOMER_ORDER_AMOUNT,CUSTOMER_ORDER_QTY,CUSTOMER_ORDER_ADDRESS,CUSTOMER_ORDER_PAYMENT,APPBAR_TITLE;
    private MaterialButton ORDER_CANCEL,ORDER_DELIVERED;
    private FloatingActionButton BACK_PRESS;
    private CheckBox CHECKBOX_ONE,CHECKBOX_TWO,CHECKBOX_THREE,CHECKBOX_FOUR;
    private TextInputEditText REPORT_FEEDBACK;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_status_detail);

        DELIVERY_BOY_ID = findViewById(R.id.deli_boy_id);
        DELIVERY_BOY_NAME = findViewById(R.id.deli_boy_name);
        CUSTOMER_ORDER_NO = findViewById(R.id.cust_order_id);
        CUSTOMER_ORDER_DATE = findViewById(R.id.cust_order_date);
        CUSTOMER_ORDER_PHONE = findViewById(R.id.cust_phone);
        CUSTOMER_ORDER_NAME = findViewById(R.id.cust_name);
        CUSTOMER_ORDER_AMOUNT = findViewById(R.id.cust_amount);
        CUSTOMER_ORDER_QTY = findViewById(R.id.cust_qty);
        CUSTOMER_ORDER_ADDRESS = findViewById(R.id.cust_address);
        CUSTOMER_ORDER_PAYMENT = findViewById(R.id.cust_payment);
        //REPORT_FEEDBACK = findViewById(R.id.report_feed_message);

        ORDER_DELIVERED = findViewById(R.id.order_delivered);
        ORDER_CANCEL = findViewById(R.id.order_cancel);

        BACK_PRESS = findViewById(R.id.fab_backPress);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);

        APPBAR_TITLE.setText("Customer Delivery Details");
        //back press activity

        BACK_PRESS.setOnClickListener(view -> Orders_Status_Detail.super.onBackPressed());
        get_customer_detail();

        SharedPreferences preferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String USER_ID = preferences.getString("USER_ID", "");
        String USER_NAME = preferences.getString("USER_NAME", "");

        DELIVERY_BOY_ID.setText(USER_ID);
        DELIVERY_BOY_NAME.setText(USER_NAME);

        ORDER_DELIVERED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Orders_Status_Detail.this, R.style.AlertDialogTheme);
                View view1 = LayoutInflater.from(Orders_Status_Detail.this).inflate(R.layout.delete_dialog,
                        (ConstraintLayout)findViewById(R.id.warning_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.dialog_title)).setText("Delivered");
                ((TextView) view1.findViewById(R.id.dialog_message)).setText("Are you sure confirm to delivered");
                ((Button) view1.findViewById(R.id.dialog_btn)).setText("NO");
                ((Button) view1.findViewById(R.id.dialog_btn2)).setText("YES");
                ((ImageView) view1.findViewById(R.id.dialog_image)).setImageResource(R.drawable.delivery_dining);

                final android.app.AlertDialog alertDialog = builder.create();

                view1.findViewById(R.id.dialog_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                view1.findViewById(R.id.dialog_btn2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = getIntent();
                        String CUST_ORDER_NO = intent.getStringExtra("order_no");
                        String CUST_ORDER_ID = intent.getStringExtra("cust_id");
                        String DELI_STATUS = String.valueOf("2");
                        String RATING = String.valueOf("2");
                        String DELI_BOY_ID = DELIVERY_BOY_ID.getText().toString();

                        progressDialog = ProgressDialog.show(Orders_Status_Detail.this, "Please wait...", null, true, false);

                        Handler handler = new Handler();
                        // Create a new Runnable for the delayed task
                        Runnable delayedTask = new Runnable() {
                            @Override
                            public void run() {
                                StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-delivery-app/salesorder_update.php",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if (response.equalsIgnoreCase("successfully")) {
                                                    Intent intent = new Intent(Orders_Status_Detail.this, MainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    Toast.makeText(Orders_Status_Detail.this, "Delivered successfully", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(Orders_Status_Detail.this, response, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                                            Toast.makeText(Orders_Status_Detail.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                ) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {

                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("order_no", CUST_ORDER_NO);
                                        params.put("cust_id", CUST_ORDER_ID);
                                        params.put("status", DELI_STATUS);
                                        params.put("rating", RATING);
                                        params.put("deli_id", DELI_BOY_ID);

                                        return params;
                                    }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(Orders_Status_Detail.this);
                                requestQueue.add(request);
                                handler.removeCallbacks(this);
                            }
                        };
                        handler.postDelayed(delayedTask, 5000);
                    }
                });

                if (alertDialog.getWindow() !=null){
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }
        });
        ORDER_CANCEL.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Orders_Status_Detail.this, R.style.AlertDialogTheme);
                View view1 = LayoutInflater.from(Orders_Status_Detail.this).inflate(R.layout.warning_cancel_feedback,
                        (LinearLayout) findViewById(R.id.report_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.report_title)).setText("CANCEL ORDER / REASONS");
                ((MaterialButton) view1.findViewById(R.id.report_no)).setText("NO");
                ((MaterialButton) view1.findViewById(R.id.report_yes)).setText("YES");

                ((TextInputEditText) view1.findViewById(R.id.report_feed_message)).setText("");


                final android.app.AlertDialog alertDialog = builder.create();

                CHECKBOX_ONE = view1.findViewById(R.id.checkbox_one);
                CHECKBOX_TWO = view1.findViewById(R.id.checkbox_two);
                CHECKBOX_THREE = view1.findViewById(R.id.checkbox_three);
                CHECKBOX_FOUR = view1.findViewById(R.id.checkbox_four);

                view1.findViewById(R.id.checkbox_one).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view ) {
                        boolean isChecked1 = ((CheckBox)view).isChecked();
                        checkboxVisible(isChecked1);

                        CHECKBOX_ONE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                                checkboxVisible(compoundButton.isChecked());
                            }
                        });
                    }

                    private void checkboxVisible(boolean isChecked) {
                        if (!isChecked) {
                            ((CheckBox) view1.findViewById(R.id.checkbox_two)).setEnabled(true);
                            ((CheckBox) view1.findViewById(R.id.checkbox_three)).setEnabled(true);
                            ((CheckBox) view1.findViewById(R.id.checkbox_four)).setEnabled(true);
                            ((MaterialButton) view1.findViewById(R.id.report_yes)).setEnabled(false);
                        }else {
                            ((CheckBox) view1.findViewById(R.id.checkbox_two)).setEnabled(false);
                            ((CheckBox) view1.findViewById(R.id.checkbox_three)).setEnabled(false);
                            ((CheckBox) view1.findViewById(R.id.checkbox_four)).setEnabled(false);
                            ((MaterialButton) view1.findViewById(R.id.report_yes)).setEnabled(true);
                        }
                    }
                });
                view1.findViewById(R.id.checkbox_two).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view ) {
                        boolean isChecked1 = ((CheckBox)view).isChecked();
                        checkboxVisible(isChecked1);

                        CHECKBOX_TWO.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                                checkboxVisible(compoundButton.isChecked());
                            }
                        });
                    }

                    private void checkboxVisible(boolean isChecked) {
                        if (!isChecked) {
                            ((CheckBox) view1.findViewById(R.id.checkbox_one)).setEnabled(true);
                            ((CheckBox) view1.findViewById(R.id.checkbox_three)).setEnabled(true);
                            ((CheckBox) view1.findViewById(R.id.checkbox_four)).setEnabled(true);
                            ((MaterialButton) view1.findViewById(R.id.report_yes)).setEnabled(false);
                        }else {
                            ((CheckBox) view1.findViewById(R.id.checkbox_one)).setEnabled(false);
                            ((CheckBox) view1.findViewById(R.id.checkbox_three)).setEnabled(false);
                            ((CheckBox) view1.findViewById(R.id.checkbox_four)).setEnabled(false);
                            ((MaterialButton) view1.findViewById(R.id.report_yes)).setEnabled(true);
                        }
                    }
                });
                view1.findViewById(R.id.checkbox_three).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view ) {
                        boolean isChecked1 = ((CheckBox)view).isChecked();
                        checkboxVisible(isChecked1);

                        CHECKBOX_THREE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                                checkboxVisible(compoundButton.isChecked());

                            }
                        });
                    }

                    private void checkboxVisible(boolean isChecked) {

                        if (!isChecked) {
                            ((CheckBox) view1.findViewById(R.id.checkbox_one)).setEnabled(true);
                            ((CheckBox) view1.findViewById(R.id.checkbox_two)).setEnabled(true);
                            ((CheckBox) view1.findViewById(R.id.checkbox_four)).setEnabled(true);
                            ((MaterialButton) view1.findViewById(R.id.report_yes)).setEnabled(false);
                        }else {
                            ((CheckBox) view1.findViewById(R.id.checkbox_one)).setEnabled(false);
                            ((CheckBox) view1.findViewById(R.id.checkbox_two)).setEnabled(false);
                            ((CheckBox) view1.findViewById(R.id.checkbox_four)).setEnabled(false);
                            ((MaterialButton) view1.findViewById(R.id.report_yes)).setEnabled(true);
                        }
                    }
                });
                view1.findViewById(R.id.checkbox_four).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view ) {
                        boolean isChecked1 = ((CheckBox)view).isChecked();
                        checkboxVisible(isChecked1);

                        CHECKBOX_FOUR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                                checkboxVisible(compoundButton.isChecked());

                            }
                        });
                    }

                    private void checkboxVisible(boolean isChecked) {

                        if (!isChecked) {
                            ((TextInputEditText) view1.findViewById(R.id.report_feed_message)).setEnabled(false);
                            ((CheckBox) view1.findViewById(R.id.checkbox_one)).setEnabled(true);
                            ((CheckBox) view1.findViewById(R.id.checkbox_two)).setEnabled(true);
                            ((CheckBox) view1.findViewById(R.id.checkbox_three)).setEnabled(true);
                            ((MaterialButton) view1.findViewById(R.id.report_yes)).setEnabled(false);
                        }else {
                            ((TextInputEditText) view1.findViewById(R.id.report_feed_message)).setEnabled(true);
                            ((CheckBox) view1.findViewById(R.id.checkbox_one)).setEnabled(false);
                            ((CheckBox) view1.findViewById(R.id.checkbox_two)).setEnabled(false);
                            ((CheckBox) view1.findViewById(R.id.checkbox_three)).setEnabled(false);
                            ((MaterialButton) view1.findViewById(R.id.report_yes)).setEnabled(true);
                        }
                    }
                });
                view1.findViewById(R.id.report_no).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                view1.findViewById(R.id.report_yes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StringBuilder result = new StringBuilder();
                        if (CHECKBOX_ONE.isChecked()){
                            result.append(CHECKBOX_ONE.getText().toString());
                        }
                        if (CHECKBOX_TWO.isChecked()){
                            result.append(CHECKBOX_TWO.getText().toString());
                        }
                        if (CHECKBOX_THREE.isChecked()){
                            result.append(CHECKBOX_THREE.getText().toString());
                        }

                        if (CHECKBOX_FOUR.isChecked()) {

                            String reason_edt = ((TextInputEditText) view1.findViewById(R.id.report_feed_message)).getText().toString();
                            if (reason_edt.isEmpty()) {
                                Toast.makeText(Orders_Status_Detail.this, "Reason is required", Toast.LENGTH_SHORT).show();
                                return; // Don't proceed further if the reason is empty
                            } else {
                                result.append(reason_edt);

                                String Reason = result.toString();

                                Intent intent = getIntent();
                                String CUST_ORDER_NO = intent.getStringExtra("order_no");
                                String CUST_ORDER_CUST_ID = intent.getStringExtra("cust_id");

                                String DELI_BOY_ID = DELIVERY_BOY_ID.getText().toString();
                                String status = String.valueOf("3");

                                progressDialog = ProgressDialog.show(Orders_Status_Detail.this, "Please wait...", null, true, false);

                                Handler handler = new Handler();
                                // Create a new Runnable for the delayed task
                                Runnable delayedTask = new Runnable() {
                                    @Override
                                    public void run() {

                                        StringRequest cancel_request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-delivery-app/salesordercancel.php",
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {

                                                        if (response.equalsIgnoreCase("canceled successfully")) {
                                                            Toast.makeText(Orders_Status_Detail.this, "canceled successfully", Toast.LENGTH_SHORT).show();
                                                            alertDialog.dismiss();
                                                            Intent intent = new Intent(Orders_Status_Detail.this, MainActivity.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                        } else {
                                                            Toast.makeText(Orders_Status_Detail.this, response, Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                                                    Toast.makeText(Orders_Status_Detail.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                        ) {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {

                                                Map<String, String> params = new HashMap<String, String>();
                                                params.put("order_no", CUST_ORDER_NO);
                                                params.put("cust_id", CUST_ORDER_CUST_ID);
                                                params.put("deli_id", DELI_BOY_ID);
                                                params.put("status", status);
                                                params.put("reason", Reason);

                                                return params;
                                            }
                                        };
                                        RequestQueue cancel_requestQueue = Volley.newRequestQueue(Orders_Status_Detail.this);
                                        cancel_requestQueue.add(cancel_request);
                                        handler.removeCallbacks(this);
                                    }
                                };
                                handler.postDelayed(delayedTask, 5000);
                            }
                        }
                        String Reason = result.toString();

                        Intent intent = getIntent();
                        String CUST_ORDER_NO = intent.getStringExtra("order_no");
                        String CUST_ORDER_CUST_ID = intent.getStringExtra("cust_id");

                        String DELI_BOY_ID = DELIVERY_BOY_ID.getText().toString();
                        String status = String.valueOf("3");

                        progressDialog = ProgressDialog.show(Orders_Status_Detail.this, "Please wait...", null, true, false);

                        Handler handler = new Handler();
                        // Create a new Runnable for the delayed task
                        Runnable delayedTask = new Runnable() {
                            @Override
                            public void run() {
                                StringRequest cancel_request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-delivery-app/salesordercancel.php",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                if (response.equalsIgnoreCase("canceled successfully")) {
                                                    Toast.makeText(Orders_Status_Detail.this, "canceled successfully", Toast.LENGTH_SHORT).show();
                                                    alertDialog.dismiss();
                                                    Intent intent = new Intent(Orders_Status_Detail.this, MainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(Orders_Status_Detail.this, response, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                                            Toast.makeText(Orders_Status_Detail.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                ) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {

                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("order_no", CUST_ORDER_NO);
                                        params.put("cust_id", CUST_ORDER_CUST_ID);
                                        params.put("deli_id", DELI_BOY_ID);
                                        params.put("status", status);
                                        params.put("reason", Reason);

                                        return params;
                                    }
                                };
                                RequestQueue cancel_requestQueue = Volley.newRequestQueue(Orders_Status_Detail.this);
                                cancel_requestQueue.add(cancel_request);
                                handler.removeCallbacks(this);
                            }
                        };
                        handler.postDelayed(delayedTask, 5000);
                    }
                });

                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }
        });
    }

    private void get_customer_detail() {
        Intent intent = getIntent();
        String CUST_ORDER_NO = intent.getStringExtra("order_no");
        String CUST_ORDER_DATE = intent.getStringExtra("order_date");
        String CUST_ORDER_ID = intent.getStringExtra("cust_id");
        String CUST_ORDER_AMOUNT = intent.getStringExtra("amount");
        String CUST_ORDER_QTY = intent.getStringExtra("qty");
        String CUST_Deli_Type = intent.getStringExtra("deli_type");
        String CUST_ORDER_ADD_ID = intent.getStringExtra("add_id");
        String CUST_ORDER_ADD_NAME = intent.getStringExtra("add_name");
        String CUST_ORDER_ADD_MOB = intent.getStringExtra("add_mobile");
        String CUST_ORDER_ADDRESS = intent.getStringExtra("add_address");
        String CUST_ORDER_STATE = intent.getStringExtra("add_state");
        String CUST_ORDER_CITY = intent.getStringExtra("add_city");
        String CUST_ORDER_PINCODE = intent.getStringExtra("add_pincode");

        if (intent != null) {

            //date change format start
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

            try {
                Date Orederd_date = inputFormat.parse(CUST_ORDER_DATE);
                String kdt = outputFormat.format(Orederd_date);

                CUSTOMER_ORDER_DATE.setText(kdt);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //date change format end

            String MDELITYPE;
            if (CUST_Deli_Type.equalsIgnoreCase("1")){
                MDELITYPE = "Cash On Delivery";
            }else {
                MDELITYPE = "Online Payment";
            }
            CUSTOMER_ORDER_PAYMENT.setText(MDELITYPE);

            double qty = Double.parseDouble(CUST_ORDER_QTY);
            CUSTOMER_ORDER_QTY.setText(String.valueOf(qty));

            CUSTOMER_ORDER_NO.setText(CUST_ORDER_NO);
            CUSTOMER_ORDER_NAME.setText(CUST_ORDER_ADD_NAME);
            CUSTOMER_ORDER_PHONE.setText(CUST_ORDER_ADD_MOB);
            CUSTOMER_ORDER_AMOUNT.setText("₹ "+CUST_ORDER_AMOUNT);
            CUSTOMER_ORDER_ADDRESS.setText(CUST_ORDER_ADDRESS+",\n"+CUST_ORDER_CITY+","+CUST_ORDER_STATE+",\n"+CUST_ORDER_PINCODE+".");
        }
    }
}