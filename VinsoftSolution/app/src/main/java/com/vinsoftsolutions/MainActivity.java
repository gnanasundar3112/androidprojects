package com.vinsoftsolutions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.vinsoftsolutions.Activity.Bank;
import com.vinsoftsolutions.Activity.Cash;
import com.vinsoftsolutions.Activity.Ledger;
import com.vinsoftsolutions.Activity.Outstanding;
import com.vinsoftsolutions.Activity.Payment;
import com.vinsoftsolutions.Activity.Purchase;
import com.vinsoftsolutions.Activity.Receipt;
import com.vinsoftsolutions.Activity.Sales;
import com.vinsoftsolutions.Internet.NetworkChangeListener;
import com.vinsoftsolutions.Service.ActivityMoving;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    CardView CASH,BANK,PURCHASE,SALES,RECEIPT,PAYMENT,OUTSTANDING,LEDGER;
    TextView DATE_MONTH_YEAR,CASH_AMOUNT;
    ImageView LIST_ICON;
    DrawerLayout DRAWER;
    NavigationView NAVIGATIONVIEW;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NAVIGATIONVIEW = (NavigationView) findViewById(R.id.side_navigation);
        NAVIGATIONVIEW.setNavigationItemSelectedListener(this);

        LIST_ICON = findViewById(R.id.burgerIcon);
        DRAWER = findViewById(R.id.drawer_layout);

        LIST_ICON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DRAWER.openDrawer(Gravity.LEFT);
            }
        });

        DATE_MONTH_YEAR = findViewById(R.id.date_mon_year);
        DATE_MONTH_YEAR.setText(getCurrentDate());
    }

    private String getCurrentDate(){
        return new SimpleDateFormat("dd-MM-yyyy  EEEE", Locale.getDefault()).format(new Date());
    }
    //navigation back press start

    @Override
    public void onBackPressed() {
        if (DRAWER.isDrawerOpen(GravityCompat.START)){
            DRAWER.closeDrawer(Gravity.LEFT);
        }
        else
        {
            //lets create alert dialog while exiting the app

            AlertDialog.Builder adb=new  AlertDialog.Builder(this);
            adb.setTitle("Confirm Exit?");
            adb.setMessage("Are you sure want to exit? ");
            adb.setCancelable(false);
            adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            adb.setNegativeButton("No",null);
            AlertDialog alertDialog = adb.create();
            alertDialog.show();
        }
    }
    //navigation back press End

    // navigation button click Start
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_cash) {
            ActivityMoving.ActivityToActivity(MainActivity.this, Cash.class);
        }else  if (id == R.id.nav_bank) {
            ActivityMoving.ActivityToActivity(MainActivity.this, Bank.class);
        }else  if (id == R.id.nav_purchase) {
            ActivityMoving.ActivityToActivity(MainActivity.this, Purchase.class);
        }else  if (id == R.id.nav_sales) {
            ActivityMoving.ActivityToActivity(MainActivity.this, Sales.class);
        }else  if (id == R.id.nav_receipt) {
            ActivityMoving.ActivityToActivity(MainActivity.this, Receipt.class);
        }else  if (id == R.id.nav_payment) {
            ActivityMoving.ActivityToActivity(MainActivity.this, Payment.class);
        }else if (id == R.id.nav_outstanding) {
            ActivityMoving.ActivityToActivity(MainActivity.this, Outstanding.class);
        }else  if (id == R.id.nav_ledger) {
            ActivityMoving.ActivityToActivity(MainActivity.this, Ledger.class);
        }else if (id == R.id.nav_logout){

            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("Logout");
            alert.setMessage("Are you sure you want to logout");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putBoolean("LoggedIn", false);
                    editor.clear();
                    editor.commit();

                    startActivity(new Intent(getApplicationContext(), Login.class));
                    overridePendingTransition(0, 0);
                    Toast.makeText(MainActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alert.show();
        }

        DRAWER.closeDrawer(GravityCompat.START);

        return true;
    }
    // navigation button click End

    // network offline filter start
    @Override
    protected void onStart() {
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
    // network offline filter End

}