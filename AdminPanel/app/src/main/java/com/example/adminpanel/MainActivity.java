package com.example.adminpanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.example.adminpanel.internet.NetworkChangeListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    CardView CATE_MASTER,PRODUCT_MASTER,PRODUCT_SIZE,USER_DELIVERY,PINCODE,REPORTS;
    TextView DATE;
    FloatingActionButton BURGER_ICON;
    DrawerLayout DRAWER;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CATE_MASTER = findViewById(R.id.cate_master);
        PRODUCT_MASTER = findViewById(R.id.product__master);
        PRODUCT_SIZE = findViewById(R.id.product_size);
        USER_DELIVERY = findViewById(R.id.user_delivery);
        PINCODE = findViewById(R.id.pincode);
        REPORTS = findViewById(R.id.reports);
        DATE = findViewById(R.id.date_mon_year);
        BURGER_ICON = findViewById(R.id.burgerIcon);
        DRAWER = findViewById(R.id.drawer_layout);

        DATE = findViewById(R.id.date_mon_year);
        DATE.setText(getCurrentDate());
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               /* switch (item.getItemId()){
                    case R.id.nav_home:
                        break;

                    case R.id.nav_profile:
                        break;

                    case R.id.nav_logout:
                        break;

                }*/
                return true;
            }
        });

        /*BURGER_ICON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DRAWER.openDrawer(Gravity.LEFT);
            }
        });*/

        CATE_MASTER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CateMaster.class);
                startActivity(intent);
            }
        });
        PRODUCT_MASTER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProductMaster.class);
                startActivity(intent);
            }
        });
        PRODUCT_SIZE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProductSize.class);
                startActivity(intent);
            }
        });
        USER_DELIVERY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DeliveryUser.class);
                startActivity(intent);
            }
        });
        PINCODE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Pin_codeMaster.class);
                startActivity(intent);
            }
        });
        REPORTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Report.class);
                startActivity(intent);
            }
        });
    }
    private String getCurrentDate(){
        return new SimpleDateFormat("dd/MM/yyyy  EEEE", Locale.getDefault()).format(new Date());
    }
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