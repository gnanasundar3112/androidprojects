package com.example.adminpanel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
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
import com.example.adminpanel.Adapter.Prod_Adapter;
import com.example.adminpanel.Model.Prod_Model;
import com.example.adminpanel.Model.Prod_Size_Model;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductMaster extends AppCompatActivity {
    private FloatingActionButton BACK, ADD_PRODUCT;
    private TextView APPBAR_TITLE, CATE_NAME;
    private TextInputEditText PRODUCT_ID, PRODUCT_NAME, TAMIL_NAME, SHORT_NAME, RATE, STOCK_AVAILABLE;
    private Spinner ACTIVE;
    private ImageView PROD_IMAGE;
    private Button PRODUCT_INSERT, PRODUCT_IMAGE_BTN;

    private RecyclerView PRODUCT_RECYCLER;
    private RecyclerView.LayoutManager PRODUCT_MANAGER;
    private List<Prod_Model> products;
    private Prod_Adapter product_Adapter;
    private ProgressBar PROGRESSBAR;

    RequestQueue requestQueue;
    ArrayList<String> prod_cate_list = new ArrayList<>();
    ArrayAdapter<String> prod_cate_adapter;
    Dialog dialog;
    private final int GALLERY_REQ_CODE = 1;
    Bitmap bitmap;
    private SearchView SEARCHVIEW;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_master);

        // products fetch  start
        PRODUCT_RECYCLER = findViewById(R.id.product_recycler);
        PRODUCT_MANAGER = new GridLayoutManager(ProductMaster.this, 1);
        PRODUCT_RECYCLER.setLayoutManager(PRODUCT_MANAGER);
        products = new ArrayList<>();
        fetchProducts();
        // products fetch  end

        BACK = findViewById(R.id.fab_backPress);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);
        PROGRESSBAR = findViewById(R.id.prodprogress);
        PRODUCT_INSERT = findViewById(R.id.prod_image_btn);
        PRODUCT_IMAGE_BTN = findViewById(R.id.prod_insert_btn);
        ADD_PRODUCT = findViewById(R.id.floating_prod_master);
        APPBAR_TITLE.setText("Product Master");
        BACK.setOnClickListener(view -> ProductMaster.super.onBackPressed());

        SEARCHVIEW = findViewById(R.id.searchView_prod);
        SEARCHVIEW.clearFocus();
        SEARCHVIEW.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fileList(newText);
                return true;
            }

            private void fileList(String text) {
                List<Prod_Model> filteredList = new ArrayList<>();
                for (Prod_Model item : products){
                    if (item.getProduct_name().toLowerCase().contains(text.toLowerCase()) ||
                            item.getProduct_tam_name().toLowerCase().contains(text.toLowerCase()) ||
                            item.getProduct_short_name().toLowerCase().contains(text.toLowerCase())){
                        filteredList.add(item);
                    }
                }
                if (filteredList.isEmpty()){
                    Toast.makeText(ProductMaster.this, "No data found", Toast.LENGTH_SHORT).show();
                }else {
                    product_Adapter.setFilteredList(filteredList);
                }
            }
        });
        /* filter from search bar End*/


        ADD_PRODUCT.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProductMaster.this, R.style.LogoutDialogThe);
                View view1 = LayoutInflater.from(ProductMaster.this).inflate(R.layout.product_dialog,
                        (LinearLayout) findViewById(R.id.prod_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.user_title)).setText("Product Master");
                ((MaterialButton) view1.findViewById(R.id.prod_cancel_btn)).setText("Cancel");
                ((MaterialButton) view1.findViewById(R.id.prod_insert_btn)).setText("Insert");

                PRODUCT_NAME = view1.findViewById(R.id.prod_name);
                TAMIL_NAME = view1.findViewById(R.id.tamil_name);
                SHORT_NAME = view1.findViewById(R.id.prod_short_name);
                RATE = view1.findViewById(R.id.prod_rate);
                STOCK_AVAILABLE = view1.findViewById(R.id.prod_stock);
                CATE_NAME = view1.findViewById(R.id.cate_name_spinner);
                ACTIVE = view1.findViewById(R.id.prod_active);
                PROD_IMAGE = view1.findViewById(R.id.prod_image);


                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProductMaster.this, R.array.Active, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_size);
                ACTIVE.setAdapter(adapter);

                final android.app.AlertDialog alertDialog = builder.create();

                view1.findViewById(R.id.prod_cancel_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                    }
                });
                view1.findViewById(R.id.prod_insert_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final ProgressDialog progressDialog1 = new ProgressDialog(ProductMaster.this);
                        progressDialog1.setMessage("Please wait...");

                        progressDialog1.show();

                        StringRequest request = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/adminpanel/image.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        progressDialog1.dismiss();
                                        if(response.equals("success")){
                                            Toast.makeText(ProductMaster.this, response, Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(ProductMaster.this, response, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog1.dismiss();
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        ) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {

                                Map<String, String> params = new HashMap<String, String>();

                                params.put("image",imageString(bitmap));

                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(request);
                    }
                });


                view1.findViewById(R.id.prod_image_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, GALLERY_REQ_CODE);
                    }
                });


                view1.findViewById(R.id.cate_name_spinner).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog = new Dialog(ProductMaster.this);

                        dialog.setContentView(R.layout.searchable_spinner);

                        dialog.getWindow().setLayout(900, 800);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                        EditText editText = dialog.findViewById(R.id.search_spinner);
                        ListView listView = dialog.findViewById(R.id.list_spinner);
                        ImageButton cancel = dialog.findViewById(R.id.dialog_cancel);

                        prod_cate_list.clear();
                        requestQueue = Volley.newRequestQueue(ProductMaster.this);
                        String url1 = "https://caustic-rinses.000webhostapp.com/adminpanel/category_fetch.php";
                        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url1, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray jsonArray = response.getJSONArray("catemaster");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        String sp_prod_name = object.getString("cate_name");
                                        String sp_prod_id = object.getString("cate_id");

                                        prod_cate_list.add(sp_prod_name);
                                        prod_cate_adapter = new ArrayAdapter<>(ProductMaster.this, android.R.layout.simple_list_item_1, prod_cate_list);
                                        prod_cate_adapter.setDropDownViewResource(R.layout.spinner_dropdown_size);
                                        listView.setAdapter(prod_cate_adapter);

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                        requestQueue.add(jsonObject);


                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                prod_cate_adapter.getFilter().filter(charSequence);
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                CATE_NAME.setText(prod_cate_adapter.getItem(i));
                                dialog.dismiss();
                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                    }
                });

                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQ_CODE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                PROD_IMAGE.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imageString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

        byte[] imaByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imaByte,Base64.DEFAULT).trim();
    }

    public void fetchProducts(){
        PROGRESSBAR = findViewById(R.id.prodprogress);
        PROGRESSBAR.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/adminpanel/product_master_fetch.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       PROGRESSBAR.setVisibility(View.GONE);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i<jsonArray.length();i++){

                                JSONObject object = jsonArray.getJSONObject(i);

                                String Product_cate = object.getString("cate_name");
                                String Product_id = object.getString("prod_id");
                                String Product_name = object.getString("prod_name");
                                String Product_tam_name = object.getString("tamil_name");
                                String Product_short_name = object.getString("short_name");
                                String Product_rate = object.getString("rate");
                                String Product_stock = object.getString("stock_type");
                                String Product_active = object.getString("active");
                                String Product_image = object.getString("image");

                                String url = "https://caustic-rinses.000webhostapp.com/admin/"+Product_image;
                                Prod_Model productModels =new Prod_Model(Product_cate,Product_id,Product_name,Product_tam_name,Product_short_name,Product_rate,Product_stock,Product_active,url);
                                products.add(productModels);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        product_Adapter = new Prod_Adapter(ProductMaster.this,products);
                        PRODUCT_RECYCLER.setAdapter(product_Adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PROGRESSBAR.setVisibility(View.GONE);
                Toast.makeText(ProductMaster.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}