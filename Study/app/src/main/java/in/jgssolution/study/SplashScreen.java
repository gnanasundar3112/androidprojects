package in.jgssolution.study;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;
import java.time.LocalTime;

public class SplashScreen extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh_screen);

        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Define restricted date range (10-12-2024 to 10-12-2050)
        LocalDate startRestrictedDate = LocalDate.of(2025, 1, 31);
        LocalDate endRestrictedDate = LocalDate.of(2050, 12, 25);


        // Check if current date fall within the restricted range
        if ((!currentDate.isBefore(startRestrictedDate) && !currentDate.isAfter(endRestrictedDate))) {
            // If current date and time are within the restricted range
            // End SplashScreen without navigating
            new Handler(Looper.getMainLooper()).postDelayed(this::finish, 3100);
            return; // Prevent further execution
        }

        // Proceed with normal navigation after delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent;
            intent = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(intent);
            finish(); // End the SplashScreen
        }, 3100);
    }
}
