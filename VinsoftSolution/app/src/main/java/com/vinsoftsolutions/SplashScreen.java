package com.vinsoftsolutions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textview.MaterialTextView;

public class SplashScreen extends AppCompatActivity {

    LinearLayout LINEAR_LOGO;
    MaterialTextView APP_NAME,CREATE_BY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        LINEAR_LOGO = findViewById(R.id.linear_logo);
        APP_NAME = findViewById(R.id.app_name);
        CREATE_BY = findViewById(R.id.create_by);

        LINEAR_LOGO.animate().translationY(4000).setDuration(2000).setStartDelay(2000);
        APP_NAME.animate().translationY(4000).setDuration(2000).setStartDelay(2000);
        CREATE_BY.animate().translationY(4000).setDuration(2000).setStartDelay(2000);

            new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME,0);
                boolean LoggedIn = sharedPreferences.getBoolean("LoggedIn",false);

                if (LoggedIn){
                    Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(SplashScreen.this,Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }
}