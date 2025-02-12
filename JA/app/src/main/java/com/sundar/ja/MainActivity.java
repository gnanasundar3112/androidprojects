package com.sundar.ja;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sundar.ja.SqlDatabase.SqlDatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private ImageView BACK_PRESS, ADD_BTN, SETTING;
    private TextView APPBAR_TITLE;

    Button BACKUP, RESTORE;
    String[] PERMISSION = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
    ActivityResultLauncher<Intent> activityResultLauncher;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ADD_BTN = findViewById(R.id.add_btn);
        SETTING = findViewById(R.id.setting);
        SETTING.setVisibility(View.GONE);
        ADD_BTN.setPadding(0, 0, 40, 0);
        ADD_BTN.setVisibility(View.GONE);
        APPBAR_TITLE = findViewById(R.id.appbarTitle);
        APPBAR_TITLE.setText("Setting");
        APPBAR_TITLE.setPadding(60, 0, 0, 0);

        //back press activity
        BACK_PRESS = findViewById(R.id.backPress);
        BACK_PRESS.setOnClickListener(view -> MainActivity.super.onBackPressed());
        BACKUP = findViewById(R.id.backup);
        RESTORE = findViewById(R.id.restore);

        BACKUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    SqlDatabaseHelper dbHelper = new SqlDatabaseHelper(MainActivity.this);
                    dbHelper.exportDatabase(MainActivity.this);
                } else {
                    requestPermissions();
                }

            }
        });
        RESTORE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    SqlDatabaseHelper dbHelper = new SqlDatabaseHelper(MainActivity.this);
                    dbHelper.importDatabase(MainActivity.this);
                } else {
                    requestPermissions();
                }
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (Environment.isExternalStorageManager()) {
                            Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
    // THIS PERMISSION FOR ANDROID 11 AND ABOVE AND BELOW STORAGE PERMISSION
    void requestPermissions(){
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
            ActivityCompat.requestPermissions(MainActivity.this,PERMISSION,30);
        }
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
                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this, "You Denied Permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}