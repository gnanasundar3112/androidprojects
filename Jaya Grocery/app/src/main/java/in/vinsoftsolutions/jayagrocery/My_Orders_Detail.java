package in.vinsoftsolutions.jayagrocery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import in.vinsoftsolutions.jayagrocery.Adapter.Orders_Detail_Adapter;
import in.vinsoftsolutions.jayagrocery.Models.Orders_Detail_Model;

import in.vinsoftsolutions.jayagrocery.R;

import in.vinsoftsolutions.jayagrocery.internet.NetworkChangeListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class My_Orders_Detail extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private FloatingActionButton BACK_PRESS;
    TextView APPBAR_TITLE,ORDERED_NO,ORDERE_DATE;
    private MaterialTextView DELI_DATE,ORDER_DATE,ORDER_STATUS,ORDER_HAS,RETURN_COUNT,ORDER_ITEM_TOTAL,ORDER_ROUND_OFF,ORDER_SHIPPING_FEE,ORDER_TOTAL;
    private ImageView CIRCLE_IMAGE1,CIRCLE_IMAGE2;
    private ProgressBar PROGRESSBAR,VERTICAL_PROGRESSBAR;
    private MaterialButton CANCEL_ORDER,NEED_HELP,RETURN_ORDER;
    private RatingBar RATINGS;
    private RecyclerView RECYCLER_ORDERS_DETAIL;
    private RecyclerView.Adapter ADAPTER_DETAIL;
    private RecyclerView.LayoutManager MANAGER_DETAIL;
    private List<Orders_Detail_Model> MODELS_DETAIL;

    private CheckBox CHECKBOX_ONE,CHECKBOX_TWO,CHECKBOX_THREE,CHECKBOX_FOUR;
    private AnimatorSet pulseAnimatorSet;
    private CountDownTimer countDownTimer;
    private boolean conditionExecuted = false;
    private AlertDialog Dialog,alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders_detail);

        BACK_PRESS = findViewById(R.id.fab_backPress);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);

        APPBAR_TITLE.setText("My Orders Details");
        //back press activity
        BACK_PRESS.setOnClickListener(view -> My_Orders_Detail.super.onBackPressed());

        DELI_DATE = findViewById(R.id.deli_date);
        ORDER_DATE = findViewById(R.id.ORDER_DATE);
        ORDER_STATUS = findViewById(R.id.deli_status);
        ORDER_HAS = findViewById(R.id.order_has_been);
        CIRCLE_IMAGE1 = findViewById(R.id.circle_img1);
        CIRCLE_IMAGE2 = findViewById(R.id.circle_img2);
        PROGRESSBAR = findViewById(R.id.progressbar_cancel);
        VERTICAL_PROGRESSBAR = findViewById(R.id.VERTICAL_PROGRESS);
        NEED_HELP = findViewById(R.id.need_help);
        CANCEL_ORDER = findViewById(R.id.cancel_btn);
        RETURN_ORDER = findViewById(R.id.return_btn);
        RETURN_COUNT = findViewById(R.id.return_count);
        RATINGS = findViewById(R.id.ratings);

        ORDER_ITEM_TOTAL = findViewById(R.id.order_prod_item_total);
        ORDER_ROUND_OFF = findViewById(R.id.order_round_off);
        ORDER_SHIPPING_FEE = findViewById(R.id.order_shipping_fee);
        ORDER_TOTAL = findViewById(R.id.order_total_amount);


        RECYCLER_ORDERS_DETAIL = findViewById(R.id.cancel_recycler);
        MANAGER_DETAIL = new GridLayoutManager(My_Orders_Detail.this, 1);
        RECYCLER_ORDERS_DETAIL.setLayoutManager(MANAGER_DETAIL);
        MODELS_DETAIL = new ArrayList<>();

        ORDERED_NO = findViewById(R.id.ORDER_DETAIL_NO);
        ORDERE_DATE = findViewById(R.id.ORDER_DETAIL_DATE);

        order_detail();
        rating_fetch();

        NEED_HELP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:8778423873"));
                startActivity(intent);
            }
        });
        CANCEL_ORDER.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(My_Orders_Detail.this, R.style.AlertDialogTheme);
                View view1 = LayoutInflater.from(My_Orders_Detail.this).inflate(R.layout.cancel_feedback_dialog,
                        (LinearLayout) findViewById(R.id.report_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.report_title)).setText("CANCEL ORDER / REASONS");
                ((MaterialButton) view1.findViewById(R.id.report_no)).setText("NO");
                ((MaterialButton) view1.findViewById(R.id.report_yes)).setText("YES");

                ((TextInputEditText) view1.findViewById(R.id.report_feed_message)).setText("");


                Dialog = builder.create();

                CHECKBOX_ONE = view1.findViewById(R.id.checkbox_one);
                CHECKBOX_TWO = view1.findViewById(R.id.checkbox_two);
                CHECKBOX_THREE = view1.findViewById(R.id.checkbox_three);
                CHECKBOX_FOUR = view1.findViewById(R.id.checkbox_four);

                view1.findViewById(R.id.checkbox_one).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isChecked1 = ((CheckBox) view).isChecked();
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
                        } else {
                            ((CheckBox) view1.findViewById(R.id.checkbox_two)).setEnabled(false);
                            ((CheckBox) view1.findViewById(R.id.checkbox_three)).setEnabled(false);
                            ((CheckBox) view1.findViewById(R.id.checkbox_four)).setEnabled(false);
                            ((MaterialButton) view1.findViewById(R.id.report_yes)).setEnabled(true);
                        }
                    }
                });
                view1.findViewById(R.id.checkbox_two).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isChecked1 = ((CheckBox) view).isChecked();
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
                        } else {
                            ((CheckBox) view1.findViewById(R.id.checkbox_one)).setEnabled(false);
                            ((CheckBox) view1.findViewById(R.id.checkbox_three)).setEnabled(false);
                            ((CheckBox) view1.findViewById(R.id.checkbox_four)).setEnabled(false);
                            ((MaterialButton) view1.findViewById(R.id.report_yes)).setEnabled(true);
                        }
                    }
                });
                view1.findViewById(R.id.checkbox_three).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isChecked1 = ((CheckBox) view).isChecked();
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
                        } else {
                            ((CheckBox) view1.findViewById(R.id.checkbox_one)).setEnabled(false);
                            ((CheckBox) view1.findViewById(R.id.checkbox_two)).setEnabled(false);
                            ((CheckBox) view1.findViewById(R.id.checkbox_four)).setEnabled(false);
                            ((MaterialButton) view1.findViewById(R.id.report_yes)).setEnabled(true);
                        }
                    }
                });
                view1.findViewById(R.id.checkbox_four).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isChecked1 = ((CheckBox) view).isChecked();
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
                        } else {
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
                        Dialog.dismiss();
                    }
                });
                view1.findViewById(R.id.report_yes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StringBuilder result = new StringBuilder();
                        if (CHECKBOX_ONE.isChecked()) {
                            result.append(CHECKBOX_ONE.getText().toString());
                            String Reason = result.toString();
                            insertCancelReason(Reason);
                        }else if (CHECKBOX_TWO.isChecked()) {
                            result.append(CHECKBOX_TWO.getText().toString());
                            String Reason = result.toString();
                            insertCancelReason(Reason);
                        }else if (CHECKBOX_THREE.isChecked()) {
                            result.append(CHECKBOX_THREE.getText().toString());
                            String Reason = result.toString();
                            insertCancelReason(Reason);
                        }else if (CHECKBOX_FOUR.isChecked()) {
                            String reason_edt = ((TextInputEditText) view1.findViewById(R.id.report_feed_message)).getText().toString();
                            if (reason_edt.isEmpty()) {
                                Toast.makeText(My_Orders_Detail.this, "Reason is required", Toast.LENGTH_SHORT).show();
                                return; // Don't proceed further if the reason is empty
                            } else {
                                result.append(reason_edt);
                                String Reason = result.toString();
                                insertCancelReason(Reason);
                            }
                        }else {
                            Toast.makeText(My_Orders_Detail.this, "Please Select Reason", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                if (Dialog.getWindow() != null) {
                    Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                Dialog.show();
            }
        });

        RETURN_ORDER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(My_Orders_Detail.this, R.style.AlertDialogTheme);
                View view1 = LayoutInflater.from(My_Orders_Detail.this).inflate(R.layout.item_return_dialog,
                        (LinearLayout) findViewById(R.id.report_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.report_title)).setText("RETURN ORDER / REASONS");
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
                    public void onClick(View view) {
                        boolean isChecked1 = ((CheckBox) view).isChecked();
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
                        } else {
                            ((CheckBox) view1.findViewById(R.id.checkbox_two)).setEnabled(false);
                            ((CheckBox) view1.findViewById(R.id.checkbox_three)).setEnabled(false);
                            ((CheckBox) view1.findViewById(R.id.checkbox_four)).setEnabled(false);
                            ((MaterialButton) view1.findViewById(R.id.report_yes)).setEnabled(true);
                        }
                    }
                });
                view1.findViewById(R.id.checkbox_two).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isChecked1 = ((CheckBox) view).isChecked();
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
                        } else {
                            ((CheckBox) view1.findViewById(R.id.checkbox_one)).setEnabled(false);
                            ((CheckBox) view1.findViewById(R.id.checkbox_three)).setEnabled(false);
                            ((CheckBox) view1.findViewById(R.id.checkbox_four)).setEnabled(false);
                            ((MaterialButton) view1.findViewById(R.id.report_yes)).setEnabled(true);
                        }
                    }
                });
                view1.findViewById(R.id.checkbox_three).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isChecked1 = ((CheckBox) view).isChecked();
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
                        } else {
                            ((CheckBox) view1.findViewById(R.id.checkbox_one)).setEnabled(false);
                            ((CheckBox) view1.findViewById(R.id.checkbox_two)).setEnabled(false);
                            ((CheckBox) view1.findViewById(R.id.checkbox_four)).setEnabled(false);
                            ((MaterialButton) view1.findViewById(R.id.report_yes)).setEnabled(true);
                        }
                    }
                });
                view1.findViewById(R.id.checkbox_four).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isChecked1 = ((CheckBox) view).isChecked();
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
                        } else {
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
                        if (CHECKBOX_ONE.isChecked()) {
                            result.append(CHECKBOX_ONE.getText().toString());
                        }
                        if (CHECKBOX_TWO.isChecked()) {
                            result.append(CHECKBOX_TWO.getText().toString());
                        }
                        if (CHECKBOX_THREE.isChecked()) {
                            result.append(CHECKBOX_THREE.getText().toString());
                        }

                        if (CHECKBOX_FOUR.isChecked()) {

                            String reason_edt = ((TextInputEditText) view1.findViewById(R.id.report_feed_message)).getText().toString();
                            if (reason_edt.isEmpty()) {
                                Toast.makeText(My_Orders_Detail.this, "Reason is required", Toast.LENGTH_SHORT).show();
                                return; // Don't proceed further if the reason is empty
                            } else {
                                result.append(reason_edt);

                                String Reason = result.toString();

                                Intent intent = getIntent();
                                String ORDER_NO = intent.getStringExtra("order_no");

                                SharedPreferences sharedPreferences = getSharedPreferences("user_send_data", MODE_PRIVATE);
                                String USER_ID = sharedPreferences.getString("USER_ID", "");

                                String status = String.valueOf("4");

                                StringRequest cancel_request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/salesorderreturn.php",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                if (response.equalsIgnoreCase("returned successfully")) {
                                                    Toast.makeText(My_Orders_Detail.this, "Return request send successfully", Toast.LENGTH_SHORT).show();
                                                    alertDialog.dismiss();
                                                    order_detail();
                                                    if (pulseAnimatorSet != null) {
                                                        pulseAnimatorSet.cancel(); // Stop the animation
                                                    }
                                                } else {
                                                    Toast.makeText(My_Orders_Detail.this, response, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                                            Toast.makeText(My_Orders_Detail.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                ) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {

                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("order_no", ORDER_NO);
                                        params.put("cust_id", USER_ID);
                                        params.put("status", status);
                                        params.put("reason", Reason);

                                        return params;
                                    }
                                };
                                RequestQueue cancel_requestQueue = Volley.newRequestQueue(My_Orders_Detail.this);
                                cancel_requestQueue.add(cancel_request);
                            }
                        }

                        String Reason = result.toString();

                        Intent intent = getIntent();
                        String ORDER_NO = intent.getStringExtra("order_no");

                        SharedPreferences sharedPreferences = getSharedPreferences("user_send_data", MODE_PRIVATE);
                        String USER_ID = sharedPreferences.getString("USER_ID", "");

                        String status = String.valueOf("4");

                        StringRequest cancel_request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/salesorderreturn.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        if (response.equalsIgnoreCase("returned successfully")) {
                                            Toast.makeText(My_Orders_Detail.this, "Return request send successfully", Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();
                                            order_detail();
                                            if (pulseAnimatorSet != null) {
                                                pulseAnimatorSet.cancel(); // Stop the animation
                                            }
                                        } else {
                                            Toast.makeText(My_Orders_Detail.this, response, Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                                    Toast.makeText(My_Orders_Detail.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        ) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {

                                Map<String, String> params = new HashMap<String, String>();
                                params.put("order_no", ORDER_NO);
                                params.put("cust_id", USER_ID);
                                params.put("status", status);
                                params.put("reason", Reason);

                                return params;
                            }
                        };
                        RequestQueue cancel_requestQueue = Volley.newRequestQueue(My_Orders_Detail.this);
                        cancel_requestQueue.add(cancel_request);
                    }
                });


                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }
        });

        String ORDER_NO = getIntent().getStringExtra("order_no");
        String ORDERED_DATE = getIntent().getStringExtra("order_date");
        ORDERED_NO.setText("Order No : "+ORDER_NO);

        //date change format start
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            Date Orederd_date = inputFormat.parse(ORDERED_DATE);
            String kdt = outputFormat.format(Orederd_date);
            ORDERE_DATE.setText("Order Date : "+kdt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //date change format end
    }


    public void insertCancelReason(String Reason){

        Intent intent = getIntent();
        String ORDER_NO = intent.getStringExtra("order_no");

        SharedPreferences sharedPreferences = getSharedPreferences("user_send_data", MODE_PRIVATE);
        String USER_ID = sharedPreferences.getString("USER_ID", "");

        String status = String.valueOf("3");

        StringRequest cancel_request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/salesordercancel.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("canceled successfully")) {
                            Toast.makeText(My_Orders_Detail.this, "canceled successfully", Toast.LENGTH_SHORT).show();
                            Dialog.dismiss();
                            order_detail();
                            if (pulseAnimatorSet != null) {
                                pulseAnimatorSet.cancel(); // Stop the animation
                            }
                        } else {
                            Toast.makeText(My_Orders_Detail.this, response, Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(My_Orders_Detail.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("order_no", ORDER_NO);
                params.put("cust_id", USER_ID);
                params.put("status", status);
                params.put("reason", Reason);

                return params;
            }
        };
        RequestQueue cancel_requestQueue = Volley.newRequestQueue(My_Orders_Detail.this);
        cancel_requestQueue.add(cancel_request);


    }

    private void startPulseAnimation(View view) {
        ObjectAnimator pulseAnimX = ObjectAnimator.ofFloat(view, "scaleX", 1.3f, 1.1f);
        ObjectAnimator pulseAnimY = ObjectAnimator.ofFloat(view, "scaleY", 1.3f, 1.1f);

        pulseAnimX.setDuration(500); // Adjust the duration as needed
        pulseAnimY.setDuration(500);

        pulseAnimX.setRepeatMode(ObjectAnimator.REVERSE);
        pulseAnimY.setRepeatMode(ObjectAnimator.REVERSE);

        pulseAnimX.setRepeatCount(ObjectAnimator.INFINITE);
        pulseAnimY.setRepeatCount(ObjectAnimator.INFINITE);

        pulseAnimX.setInterpolator(new AccelerateDecelerateInterpolator());
        pulseAnimY.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(view, "rotation", 0, 360);
        rotateAnim.setDuration(2000); // Adjust the duration as needed
        rotateAnim.setRepeatCount(ObjectAnimator.INFINITE);

        pulseAnimatorSet = new AnimatorSet();
        pulseAnimatorSet.playTogether(pulseAnimX, pulseAnimY, rotateAnim);
        pulseAnimatorSet.start();
    }

    private void order_detail() {
        String ORDER_NO = getIntent().getStringExtra("order_no");

        SharedPreferences sharedPreferences = getSharedPreferences("user_send_data", MODE_PRIVATE);
        String USER_ID = sharedPreferences.getString("USER_ID", "");

        PROGRESSBAR.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/order_prod_detail_fetch.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PROGRESSBAR.setVisibility(View.GONE);

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            MODELS_DETAIL.clear();
                            double item_total = 0;
                            double itemTotal = 0;

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String Cancel_Tamil = jsonObject.getString("tamil_name");
                                String Cancel_Name = jsonObject.getString("prod_name");
                                String Cancel_Order_No = jsonObject.getString("order_no");
                                String Cancel_Order_Date = jsonObject.getString("order_date");
                                String Cancel_Gram = jsonObject.getString("size_name");
                                String Cancel_Qty = jsonObject.getString("qty");
                                double Cancel_Rate = jsonObject.getDouble("amount");
                                String Cancel_Image = jsonObject.getString("prod_img");
                                String Cancel_Status = jsonObject.getString("status");
                                String Deli_Man_Id = jsonObject.getString("deli_man");
                                double Deli_Charg = jsonObject.getDouble("deli_charg");
                                String Rating = jsonObject.getString("rating");
                                String Crea_Date = jsonObject.getString("crea_date");
                                String Cancel_Cancel_Date = jsonObject.getString("modi_date");

                                Orders_Detail_Model cancelModel = new Orders_Detail_Model(Cancel_Tamil,Cancel_Name,Cancel_Order_No,Cancel_Order_Date,Cancel_Gram,Cancel_Qty,Cancel_Image,Cancel_Status,Cancel_Cancel_Date,Cancel_Rate);
                                MODELS_DETAIL.add(cancelModel);


                                itemTotal += Cancel_Rate;

                                item_total += Cancel_Rate + Deli_Charg;// CALCULATE FOR ALL VALUE

                                double roundedTotal = Math.round(item_total); // Round item_total to nearest integer
                                double inci = item_total - roundedTotal; // Calculate the fractional part
                                ORDER_ROUND_OFF.setText(String.valueOf(inci));

                                ORDER_SHIPPING_FEE.setText("₹ " + Deli_Charg);
                                // Remove non-numeric characters before parsing
                                String shippingFeeText = ORDER_SHIPPING_FEE.getText().toString().replaceAll("[^0-9.]", "");
                                double SHIPPING_FEE = Double.parseDouble(shippingFeeText);

                                ORDER_ITEM_TOTAL.setText("₹ " + String.format("%.1f", itemTotal));
                                // Remove non-numeric characters before parsing
                                String orderItemTotalText = ORDER_ITEM_TOTAL.getText().toString().replaceAll("[^0-9.]", "");
                                double order_item_total = Double.parseDouble(orderItemTotalText);


                                double TOTAL = order_item_total+SHIPPING_FEE;
                                ORDER_TOTAL.setText("₹ "+String.format("%.1f",TOTAL+inci));


                                //date change format start
                                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

                                try {
                                    Date Orederd_date = inputFormat.parse(Cancel_Order_Date);
                                    String kdt = outputFormat.format(Orederd_date);

                                    Date cancel_date = inputFormat.parse(Cancel_Cancel_Date);
                                    String kdt1 = outputFormat.format(cancel_date);

                                    ORDER_DATE.setText(kdt);
                                    DELI_DATE.setText(kdt1);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                //date change format end

                                ORDER_STATUS.setText(Cancel_Status);

                                String RATING = String.valueOf("2");
                                if (Rating.equalsIgnoreCase(RATING)){

                                    // Check if the dialog is already open
                                    if (alertDialog != null && alertDialog.isShowing()) {
                                        // Dialog is already open, no need to create a new one
                                        return;
                                    }

                                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(My_Orders_Detail.this, R.style.AlertDialogTheme);
                                    View view1 = LayoutInflater.from(My_Orders_Detail.this).inflate(R.layout.rating_feed_dialog,
                                            (LinearLayout) findViewById(R.id.rating_dialog));

                                    builder.setView(view1);

                                    alertDialog = builder.create();

                                    MaterialTextView DROPDOWN_TITTLE = (MaterialTextView) view1.findViewById(R.id.dropdown_title);
                                    String TITLE = String.valueOf("Send Feedback");
                                    DROPDOWN_TITTLE.setText(TITLE);


                                    MaterialButton cancel = (MaterialButton) view1.findViewById(R.id.dialog_cancel);
                                    MaterialButton send = (MaterialButton) view1.findViewById(R.id.dialog_send);
                                    TextInputEditText Remarks = (TextInputEditText) view1.findViewById(R.id.feed_dialog_detail);
                                    RatingBar Ratings = (RatingBar) view1.findViewById(R.id.dialog_rating);


                                    Ratings.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                        @Override
                                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                            // Log the rating value to see what is being received
                                            final int selectedRating = (int) rating;

                                            // Update the rating when the send button is clicked
                                            send.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    String REMARKS = Remarks.getText().toString();

                                                    if (REMARKS.isEmpty()){
                                                        Toast.makeText(My_Orders_Detail.this, "Please Enter Feedback", Toast.LENGTH_SHORT).show();
                                                    }else {

                                                        final ProgressDialog progressDialog = new ProgressDialog(My_Orders_Detail.this);
                                                        progressDialog.setMessage("Please wait...");

                                                        progressDialog.show();

                                                        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/rating.php",
                                                                new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        progressDialog.dismiss();
                                                                        if (response.equalsIgnoreCase("Send Successfully")) {
                                                                            rating_fetch();
                                                                            Toast.makeText(My_Orders_Detail.this, "Thank you for your Ratings", Toast.LENGTH_SHORT).show();
                                                                            alertDialog.dismiss();
                                                                        } else {
                                                                            Toast.makeText(My_Orders_Detail.this, response, Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                progressDialog.dismiss();
                                                                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                                                                    Toast.makeText(My_Orders_Detail.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }) {
                                                            @Override
                                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                                Map<String, String> params = new HashMap<String, String>();
                                                                params.put("cust_id", USER_ID);
                                                                params.put("order_no", Cancel_Order_No);
                                                                params.put("deli_man", Deli_Man_Id);
                                                                params.put("rating", String.valueOf(selectedRating));
                                                                params.put("remarks", REMARKS);
                                                                return params;
                                                            }
                                                        };

                                                        RequestQueue requestQueue = Volley.newRequestQueue(My_Orders_Detail.this);
                                                        requestQueue.add(request);
                                                    }
                                                }
                                            });
                                        }
                                    });

                                    cancel.setOnClickListener(new View.OnClickListener() {
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

                                String ORDER = ORDER_STATUS.getText().toString();
                                String ORDERED_STATUS = String.valueOf("Ordered");
                                String DELI_STATUS = String.valueOf("Delivered");
                                String CANCEL_STATUS = String.valueOf("Canceled");
                                String STATUS_BEEN = String.valueOf("Order has been delivered");
                                String CANCEL_STATUS_BEEN = String.valueOf("Order has been Canceled");

                                if (ORDERED_STATUS.equalsIgnoreCase(ORDER)){
                                    startPulseAnimation(CIRCLE_IMAGE1);
                                    ORDER_STATUS.setText(DELI_STATUS);
                                    DELI_DATE.setText("");
                                    CANCEL_ORDER.setVisibility(View.VISIBLE);
                                    RETURN_ORDER.setVisibility(View.GONE);
                                    CIRCLE_IMAGE1.setImageTintList(ColorStateList.valueOf(Color.parseColor("#1B5E20")));
                                }else if (DELI_STATUS.equalsIgnoreCase(ORDER)){
                                    CIRCLE_IMAGE1.setImageTintList(ColorStateList.valueOf(Color.parseColor("#1B5E20")));
                                    CIRCLE_IMAGE2.setImageTintList(ColorStateList.valueOf(Color.parseColor("#1B5E20")));
                                    VERTICAL_PROGRESSBAR.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#1B5E20")));
                                    ORDER_HAS.setText(STATUS_BEEN);
                                    CANCEL_ORDER.setVisibility(View.GONE);
                                    RETURN_ORDER.setVisibility(View.GONE);
                                    RATINGS.setVisibility(View.VISIBLE);
                                }else if (CANCEL_STATUS.equalsIgnoreCase(ORDER)){
                                    ORDER_STATUS.setTextColor(Color.parseColor("#EA495F"));
                                    CIRCLE_IMAGE2.setImageTintList(ColorStateList.valueOf(Color.parseColor("#EA495F")));
                                    CIRCLE_IMAGE1.setImageTintList(ColorStateList.valueOf(Color.parseColor("#EA495F")));
                                    VERTICAL_PROGRESSBAR.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#EA495F")));
                                    ORDER_HAS.setTextColor(Color.parseColor("#EA495F"));
                                    CANCEL_ORDER.setVisibility(View.GONE);
                                    ORDER_HAS.setText(CANCEL_STATUS_BEEN);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        ADAPTER_DETAIL = new Orders_Detail_Adapter(My_Orders_Detail.this, MODELS_DETAIL);
                        RECYCLER_ORDERS_DETAIL.setAdapter(ADAPTER_DETAIL);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PROGRESSBAR.setVisibility(View.GONE);
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(My_Orders_Detail.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                params.put("cust_id",USER_ID);
                params.put("order_no",ORDER_NO);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    // network offline filter start

    public void rating_fetch() {
        String ORDER_NO = getIntent().getStringExtra("order_no");

        SharedPreferences sharedPreferences = getSharedPreferences("user_send_data", MODE_PRIVATE);
        String USER_ID = sharedPreferences.getString("USER_ID", "");

        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/rating_fetch.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String RATING = jsonObject.getString("rating");

                                RATINGS = findViewById(R.id.ratings);
                                if ("1".equalsIgnoreCase(RATING)) {
                                    RATINGS.setRating(1);
                                } else if ("2".equalsIgnoreCase(RATING)) {
                                    RATINGS.setRating(2);
                                } else if ("3".equalsIgnoreCase(RATING)) {
                                    RATINGS.setRating(3);
                                } else if ("4".equalsIgnoreCase(RATING)) {
                                    RATINGS.setRating(4);
                                } else if ("5".equalsIgnoreCase(RATING)) {
                                    RATINGS.setRating(5);
                                }
                                RATINGS.setIsIndicator(true);
                                RATINGS.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#DAA82E")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(My_Orders_Detail.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                params.put("cust_id", USER_ID);
                params.put("order_no", ORDER_NO);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


    @Override
    protected void onStart() {
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
    }
    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
    // network offline filter End
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancel the countdown timer to avoid leaks
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}