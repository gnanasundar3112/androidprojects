package com.example.sriganesh;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {

    private MaterialTextView log_Forgot_password;
    TextInputEditText Log_phone, Log_Password;
    Button LogIn ;
    public static String PREFS_NAME="myProfile";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log_phone = (TextInputEditText) findViewById(R.id.log_phone);
        Log_Password = (TextInputEditText) findViewById(R.id.log_password);
        LogIn = (Button) findViewById(R.id.login);
        log_Forgot_password = (MaterialTextView) findViewById(R.id.log_forgot_password);

        log_Forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(Login.this,ForgotPassword.class);
                // startActivity(intent);
                // finish();
            }
        });


        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String PHONE = Log_phone.getText().toString().trim();
                String PASSWORD = Log_Password.getText().toString().trim();

                if (PHONE.isEmpty()) {
                    Log_phone.setError("Mobile number is empty");
                } else {
                    if (PASSWORD.isEmpty()) {
                        Log_Password.setError("Password is empty");
                    } else {
                        //insertData();
                        startActivity(new Intent(Login.this, MainActivity.class));
                        finish();
                        SharedPreferences sharedPreferences1 = getSharedPreferences(Login.PREFS_NAME, 0);
                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();

                        editor1.putBoolean("LoggedIn", true);
                        editor1.commit();
                    }
                }
            }
        });
    }
        private void insertData() {
            final ProgressDialog progressDialog1 = new ProgressDialog(Login.this);
            progressDialog1.setMessage("Please wait...");

            progressDialog1.show();
            String PHONE = Log_phone.getText().toString().trim();
            String PASSWORD = Log_Password.getText().toString().trim();

            StringRequest request = new StringRequest(Request.Method.POST,  "http://192.168.59.112/sriganesh/login.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog1.dismiss();
                            if (response.equalsIgnoreCase("Successfully login")) {
                                Log_phone.setText("");
                                Log_Password.setText("");
                                startActivity(new Intent(Login.this, MainActivity.class));
                                finish();
                                Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Login.this,"Invalid user name or password", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog1.dismiss();
                    Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cust_phone",PHONE);
                    params.put("cust_pass",PASSWORD);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
            requestQueue.add(request);
        }
    }
