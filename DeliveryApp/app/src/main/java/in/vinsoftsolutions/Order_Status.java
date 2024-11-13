package in.vinsoftsolutions;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.vinsoftsolutions.Adapter.OrderAdapter;
import in.vinsoftsolutions.Model.OrderModel;

public class Order_Status extends AppCompatActivity {

    private RecyclerView RECYCLER_ORDER_STATUS;
    private RecyclerView.LayoutManager ORDER_STATUS_MANAGER;
    private List<OrderModel> orderModels;
    private OrderAdapter orderAdapter;
    private ProgressBar PROGRESSBAR;
    // recyclerView id name declaration end
    private TextView APPBAR_TITLE;
    private FloatingActionButton BACK_PRESS;
    LinearLayout linearLayout;
    LottieAnimationView ORDER_EMPTY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        /* category fetch  start*/
        PROGRESSBAR = findViewById(R.id.order_progress);
        RECYCLER_ORDER_STATUS = findViewById(R.id.recycler_order_status);
        ORDER_STATUS_MANAGER = new GridLayoutManager(Order_Status.this,1);
        RECYCLER_ORDER_STATUS.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
        orderModels = new ArrayList<>();
        /* category fetch  end*/
        ORDER_EMPTY = findViewById(R.id.embty_lottie);
        BACK_PRESS = findViewById(R.id.fab_backPress);
        linearLayout = findViewById(R.id.linearLayout3);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);

        APPBAR_TITLE.setText("Order Status");
        //back press activity
        BACK_PRESS.setOnClickListener(view -> Order_Status.super.onBackPressed());
    }
    private void order_status(){
        orderModels.clear();
        PROGRESSBAR.setVisibility(View.VISIBLE);

        if (!isNetworkAvailable()) {
            showNetworkErrorDialog();
            return;
        }
        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-delivery-app/order_status.php",
                new Response.Listener<String>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(String response) {
                PROGRESSBAR.setVisibility(View.GONE);

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String Order_No = jsonObject.getString("order_no");
                        String Order_Date = jsonObject.getString("order_date");
                        String Order_Cust_Id = jsonObject.getString("cust_id");
                        String Order_Amount = jsonObject.getString("amount");
                        String Order_Qty = jsonObject.getString("qty");
                        String Order_Deli_Type = jsonObject.getString("deli_type");
                        String Order_Add_id = jsonObject.getString("add_id");
                        String Order_Add_Name = jsonObject.getString("add_name");
                        String Order_Add_Mobile = jsonObject.getString("add_mobile");
                        String Order_Add_Address = jsonObject.getString("add_address");
                        String Order_Add_State = jsonObject.getString("add_state");
                        String Order_Add_City = jsonObject.getString("add_city");
                        String Order_Add_Pincode = jsonObject.getString("add_pincode");

                        OrderModel orderModel = new OrderModel(Order_No,Order_Date,Order_Cust_Id,Order_Amount,Order_Qty,Order_Deli_Type,Order_Add_id,Order_Add_Name,Order_Add_Mobile,Order_Add_Address,Order_Add_State,Order_Add_City,Order_Add_Pincode);
                        orderModels.add(orderModel);
                    }
                    if (jsonArray.length() > 0) {
                        ORDER_EMPTY.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                    }else {
                        ORDER_EMPTY.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                orderAdapter = new OrderAdapter(Order_Status.this, orderModels);
                RECYCLER_ORDER_STATUS.setAdapter(orderAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PROGRESSBAR.setVisibility(View.GONE);
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Order_Status.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    showNetworkErrorDialog();
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(Order_Status.this);
        requestQueue.add(request);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    // Function to show a dialog for network error
    private void showNetworkErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Order_Status.this);
        builder.setTitle("Network Error").setMessage("Please check your internet connection and try again.")
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i){
                    order_status();
                }
            })
            .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        order_status();
    }
}