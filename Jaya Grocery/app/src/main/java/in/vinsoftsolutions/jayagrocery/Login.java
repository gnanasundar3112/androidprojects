package in.vinsoftsolutions.jayagrocery;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import in.vinsoftsolutions.jayagrocery.internet.NetworkChangeListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    TextInputEditText Log_Phone,Log_Password;
    ImageView Password_Icon;
    MaterialTextView Forgot_password,Log_Register;
    AppCompatButton LogIn;
    RelativeLayout Google_Log;
    private boolean passwordShowing = false;
    String[] PERMISSION = new String[]{Manifest.permission.POST_NOTIFICATIONS};
    boolean PERMISSION_POST_NOTIFICATION = false;
    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log_Phone = findViewById(R.id.log_phone);
        Log_Password = findViewById(R.id.log_password);
        Password_Icon = findViewById(R.id.password_icon);

        Forgot_password = findViewById(R.id.forgot_password);

        LogIn = findViewById(R.id.login);
        Google_Log = findViewById(R.id.google_login);
        Log_Register = findViewById(R.id.log_register);

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String USER_PHONE = Log_Phone.getText().toString();
                String USER_PASS = Log_Password.getText().toString();
                if (USER_PHONE.isEmpty()) {
                    Toast.makeText(Login.this, "Please Enter Mobile No ", Toast.LENGTH_SHORT).show();
                } else if (USER_PASS.isEmpty()) {
                    Toast.makeText(Login.this, "Please Enter Password ", Toast.LENGTH_SHORT).show();
                } else{
                    if (!PERMISSION_POST_NOTIFICATION){
                        requestPermissions();
                    }else {
                        insertData();
                    }

                }
            }
        });

        Google_Log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this, "Not Available", Toast.LENGTH_SHORT).show();
            }
        });

        Password_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // password is show or not
                if (passwordShowing){
                    passwordShowing = false;
                    Log_Password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);;
                    Password_Icon.setImageResource(R.drawable.eye);
                }else {
                    passwordShowing = true;
                    Log_Password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);;
                    Password_Icon.setImageResource(R.drawable.eye);
                }
                Log_Password.setSelection(Log_Password.length());
            }
        });

        Log_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });
        Forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Login_forget_pass_otp.class);
                startActivity(intent);
            }
        });
    }

    private void insertData() {
        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Please wait...");

        progressDialog.show();
        String CUST_PHONE = Log_Phone.getText().toString().trim();
        String PASSWORD = Log_Password.getText().toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST,  "https://vinsoftsolutions.in/jaya-android/login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if (response.equalsIgnoreCase("Successfully login")) {
                            StringRequest request = new StringRequest(Request.Method.POST,  "https://vinsoftsolutions.in/jaya-android/otp.php",
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            String respons = response.trim();
                                            if (respons.equalsIgnoreCase("otp sent successfully")) {
                                                Log_Phone.setText("");
                                                Log_Password.setText("");

                                                Intent intent = new Intent(Login.this, OtpVerification.class);
                                                intent.putExtra("mobile_no",CUST_PHONE);
                                                startActivity(intent);
                                                finish();

                                                Toast.makeText(Login.this, "Login successfully", Toast.LENGTH_SHORT).show();
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
                                    params.put("cust_phone",CUST_PHONE);
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
                progressDialog.dismiss();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Login.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("cust_phone",CUST_PHONE);
                params.put("cust_pass",PASSWORD);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
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


    @SuppressLint("NewApi")
    void requestPermissions(){
        if (ContextCompat.checkSelfPermission(Login.this,PERMISSION[0]) == PackageManager.PERMISSION_GRANTED){
            PERMISSION_POST_NOTIFICATION= true;
        }else {
            if(shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)){
                Log.d("Permission","inside else first time don't allow");
            }else {
                Log.d("Permission","inside else Second time don't allow");
            }
            resultLauncher.launch(PERMISSION[0]);
        }
    }
    private ActivityResultLauncher<String> resultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted->{
        if (isGranted){
            PERMISSION_POST_NOTIFICATION = true;
        }else {
            PERMISSION_POST_NOTIFICATION = false;
            showPermissionDialog();
        }
    });

    @SuppressLint("MissingInflatedId")
    private void showPermissionDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Login.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(Login.this).inflate(R.layout.allownotidialog,
                (ConstraintLayout)findViewById(R.id.notifi_dialog));

        builder.setView(view);

        final android.app.AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.notifi_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.notifi_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",getPackageName(),null);
                intent.setData(uri);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() !=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}