package in.vinsoftsolutions.aspn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

public class Login extends AppCompatActivity {
    TextInputEditText Log_user,Log_Password;
    ImageView Password_Icon;
    AppCompatButton LogIn;
    private boolean passwordShowing = false;
    public static String PREFS_NAME="myProfile";
    final String username= "admin";
    final String pass= "admin123";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log_user = findViewById(R.id.log_username);
        Log_Password = findViewById(R.id.log_password);
        Password_Icon = findViewById(R.id.password_icon);
        LogIn = findViewById(R.id.login);

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String USER_NAME = Log_user.getText().toString();
                String USER_PASS = Log_Password.getText().toString();

                if (USER_NAME.isEmpty()) {
                    Toast.makeText(Login.this, "Please Enter User Name ", Toast.LENGTH_SHORT).show();
                } else if (USER_PASS.isEmpty()) {
                    Toast.makeText(Login.this, "Please Enter Password ", Toast.LENGTH_SHORT).show();
                } else{
                    final ProgressDialog progressDialog = new ProgressDialog(Login.this);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();

                    if (username.equals(USER_NAME) && pass.equals(USER_PASS)) {
                        SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("LoggedIn", true);
                        editor.commit();

                        Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }
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
    }
}