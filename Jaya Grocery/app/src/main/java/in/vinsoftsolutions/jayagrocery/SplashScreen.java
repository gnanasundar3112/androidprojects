package in.vinsoftsolutions.jayagrocery;

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

import in.vinsoftsolutions.jayagrocery.R;
import com.google.android.material.textview.MaterialTextView;

public class SplashScreen extends AppCompatActivity {

    ConstraintLayout Constraint_Anim;
    LinearLayout Linear_Logo;
    MaterialTextView App_Name,Sale_Prod_Name,Vinsoft_Logo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Constraint_Anim = findViewById(R.id.constraintLayout);
        Linear_Logo = findViewById(R.id.linear_logo);
        App_Name = findViewById(R.id.app_name);
        Sale_Prod_Name = findViewById(R.id.sale_prod_name);
        Vinsoft_Logo = findViewById(R.id.vinsoft_logo);

        Linear_Logo.animate().translationY(4000).setDuration(2000).setStartDelay(2000);
        App_Name.animate().translationY(4000).setDuration(2000).setStartDelay(2000);
        Sale_Prod_Name.animate().translationY(4000).setDuration(2000).setStartDelay(2000);
        Vinsoft_Logo.animate().translationY(4000).setDuration(2000).setStartDelay(2000);

        //change status bar colour
        changeStatusBarColour(getResources().getColor(R.color.blue_200));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences = getSharedPreferences(OtpVerification.PREFS_NAME,0);
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
    private void changeStatusBarColour(int color) {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }
}