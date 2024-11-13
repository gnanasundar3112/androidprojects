package in.vinsoftsolutions.jayagrocery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import in.vinsoftsolutions.jayagrocery.Adapter.CartAdapter;
import in.vinsoftsolutions.jayagrocery.Models.CartModel;

import in.vinsoftsolutions.jayagrocery.R;

import in.vinsoftsolutions.jayagrocery.internet.NetworkChangeListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private FloatingActionButton BACK_PRESS;
    TextView APPBAR_TITLE;
    private RecyclerView RECYCLERVIEW_CART;
    private RecyclerView.LayoutManager PRODUCT_MANAGER;
    private List<CartModel> CART_PRODUCTS;
    private CartAdapter PRODUCT_ADAPTER;
    private ProgressBar PROGRESSBAR;
    MaterialTextView CART_ITEM_QTY,CART_TOTAL;
    MaterialButton PLACE_ORDER_BTN;
    ScrollView CART_SCROLL;
    ImageView EMPTY_IMAGE;
    double AmountLimit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        BACK_PRESS = findViewById(R.id.fab_backPress);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);

        APPBAR_TITLE.setText("My Cart");
        //back press activity
        BACK_PRESS.setOnClickListener(view -> CartActivity.super.onBackPressed());

        PROGRESSBAR = findViewById(R.id.cart_progress);

        // products fetch  start
        RECYCLERVIEW_CART = findViewById(R.id.cart_recycler);
        PRODUCT_MANAGER = new GridLayoutManager(CartActivity.this,1);
        RECYCLERVIEW_CART.setLayoutManager(PRODUCT_MANAGER);
        CART_PRODUCTS = new ArrayList<>();
        // products fetch  end

        EMPTY_IMAGE = findViewById(R.id.cart_empty);
        CART_SCROLL = findViewById(R.id.cart_scroll);

        CART_ITEM_QTY = findViewById(R.id.cart_item_qty);

        CART_TOTAL = findViewById(R.id.cart_total);
        PLACE_ORDER_BTN = findViewById(R.id.place_order_btn);

        PLACE_ORDER_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double TOTAL = Double.parseDouble(CART_TOTAL.getText().toString());

                if (AmountLimit < TOTAL){
                    Intent intent = new Intent(CartActivity.this,Order_Confirm.class);
                    startActivity(intent);
                    finish();

                    String cart_total =CART_TOTAL.getText().toString();

                    SharedPreferences sharedPreferences = getSharedPreferences("amount_sent", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("total_amt",cart_total);
                    editor.apply();

                }else {
                    Toast.makeText(CartActivity.this, "Please place an order for more than Rs."+AmountLimit, Toast.LENGTH_LONG).show();
                }
            }
        });

        calculation();

    }

    public void fetchCartDetails() {

        SharedPreferences sharedPreferences = getSharedPreferences("user_send_data", MODE_PRIVATE);
        String USER_ID = sharedPreferences.getString("USER_ID", "");
        PROGRESSBAR.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/cart_prod_fetch.php", new Response.Listener<String>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(String response) {
                try {
                    PROGRESSBAR.setVisibility(View.GONE);
                    JSONArray jsonArray = new JSONArray(response);
                    CART_PRODUCTS.clear();
                    double item_total = 0;

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String Cart_cust_id = jsonObject.getString("cust_id");
                        String Cart_id = jsonObject.getString("prod_id");
                        String Cart_image = jsonObject.getString("prod_img");
                        String Cart_eng = jsonObject.getString("prod_name");
                        String Cart_tam = jsonObject.getString("tamil_name");
                        double Cart_price = jsonObject.getDouble("rate");
                        String Cart_gram = jsonObject.getString("gram");
                        String Cart_qty = jsonObject.getString("qty");
                        double Cart_totalPrice = jsonObject.getDouble("amount");
                        String Cart_out_stock = jsonObject.getString("stock_type");
                        double Amount_limit = jsonObject.getDouble("amount_limit");
                        AmountLimit = Amount_limit;

                        CartModel cartModel = new CartModel(Cart_image, Cart_eng, Cart_tam, Cart_gram, Cart_qty, Cart_id, Cart_cust_id,Cart_out_stock, Cart_price, Cart_totalPrice);
                        CART_PRODUCTS.add(cartModel);

                        item_total += Cart_totalPrice;

                        double total = item_total;
                        CART_TOTAL.setText(String.format("%.2f",total));

                        if ("out of stock".equalsIgnoreCase(Cart_out_stock)) {
                            PLACE_ORDER_BTN.setEnabled(false);
                            PLACE_ORDER_BTN.setTextColor(Color.parseColor("#000000"));
                            PLACE_ORDER_BTN.setBackgroundColor(Color.parseColor("#A4A4A4"));
                        } else {
                            PLACE_ORDER_BTN.setEnabled(true);
                            PLACE_ORDER_BTN.setTextColor(Color.parseColor("#ffffff"));
                            PLACE_ORDER_BTN.setBackgroundColor(Color.parseColor("#6200EA"));
                        }

                    }

                    if (jsonArray.length() > 0) {
                        CART_SCROLL.setVisibility(View.VISIBLE);
                        PLACE_ORDER_BTN.setVisibility(View.VISIBLE);
                        EMPTY_IMAGE.setVisibility(View.GONE);
                    } else {
                        CART_SCROLL.setVisibility(View.GONE);
                        PLACE_ORDER_BTN.setVisibility(View.GONE);
                        EMPTY_IMAGE.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                PRODUCT_ADAPTER = new CartAdapter(CartActivity.this, CART_PRODUCTS);
                RECYCLERVIEW_CART.setAdapter(PRODUCT_ADAPTER);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PROGRESSBAR.setVisibility(View.GONE);
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(CartActivity.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
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

    // Add this method to recalculate the total

    public void calculation(){
        double cartTotal = 0;
        for (CartModel cartItem : CART_PRODUCTS) {
            cartTotal += cartItem.getCart_totalPrice();
        }
        CART_TOTAL.setText(String.format("%.2f", cartTotal));
    }

    //cartActivity
    public void updateCartTotal(double total) {
        CART_TOTAL.setText(String.format("%.2f", total));
        if (CART_PRODUCTS.isEmpty()) {
            // Cart is empty, hide relevant views and show empty cart message
            CART_SCROLL.setVisibility(View.GONE);
            PLACE_ORDER_BTN.setVisibility(View.GONE);
            EMPTY_IMAGE.setVisibility(View.VISIBLE);
        } else {
            // Cart has items, make sure relevant views are visible
            CART_SCROLL.setVisibility(View.VISIBLE);
            PLACE_ORDER_BTN.setVisibility(View.VISIBLE);
            EMPTY_IMAGE.setVisibility(View.GONE);
        }
    }

    public void updateOutOfStock() {
        PLACE_ORDER_BTN.setEnabled(false);
        PLACE_ORDER_BTN.setTextColor(Color.parseColor("#000000"));
        PLACE_ORDER_BTN.setBackgroundColor(Color.parseColor("#A4A4A4"));
    }
    public void updateEnableOutOfStock() {
        fetchCartDetails();
    }

    // network offline filter start
    @Override
    protected void onStart() {
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);

        fetchCartDetails();
        super.onStart();
    }
    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
    // network offline filter End
}