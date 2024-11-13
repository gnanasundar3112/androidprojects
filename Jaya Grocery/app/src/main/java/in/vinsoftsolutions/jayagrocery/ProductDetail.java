package in.vinsoftsolutions.jayagrocery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import in.vinsoftsolutions.jayagrocery.Adapter.ProductDetailAdapter;
import in.vinsoftsolutions.jayagrocery.Models.CartCountModel;
import in.vinsoftsolutions.jayagrocery.Models.ProductModel;

import in.vinsoftsolutions.jayagrocery.internet.NetworkChangeListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductDetail extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private FloatingActionButton BACK_PRESS,FLOATING_CART;
    TextView APPBAR_TITLE;
    MaterialTextView PROD_ID,PROD_NAME,PROD_TAM_NAME,PROD_RATE,STOCK_TYPE,PROD_QTY,SIZE_PRICE,PROD_TOTAL_PRICE,SIZE_ID,SIZE_NAME,CART_COUNT;
    Spinner SIZE_SPINNER;
    ImageView PROD_IMG;
    private CircleImageView PLUS_BTN,MINUS_BTN;
    private int numberOrder=1;
    TextInputEditText CHECK_PIN_CODE;
    MaterialButton ADD_TO_CART_BTN,PIN_CODE_VERIFY;
    private ProgressBar PROGRESSBAR;
    RequestQueue REQUESTQUEUE;
    Dialog DIALOG;
    ArrayList<String> size_list = new ArrayList<>();
    ArrayList<String> size_id_list = new ArrayList<>();
    ArrayAdapter<String> size_adapter;

    private RecyclerView RECYCLERVIEW_PRODUCT;
    private RecyclerView.LayoutManager PRODUCT_MANAGER;
    private List<ProductModel> PRODUCTS;
    private ProductDetailAdapter PRODUCT_ADAPTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        BACK_PRESS = findViewById(R.id.fab_backPress);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);

        APPBAR_TITLE.setText("Product Detail");
        //back press activity
        BACK_PRESS.setOnClickListener(view -> ProductDetail.super.onBackPressed());

        PROD_ID = findViewById(R.id.detail_prod_id);
        PROD_NAME = findViewById(R.id.detail_eng_name);
        PROD_TAM_NAME = findViewById(R.id.detail_tam_name);
        PROD_RATE = findViewById(R.id.detail_prod_price);
        PROD_IMG = findViewById(R.id.detail_prod_img);
        ADD_TO_CART_BTN = findViewById(R.id.det_add_to_cart);

        PLUS_BTN = findViewById(R.id.plus);
        MINUS_BTN = findViewById(R.id.minus);
        PROD_QTY = findViewById(R.id.detail_prod_number);

        SIZE_SPINNER = findViewById(R.id.spinner_size);
        SIZE_ID = findViewById(R.id.detail_size_id);
        SIZE_NAME = findViewById(R.id.detail_size_name);

        SIZE_PRICE = findViewById(R.id.detail_size_price);
        PROD_TOTAL_PRICE = findViewById(R.id.detail_prod_total_price);

        CART_COUNT = findViewById(R.id.cart_counter);

        CHECK_PIN_CODE = findViewById(R.id.pincode);
        PIN_CODE_VERIFY = findViewById(R.id.detail_pincode_verify);

        PROGRESSBAR = findViewById(R.id.detail_ProgressBar);

        // products fetch  start
        RECYCLERVIEW_PRODUCT = findViewById(R.id.det_prod_recyclerView);
        PRODUCT_MANAGER = new GridLayoutManager(ProductDetail.this,2);
        RECYCLERVIEW_PRODUCT.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.HORIZONTAL,false));
        PRODUCTS = new ArrayList<>();
        // products fetch  end

        products();
        cartCount();
        fetchProducts();
        //getSizeRate(size_id, prod_id);

        FLOATING_CART  = findViewById(R.id.floatingCart);
        FLOATING_CART.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetail.this,CartActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ADD_TO_CART_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String size_name = SIZE_ID.getText().toString();
                if (size_name.isEmpty()){
                    Toast.makeText(ProductDetail.this, "Please select weight", Toast.LENGTH_SHORT).show();
                }else {
                    insertData();
                }
            }
        });
        PIN_CODE_VERIFY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPincode();
            }
        });

        SpinnerSize();
    }


    public void SpinnerSize() {
        REQUESTQUEUE = Volley.newRequestQueue(ProductDetail.this);
        size_list.clear();
        String spinner_prod_id = PROD_ID.getText().toString();

        String url = "https://vinsoftsolutions.in/jaya-android/product_size.php?prod_id=" + spinner_prod_id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("productsize");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String size_id = object.getString("size_id");
                        String size_name = object.getString("size_name");
                        String factor = object.getString("factor");
                        String rate = object.getString("rate");

                        size_list.add(size_name);
                        size_id_list.add(size_id);
                        size_adapter = new ArrayAdapter<>(ProductDetail.this, android.R.layout.simple_spinner_item, size_list);
                        size_adapter.setDropDownViewResource(R.layout.item_drop_down);
                        SIZE_SPINNER.setAdapter(size_adapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(ProductDetail.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        REQUESTQUEUE.add(jsonObjectRequest);
        SIZE_SPINNER.setOnItemSelectedListener(this);
    }
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner_size) {
            String  size_name = size_list.get(position);
            String size_id = size_id_list.get(position);

            SIZE_ID.setText(size_id);
            SIZE_NAME.setText(size_name);

            String prod_id = PROD_ID.getText().toString();

            StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/prod_size_rate.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            PROGRESSBAR.setVisibility(View.GONE);
                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    double rate = Double.parseDouble(object.getString("rate"));
                                    SIZE_PRICE.setText(String.format("%.1f", rate));
                                    PROD_RATE.setText(String.format("%.1f", rate));

                                    double total_price = numberOrder * rate;
                                    PROD_TOTAL_PRICE.setText(String.format("%.2f", total_price)); // Format to display 2 decimal places
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    PROGRESSBAR.setVisibility(View.GONE);
                    if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                        Toast.makeText(ProductDetail.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("prod_id", prod_id);
                    params.put("size_id", size_id);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @SuppressLint("ResourceType")
    private void products() {
        Intent intent = getIntent();
        double Price = intent.getDoubleExtra("rate", 0);
        String English_name = intent.getStringExtra("prod_name");
        String Tamil_name = intent.getStringExtra("prod_tam_name");
        String Prod_Id = intent.getStringExtra("prod_id");
        String Detail_stock = intent.getStringExtra("stock_type");
        String Image = intent.getStringExtra("prod_img");


        double total_price = numberOrder * Price;

        // Format the total price with two decimal places
        String formattedTotalPrice = String.format("%.2f", total_price);

        PROD_TOTAL_PRICE.setText(formattedTotalPrice);
        SIZE_PRICE.setText(String.format("%.2f", Price));


        PLUS_BTN.setOnClickListener(view -> {
            numberOrder=numberOrder + 1;
            PROD_QTY.setText(""+numberOrder);
            double Detail_grams_rate = Double.parseDouble(SIZE_PRICE.getText().toString().trim());

            double prod_total_price = numberOrder*Detail_grams_rate;
            PROD_TOTAL_PRICE.setText(String.format("%.2f",prod_total_price));// Format to display 2 decimal places
        });
        MINUS_BTN.setOnClickListener(view -> {
            if (numberOrder>1){
                numberOrder=numberOrder- 1;
            }
            PROD_QTY.setText(""+numberOrder);
            double Detail_grams_rate = Double.parseDouble(SIZE_PRICE.getText().toString().trim());

            double prod_total_price = numberOrder*Detail_grams_rate;
            PROD_TOTAL_PRICE.setText(String.format("%.2f",prod_total_price));
        });


        String stock = String.valueOf('2');
        if (Detail_stock.equals(stock)){
            ADD_TO_CART_BTN.setEnabled(false);
            ADD_TO_CART_BTN.setText("Out of stock");
            ADD_TO_CART_BTN.setBackgroundColor(Color.parseColor(getString(R.color.gray_btn_bg_color)));
            ADD_TO_CART_BTN.setTextColor(Color.parseColor(getString(R.color.red)));
        }else {
            ADD_TO_CART_BTN.setEnabled(true);
        }

        if (intent != null) {
            PROD_NAME.setText(English_name);
            PROD_TAM_NAME.setText(Tamil_name);
            PROD_ID.setText(Prod_Id);
            Glide.with(ProductDetail.this).load(Image).placeholder(R.drawable.prograss_animination).into(PROD_IMG);
        }
    }
    public void insertData() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_send_data", MODE_PRIVATE);
        String USER_ID = sharedPreferences.getString("USER_ID", "");

        String PRODUCT_ID = PROD_ID.getText().toString().trim();
        String Prod_SIZE_ID = SIZE_ID.getText().toString().trim();
        String PRODUCT_PRICE = PROD_RATE.getText().toString().trim();
        String PRODUCT_QTY = PROD_QTY.getText().toString().trim();
        String TOTAL_AMT = PROD_TOTAL_PRICE.getText().toString().trim();


        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/add_cart_prod.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("Added to cart")){
                            cartCount();
                            Toast.makeText(ProductDetail.this, "Added to cart", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(ProductDetail.this, response, Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(ProductDetail.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("cust_id",USER_ID);
                params.put("prod_id",PRODUCT_ID);
                params.put("size_id",Prod_SIZE_ID);
                params.put("rate",PRODUCT_PRICE);
                params.put("qty",PRODUCT_QTY);
                params.put("amount",TOTAL_AMT);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ProductDetail.this);
        requestQueue.add(request);
    }
    public void cartCount() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_send_data", MODE_PRIVATE);
        String USER_ID = sharedPreferences.getString("USER_ID", "");

        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/cartcounter.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String cart_count = jsonObject.getString("row_count");

                                CartCountModel countModel = new CartCountModel(cart_count);

                                CART_COUNT.setText(countModel.getCart_count());
                            }

                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(ProductDetail.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                params.put("cust_id",USER_ID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    public void checkPincode() {

        final ProgressDialog progressDialog1 = new ProgressDialog(ProductDetail.this);
        progressDialog1.setMessage("Please wait...");

        progressDialog1.show();

        String check_pinCode = CHECK_PIN_CODE.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST,  "https://vinsoftsolutions.in/jaya-android/pincode_master.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog1.dismiss();
                        if (response.equalsIgnoreCase("Delivery not Available")) {

                            CHECK_PIN_CODE.setText("");

                            Toast.makeText(ProductDetail.this, response, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProductDetail.this,response, Toast.LENGTH_SHORT).show();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog1.dismiss();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(ProductDetail.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("pincode",check_pinCode);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ProductDetail.this);
        requestQueue.add(request);
    }
    public void fetchProducts() {

        PROGRESSBAR.setVisibility(View.VISIBLE);
        String prod_id = PROD_ID.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/detail_prod_fetch.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PROGRESSBAR.setVisibility(View.GONE);
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                String product_id = object.getString("prod_id");
                                String prod_eng_name = object.getString("prod_name");
                                String prod_tam_name = object.getString("tamil_name");
                                double prod_price = object.getDouble("rate");
                                String size_name = object.getString("size_name");
                                String prod_stock = object.getString("stock_type");
                                String prod_image = object.getString("prod_img");
                                ProductModel productModels =new ProductModel(product_id,prod_eng_name,prod_tam_name,prod_image,prod_stock,size_name,prod_price);
                                PRODUCTS.add(productModels);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        PRODUCT_ADAPTER = new ProductDetailAdapter(ProductDetail.this,PRODUCTS);
                        RECYCLERVIEW_PRODUCT.setAdapter(PRODUCT_ADAPTER);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PROGRESSBAR.setVisibility(View.GONE);
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(ProductDetail.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                params.put("prod_id", prod_id);

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
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
    // network offline filter End
}