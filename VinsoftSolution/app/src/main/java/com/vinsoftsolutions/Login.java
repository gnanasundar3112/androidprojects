package com.vinsoftsolutions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

public class Login extends AppCompatActivity {

    TextInputEditText USER_NAME,PASSWORD;
    ImageView PASS_ICON;
    AppCompatButton LOGIN_BTN;
    private boolean PASSWORD_SHOWING = false;
    public static String PREFS_NAME="LOGIN_PROFILE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        USER_NAME = findViewById(R.id.log_username);
        PASSWORD = findViewById(R.id.log_password);
        PASS_ICON = findViewById(R.id.password_icon);
        LOGIN_BTN = findViewById(R.id.login);

        LOGIN_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserName = USER_NAME.getText().toString();
                String UserPass = PASSWORD.getText().toString();
                if (UserName.isEmpty()) {
                    Toast.makeText(Login.this, "Please Enter User Name", Toast.LENGTH_SHORT).show();
                } else if (UserPass.isEmpty()) {
                    Toast.makeText(Login.this, "Please Enter Password ", Toast.LENGTH_SHORT).show();
                } else{
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();

                    SharedPreferences sharedPreferences1 = getSharedPreferences(Login.PREFS_NAME, 0);
                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();

                    editor1.putBoolean("LoggedIn", true);
                    editor1.commit();
                }
            }
        });

        PASS_ICON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // password is show or not
                if (PASSWORD_SHOWING){
                    PASSWORD_SHOWING = false;
                    PASSWORD.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);;
                    PASS_ICON.setImageResource(R.drawable.eye);
                }else {
                    PASSWORD_SHOWING = true;
                    PASSWORD.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);;
                    PASS_ICON.setImageResource(R.drawable.eye);
                }
                PASSWORD.setSelection(PASSWORD.length());
            }
        });
    }
}