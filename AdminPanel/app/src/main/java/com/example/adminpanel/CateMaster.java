package com.example.adminpanel;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.adminpanel.Adapter.Cate_Adapter;
import com.example.adminpanel.Model.Cate_Model;
import com.example.adminpanel.Model.Prod_Size_Model;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CateMaster extends AppCompatActivity {

    private FloatingActionButton BACK,ADD_CATEGORY;
    private TextView APPBAR_TITLE;
    private TextInputEditText CATE_NAME,TAMIL_NAME,SHORT_NAME;
    private Spinner ACTIVE;
    private ImageView IMAGE;
    private Button CATE_INSERT,CATE_IMAGE_BTN;
    private ProgressBar PROGRASSBAR;
    private final int GALLERY_REQ_CODE = 1;
    private static final long MAX_FILE_SIZE = 1000 * 1024;
    private Bitmap bitmap;
    String encodeImage;
    private RecyclerView RECYCLERVIEW_CATE;
    private RecyclerView.LayoutManager CATE_MANAGER;
    private List<Cate_Model> categoryModels;
    private Cate_Adapter categoryAdapter;
    private SearchView SEARCHVIEW;
    private static final int MINIMUM_QUALITY = 1000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cate_master);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        BACK = findViewById(R.id.fab_backPress);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);
        PROGRASSBAR = findViewById(R.id.ProgressBar1);
        fetchCategory();

        /* category fetch  start*/
        RECYCLERVIEW_CATE = findViewById(R.id.cate_recycler);
        CATE_MANAGER = new GridLayoutManager(CateMaster.this, 1);
        RECYCLERVIEW_CATE.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        categoryModels = new ArrayList<>();
        /* category fetch  end*/

        ADD_CATEGORY = findViewById(R.id.floating_cate_btn);
        APPBAR_TITLE.setText("Cate Master");
        BACK.setOnClickListener(view -> CateMaster.super.onBackPressed());

        SEARCHVIEW = findViewById(R.id.searchView_cate);
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
                List<Cate_Model> filteredList = new ArrayList<>();
                for (Cate_Model item : categoryModels){
                    if (item.getCate_name().toLowerCase().contains(text.toLowerCase()) || item.getTamil_name().toLowerCase().contains(text.toLowerCase()) ||
                            item.getShort_name().toLowerCase().contains(text.toLowerCase())){
                        filteredList.add(item);
                    }
                }
                if (filteredList.isEmpty()){
                    Toast.makeText(CateMaster.this, "No data found", Toast.LENGTH_SHORT).show();
                }else {
                    categoryAdapter.setFilteredList(filteredList);
                }
            }
        });
        /* filter from search bar End*/

        ADD_CATEGORY.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View view) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CateMaster.this, R.style.LogoutDialogThe);
                View view1 = LayoutInflater.from(CateMaster.this).inflate(R.layout.cate_dialog,
                        (LinearLayout) findViewById(R.id.cate_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.user_title)).setText("Category Master");
                ((MaterialButton) view1.findViewById(R.id.cate_cancel)).setText("Cancel");
                ((MaterialButton) view1.findViewById(R.id.cate_insert)).setText("Insert");

                CATE_NAME = view1.findViewById(R.id.cate_name);
                TAMIL_NAME = view1.findViewById(R.id.tamil_name);
                SHORT_NAME = view1.findViewById(R.id.short_name);
                ACTIVE = view1.findViewById(R.id.cate_active);
                IMAGE = view1.findViewById(R.id.cate_image);
                CATE_IMAGE_BTN = view1.findViewById(R.id.cate_image_btn);
                CATE_INSERT = view1.findViewById(R.id.cate_insert);

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CateMaster.this, R.array.Active, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_size);
                ACTIVE.setAdapter(adapter);

                final android.app.AlertDialog alertDialog = builder.create();


                view1.findViewById(R.id.cate_image_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, GALLERY_REQ_CODE);

                    }
                });

                view1.findViewById(R.id.cate_insert).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        /*  if (encodeImage != null) {

                            long fileSize = encodeImage.getBytes().length;

                            if (fileSize > MAX_FILE_SIZE) {
                                Toast.makeText(CateMaster.this, "Maximum 1 MP", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }*/

                        String cate_name_error = ((TextInputEditText) view1.findViewById(R.id.cate_name)).getText().toString();
                        String tamil_name_error = ((TextInputEditText) view1.findViewById(R.id.tamil_name)).getText().toString();


                            if (cate_name_error.isEmpty()) {
                                ((TextInputEditText) view1.findViewById(R.id.cate_name)).setError("Category name is empty");
                            } else {
                            if (tamil_name_error.isEmpty()) {
                                ((TextInputEditText) view1.findViewById(R.id.tamil_name)).setError("Tamil name is empty");
                            } else {
                                final ProgressDialog progressDialog1 = new ProgressDialog(CateMaster.this);
                                progressDialog1.setMessage("Please wait...");

                                progressDialog1.show();

                                String cate_name = ((TextInputEditText) view1.findViewById(R.id.cate_name)).getText().toString();
                                String tamil_name = ((TextInputEditText) view1.findViewById(R.id.tamil_name)).getText().toString();
                                String short_name = ((TextInputEditText) view1.findViewById(R.id.short_name)).getText().toString();
                                String active = ((Spinner) view1.findViewById(R.id.cate_active)).getSelectedItem().toString();


                                StringRequest request = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/adminpanel/catemaster.php",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                progressDialog1.dismiss();
                                                if (response.equals("success")) {
                                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                                                } else {
                                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
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
                                        params.put("cate_name", cate_name);
                                        params.put("tamil_name", tamil_name);
                                        params.put("short_name", short_name);
                                        params.put("active", active);
                                        //params.put("image",ImageString(bitmap));

                                        return params;
                                    }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                requestQueue.add(request);
                            }
                        }
                    }
                });

                view1.findViewById(R.id.cate_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

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

        if (requestCode ==GALLERY_REQ_CODE && resultCode ==RESULT_OK && data !=null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                IMAGE.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String ImageString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

        byte[] imaByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imaByte,Base64.DEFAULT);
    }
    public void fetchCategory(){
    PROGRASSBAR.setVisibility(View.VISIBLE);
    StringRequest request = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/adminpanel/category_fetch_detail.php",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    PROGRASSBAR.setVisibility(View.GONE);

                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i<jsonArray.length();i++){

                            JSONObject object = jsonArray.getJSONObject(i);

                            String Cate_id = object.getString("cate_id");
                            String Cate_name = object.getString("cate_name");
                            String tamil_name = object.getString("tamil_name");
                            String Short_name = object.getString("short_name");
                            String Active = object.getString("active");
                            String Cate_image = object.getString("image");

                            String url = "https://caustic-rinses.000webhostapp.com/adminpanel/"+Cate_image;
                            Cate_Model categoryModel =new Cate_Model (Cate_id,Cate_name,tamil_name,Short_name,Active,url);
                            categoryModels.add(categoryModel);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    categoryAdapter = new Cate_Adapter(CateMaster.this,categoryModels);
                    RECYCLERVIEW_CATE.setAdapter(categoryAdapter);
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            PROGRASSBAR.setVisibility(View.GONE);
            Toast.makeText(CateMaster.this, error.toString(), Toast.LENGTH_SHORT).show();
        }
    });
    RequestQueue requestQueue = Volley.newRequestQueue(this);
    requestQueue.add(request);
}
}