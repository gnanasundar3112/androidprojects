package in.vinsoftsolutions.jayagrocery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import in.vinsoftsolutions.jayagrocery.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.HashMap;
import java.util.Map;

public class Log_New_Password extends AppCompatActivity {

    MaterialTextView MUSER_PHONE;
    AppCompatButton CHANGE_PASSWORD;
    TextView APPBAR_TITLE;
    private FloatingActionButton BACK_PRESS;
    TextInputEditText NEW_PASSWORD,NEW_CON_PASSWORD;
    ImageView NEW_PASS_ICON,NEW_CON_ICON;
    CardView CHECK_ONE,CHECK_TWO,CHECK_THREE,CHECK_FOUR;
    private boolean MINIMUM8LETTERS=false,MINIMUM_ONE_UPPERCASE=false,MINIMUM_ONE_NUMBER=false,MINIMUM_ONE_SYMBOL=false,REGISTER_CLICKABLE=false;
    private boolean PasswordShowing = false;
    private boolean ConPassShowing = false;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_new_password);
        BACK_PRESS = findViewById(R.id.fab_backPress);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);

        MUSER_PHONE = findViewById(R.id.phone_no);
        CHANGE_PASSWORD = findViewById(R.id.change_passeord);

        NEW_PASSWORD = findViewById(R.id.new_password);
        NEW_CON_PASSWORD = findViewById(R.id.new_con_password);
        NEW_PASS_ICON = findViewById(R.id.new_password_icon);
        NEW_CON_ICON = findViewById(R.id.new_con_pass_icon);

        CHECK_ONE = findViewById(R.id.check_one);
        CHECK_TWO = findViewById(R.id.check_two);
        CHECK_THREE = findViewById(R.id.check_three);
        CHECK_FOUR = findViewById(R.id.check_four);

        APPBAR_TITLE.setText("New Password");
        //back press activity
        BACK_PRESS.setOnClickListener(view -> Log_New_Password.super.onBackPressed());

        String MOBILE = getIntent().getStringExtra("mobile_no");
        MUSER_PHONE.setText(MOBILE);

        NEW_PASS_ICON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // password is show or not
                if (PasswordShowing){
                    PasswordShowing = false;
                    NEW_PASSWORD.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);;
                    NEW_PASS_ICON.setImageResource(R.drawable.eye);
                }else {
                    PasswordShowing = true;
                    NEW_PASSWORD.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);;
                    NEW_PASS_ICON.setImageResource(R.drawable.eye);
                }
                NEW_PASSWORD.setSelection(NEW_PASSWORD.length());
            }
        });
        NEW_CON_ICON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // password is show or not
                if (ConPassShowing){
                    ConPassShowing = false;
                    NEW_CON_PASSWORD.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);;
                    NEW_CON_ICON.setImageResource(R.drawable.eye);
                }else {
                    ConPassShowing = true;
                    NEW_CON_PASSWORD.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);;
                    NEW_CON_ICON.setImageResource(R.drawable.eye);
                }
                NEW_CON_PASSWORD.setSelection(NEW_CON_PASSWORD.length());
            }
        });

        CHANGE_PASSWORD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String USER_NEW_PASS = NEW_PASSWORD.getText().toString();
                String USER_NEW_CON_PASS = NEW_CON_PASSWORD.getText().toString();


                if (USER_NEW_PASS.isEmpty()) {
                    Toast.makeText(Log_New_Password.this, "Please Enter the New Password ", Toast.LENGTH_SHORT).show();
                }else if (USER_NEW_CON_PASS.isEmpty()) {
                    Toast.makeText(Log_New_Password.this, "Please Enter Confirm Password ", Toast.LENGTH_SHORT).show();
                }else if (!USER_NEW_PASS.equals(USER_NEW_CON_PASS)){
                    Toast.makeText(Log_New_Password.this, "Password not matching ", Toast.LENGTH_SHORT).show();
                }else if (MINIMUM8LETTERS && MINIMUM_ONE_NUMBER && MINIMUM_ONE_SYMBOL && MINIMUM_ONE_UPPERCASE &&  NEW_PASSWORD.length() > 0) {
                    insertNewPass();
                }else {
                    Toast.makeText(Log_New_Password.this, "Password Criteria not fulfilled", Toast.LENGTH_SHORT).show();
                }
            }

        });
        inputChange();
    }

    @SuppressLint("ResourceType")
    private void passwordCheck(){
        String CUST_PASSWORD = NEW_PASSWORD.getText().toString();

        if (CUST_PASSWORD.length() >=8){
            MINIMUM8LETTERS = true;
            CHECK_ONE.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }else {
            MINIMUM8LETTERS = false;
            CHECK_ONE.setCardBackgroundColor(Color.parseColor(getString(R.color.white)));
        }

        if (CUST_PASSWORD.matches("(.*[A-Z].*)")){
            MINIMUM_ONE_UPPERCASE = true;
            CHECK_TWO.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }else {
            MINIMUM_ONE_UPPERCASE = false;
            CHECK_TWO.setCardBackgroundColor(Color.parseColor(getString(R.color.white)));
        }

        if (CUST_PASSWORD.matches("(.*[0-9].*)")){
            MINIMUM_ONE_NUMBER = true;
            CHECK_THREE.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }else {
            MINIMUM_ONE_NUMBER = false;
            CHECK_THREE.setCardBackgroundColor(Color.parseColor(getString(R.color.white)));
        }

        if (CUST_PASSWORD.matches("^(?=.*[_.!@#$%^&*()+=<>?/:'~`]).*$")){
            MINIMUM_ONE_SYMBOL = true;
            CHECK_FOUR.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }else {
            MINIMUM_ONE_SYMBOL = false;
            CHECK_FOUR.setCardBackgroundColor(Color.parseColor(getString(R.color.white)));
        }
    }
    private void inputChange(){
        NEW_PASSWORD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void insertNewPass() {

        final ProgressDialog progressDialog = new ProgressDialog(Log_New_Password.this);
        progressDialog.setMessage("Please wait...");

        String NEW_PASS = NEW_PASSWORD.getText().toString();
        String USER_PHONE = MUSER_PHONE.getText().toString();

        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/log_foget_pass.php",
                new Response.Listener<String>() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        if (response.equalsIgnoreCase("changed successfully")) {
                            Intent intent = new Intent(Log_New_Password.this, Login.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            Toast.makeText(Log_New_Password.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Log_New_Password.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Log_New_Password.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("new_pass",NEW_PASS);
                params.put("cust_phone",USER_PHONE);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Log_New_Password.this);
        requestQueue.add(request);
    }
}