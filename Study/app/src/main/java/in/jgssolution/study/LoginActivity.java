package in.jgssolution.study;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import in.jgssolution.study.Api.LoginApi;
import in.jgssolution.study.CallbackMessage.ResponseCallback;
import in.jgssolution.study.Internet.NetworkChangeListener;

public class LoginActivity extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private TextInputEditText STUD_NAME, STUD_PASS;
    private AppCompatButton LOGIN;
    private MaterialTextView LOG_REGISTER;
    private ImageView PASS_ICON;
    private boolean passwordShowing = false;
    public static String PREFS_NAME="login";
    private LoginApi LOGIN_API;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        STUD_NAME = findViewById(R.id.user_name);
        STUD_PASS = findViewById(R.id.user_pass);
        PASS_ICON = findViewById(R.id.password_icon);
        LOGIN = findViewById(R.id.login);
        LOG_REGISTER = findViewById(R.id.log_register);

        //USE USER VALID CLASS
        LOGIN_API = new LoginApi(LoginActivity.this);

        PASS_ICON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // password is show or not
                if (passwordShowing){
                    passwordShowing = false;
                    STUD_PASS.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    PASS_ICON.setImageResource(R.drawable.eye);
                }else {
                    passwordShowing = true;
                    STUD_PASS.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    PASS_ICON.setImageResource(R.drawable.eye_off);
                }
                STUD_PASS.setSelection(STUD_PASS.length());
            }
        });

        LOG_REGISTER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        LOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studName = STUD_NAME.getText().toString().trim();
                String studPass = STUD_PASS.getText().toString().trim();

                if (studName.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter user name", Toast.LENGTH_SHORT).show();
                } else if (studPass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                }else {
                    LOGIN_API.validUserNameAndPassword(studName, studPass, new ResponseCallback() {
                        @Override
                        public void onSuccess(String message) {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            // Navigate to the next screen
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        @Override
                        public void onFailure(String error) {
                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeListener);
    }
}