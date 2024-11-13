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
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import in.vinsoftsolutions.jayagrocery.R;

import in.vinsoftsolutions.jayagrocery.internet.NetworkChangeListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    CircleImageView User_Image;
    TextInputEditText User_Name,User_Email,User_Phone,User_Pass,User_Con_Pass;
    ImageView Select_Img_Btn,Pass_Icon,Con_Pass_Icon;
    AppCompatButton Register;
    MaterialTextView Register_Log;
    CardView CHECK_ONE,CHECK_TWO,CHECK_THREE,CHECK_FOUR;
    private boolean MINIMUM8LETTERS=false,MINIMUM_ONE_UPPERCASE=false,MINIMUM_ONE_NUMBER=false,MINIMUM_ONE_SYMBOL=false,REGISTER_CLICKABLE=false;
    private boolean PasswordShowing = false;
    private boolean ConPassShowing = false;
    private final int GALLERY_REQ_CODE = 1;
    private Bitmap bitmap;
    private static final long MAX_FILE_SIZE = 1000 * 1024;
    String[] PERMISSION = {READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE};
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        User_Name = findViewById(R.id.user_name);
        User_Email = findViewById(R.id.user_email);
        User_Phone = findViewById(R.id.user_phone);
        User_Pass = findViewById(R.id.user_pass);
        User_Con_Pass = findViewById(R.id.con_pass);

        // copy paste option disable
        User_Pass.setLongClickable(false);
        User_Con_Pass.setLongClickable(false);

        User_Image = findViewById(R.id.user_image);
        Select_Img_Btn = findViewById(R.id.select_img_btn);
        Pass_Icon = findViewById(R.id.pass_icon);
        Con_Pass_Icon = findViewById(R.id.con_pass_icon);

        Register = findViewById(R.id.register);
        Register_Log = findViewById(R.id.reg_login);

        CHECK_ONE = findViewById(R.id.check_one);
        CHECK_TWO = findViewById(R.id.check_two);
        CHECK_THREE = findViewById(R.id.check_three);
        CHECK_FOUR = findViewById(R.id.check_four);

        inputChange();

        Select_Img_Btn.setOnClickListener(new View.OnClickListener() {
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

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK){
                    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.R) {
                        if (Environment.isExternalStorageManager()){
                            Toast.makeText(Register.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Register.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String USER_NAME = User_Name.getText().toString();
                String USER_EMAIL = User_Email.getText().toString();
                String USER_PHONE = User_Phone.getText().toString();
                String USER_PASS = User_Pass.getText().toString();
                String USER_CON_PASS = User_Con_Pass.getText().toString();

                if (ImageString(bitmap) != null) {
                    long fileSize = ImageString(bitmap).getBytes().length;
                    if (fileSize > MAX_FILE_SIZE) {
                        Toast.makeText(Register.this, "Please select an image with dimensions less than or equal to 1280x720 (1MP)", Toast.LENGTH_SHORT).show();
                        return;
                    }
                 }
                if(USER_NAME.isEmpty()){
                    Toast.makeText(Register.this, "Please Enter User Name ", Toast.LENGTH_SHORT).show();
                } else if (USER_EMAIL.isEmpty()) {
                    Toast.makeText(Register.this, "Please Enter User Email ", Toast.LENGTH_SHORT).show();
                }else if (USER_PHONE.isEmpty()) {
                    Toast.makeText(Register.this, "Please Enter Mobile No ", Toast.LENGTH_SHORT).show();
                }else if (USER_PHONE.length() < 10) {
                    Toast.makeText(Register.this, "Please check correct Mobile number", Toast.LENGTH_SHORT).show();
                } else if (!USER_PHONE.matches("[0-9]+")) {
                    Toast.makeText(Register.this, "Please enter a valid phone number with numbers only", Toast.LENGTH_SHORT).show();
                }else if (USER_PASS.isEmpty()) {
                    Toast.makeText(Register.this, "Please Enter Password ", Toast.LENGTH_SHORT).show();
                }else if (USER_CON_PASS.isEmpty()) {
                    Toast.makeText(Register.this, "Please Enter Confirm Password ", Toast.LENGTH_SHORT).show();
                }else if (!USER_PASS.equals(USER_CON_PASS)){
                    Toast.makeText(Register.this, "Password not matching ", Toast.LENGTH_SHORT).show();
                }else {
                    if (MINIMUM8LETTERS && MINIMUM_ONE_NUMBER && MINIMUM_ONE_SYMBOL && MINIMUM_ONE_UPPERCASE &&  USER_PASS.length() > 0) {
                        insertData();
                    }else {
                        Toast.makeText(Register.this, "Password Criteria not fulfilled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Pass_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // password is show or not
                if (PasswordShowing){
                    PasswordShowing = false;
                    User_Pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);;
                    Pass_Icon.setImageResource(R.drawable.eye);
                }else {
                    PasswordShowing = true;
                    User_Pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);;
                    Pass_Icon.setImageResource(R.drawable.eye);
                }
                User_Pass.setSelection(User_Pass.length());
            }
        });
        Con_Pass_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // password is show or not
                if (ConPassShowing){
                    ConPassShowing = false;
                    User_Con_Pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);;
                    Con_Pass_Icon.setImageResource(R.drawable.eye);
                }else {
                    ConPassShowing = true;
                    User_Con_Pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);;
                    Con_Pass_Icon.setImageResource(R.drawable.eye);
                }
                User_Con_Pass.setSelection(User_Con_Pass.length());
            }
        });

        Register_Log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // password input valid function start
    @SuppressLint("ResourceType")
    private void passwordCheck(){
        String CUST_PASSWORD = User_Pass.getText().toString();

        if (CUST_PASSWORD.length() >=8){
            MINIMUM8LETTERS = true;
            CHECK_ONE.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }else {
            MINIMUM8LETTERS = false;
            CHECK_ONE.setCardBackgroundColor(Color.parseColor(getString(R.color.white)));
        }

        if (CUST_PASSWORD.matches("(.*[A-Z].*)")){
            MINIMUM_ONE_UPPERCASE = true;
            CHECK_TWO.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }else {
            MINIMUM_ONE_UPPERCASE = false;
            CHECK_TWO.setCardBackgroundColor(Color.parseColor(getString(R.color.white)));
        }

        if (CUST_PASSWORD.matches("(.*[0-9].*)")){
            MINIMUM_ONE_NUMBER = true;
            CHECK_THREE.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }else {
            MINIMUM_ONE_NUMBER = false;
            CHECK_THREE.setCardBackgroundColor(Color.parseColor(getString(R.color.white)));
        }

        if (CUST_PASSWORD.matches("^(?=.*[_.!@#$%^&*()+=<>?/:'~`]).*$")){
            MINIMUM_ONE_SYMBOL = true;
            CHECK_FOUR.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }else {
            MINIMUM_ONE_SYMBOL = false;
            CHECK_FOUR.setCardBackgroundColor(Color.parseColor(getString(R.color.white)));
        }
    }
    @SuppressLint("ResourceType")

    private void inputChange(){
        User_Pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    // input valid function end


    //insert data for database
    private void insertData() {
        final ProgressDialog progressDialog = new ProgressDialog(Register.this);
        progressDialog.setMessage("Please wait...");

        String USER_NAME = User_Name.getText().toString();
        String USER_EMAIL = User_Email.getText().toString();
        String USER_PHONE = User_Phone.getText().toString();
        String USER_PASS = User_Pass.getText().toString();

        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/register.php",
                new Response.Listener<String>() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        if (response.equalsIgnoreCase("Register successfully")) {
                            startActivity(new Intent(Register.this, Login.class));
                            finish();
                            Toast.makeText(Register.this, "Register successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Register.this, response, Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Register.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("cust_name",USER_NAME);
                params.put("cust_email",USER_EMAIL);
                params.put("cust_phone",USER_PHONE);
                params.put("cust_pass",USER_PASS);
                params.put("cust_image",ImageString(bitmap));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Register.this);
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
                User_Image.setImageBitmap(bitmap);

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

    // THIS PERMISSION FOR ANDROID 11 AND ABOVE AND BELOW STORAGE PERMISSION
    void requestPermissions(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Register.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(Register.this).inflate(R.layout.allownotidialog,
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
                    ActivityCompat.requestPermissions(Register.this,PERMISSION,30);
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
                        Toast.makeText(Register.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(Register.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Register.this, "You Denied Permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}