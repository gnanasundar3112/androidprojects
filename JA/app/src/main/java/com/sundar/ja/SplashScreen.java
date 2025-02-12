package com.sundar.ja;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

import com.google.android.material.textview.MaterialTextView;

public class SplashScreen extends AppCompatActivity {
    ConstraintLayout Constraint_Anim;
    LinearLayout Linear_Logo;
    MaterialTextView App_Name, Create_by;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Constraint_Anim = findViewById(R.id.constraintLayout);
        Linear_Logo = findViewById(R.id.linear_logo);
        App_Name = findViewById(R.id.app_name);
        Create_by = findViewById(R.id.create_by);

        Linear_Logo.animate().translationY(4000).setDuration(2000).setStartDelay(2000);
        App_Name.animate().translationY(4000).setDuration(2000).setStartDelay(2000);
        Create_by.animate().translationY(4000).setDuration(2000).setStartDelay(2000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this,PartyMaster.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}