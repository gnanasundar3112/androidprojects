package in.jgssolution.study;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;

import in.jgssolution.study.Internet.NetworkChangeListener;

public class MainActivity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private LottieAnimationView START_TEST;
    private boolean isDialogShowing = false;
    private boolean isExitConfirmed = false;
    private String USER_NAME, ROLL_NO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        START_TEST = findViewById(R.id.start_test);

        START_TEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,QuestionActivity.class);
                intent.putExtra("username",USER_NAME);
                intent.putExtra("rollNo",ROLL_NO);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed() {

        if (!isDialogShowing && !isExitConfirmed) {
            isDialogShowing = true;

            // Create an AlertDialog to confirm exit
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Confirm Exit");
            adb.setMessage("Are you sure you want to exit?");
            adb.setCancelable(false);
            adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    isDialogShowing = false;
                    isExitConfirmed = true; // Set the flag to indicate exit is confirmed
                    finish();
                }
            });
            adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    isDialogShowing = false;
                }
            });
            AlertDialog alertDialog = adb.create();
            alertDialog.show();
        }
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