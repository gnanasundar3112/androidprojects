package in.vinsoftsolutions;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    TextInputEditText LOG_PHONE,LOG_PASSWORD;
    Button LOGIN ;
    private boolean passwordShowing = false;
    ImageView Password_Icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LOG_PHONE = (TextInputEditText) findViewById(R.id.log_phone);
        LOG_PASSWORD = (TextInputEditText) findViewById(R.id.log_password);
        Password_Icon = findViewById(R.id.password_icon);
        LOGIN = (Button) findViewById(R.id.login);

        Password_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // password is show or not
                if (passwordShowing){
                    passwordShowing = false;
                    LOG_PASSWORD.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);;
                    Password_Icon.setImageResource(R.drawable.eye);
                }else {
                    passwordShowing = true;
                    LOG_PASSWORD.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);;
                    Password_Icon.setImageResource(R.drawable.eye);
                }
                LOG_PASSWORD.setSelection(LOG_PASSWORD.length());
            }
        });

        LOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String USER = LOG_PHONE.getText().toString().trim();
                String PASSWORD = LOG_PASSWORD.getText().toString().trim();

                if (USER.isEmpty()) {
                    LOG_PHONE.setError("User Name is empty");
                }else {
                    if (PASSWORD.isEmpty()) {
                        LOG_PASSWORD.setError("Password is empty");
                    } else {
                        insertData();
                    }
                }
            }
        });
    }

    private void insertData() {
        final ProgressDialog progressDialog1 = new ProgressDialog(Login.this);
        progressDialog1.setMessage("Please wait...");

        progressDialog1.show();
        String MOBILE = LOG_PHONE.getText().toString().trim();
        String PASSWORD = LOG_PASSWORD.getText().toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST,  "https://vinsoftsolutions.in/jaya-delivery-app/login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog1.dismiss();
                        if (response.equalsIgnoreCase("Successfully login")) {

                            StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-delivery-app/otp.php",
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            String RESPONSE = response.trim();
                                            if (RESPONSE.equalsIgnoreCase("otp sent successfully")) {
                                                LOG_PHONE.setText("");
                                                LOG_PASSWORD.setText("");

                                                Toast.makeText(Login.this, "Login successfully", Toast.LENGTH_SHORT).show();


                                                Intent intent = new Intent(Login.this, OtpVerification.class);
                                                intent.putExtra("mobile_no",MOBILE);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("user_phone", MOBILE);

                                    return params;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
                            requestQueue.add(request);

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
                params.put("user_phone",MOBILE);
                params.put("user_pass",PASSWORD);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        requestQueue.add(request);
    }
}