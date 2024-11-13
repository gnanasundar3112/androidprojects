package com.example.adminpanel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    TextView WELCOME;
    de.hdodenhof.circleimageview.CircleImageView CIRCLE_IMAGE;
    Animation LOGO,TEXT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        CIRCLE_IMAGE = findViewById(R.id.circleImageView);
        WELCOME = findViewById(R.id.splash_wel);

        TEXT = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.text_animinations);
        LOGO = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.logo_animinations);

        CIRCLE_IMAGE.setAnimation(LOGO);
        WELCOME.setAnimation(TEXT);

        //change status bar colour
        changeStatusBarColour(getResources().getColor(R.color.blue_200));

        // This method will be executed once the timer is over

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
        }, 5000);
    }

    private void changeStatusBarColour(int color) {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }
}