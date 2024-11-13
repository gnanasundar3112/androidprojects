package in.vinsoftsolutions.aspn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    NavigationView navigationView;
    DrawerLayout drawer;
    ImageButton burgerIcon;
    private MaterialTextView NAV_NAME,NAV_EMAIL;
    private TextView DATE;
    private boolean isDialogShowing = false;
    private boolean isExitConfirmed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.side_navigation);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        //header image view start
        View nav_head = navigationView.getHeaderView(0);
        NAV_NAME = nav_head.findViewById(R.id.nave_name);
        NAV_EMAIL = nav_head.findViewById(R.id.nave_email);
        //header image view end

        burgerIcon = findViewById(R.id.burgerIcon);
        drawer = findViewById(R.id.drawer_layout);

        burgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        DATE = findViewById(R.id.date_mon_year);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = dateFormat.format(new Date());
        DATE.setText(currentDate);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(Gravity.LEFT);
        } else {
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
    }
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {
        } else if (id == R.id.nav_profile) {
//            startActivity(new Intent(MainActivity.this, Profile.class));
        }else if (id == R.id.nav_receipt){
            startActivity(new Intent(MainActivity.this, Receipt.class));
        } else if (id == R.id.nav_logout) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
            View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.warning_dialog,
                    (ConstraintLayout)findViewById(R.id.warning_dialog));

            builder.setView(view1);
            ((TextView) view1.findViewById(R.id.dialog_title)).setText("Logout");
            ((TextView) view1.findViewById(R.id.dialog_message)).setText("Are you sure you want to logout");
            ((Button) view1.findViewById(R.id.dialog_cancel)).setText("NO");
            ((Button) view1.findViewById(R.id.dialog_submit)).setText("YES");
            ((ImageView) view1.findViewById(R.id.dialog_image)).setImageResource(R.drawable.logout);

            final android.app.AlertDialog alertDialog = builder.create();

            view1.findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            view1.findViewById(R.id.dialog_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME,0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putBoolean("LoggedIn",false);
                    editor.clear();
                    editor.commit();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();

                    Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            });
            if (alertDialog.getWindow() !=null){
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            alertDialog.show();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}