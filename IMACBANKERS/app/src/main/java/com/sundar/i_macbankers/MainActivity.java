package com.sundar.i_macbankers;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.sundar.i_macbankers.Adapter.NotificationAdapter;
import com.sundar.i_macbankers.Models.LoginUser;
import com.sundar.i_macbankers.Models.NotificationModel;
import com.sundar.i_macbankers.internet.NetworkChangeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private MaterialTextView NAV_NAME,NAV_EMAIL,NOTIFY_COUNT;
    private TextView DATE;
    private ImageButton burgerIcon;
    private boolean isDialogShowing = false;
    private boolean isExitConfirmed = false;
    String notification;
    private FloatingActionButton NOTIFICATION_BTN;
    ArrayList<NotificationModel> notificationModels = new ArrayList<>();
    ListView listView;
    android.app.AlertDialog alertDialog;
    private boolean isAlertDialogCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notification = Links.notification;

        navigationView = findViewById(R.id.side_navigation);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        //header image view start
        View nav_head = navigationView.getHeaderView(0);
        NAV_NAME = nav_head.findViewById(R.id.nave_name);
        NAV_EMAIL = nav_head.findViewById(R.id.nave_email);
        //header image view end

        NOTIFY_COUNT = findViewById(R.id.notifi_count);
        NOTIFICATION_BTN = findViewById(R.id.NotificationBtn);
        DATE = findViewById(R.id.date_mon_year);
        burgerIcon = findViewById(R.id.burgerIcon);
        drawer = findViewById(R.id.drawer_layout);

        burgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = dateFormat.format(new Date());
        DATE.setText(currentDate);

        NOTIFICATION_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.notificationdialog,
                        (LinearLayout) findViewById(R.id.notification_dialog));
                builder.setView(view);

                alertDialog = builder.create();

                listView = view.findViewById(R.id.notification_list);


                message();
                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    // Set gravity and top margin
                    WindowManager.LayoutParams layoutParams = alertDialog.getWindow().getAttributes();
                    layoutParams.gravity = Gravity.TOP;
                    layoutParams.y = 120; // Set the top margin here (adjust as needed)
                    alertDialog.getWindow().setAttributes(layoutParams);

                }
                alertDialog.show();
                isAlertDialogCreated = true;
            }
        });

    }

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
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_cate_master) {
            startActivity(new Intent(MainActivity.this, CateMaster.class));
        }else if (id == R.id.nav_prod_master){
            startActivity(new Intent(MainActivity.this, ProductMaster.class));
        }else if (id == R.id.nav_grad_master) {
            startActivity(new Intent(MainActivity.this, GradeMaster.class));
        }else if (id == R.id.nav_grade_rate){
            startActivity(new Intent(MainActivity.this, GradeRate.class));
        } else if (id == R.id.nav_part_master) {
            startActivity(new Intent(MainActivity.this, PartyMaster.class));
        } else if (id == R.id.nav_loan) {
            startActivity(new Intent(MainActivity.this, Loan.class));
        } else if (id == R.id.nav_receipt) {
            startActivity(new Intent(MainActivity.this, Receipt.class));
        }else if (id == R.id.nav_reports) {
            startActivity(new Intent(MainActivity.this, Reports.class));
        }else if (id == R.id.nav_setting) {
            startActivity(new Intent(MainActivity.this, Settings.class));
        }else if (id == R.id.nav_logout) {
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

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);

        message();

    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }

    public void message(){
        StringRequest request = new StringRequest(Request.Method.POST, notification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        notificationModels.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String loan_date = object.getString("loan_date");
                                String loan_no = object.getString("loan_no");
                                String cust_name = object.getString("cust_name");
                                String count = object.getString("count");

                                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                String updateDate = null;

                                try {
                                    Date date = originalFormat.parse(loan_date);
                                    SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                    updateDate = newFormat.format(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                NOTIFY_COUNT.setText(count);

                                NotificationModel notificationModel = new NotificationModel();
                                notificationModel.setLoan_no(loan_no);
                                notificationModel.setLoan_date(updateDate);
                                notificationModel.setCustomer_name(cust_name);
                                notificationModels.add(notificationModel);

                                NotificationAdapter listAdapter = new NotificationAdapter(MainActivity.this, notificationModels);
                                if (isAlertDialogCreated == true) {
                                    listView.setAdapter(listAdapter);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(MainActivity.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
    }


}