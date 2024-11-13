package in.vinsoftsolutions.jayagrocery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
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
import in.vinsoftsolutions.jayagrocery.Adapter.Cate_Prod_Adapter;
import in.vinsoftsolutions.jayagrocery.Models.ProductModel;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cate_products extends AppCompatActivity {
    private FloatingActionButton BACK_PRESS;
    TextView APPBAR_TITLE,CATEGORY;
    private RecyclerView RECYCLERVIEW_PRODUCT;
    private RecyclerView.LayoutManager PRODUCT_MANAGER;
    private List<ProductModel> PRODUCTS;
    private Cate_Prod_Adapter PRODUCT_ADAPTER;
    private ProgressBar PROGRESSBAR;
    private TextInputEditText SEARCH_VIEW;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cate_products);

        BACK_PRESS = findViewById(R.id.fab_backPress);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);
        CATEGORY = findViewById(R.id.cate_prod_title);

        APPBAR_TITLE.setText("Category Products");
        //back press activity
        BACK_PRESS.setOnClickListener(view -> Cate_products.super.onBackPressed());

        // products fetch  start
        RECYCLERVIEW_PRODUCT = findViewById(R.id.cate_prod_recycler);
        PRODUCT_MANAGER = new GridLayoutManager(Cate_products.this,2);
        RECYCLERVIEW_PRODUCT.setLayoutManager(PRODUCT_MANAGER);
        PRODUCTS = new ArrayList<>();
        // products fetch  end

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
                List<ProductModel> filteredList = new ArrayList<>();
                for (ProductModel item : PRODUCTS) {
                    if (item.getProd_tam_name().toLowerCase().contains(text.toLowerCase()) || (item.getProd_name().toLowerCase().contains(text.toLowerCase()))){
                        filteredList.add(item);
                    }
                }
                if (filteredList.isEmpty()) {
                    Toast.makeText(Cate_products.this, "No data found", Toast.LENGTH_SHORT).show();
                } else {
                    PRODUCT_ADAPTER.setFilteredList(filteredList);
                }
            }
        });

        fetchProducts();
    }

    private void fetchProducts() {
        Intent intent = getIntent();
        String CATE_ID = intent.getStringExtra("cate_id");
        String CATE_NAME = intent.getStringExtra("cate_name")+" / "+intent.getStringExtra("tamil_name");

        if (intent != null) {
            CATEGORY = findViewById(R.id.cate_prod_title);
            CATEGORY.setText(CATE_NAME);
        }
        PROGRESSBAR = findViewById(R.id.cate_prod_progress);

        PROGRESSBAR.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST,"https://vinsoftsolutions.in/jaya-android/cate_prod_fetch.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PROGRESSBAR.setVisibility(View.GONE);
                        PRODUCTS.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                String product_id = object.getString("prod_id");
                                String product_eng_name = object.getString("prod_name");
                                String product_tam_name = object.getString("tamil_name");
                                double product_price = object.getDouble("rate");
                                String size_name = object.getString("size_name");
                                String product_stock = object.getString("stock_type");
                                String product_image = object.getString("prod_img");

                                ProductModel cate_prod_model = new ProductModel(product_id,product_eng_name,product_tam_name,product_image,product_stock,size_name,product_price);
                                PRODUCTS.add(cate_prod_model);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        PRODUCT_ADAPTER = new Cate_Prod_Adapter(Cate_products.this, PRODUCTS);
                        RECYCLERVIEW_PRODUCT.setAdapter(PRODUCT_ADAPTER);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PROGRESSBAR.setVisibility(View.GONE);
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Cate_products.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cate_id", CATE_ID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}