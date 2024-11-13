package in.vinsoftsolutions.jayagrocery;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import in.vinsoftsolutions.jayagrocery.Models.ProfileModel;

import in.vinsoftsolutions.jayagrocery.R;

import in.vinsoftsolutions.jayagrocery.internet.NetworkChangeListener;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private FloatingActionButton BACK_PRESS,EDIT_BTN;
    TextView APPBAR_TITLE,MCUST_ID,MCUST_NAME,MCUST_EMAIL,MCUST_PHONE;
    private CircleImageView MCUST_IMG;
    LinearLayout CHANGE_PASS;
    ImageView USER_IMAGE,USER_IMAGE_BTN;
    private final int GALLERY_REQ_CODE = 1;
    private Bitmap bitmap;
    private static final long MAX_FILE_SIZE = 1000 * 1024;
    private ProgressDialog progressDialog;
    String[] PERMISSION = {READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE};
    ActivityResultLauncher<Intent> activityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BACK_PRESS = findViewById(R.id.fab_backPress);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);

        MCUST_ID = findViewById(R.id.profile_Id);
        MCUST_NAME = findViewById(R.id.profile_name);
        MCUST_EMAIL = findViewById(R.id.profile_email);
        MCUST_PHONE = findViewById(R.id.profile_mobile);
        MCUST_IMG = findViewById(R.id.profile_img);

        CHANGE_PASS = findViewById(R.id.change_pass);

        EDIT_BTN = findViewById(R.id.floatingEdit);

        APPBAR_TITLE.setText("Profile");
        //back press activity
        BACK_PRESS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        fetchProfile();

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK){
                    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.R) {
                        if (Environment.isExternalStorageManager()){
                            Toast.makeText(Profile.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Profile.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        MCUST_IMG.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Profile.this, R.style.AlertDialogTheme);
                View view1 = LayoutInflater.from(Profile.this).inflate(R.layout.image_view_dialog,
                        (LinearLayout) findViewById(R.id.view_img_dialog));

                builder.setView(view1);

                final android.app.AlertDialog alertDialog = builder.create();
                USER_IMAGE = view1.findViewById(R.id.dialog_pro_img);

                Drawable USER_IMG = MCUST_IMG.getDrawable();
                Glide.with(Profile.this).load(USER_IMG).into(USER_IMAGE);

                view1.findViewById(R.id.dialog_pro_cancel).setOnClickListener(new View.OnClickListener() {
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

        CHANGE_PASS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String CUST_ID = MCUST_ID.getText().toString();
                String CUST_MOBILE = MCUST_PHONE.getText().toString();

                Intent intent = new Intent(Profile.this, Forrget_Pass_Otp.class);
                intent.putExtra("cust_id",CUST_ID);
                intent.putExtra("cust_mobile",CUST_MOBILE);
                startActivity(intent);
            }
        });

        EDIT_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Profile.this, R.style.AlertDialogTheme);
                View view1 = LayoutInflater.from(Profile.this).inflate(R.layout.edit_profile_dialog,
                        (ConstraintLayout) findViewById(R.id.edit_pro_dialog));

                builder.setView(view1);

                final android.app.AlertDialog alertDialog = builder.create();

                TextInputEditText MUSER_NAME = (TextInputEditText) view1.findViewById(R.id.edit_user_name);
                TextInputEditText MUSER_EMAIL = (TextInputEditText) view1.findViewById(R.id.edit_user_email);
                TextInputEditText MUSER_MOBILE = (TextInputEditText) view1.findViewById(R.id.edit_user_phone);
                USER_IMAGE = view1.findViewById(R.id.edit_user_image);
                USER_IMAGE_BTN = view1.findViewById(R.id.edit_select_img_btn);

                String USER_NAME = MCUST_NAME.getText().toString();
                String USER_EMAIL = MCUST_EMAIL.getText().toString();
                String USER_PHONE = MCUST_PHONE.getText().toString();
                Drawable USER_IMG = MCUST_IMG.getDrawable();

                MUSER_NAME.setText(USER_NAME);
                MUSER_EMAIL.setText(USER_EMAIL);
                MUSER_MOBILE.setText(USER_PHONE);
                Glide.with(Profile.this).load(USER_IMG).into(USER_IMAGE);

                USER_IMAGE_BTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(checkPermission()){
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, GALLERY_REQ_CODE);
                        }else {
                            requestPermissions();
                        }
                    }
                });

                view1.findViewById(R.id.edit_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String USER_PHONE = MUSER_MOBILE.getText().toString();
                        String USER_NAME = MUSER_NAME.getText().toString();
                        String USER_EMAIL = MUSER_EMAIL.getText().toString();

                        SharedPreferences sharedPreferences = getSharedPreferences("user_send_data", MODE_PRIVATE);
                        String USER_ID = sharedPreferences.getString("USER_ID", "");

                        if (ImageString(bitmap) != null) {
                            long fileSize = ImageString(bitmap).getBytes().length;
                            if (fileSize > MAX_FILE_SIZE) {
                                Toast.makeText(Profile.this, "Please select an image with dimensions less than or equal to 1280x720 (1MP)", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        if (USER_IMAGE.getDrawable() == null) {
                            Toast.makeText(Profile.this, "Please select an image", Toast.LENGTH_SHORT).show();
                        }else if (USER_PHONE.isEmpty()) {
                            Toast.makeText(Profile.this, "Please Enter Mobile No ", Toast.LENGTH_SHORT).show();
                        }else if (USER_PHONE.length() < 10) {
                            Toast.makeText(Profile.this, "Please check correct Mobile number", Toast.LENGTH_SHORT).show();
                        } else if (!USER_PHONE.matches("[0-9]+")) {
                            Toast.makeText(Profile.this, "Please enter a valid phone number with numbers only", Toast.LENGTH_SHORT).show();
                        }else {

                            progressDialog = ProgressDialog.show(Profile.this, "Please wait...", null, true, false);

                            Handler handler = new Handler();
                            // Create a new Runnable for the delayed task
                            Runnable delayedTask = new Runnable() {
                                @Override
                                public void run() {
                                    StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/edit_profile.php",
                                            new Response.Listener<String>() {
                                                @SuppressLint("ResourceType")
                                                @Override
                                                public void onResponse(String response) {
                                                    progressDialog.dismiss();

                                                    if (response.equalsIgnoreCase("Changed")) {
                                                        fetchProfile();
                                                        alertDialog.dismiss();
                                                        Toast.makeText(Profile.this, "Changed successfully", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(Profile.this, response, Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            progressDialog.dismiss();
                                            if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                                                Toast.makeText(Profile.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                    ) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<String, String>();

                                            params.put("cust_id", USER_ID);
                                            params.put("cust_name", USER_NAME);
                                            params.put("cust_email", USER_EMAIL);
                                            params.put("cust_phone", USER_PHONE);
                                            params.put("cust_image", ImageString(bitmap));
                                            return params;
                                        }
                                    };

                                    RequestQueue requestQueue = Volley.newRequestQueue(Profile.this);
                                    requestQueue.add(request);
                                    handler.removeCallbacks(this);
                                }
                            };
                            handler.postDelayed(delayedTask, 5000);
                        }
                    }
                });


                view1.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener() {
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

    private void fetchProfile() {

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

                                MCUST_ID.setText(profileModel.getCUST_ID());
                                MCUST_NAME.setText(profileModel.getCUST_NAME());
                                MCUST_EMAIL.setText(profileModel.getCUST_EMAIL());
                                MCUST_PHONE.setText(profileModel.getCUST_MOBILE());
                                Glide.with(Profile.this).load(profileModel.getIMG()).into(MCUST_IMG);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Profile.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
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
    // image convert for string and compress the quality
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==GALLERY_REQ_CODE && resultCode ==RESULT_OK && data !=null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                USER_IMAGE.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String ImageString(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imaByte = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(imaByte, Base64.DEFAULT);
        } else {
            return "";
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Profile.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // network offline filter start
    @Override
    protected void onStart() {
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        fetchProfile();
        super.onStart();
    }
    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
    // network offline filter End

    // THIS PERMISSION FOR ANDROID 11 AND ABOVE AND BELOW STORAGE PERMISSION
    void requestPermissions(){

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Profile.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(Profile.this).inflate(R.layout.allownotidialog,
                (ConstraintLayout)findViewById(R.id.notifi_dialog));

        builder.setView(view);

        final android.app.AlertDialog alertDialog = builder.create();

        ((TextView) view.findViewById(R.id.notifi_title)).setText("Permission");
        ((TextView) view.findViewById(R.id.notifi_message)).setText("Allow Storage Permission For Setting");

        view.findViewById(R.id.notifi_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.notifi_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
                    try {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                        intent.addCategory("android.intent.category.DEFAULT");
                        intent.setData(Uri.parse(String.format("package:%s", new Object[]{getApplicationContext().getPackageManager()})));
                        activityResultLauncher.launch(intent);
                    }catch (Exception e){
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                        activityResultLauncher.launch(intent);
                    }
                }
                else {
                    ActivityCompat.requestPermissions(Profile.this,PERMISSION,30);
                }
            }
        });

        if (alertDialog.getWindow() !=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
    boolean checkPermission(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager();
        }
        else {
            int readCheck = ContextCompat.checkSelfPermission(getApplicationContext(),READ_EXTERNAL_STORAGE);
            int writeCheck = ContextCompat.checkSelfPermission(getApplicationContext(),WRITE_EXTERNAL_STORAGE);
            return readCheck == PackageManager.PERMISSION_GRANTED && writeCheck == PackageManager.PERMISSION_GRANTED;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 30:
                if (grantResults.length > 0){
                    boolean readPer = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean writePer = grantResults[1]==PackageManager.PERMISSION_GRANTED;

                    if (readPer && writePer){
                        Toast.makeText(Profile.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(Profile.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Profile.this, "You Denied Permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}