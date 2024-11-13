package in.vinsoftsolutions.jayagrocery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Login_forget_pass_otp extends AppCompatActivity {

    private FloatingActionButton BACK_PRESS;
    TextView APPBAR_TITLE;
    TextInputEditText MOBILE_NO;
    AppCompatButton VERIFY,SEND_OTP;
    MaterialTextView OTP_MOBILE_NO,OTP_TIMER;
    EditText OTP1,OTP2,OTP3,OTP4,OTP5,OTP6;
    private int clearCounter = 0;
    LinearLayout OTP_LINEAR,MOBILE_LINEAR;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_forget_pass);

        BACK_PRESS = findViewById(R.id.fab_backPress);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);

        APPBAR_TITLE.setText("Password Change");
        //back press activity
        BACK_PRESS.setOnClickListener(view -> Login_forget_pass_otp.super.onBackPressed());

        MOBILE_NO = findViewById(R.id.log_forgrt_phone);
        VERIFY = findViewById(R.id.log_forget_otp_verify);
        SEND_OTP = findViewById(R.id.log_forget_send_otp);

        OTP1 = findViewById(R.id.forget_Otp1);
        OTP2 = findViewById(R.id.forget_Otp2);
        OTP3 = findViewById(R.id.forget_Otp3);
        OTP4 = findViewById(R.id.forget_Otp4);
        OTP5 = findViewById(R.id.forget_Otp5);
        OTP6 = findViewById(R.id.forget_Otp6);
        OTP_TIMER = findViewById(R.id.forget_otp_timer);

        setEditTextListeners(OTP1, OTP2, OTP1);
        setEditTextListeners(OTP2, OTP3, OTP1);
        setEditTextListeners(OTP3, OTP4, OTP2);
        setEditTextListeners(OTP4, OTP5, OTP3);
        setEditTextListeners(OTP5, OTP6, OTP4);
        setEditTextListeners(OTP6, OTP6, OTP5);

        OTP_LINEAR = findViewById(R.id.otp_linear);
        MOBILE_LINEAR = findViewById(R.id.mobile_linear);
        OTP_MOBILE_NO = findViewById(R.id.log_otp_mobile_no);

        SEND_OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Mobile = MOBILE_NO.getText().toString();
                if (Mobile.isEmpty()){
                    Toast.makeText(Login_forget_pass_otp.this, "Please Enter the Mobile No", Toast.LENGTH_SHORT).show();
                }else if (Mobile.length() < 10) {
                    Toast.makeText(Login_forget_pass_otp.this, "Please check correct Mobile number", Toast.LENGTH_SHORT).show();
                } else if (!Mobile.matches("[0-9]+")) {
                    Toast.makeText(Login_forget_pass_otp.this, "Please enter a valid phone number with numbers only", Toast.LENGTH_SHORT).show();
                }else {
                    otpSending();
                }
            }
        });
        VERIFY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String OTP = OTP1.getText().toString().trim() + OTP2.getText().toString().trim() +  OTP3.getText().toString().trim() +
                        OTP4.getText().toString().trim() + OTP5.getText().toString().trim() + OTP6.getText().toString().trim();
                if (OTP.isEmpty()){
                    Toast.makeText(Login_forget_pass_otp.this, "Please Enter the OTP", Toast.LENGTH_SHORT).show();
                }else {
                    verify_otp();
                }
            }
        });
    }

    public void CLEAR(){
        OTP1.setText("");
        OTP2.setText("");
        OTP3.setText("");
        OTP4.setText("");
        OTP5.setText("");
        OTP6.setText("");
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
                OTP_LINEAR.setVisibility(View.GONE);
                MOBILE_LINEAR.setVisibility(View.VISIBLE);
                OTP_TIMER.setVisibility(View.GONE);
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
    private void otpSending(){
        final ProgressDialog progressDialog = new ProgressDialog(Login_forget_pass_otp.this);
        progressDialog.setMessage("Please wait...");

        progressDialog.show();

        String CUST_PHONE = MOBILE_NO.getText().toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST,  "https://vinsoftsolutions.in/jaya-android/forget_otp.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String message = response.toString().trim();
                        progressDialog.dismiss();
                        if (message.equalsIgnoreCase("Please sign in")){
                            Toast.makeText(Login_forget_pass_otp.this, "Phone number not registered. Registering the phone number...", Toast.LENGTH_LONG).show();
                        } else if (message.equalsIgnoreCase("otp sent successfully")){
                            OTP_MOBILE_NO.setText(CUST_PHONE);
                            OTP_LINEAR.setVisibility(View.VISIBLE);
                            OTP_TIMER.setVisibility(View.VISIBLE);
                            VERIFY.setVisibility(View.VISIBLE);
                            MOBILE_LINEAR.setVisibility(View.GONE);
                            startResendTimer();
                            Toast.makeText(Login_forget_pass_otp.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Login_forget_pass_otp.this, "OTP sent failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Login_forget_pass_otp.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("cust_phone",CUST_PHONE);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Login_forget_pass_otp.this);
        requestQueue.add(request);
    }
    private void verify_otp() {
        final ProgressDialog progressDialog1 = new ProgressDialog(Login_forget_pass_otp.this);
        progressDialog1.setMessage("Please wait...");
        progressDialog1.show();

        String Mobile = OTP_MOBILE_NO.getText().toString();

        String OTP = OTP1.getText().toString().trim() + OTP2.getText().toString().trim() +  OTP3.getText().toString().trim() +
                OTP4.getText().toString().trim() + OTP5.getText().toString().trim() + OTP6.getText().toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/log_forget_otp_verify.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog1.dismiss();
                        if (response.equalsIgnoreCase("Success")){
                            Intent intent = new Intent(Login_forget_pass_otp.this, Log_New_Password.class);
                            intent.putExtra("mobile_no",Mobile);
                            startActivity(intent);
                            finish();

                            Toast.makeText(Login_forget_pass_otp.this, response, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Login_forget_pass_otp.this, response, Toast.LENGTH_SHORT).show();
                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog1.dismiss();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Login_forget_pass_otp.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("otp", OTP);
                params.put("cust_phone",Mobile);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Login_forget_pass_otp.this);
        requestQueue.add(request);
    }
}