package in.vinsoftsolutions;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.vinsoftsolutions.internet.NetworkChangeListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private CardView ORDER_STATUS,DELIVERY_STATUS,CANCEL_STATUS;
    private TextView ORDER_COUNT,DELIVERY_COUNT,CANCEL_COUNT;
    ImageView BURGER_ICON;
    DrawerLayout DRAWER;
    NavigationView NAVIGATION;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ORDER_STATUS = findViewById(R.id.order_status);
        DELIVERY_STATUS = findViewById(R.id.delivery_status);
        CANCEL_STATUS = findViewById(R.id.cancel_status);
        ORDER_COUNT = findViewById(R.id.order_count);
        DELIVERY_COUNT = findViewById(R.id.delivery_count);
        CANCEL_COUNT = findViewById(R.id.cancel_count);
        NAVIGATION = findViewById(R.id.side_navigation);
        NAVIGATION.setNavigationItemSelectedListener(MainActivity.this);

        ORDER_STATUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Order_Status.class);
                startActivity(intent);
            }
        });

        DELIVERY_STATUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Delivered_Status.class);
                startActivity(intent);
            }
        });
        CANCEL_STATUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Cancel_status.class);
                startActivity(intent);
            }
        });
        BURGER_ICON = findViewById(R.id.iv_burgerIcon);
        DRAWER = findViewById(R.id.drawer_layout);

        BURGER_ICON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DRAWER.openDrawer(Gravity.LEFT);
            }
        });
    }


    private void OrderCount() {
        ORDER_COUNT.refreshDrawableState();
        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-delivery-app/order_count.php",
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String cart_count = jsonObject.getString("row_count");

                        ORDER_COUNT.setText("Total Order - " + cart_count);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(MainActivity.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void DeliveryCount() {
        ORDER_COUNT.refreshDrawableState();
        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-delivery-app/delivery_count.php",
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String cart_count = jsonObject.getString("row_count");

                        DELIVERY_COUNT.setText("Total Delivery - " + cart_count);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(MainActivity.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    private void CancelCount() {

        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-delivery-app/cancel_count.php",
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String cart_count = jsonObject.getString("row_count");

                        CANCEL_COUNT.setText("Total Cancel - " + cart_count);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(MainActivity.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.profile) {
            startActivity(new Intent(MainActivity.this, Profile.class));
            Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
        }else {
            if (id == R.id.nav_refer_logout) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
                View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.delete_dialog,
                        (ConstraintLayout)findViewById(R.id.warning_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.dialog_title)).setText("Logout");
                ((TextView) view1.findViewById(R.id.dialog_message)).setText("Are you sure you want to logout");
                ((Button) view1.findViewById(R.id.dialog_btn)).setText("NO");
                ((Button) view1.findViewById(R.id.dialog_btn2)).setText("YES");
                //((ImageView) view1.findViewById(R.id.dialog_image)).setImageResource(R.drawable.onion1);


                final android.app.AlertDialog alertDialog = builder.create();

                view1.findViewById(R.id.dialog_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                view1.findViewById(R.id.dialog_btn2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sharedPreferences = getSharedPreferences(OtpVerification.PREFS_NAME,0);
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

        }

        DRAWER.closeDrawer(GravityCompat.START);
        return true;
    }

    // network offline filter start
    @Override
    protected void onStart() {
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);

        OrderCount();
        DeliveryCount();
        CancelCount();
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    // network offline filter End
}