package com.example.gifts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout constraint_anim;
    LinearLayout linear_logo;
    MaterialTextView app_name,function_Name,my_logo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        constraint_anim = findViewById(R.id.constraintLayout);
        linear_logo = findViewById(R.id.linear_logo);
        app_name = findViewById(R.id.app_name);
        function_Name = findViewById(R.id.function_Name);
        my_logo = findViewById(R.id.my_logo);

        linear_logo.animate().translationY(4000).setDuration(2000).setStartDelay(2000);
        app_name.animate().translationY(4000).setDuration(2000).setStartDelay(2000);
        function_Name.animate().translationY(4000).setDuration(2000).setStartDelay(2000);
        my_logo.animate().translationY(4000).setDuration(2000).setStartDelay(2000);

        //change status bar colour
        changeStatusBarColour(getResources().getColor(R.color.blue_200));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,IncomOutActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
    private void changeStatusBarColour(int color) {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }
}