package in.vinsoftsolutions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import in.vinsoftsolutions.Adapter.DeliveryAdapter;
import in.vinsoftsolutions.Model.DeliveryModel;

public class Delivered_Status extends AppCompatActivity {
    private TextView APPBAR_TITLE;
    private FloatingActionButton BACK_PRESS;

    // recyclerView id name declaration start
    private RecyclerView RECYCLER_DELIVERY_STATUS;
    private RecyclerView.LayoutManager DELIVERY_STATUS_MANAGER;
    private List<DeliveryModel> deliveryModels;
    private DeliveryAdapter deliveryAdapter;
    private ProgressBar PROGRESSBAR;
    // recyclerView id name declaration end
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivered_status);

        /* fetch  start*/
        PROGRESSBAR = findViewById(R.id.delivery_progress);
        RECYCLER_DELIVERY_STATUS = findViewById(R.id.recycler_delivery_status);
        DELIVERY_STATUS_MANAGER = new GridLayoutManager(Delivered_Status.this,1);
        RECYCLER_DELIVERY_STATUS.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
        deliveryModels = new ArrayList<>();
        /* fetch  end*/

        BACK_PRESS = findViewById(R.id.fab_backPress);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);

        APPBAR_TITLE.setText("Delivery Status");
        //back press activity
        BACK_PRESS.setOnClickListener(view -> Delivered_Status.super.onBackPressed());
    }
    private void delivery_status() {
        PROGRESSBAR.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-delivery-app/delivery_status.php",
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
                        String Order_Date = jsonObject.getString("modi_date");
                        String Order_Phone = jsonObject.getString("status");

                        DeliveryModel deliveryModel = new DeliveryModel(Order_No,Order_Date,Order_Phone);
                        deliveryModels.add(deliveryModel);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                deliveryAdapter = new DeliveryAdapter(Delivered_Status.this, deliveryModels);
                RECYCLER_DELIVERY_STATUS.setAdapter(deliveryAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PROGRESSBAR.setVisibility(View.GONE);
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Delivered_Status.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(Delivered_Status.this);
        requestQueue.add(request);
    }
    @Override
    protected void onStart() {
        super.onStart();
        delivery_status();
    }
}