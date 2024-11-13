package in.vinsoftsolutions.jayagrocery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import in.vinsoftsolutions.jayagrocery.Models.ProfileModel;

import in.vinsoftsolutions.jayagrocery.R;

import in.vinsoftsolutions.jayagrocery.internet.NetworkChangeListener;

import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OtpVerification extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    AppCompatButton VERIFY,RESEND_OTP;
    MaterialTextView USER_ID,OTP_MOBILE_NO,OTP_TIMER;
    EditText OTP1,OTP2,OTP3,OTP4,OTP5,OTP6;

    public static String PREFS_NAME="myProfile";
    private int clearCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        OTP_MOBILE_NO = findViewById(R.id.otp_mobile_no);
        USER_ID = findViewById(R.id.user_id);

        RESEND_OTP =findViewById(R.id.send_otp_again);
        OTP_TIMER = findViewById(R.id.otp_timer);

        OTP1 = findViewById(R.id.inputOtp1);
        OTP2 = findViewById(R.id.inputOtp2);
        OTP3 = findViewById(R.id.inputOtp3);
        OTP4 = findViewById(R.id.inputOtp4);
        OTP5 = findViewById(R.id.inputOtp5);
        OTP6 = findViewById(R.id.inputOtp6);
        VERIFY = findViewById(R.id.Verify);

        setEditTextListeners(OTP1, OTP2, OTP1);
        setEditTextListeners(OTP2, OTP3, OTP1);
        setEditTextListeners(OTP3, OTP4, OTP2);
        setEditTextListeners(OTP4, OTP5, OTP3);
        setEditTextListeners(OTP5, OTP6, OTP4);
        setEditTextListeners(OTP6, OTP6, OTP5);
        OTP1.requestFocus();

        String MOBILE = getIntent().getStringExtra("mobile_no");
        OTP_MOBILE_NO.setText(MOBILE);

        VERIFY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String OTP = OTP1.getText().toString().trim() + OTP2.getText().toString().trim() +  OTP3.getText().toString().trim() +
                        OTP4.getText().toString().trim() + OTP5.getText().toString().trim() + OTP6.getText().toString().trim();
                if (OTP.isEmpty()){
                    Toast.makeText(OtpVerification.this, "Please Enter the OTP", Toast.LENGTH_SHORT).show();
                }else {
                    verify_otp();
                }
            }
        });

        RESEND_OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resend_otp();
                OTP_TIMER.setVisibility(View.VISIBLE);
                VERIFY.setVisibility(View.VISIBLE);
                RESEND_OTP.setVisibility(View.GONE);
                OTP1.requestFocus();
                CLEAR();
                startResendTimer();
            }
        });

        fetchData();
        startResendTimer();
    }

    // otp ediText input change listener start
    private void setEditTextListeners(final EditText currentEditText, final EditText nextEditText, final EditText previousEditText) {
        currentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called to notify you that the characters within `s` are about to be replaced
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method is called to notify you that somewhere within `s` changes have occurred
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is called to notify you that the characters within `s` have been replaced
                if (s.length() == 1) {
                    // Move focus to the next EditText
                    clearCounter = 0; // Reset the clear counter when a digit is entered
                    nextEditText.requestFocus();
                }
            }
        });

        currentEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // Handle backspace key to clear the text and move focus to the previous EditText
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (currentEditText.getText().length() == 0) {
                        clearCounter++;
                        if (clearCounter == 1) {
                            // Move focus to the previous EditText after clearing twice
                            clearCounter = 0;
                            currentEditText.clearFocus();
                            previousEditText.requestFocus();
                            // Consume the event
                            return true;
                        }
                    } else {
                        // Reset the counter if a character is entered
                        clearCounter = 0;
                    }
                }
                return false;
            }
        });
    }
    // otp ediText input change listener end
    private void startResendTimer() {
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                String mDuration = String.format(Locale.ENGLISH, "%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(l),
                        TimeUnit.MILLISECONDS.toSeconds(l) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));

                OTP_TIMER.setText(mDuration);
            }

            @Override
            public void onFinish() {
                RESEND_OTP.setVisibility(View.VISIBLE);
                OTP_TIMER.setVisibility(View.GONE);
                VERIFY.setVisibility(View.GONE);
                CLEAR();
                if (!OTP6.toString().trim().isEmpty()) {
                    OTP1.requestFocus();
                    int position = OTP6.length();
                    Editable CURSOR = OTP6.getText();
                    Selection.setSelection(CURSOR, position);
                }
            }
        }.start();
    }
    public void CLEAR(){
        OTP1.setText("");
        OTP2.setText("");
        OTP3.setText("");
        OTP4.setText("");
        OTP5.setText("");
        OTP6.setText("");
    }
    public void fetchData() {
        String MOBILE = getIntent().getStringExtra("mobile_no");

        StringRequest request = new StringRequest(Request.Method.POST,  "https://www.vinsoftsolutions.in/jaya-android/userprofile.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String CUST_ID = jsonObject.getString("cust_id");
                                String CUST_NAME = jsonObject.getString("cust_name");
                                String CUST_EMAIL = jsonObject.getString("cust_email");
                                String CUST_MOBILE = jsonObject.getString("cust_phone");
                                String CUST_IMG = jsonObject.getString("cust_img");

                                ProfileModel profileModel = new ProfileModel(CUST_ID,CUST_NAME,CUST_EMAIL,CUST_MOBILE,CUST_IMG);

                                OTP_MOBILE_NO.setText(profileModel.getCUST_MOBILE());
                                USER_ID.setText(profileModel.getCUST_ID());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(OtpVerification.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cust_phone", MOBILE);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    private void Resend_otp() {
        String OTP_MOBILE = OTP_MOBILE_NO.getText().toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST,  "https://vinsoftsolutions.in/jaya-android/otp.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OtpVerification.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("cust_phone",OTP_MOBILE);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(OtpVerification.this);
        requestQueue.add(request);
    }

    private void verify_otp() {
        final ProgressDialog progressDialog1 = new ProgressDialog(OtpVerification.this);
        progressDialog1.setMessage("Please wait...");
        progressDialog1.show();

        String USERID = USER_ID.getText().toString();

        String OTP = OTP1.getText().toString().trim() + OTP2.getText().toString().trim() +  OTP3.getText().toString().trim() +
                OTP4.getText().toString().trim() + OTP5.getText().toString().trim() + OTP6.getText().toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/otpverfication.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog1.dismiss();
                        if (response.equalsIgnoreCase("Successfully")){
                            startActivity(new Intent(OtpVerification.this, MainActivity.class));
                            finish();

                            SharedPreferences sharedPreferences1 = getSharedPreferences(OtpVerification.PREFS_NAME, 0);
                            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                            editor1.putBoolean("LoggedIn", true);
                            editor1.commit();

                            SharedPreferences preferences = getSharedPreferences("user_send_data", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("USER_ID", USERID);
                            editor.apply();

                            Toast.makeText(OtpVerification.this, response, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(OtpVerification.this, response, Toast.LENGTH_SHORT).show();
                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog1.dismiss();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(OtpVerification.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("otp", OTP);
                params.put("cust_id",USERID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(OtpVerification.this);
        requestQueue.add(request);
    }
    // network offline filter start
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
}