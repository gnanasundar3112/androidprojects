package com.example.sriganesh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sriganesh.Activity.Bank;
import com.example.sriganesh.Adapter.BankAdapter;
import com.example.sriganesh.Model.BankModel;
import com.example.sriganesh.Model.CashModel;
import com.example.sriganesh.internet.NetworkChangeListener;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    CardView Cash,Bank,Purchase,Sales,Reciept,Payment,OutStanding,Ledger;
    TextView DateMonthYear,CASH_AMOUNT;
    FloatingActionButton burgerIcon;
    DrawerLayout drawer;
    NavigationView navigationView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CASH_AMOUNT = findViewById(R.id.cash_amount);

        Cash = findViewById(R.id.cash);
        Bank = findViewById(R.id.bank);
        Purchase = findViewById(R.id.purchase);
        Sales = findViewById(R.id.sales);
        Reciept = findViewById(R.id.reciept);
        Payment = findViewById(R.id.payment);
        OutStanding = findViewById(R.id.outStanding);
        Ledger = findViewById(R.id.ledger);
        navigationView = (NavigationView) findViewById(R.id.side_navigation);
        navigationView.setNavigationItemSelectedListener(this);

        cash_amount();
        burgerIcon = findViewById(R.id.burgerIcon);
        drawer = findViewById(R.id.drawer_layout);

        burgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        DateMonthYear = findViewById(R.id.date_mon_year);
        DateMonthYear.setText(getCurrentDate());

        Cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, com.example.sriganesh.Activity.Cash.class);
                startActivity(intent);
            }
        });
        Bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, com.example.sriganesh.Activity.Bank.class);
                startActivity(intent);
            }
        });
        Purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, com.example.sriganesh.Activity.Purchase.class);
                startActivity(intent);
            }
        });
        Sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, com.example.sriganesh.Activity.Sales.class);
                startActivity(intent);
            }
        });
        Reciept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, com.example.sriganesh.Activity.Reciept.class);
                startActivity(intent);
            }
        });
        Payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, com.example.sriganesh.Activity.Payment.class);
                startActivity(intent);
            }
        });
        OutStanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, com.example.sriganesh.Activity.Outstanding.class);
                startActivity(intent);
            }
        });
        Ledger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, com.example.sriganesh.Activity.Ledger.class);
                startActivity(intent);
            }
        });

    }

    private void cash_amount() {
        DateMonthYear = findViewById(R.id.date_mon_year);
        String date = DateMonthYear.getText().toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.225.112/php/ganesh/cash.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);


                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String Cash_Name = jsonObject.getString("Amount");
                        double Cash_Amount = jsonObject.getDouble("Amount");

                        CashModel cashModel = new CashModel(Cash_Name,Cash_Amount);

                        CASH_AMOUNT.setText("₹ "+cashModel.getCash_Amount());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                params.put("today_data",date);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private String getCurrentDate(){
        return new SimpleDateFormat("dd/MM/yyyy  EEEE", Locale.getDefault()).format(new Date());
    }
    //navigation back press start

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(Gravity.LEFT);
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
    @SuppressLint("MissingInflatedId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.nav_logout:

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this, R.style.LogoutDialogTheme);
                View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.warning_dialog_2,
                        (ConstraintLayout)findViewById(R.id.logout_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.logout_title)).setText("Logout");
                ((TextView) view1.findViewById(R.id.logout_message)).setText("Are you sure you want to logout");
                ((Button) view1.findViewById(R.id.logout_cancel)).setText("NO");
                ((Button) view1.findViewById(R.id.logout_okay)).setText("YES");
                ((ImageView) view1.findViewById(R.id.imageView)).setImageResource(R.drawable.baseline_logout);


                final android.app.AlertDialog alertDialog = builder.create();

                view1.findViewById(R.id.logout_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                view1.findViewById(R.id.logout_okay).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME,0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putBoolean("LoggedIn",false);
                        editor.clear();
                        editor.commit();

                        startActivity(new Intent(getApplicationContext(), Login.class));
                        overridePendingTransition(0,0);
                        Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                        finish();
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