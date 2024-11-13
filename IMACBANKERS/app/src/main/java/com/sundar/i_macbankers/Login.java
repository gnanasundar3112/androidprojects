package com.sundar.i_macbankers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.sundar.i_macbankers.Models.LoginUser;
import com.sundar.i_macbankers.internet.NetworkChangeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private TextInputEditText USER_NAME,USER_PASS;
    private ImageView PASS_ICON;
    private AppCompatButton LOGIN;
    private boolean passwordShowing = false;
    private ArrayList<LoginUser> LOG_USER = new ArrayList<>();

    String login_url,UserName,UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_url = Links.login_url;

        USER_NAME = findViewById(R.id.log_userName);
        USER_PASS = findViewById(R.id.log_password);
        PASS_ICON = findViewById(R.id.password_icon);
        LOGIN = findViewById(R.id.login);

        PASS_ICON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // password is show or not
                if (passwordShowing){
                    passwordShowing = false;
                    USER_PASS.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);;
                    PASS_ICON.setImageResource(R.drawable.eye);
                }else {
                    passwordShowing = true;
                    USER_PASS.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);;
                    PASS_ICON.setImageResource(R.drawable.eye);
                }
                USER_PASS.setSelection(USER_PASS.length());
            }
        });

        LOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String User_Name = USER_NAME.getText().toString();
                String User_Pass = USER_PASS.getText().toString();
                if (User_Name.isEmpty()) {
                    Toast.makeText(Login.this, "Please Enter User Name ", Toast.LENGTH_SHORT).show();
                } else if (User_Pass.isEmpty()) {
                    Toast.makeText(Login.this, "Please Enter Password ", Toast.LENGTH_SHORT).show();
                } else{
                    loginUser(User_Name,User_Pass);
                }
            }
        });
    }

    private void loginUser(String User_Name, String User_Pass) {
        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST,  login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String responses = response.toString().trim();

                        progressDialog.dismiss();

                        if (responses.equalsIgnoreCase("Invalid User")) {
                            Toast.makeText(Login.this,"Invalid user name or password", Toast.LENGTH_SHORT).show();
                        } else {

                            String User_Name = USER_NAME.getText().toString();
                            UserName = User_Name;

                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Login.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user_id", UserId);
                            editor.putString("user_name", UserName);
                            editor.apply();

                            Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Login.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_name",User_Name);
                params.put("user_pass",User_Pass);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        requestQueue.add(request);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);

    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }

}