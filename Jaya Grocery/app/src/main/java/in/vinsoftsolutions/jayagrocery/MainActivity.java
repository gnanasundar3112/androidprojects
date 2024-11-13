package in.vinsoftsolutions.jayagrocery;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import in.vinsoftsolutions.jayagrocery.Adapter.CategoryAdapter;
import in.vinsoftsolutions.jayagrocery.Adapter.OfferAdapter;
import in.vinsoftsolutions.jayagrocery.Adapter.ProductMainAdapter;
import in.vinsoftsolutions.jayagrocery.Models.CartCountModel;
import in.vinsoftsolutions.jayagrocery.Models.CategoryModel;
import in.vinsoftsolutions.jayagrocery.Models.OfferModel;
import in.vinsoftsolutions.jayagrocery.Models.ProductModel;
import in.vinsoftsolutions.jayagrocery.Models.ProfileModel;

import in.vinsoftsolutions.jayagrocery.R;

import in.vinsoftsolutions.jayagrocery.internet.NetworkChangeListener;

import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private AppUpdateManager appUpdateManager;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    NavigationView navigationView;
    DrawerLayout drawer;
    ImageView burgerIcon;
    private CircleImageView NAV_IMG;
    private MaterialTextView NAV_NAME,NAV_EMAIL,CART_COUNT;
    private FloatingActionButton FLOATING_CART;
    private RecyclerView RECYCLERVIEW_CATEGORY,RECYCLERVIEW_PRODUCT,RECYCLERVIEW_OFFER;
    private RecyclerView.LayoutManager CATEGORY_MANAGER,PRODUCT_MANAGER,OFFER_MANAGER;
    private List<CategoryModel> CATEGORY_MODELS;
    private CategoryAdapter CATEGORY_ADAPTER;
    private List<ProductModel> PRODUCTS;
    private ProductMainAdapter PRODUCT_ADAPTER;
    private List<OfferModel> OFFERS;
    private OfferAdapter OFFER_ADAPTER;
    private ProgressBar PROGRESSBAR,PROGRESSBAR_PROD;
    private TextInputEditText SEARCH_VIEW;
    private boolean isDialogShowing = false;
    private boolean isExitConfirmed = false;
    Dialog DIALOG;
    //auto scroll recyclerview start
    private Handler autoScrollHandler = new Handler();
    private Runnable autoScrollRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                // Update the current positions in your adapters
                int offerPosition = OFFER_ADAPTER.getCurrentPosition();
                // Calculate the next positions (wrap around if necessary)
                int offerNextPosition = (offerPosition + 1) % OFFER_ADAPTER.getItemCount();
                // Scroll the RecyclerViews to the next positions
                RECYCLERVIEW_OFFER.smoothScrollToPosition(offerNextPosition);
                // Update the current positions in the adapters
                OFFER_ADAPTER.setCurrentPosition(offerNextPosition);
                // Delay in milliseconds between scrolls (adjust as needed)
                autoScrollHandler.postDelayed(this, 80000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

      //auto scroll recyclerview end

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //App update available for play store code start
        appUpdateManager = AppUpdateManagerFactory.create(this);
        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    AppUpdateDialog();
            }
        });
        //App update available for play store code end

        navigationView = findViewById(R.id.side_navigation);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        PROGRESSBAR = findViewById(R.id.ProgressBar);
        PROGRESSBAR_PROD = findViewById(R.id.prodp_progressBar);

        //header image view start
        View nav_head = navigationView.getHeaderView(0);
        NAV_IMG = nav_head.findViewById(R.id.nav_img);
        NAV_NAME = nav_head.findViewById(R.id.nave_name);
        NAV_EMAIL = nav_head.findViewById(R.id.nave_email);
        //header image view end

        /* category fetch  start*/
        RECYCLERVIEW_CATEGORY = findViewById(R.id.recycler_category);
        CATEGORY_MANAGER = new GridLayoutManager(MainActivity.this,1);
        RECYCLERVIEW_CATEGORY.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.HORIZONTAL,false));
        CATEGORY_MODELS = new ArrayList<>();

        /* category fetch  end*/

        // products fetch  start
        RECYCLERVIEW_PRODUCT = findViewById(R.id.recycler_product);
        PRODUCT_MANAGER = new GridLayoutManager(MainActivity.this,1);
        RECYCLERVIEW_PRODUCT.setLayoutManager(PRODUCT_MANAGER);
        PRODUCTS = new ArrayList<>();
        // products fetch  end

        // offer fetch  start
        RECYCLERVIEW_OFFER = findViewById(R.id.recycler_offer);
        OFFER_MANAGER = new GridLayoutManager(MainActivity.this,1);
        RECYCLERVIEW_OFFER.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.HORIZONTAL,false));
        OFFERS = new ArrayList<>();

        OFFER_ADAPTER = new OfferAdapter(MainActivity.this, OFFERS);
        RECYCLERVIEW_OFFER.setAdapter(OFFER_ADAPTER);
        autoScrollHandler.postDelayed(autoScrollRunnable, 80000);
        // products fetch  end

        CART_COUNT = findViewById(R.id.cartCounter);

        burgerIcon = findViewById(R.id.iv_burgerIcon);
        drawer = findViewById(R.id.drawer_layout);

        burgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        FLOATING_CART  = findViewById(R.id.floatingCart);
        FLOATING_CART.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CartActivity.class);
                startActivity(intent);
            }
        });

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
                    if (item.getProd_name().toLowerCase().contains(text.toLowerCase()) || (item.getProd_tam_name().toLowerCase().contains(text.toLowerCase()))){
                        filteredList.add(item);
                    }
                }
                if (filteredList.isEmpty()) {
                    Toast.makeText(MainActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                } else {
                    PRODUCT_ADAPTER.setFilteredList(filteredList);
                }
            }
        });
        /* filter from search bar End*/

        fetchCategory();
        //offers();
    }

    public void fetchProfile() {

        SharedPreferences sharedPreferences = getSharedPreferences("user_send_data", MODE_PRIVATE);
        String USER_ID = sharedPreferences.getString("USER_ID", "");

        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/customerprofile.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String CUST_ID = jsonObject.getString("cust_id");
                                String CUST_NAME = jsonObject.getString("cust_name");
                                String CUST_EMAIL = jsonObject.getString("cust_email");
                                String CUST_MOBILE = jsonObject.getString("cust_phone");
                                String CUST_IMG = jsonObject.getString("cust_img");

                                ProfileModel profileModel = new ProfileModel(CUST_ID,CUST_NAME,CUST_EMAIL,CUST_MOBILE,CUST_IMG);

                                NAV_NAME.setText(profileModel.getCUST_NAME());
                                NAV_EMAIL.setText(profileModel.getCUST_EMAIL());
                                Glide.with(MainActivity.this).load(profileModel.getIMG()).into(NAV_IMG);

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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                params.put("cust_id", USER_ID);

                return params;
            }
        };
        // Set a custom retry policy for the request
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    public void offers(){
        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/offer_fetch.php",
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            PRODUCTS.clear();
                            for (int i = 0; i<jsonArray.length();i++){

                                JSONObject object = jsonArray.getJSONObject(i);

                                String OFFER_NAME = object.getString("prod_name");
                                String OFFER_TYPE = object.getString("offer_type");
                                String OFFER_STORAGE_LIFE = object.getString("offer_life");
                                String OFFER_PRICE = object.getString("offer_price");
                                String OFFER_PROD_RATE = object.getString("prod_rate");
                                String OFFER_DESCRIPTION = object.getString("offer_desc");
                                String OFFER_PERCENTAGE = object.getString("offer_per");
                                String OFFER_IMAGE = object.getString("offer_img");

                                OfferModel offerModel =new OfferModel(OFFER_NAME,OFFER_TYPE,OFFER_STORAGE_LIFE,OFFER_PRICE,OFFER_PROD_RATE,OFFER_DESCRIPTION,OFFER_PERCENTAGE,OFFER_IMAGE);
                                OFFERS.add(offerModel);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        OFFER_ADAPTER = new OfferAdapter(MainActivity.this,OFFERS);
                        RECYCLERVIEW_OFFER.setAdapter(OFFER_ADAPTER);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(MainActivity.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Set a custom retry policy for the request
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    // Category fetch  start
    public void fetchCategory(){
        PROGRESSBAR.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/category_fetch.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PROGRESSBAR.setVisibility(View.GONE);
                        CATEGORY_MODELS.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i<jsonArray.length();i++){

                                JSONObject object = jsonArray.getJSONObject(i);

                                String cate_id = object.getString("cate_id");
                                String cate_eng_name = object.getString("cate_name");
                                String cate_tam_name = object.getString("tamil_name");
                                String cate_image = object.getString("cate_img");

                                CategoryModel categoryModel =new CategoryModel (cate_id,cate_eng_name,cate_tam_name,cate_image);
                                CATEGORY_MODELS.add(categoryModel);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        CATEGORY_ADAPTER = new CategoryAdapter(MainActivity.this,CATEGORY_MODELS);
                        RECYCLERVIEW_CATEGORY.setAdapter(CATEGORY_ADAPTER);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PROGRESSBAR.setVisibility(View.GONE);
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(MainActivity.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    // Category fetch  End

    // products fetch  start
    public void fetchProducts(){
        PROGRESSBAR_PROD.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/products_fetch.php",
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            PROGRESSBAR_PROD.setVisibility(View.GONE);
                            JSONArray jsonArray = new JSONArray(response);
                            PRODUCTS.clear();
                            for (int i = 0; i<jsonArray.length();i++){

                                JSONObject object = jsonArray.getJSONObject(i);

                                String prod_id = object.getString("prod_id");
                                String prod_eng_name = object.getString("prod_name");
                                String prod_tam_name = object.getString("tamil_name");
                                double prod_price = object.getDouble("rate");
                                String size_name = object.getString("size_name");
                                String prod_stock = object.getString("stock_type");
                                String prod_image = object.getString("prod_img");

                                ProductModel productModels =new ProductModel(prod_id,prod_eng_name,prod_tam_name,prod_image,prod_stock,size_name,prod_price);
                                PRODUCTS.add(productModels);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        PRODUCT_ADAPTER = new ProductMainAdapter(MainActivity.this,PRODUCTS);
                        RECYCLERVIEW_PRODUCT.setAdapter(PRODUCT_ADAPTER);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PROGRESSBAR_PROD.setVisibility(View.GONE);
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(MainActivity.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    // products fetch  End
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
                    Toast.makeText(MainActivity.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

         int id = item.getItemId();

          if (id == R.id.nav_home) {
              Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
          } else if (id == R.id.nav_profile) {
              startActivity(new Intent(MainActivity.this, Profile.class));
              Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
          }else if (id == R.id.nav_address){
              startActivity(new Intent(MainActivity.this, Address.class));
              Toast.makeText(this, "Address", Toast.LENGTH_SHORT).show();
          } else if (id == R.id.nav_myorders) {
              startActivity(new Intent(MainActivity.this, My_orders.class));
              Toast.makeText(this, "My Orders", Toast.LENGTH_SHORT).show();
          } else if (id == R.id.nav_help) {
              startActivity(new Intent(MainActivity.this, Help.class));
              Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
          } else if (id == R.id.nav_refer_friends) {
              startActivity(new Intent(MainActivity.this, Refer_Friends.class));
              Toast.makeText(this, "Refer Friends", Toast.LENGTH_SHORT).show();
          } else {
              if (id == R.id.nav_logout) {

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

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void AppUpdateDialog(){
        DIALOG = new Dialog(MainActivity.this);
        DIALOG.setContentView(R.layout.app_update_dialog);
        DIALOG.getWindow().setLayout(900,800);
        DIALOG.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DIALOG.show();

        AppCompatButton UPDATE = DIALOG.findViewById(R.id.app_update);
        ImageButton CANCEL = DIALOG.findViewById(R.id.dialog_cancel);

        UPDATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationContext().getPackageName())));
                } catch (ActivityNotFoundException e) {
                    // If Play Store app is not available, open the app page in a browser
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                }
                // Close the dialog if needed
                DIALOG.dismiss();
            }
        });
        CANCEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DIALOG.dismiss();
            }
        });
    }

    // network offline filter start
    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);

        fetchProfile();
        fetchProducts();
        cartCount();

        // Start the automatic scrolling
        autoScrollHandler.post(autoScrollRunnable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }
    // network offline filter End

    @Override
    protected void onPause() {
        super.onPause();
        // Stop the automatic scrolling
        autoScrollHandler.removeCallbacks(autoScrollRunnable);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}