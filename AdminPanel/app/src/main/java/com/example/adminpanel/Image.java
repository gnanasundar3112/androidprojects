package com.example.adminpanel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.adminpanel.databinding.ActivityImageBinding;
import com.example.adminpanel.databinding.ActivityMainBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Image extends AppCompatActivity {

    ImageView UPLOAD_IMAGE;
    Button UPLOAD,UPLOAD_INSERT;

    public static final int GALLERY_REQ_CODE = 42;
    private Bitmap bitmap;

    ActivityImageBinding binding;
    String path,product_image_name;
    Uri uri_image = null;
    String[] permissionRequired = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    String upload_url = "https://caustic-rinses.000webhostapp.com/adminpanel/image.php";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UPLOAD_IMAGE = findViewById(R.id.upload_image);
        UPLOAD = findViewById(R.id.upload);
        UPLOAD_INSERT = findViewById(R.id.upload_insert);


        if (AskPermissions(this,permissionRequired)){
            ActivityCompat.requestPermissions(this,permissionRequired,1);
        }
        binding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        UPLOAD_INSERT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if (path.isEmpty()){
                 Toast.makeText(Image.this, "Please select image", Toast.LENGTH_SHORT).show();
             }else {
                 new uploadImageTask().execute();
             }
            }
        });
    }

    public void showFileChooser(){
        Toast.makeText(this, "Choose Picture", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setType("image/jpeg");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),GALLERY_REQ_CODE);
    }

    @SuppressLint("Range")
    public String getPath(Uri uri){

        try {
            Cursor cursor = getContentResolver().query(uri,null,null,null,null);
            cursor.moveToFirst();
            String document_id  = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":")+1);
            Log.d("document_id",document_id+"");
            cursor.close();

            cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null,MediaStore.Images.Media._ID+" = ? ",
                    new String[]{document_id},null);
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));


            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return path;
    }

    public class uploadImageTask extends AsyncTask<Void,Void,Void>{

        String name = "", path= "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            name = product_image_name.toString();
            path = getPath(uri_image);
            Log.d("name",name+"");
            Log.d("path",path+"");

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                /*new MultipartUploadRequest(Image.this,name,upload_url).setMethod("POST")
                        .addFileToUpload(path,"uploadfile")
                        .addParameter("name",name)
                        .setMaxRetries(2).startUpload();*/


            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            clearData();
        }
    }


    public static boolean AskPermissions(Context context,String...permission){

        if (context!=null && permission!=null){
            for (String permissions : permission){
                if (ActivityCompat.checkSelfPermission(context,permissions)!= PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }

    public void clearData(){
        binding.uploadImage.setImageResource(R.drawable.vinsoft_logo);
        path = "";
        Toast.makeText(getApplicationContext(), "Image uploaded...", Toast.LENGTH_SHORT).show();
    }




    @SuppressLint("WrongConstant")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==GALLERY_REQ_CODE && resultCode ==RESULT_OK && data !=null && data.getData() !=null) {
            uri_image = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri_image);
                binding.uploadImage.setImageBitmap(bitmap);

                Log.d("uri_image",uri_image+"");

                product_image_name = ""+getPath(uri_image).toString().substring(getPath(uri_image).toString().lastIndexOf('/')+1);

                final int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                getContentResolver().takePersistableUriPermission(uri_image,takeFlags);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String ImageString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

        byte[] imaByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imaByte,Base64.DEFAULT).trim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}