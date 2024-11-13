package com.example.adminpanel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.example.adminpanel.Adapter.Cate_Adapter;
import com.example.adminpanel.Adapter.UserAdapter;
import com.example.adminpanel.Model.Cate_Model;
import com.example.adminpanel.Model.UserModel;
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

public class DeliveryUser extends AppCompatActivity {
    private FloatingActionButton BACK, ADD_USER;
    private TextView APPBAR_TITLE;
    private TextInputEditText USER_NAME, USER_PASSWORD, USER_AADHAR, USER_ADDRESS;
    private Spinner USER_ACTIVE;
    private ImageView USER_IMAGE, AADHAR_IMAGE, BIKE_IMAGE;
    private Button USER_IMAGE_BTN, AADHAR_IMAGE_BTN, BIKE_IMAGE_BTN;
    private final int USER_GALLERY_REQ_CODE = 1;
    private final int AADHAR_GALLERY_REQ_CODE = 2;
    private final int BIKE_GALLERY_REQ_CODE = 3;
    Bitmap user_bitmap,aadhar_bitmap,bike_bitmap;
    private RecyclerView RECYCLERVIEW_USER;
    private RecyclerView.LayoutManager USER_MANAGER;
    private List<UserModel> userModels;
    private UserAdapter userAdapter;
    private ProgressBar PROGRASSBAR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_user);
        BACK = findViewById(R.id.fab_backPress);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);

        /* user fetch  start*/
        RECYCLERVIEW_USER = findViewById(R.id.user_recycler);
        USER_MANAGER = new GridLayoutManager(DeliveryUser.this, 1);
        RECYCLERVIEW_USER.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        userModels = new ArrayList<>();
        PROGRASSBAR = findViewById(R.id.ProgressBar_user);
        fetchUser();
        /* user fetch  end*/

        USER_NAME = findViewById(R.id.user_name);
        USER_PASSWORD = findViewById(R.id.user_password);
        USER_AADHAR = findViewById(R.id.user_aadhar_no);
        USER_ADDRESS = findViewById(R.id.user_address);
        USER_ACTIVE = findViewById(R.id.user_active);

        USER_IMAGE_BTN = findViewById(R.id.user_image_btn);
        ADD_USER = findViewById(R.id.floating_user_btn);

        APPBAR_TITLE.setText("Delivery User");
        BACK.setOnClickListener(view -> DeliveryUser.super.onBackPressed());

        ADD_USER.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DeliveryUser.this, R.style.LogoutDialogThe);
                View view1 = LayoutInflater.from(DeliveryUser.this).inflate(R.layout.user_dialog,
                        (LinearLayout) findViewById(R.id.user_dialog));

                builder.setView(view1);
                ((TextView) view1.findViewById(R.id.user_title)).setText("Delivery User Detail");
                ((MaterialButton) view1.findViewById(R.id.user_cancel_btn)).setText("Cancel");
                ((MaterialButton) view1.findViewById(R.id.user_insert_btn)).setText("Insert");

                USER_NAME = view1.findViewById(R.id.user_name);
                USER_PASSWORD = view1.findViewById(R.id.user_password);
                USER_AADHAR = view1.findViewById(R.id.user_aadhar_no);
                USER_ADDRESS = view1.findViewById(R.id.user_address);
                USER_ACTIVE = view1.findViewById(R.id.user_active);
                USER_IMAGE_BTN = view1.findViewById(R.id.user_image_btn);
                AADHAR_IMAGE_BTN = view1.findViewById(R.id.adhar_image_btn);
                BIKE_IMAGE_BTN = view1.findViewById(R.id.bick_image_btn);

                USER_IMAGE = view1.findViewById(R.id.user_image);
                AADHAR_IMAGE = view1.findViewById(R.id.adhar_image);
                BIKE_IMAGE = view1.findViewById(R.id.bick_image);

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(DeliveryUser.this, R.array.Active, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_size);
                USER_ACTIVE.setAdapter(adapter);

                final android.app.AlertDialog alertDialog = builder.create();

                view1.findViewById(R.id.user_image_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, USER_GALLERY_REQ_CODE);

                    }
                });
                view1.findViewById(R.id.adhar_image_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, AADHAR_GALLERY_REQ_CODE);
                    }
                });
                view1.findViewById(R.id.bick_image_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, BIKE_GALLERY_REQ_CODE);

                    }
                });


                view1.findViewById(R.id.user_insert_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String user_name = ((TextInputEditText) view1.findViewById(R.id.user_name)).getText().toString();
                        String user_phone = ((TextInputEditText) view1.findViewById(R.id.user_phone)).getText().toString();
                        String user_password = ((TextInputEditText) view1.findViewById(R.id.user_password)).getText().toString();
                        String user_aadhar = ((TextInputEditText) view1.findViewById(R.id.user_aadhar_no)).getText().toString();
                        String user_address = ((TextInputEditText) view1.findViewById(R.id.user_address)).getText().toString();
                        String active = ((Spinner) view1.findViewById(R.id.user_active)).getSelectedItem().toString();


                        final ProgressDialog progressDialog1 = new ProgressDialog(DeliveryUser.this);
                        progressDialog1.setMessage("Please wait...");

                        progressDialog1.show();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/adminpanel/usermaster.php",
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
                                Toast.makeText(DeliveryUser.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog1.dismiss();
                            }
                        } )
                        {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> params = new HashMap<>();
                                params.put("user_name",user_name);
                                params.put("user_phone",user_phone);
                                params.put("user_password",user_password);
                                params.put("aadhar_no",user_aadhar);
                                params.put("user_address",user_address);
                               // params.put("user_image",UserImageString(user_bitmap));
                               // params.put("aadhar_image",AAdharImageString(aadhar_bitmap));
                               // params.put("bike_image",BikeImageString(bike_bitmap));
                                params.put("active",active);
                                return params;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(stringRequest);
                    }
                });
                view1.findViewById(R.id.user_cancel_btn).setOnClickListener(new View.OnClickListener() {
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

        if (requestCode == USER_GALLERY_REQ_CODE && resultCode == RESULT_OK && data != null) {
            Uri user_uri = data.getData();
            try {
                user_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), user_uri);
                USER_IMAGE.setImageBitmap(user_bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == AADHAR_GALLERY_REQ_CODE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                aadhar_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                AADHAR_IMAGE.setImageBitmap(aadhar_bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == BIKE_GALLERY_REQ_CODE && resultCode == RESULT_OK && data != null) {
            Uri bick_uri = data.getData();
            try {
                bike_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), bick_uri);
                BIKE_IMAGE.setImageBitmap(bike_bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String UserImageString(Bitmap user_bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        user_bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

        byte[] imaByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imaByte,Base64.DEFAULT).trim();
    }
    private String AAdharImageString(Bitmap aadhar_bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        aadhar_bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

        byte[] imaByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imaByte,Base64.DEFAULT).trim();
    }
    private String BikeImageString(Bitmap bike_bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bike_bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

        byte[] imaByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imaByte,Base64.DEFAULT).trim();
    }

    public void fetchUser(){
        PROGRASSBAR.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/adminpanel/user_delivery_data.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PROGRASSBAR.setVisibility(View.GONE);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i<jsonArray.length();i++){

                                JSONObject object = jsonArray.getJSONObject(i);

                                String User_Id = object.getString("user_id");
                                String User_Name = object.getString("user_name");
                                String User_mobile = object.getString("user_phone");
                                String User_password = object.getString("user_password");
                                String User_aadhar = object.getString("aadhar_no");
                                String User_address = object.getString("user_address");
                                String User_image = object.getString("user_image");
                                String Adhar_image = object.getString("aadhar_image");
                                String Veh_image = object.getString("bike_image");
                                String Active = object.getString("active");

                                String USER_URL = "https://caustic-rinses.000webhostapp.com/admin/"+User_image;
                                String AADHAR_URL = "https://caustic-rinses.000webhostapp.com/admin/"+Adhar_image;
                                String VEHICLE_URL = "https://caustic-rinses.000webhostapp.com/admin/"+Veh_image;
                                UserModel userModel =new UserModel (User_Id,User_Name,User_mobile,User_password,User_aadhar,User_address,USER_URL,AADHAR_URL,VEHICLE_URL,Active);
                                userModels.add(userModel);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        userAdapter = new UserAdapter(DeliveryUser.this,userModels);
                        RECYCLERVIEW_USER.setAdapter(userAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PROGRASSBAR.setVisibility(View.GONE);
                Toast.makeText(DeliveryUser.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}