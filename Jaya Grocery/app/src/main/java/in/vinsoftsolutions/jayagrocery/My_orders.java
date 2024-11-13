package in.vinsoftsolutions.jayagrocery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import in.vinsoftsolutions.jayagrocery.Adapter.My_order_Adapter;
import in.vinsoftsolutions.jayagrocery.Models.My_order_Model;

import in.vinsoftsolutions.jayagrocery.R;

import in.vinsoftsolutions.jayagrocery.internet.NetworkChangeListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class My_orders extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private FloatingActionButton BACK_PRESS;
    TextView APPBAR_TITLE;
    private RecyclerView RECYCLER_ORDERS_PROD;
    private RecyclerView.LayoutManager ORDERS_PRODUCT_MANAGER;
    private List<My_order_Model> ORDERS_PRODUCTS;
    private My_order_Adapter ORDERS_PRODUCT_ADAPTER;
    private ProgressBar PROGRESSBAR;
    private TextInputEditText SEARCH_VIEW;
    ImageView EMPTY_IMG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_oreders);

        BACK_PRESS = findViewById(R.id.fab_backPress);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);

        APPBAR_TITLE.setText("My Orders");
        //back press activity
        BACK_PRESS.setOnClickListener(view -> My_orders.super.onBackPressed());

        // products fetch  start
        RECYCLER_ORDERS_PROD = findViewById(R.id.recycler_myOrders);
        ORDERS_PRODUCT_MANAGER = new GridLayoutManager(My_orders.this,1);
        RECYCLER_ORDERS_PROD.setLayoutManager(ORDERS_PRODUCT_MANAGER);
        ORDERS_PRODUCTS = new ArrayList<>();
        // products fetch  end
        PROGRESSBAR = findViewById(R.id.myOrders_progressbar);
        EMPTY_IMG = findViewById(R.id.cart_empty_img);

        /* filter from search bar start*/
        SEARCH_VIEW = findViewById(R.id.searchView);
        SEARCH_VIEW.clearFocus();
        SEARCH_VIEW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this example
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fileList(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    // Disable the cursor pointer when the search view is empty
                    SEARCH_VIEW.setCursorVisible(false);
                } else {
                    // Enable the cursor pointer when there is text in the search view
                    SEARCH_VIEW.setCursorVisible(true);
                }
            }
            private void fileList(String text) {
                List<My_order_Model> filteredList = new ArrayList<>();
                for (My_order_Model item : ORDERS_PRODUCTS) {
                    if (item.getMy_order_number().toLowerCase().contains(text.toLowerCase()) || (item.getMy_order_date().toLowerCase().contains(text.toLowerCase())
                            || (item.getMy_order_status().toLowerCase().contains(text.toLowerCase())))){
                        filteredList.add(item);
                    }
                }
                if (filteredList.isEmpty()) {
                    Toast.makeText(My_orders.this, "No data found", Toast.LENGTH_SHORT).show();
                } else {
                    ORDERS_PRODUCT_ADAPTER.setFilteredList(filteredList);
                }
            }
        });
        /* filter from search bar End*/
    }
    private void FetchMyOrders(){
        SharedPreferences sharedPreferences = getSharedPreferences("user_send_data", MODE_PRIVATE);
        String USER_ID = sharedPreferences.getString("USER_ID", "");

        PROGRESSBAR.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/order_prod_fetch.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PROGRESSBAR.setVisibility(View.GONE);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            ORDERS_PRODUCTS.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String my_order_image = jsonObject.getString("prod_img");
                                String my_order_status = jsonObject.getString("order_date");
                                String my_order_number = jsonObject.getString("order_no");
                                String my_order_cancel = jsonObject.getString("status");

                                My_order_Model order_Model = new My_order_Model(my_order_status,my_order_number,my_order_image,my_order_cancel);
                                ORDERS_PRODUCTS.add(order_Model);
                            }
                            if (jsonArray.length() > 0) {
                                RECYCLER_ORDERS_PROD.setVisibility(View.VISIBLE);
                                EMPTY_IMG.setVisibility(View.GONE);
                            } else {
                                RECYCLER_ORDERS_PROD.setVisibility(View.GONE);
                                EMPTY_IMG.setVisibility(View.VISIBLE);
                            }
                        } catch(JSONException e) {
                            e.printStackTrace();

                        }
                        ORDERS_PRODUCT_ADAPTER = new My_order_Adapter(My_orders.this, ORDERS_PRODUCTS);
                        RECYCLER_ORDERS_PROD.setAdapter(ORDERS_PRODUCT_ADAPTER);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PROGRESSBAR.setVisibility(View.GONE);
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(My_orders.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("cust_id", USER_ID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }
    // network offline filter start
    @Override
    protected void onStart() {
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);

        FetchMyOrders();
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
    // network offline filter End
}