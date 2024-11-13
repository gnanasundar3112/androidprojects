package com.example.adminpanel;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.L;
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

    TextInputEditText LOG_USER,LOG_PASSWORD;
    Button LOGIN ;
    public static String PREFS_NAME="myProfile";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LOG_USER = (TextInputEditText) findViewById(R.id.log_user);
        LOG_PASSWORD = (TextInputEditText) findViewById(R.id.log_password);
        LOGIN = (Button) findViewById(R.id.login);

        LOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String USER = LOG_USER.getText().toString().trim();
                String PASSWORD = LOG_PASSWORD.getText().toString().trim();

                if (USER.isEmpty()) {
                    LOG_USER.setError("Mobile number is empty");
                }else {
                    if (PASSWORD.isEmpty()) {
                        LOG_PASSWORD.setError("Password is empty");
                    } else {
                        insertData();

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
        String USER = LOG_USER.getText().toString().trim();
        String PASSWORD = LOG_PASSWORD.getText().toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST,  "https://caustic-rinses.000webhostapp.com/adminpanel/admin_user.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog1.dismiss();
                        if (response.equalsIgnoreCase("Successfully login")) {
                            LOG_USER.setText("");
                            LOG_PASSWORD.setText("");
                            startActivity(new Intent(Login.this, MainActivity.class));
                            finish();
                            Toast.makeText(Login.this,"Login successfully", Toast.LENGTH_SHORT).show();

                            //Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
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
                params.put("user_name",USER);
                params.put("user_pass",PASSWORD);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}