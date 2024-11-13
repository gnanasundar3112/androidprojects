package in.vinsoftsolutions.aspn;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.vinsoftsolutions.aspn.Adapter.ReceiptAdapter;
import in.vinsoftsolutions.aspn.Models.ReceiptModel;

public class Receipt extends AppCompatActivity {
    private ImageView BACK_PRESS;
    private TextView APPBAR_TITLE;
    private TextView RECEIPT_NO,RECEIPT_DATE,PARTY_NAME;
    private String PARTY_ID;
    private EditText DISCOUNT;
    private MaterialButton OK,SMS,PDF,NEXT;

    private RecyclerView RPT_RECYCLER;
    private RecyclerView.LayoutManager RPT_MANAGER;
    private List<ReceiptModel> RECEIPT;
    private ReceiptAdapter RPT_ADAPTER;

    private Dialog dialog;

    String[] PERMISSION = {READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE};
    ActivityResultLauncher<Intent> activityResultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipte);

        APPBAR_TITLE = findViewById(R.id.appbarTitle);
        APPBAR_TITLE.setText("Receipt");

        BACK_PRESS = findViewById(R.id.backPress);
        BACK_PRESS.setOnClickListener(view -> Receipt.super.onBackPressed());

        // fetch  start
        RPT_RECYCLER = findViewById(R.id.rpt_recyclerView);
        RPT_MANAGER = new GridLayoutManager(Receipt.this, 1);
        RPT_RECYCLER.setLayoutManager(RPT_MANAGER);
        RECEIPT = new ArrayList<>();

        RECEIPT.add(new ReceiptModel("AR000001", "2024-04-29", "5", "100", "90"));
        RECEIPT.add(new ReceiptModel("AR000002", "2024-05-02", "20", "10000", "190"));

        RPT_ADAPTER = new ReceiptAdapter(Receipt.this,RECEIPT);
        RPT_RECYCLER.setAdapter(RPT_ADAPTER);


        RECEIPT_NO = findViewById(R.id.rpt_no);
        RECEIPT_DATE = findViewById(R.id.rpt_date);
        PARTY_NAME = findViewById(R.id.party_name);
        DISCOUNT = findViewById(R.id.discount);
        OK = findViewById(R.id.btn_ok);
        SMS = findViewById(R.id.btn_sms);
        PDF = findViewById(R.id.btn_pdf);
        NEXT = findViewById(R.id.btn_next);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = dateFormat.format(new Date());
        RECEIPT_DATE.setText(currentDate);

        PARTY_NAME.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Receipt_no = RECEIPT_NO.getText().toString().trim();
                String Receipt_date = RECEIPT_DATE.getText().toString().trim();

                PartyFetch();
            }
        });

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Discount = DISCOUNT.getText().toString().trim();

                Toast.makeText(Receipt.this, Discount, Toast.LENGTH_SHORT).show();
            }
        });

        SMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Receipt.this, "SMS BTN CLICK", Toast.LENGTH_SHORT).show();
            }
        });

        PDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission()){
                    pdfCrate();
                }else {
                    requestPermissions();
                }
            }
        });

        NEXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Receipt.this, "NEXT BTN CLICK", Toast.LENGTH_SHORT).show();
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK){
                    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.R) {
                        if (Environment.isExternalStorageManager()){
                            Toast.makeText(Receipt.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Receipt.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
    public void PartyFetch(){

        // Create a list to store values
        HashMap<String,String> party = new HashMap<String,String>();
        party.put("10","sundar");
        party.put("20","Mani");
        party.put("30","Surash");
        party.put("40","ramesh");

        List<String> partyNames = new ArrayList<>(party.values());
        Collections.reverse(partyNames);

        dialog = new Dialog(Receipt.this);
        dialog.setContentView(R.layout.dialog_search_spinner);

        // Set dialog width to match parent
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = 1000;
        dialog.getWindow().setAttributes(layoutParams);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        EditText editText = dialog.findViewById(R.id.spinner_search);
        ListView listView = dialog.findViewById(R.id.spinner_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>((Context) Receipt.this, android.R.layout.simple_list_item_1, partyNames);
        listView.setAdapter(adapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("Range")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item's value
                String selectedPartyName = adapter.getItem(position);

                // Find the key corresponding to the selected value
                String selectedPartyId = null;
                for (Map.Entry<String, String> entry : party.entrySet()) {
                    if (entry.getValue().equals(selectedPartyName)) {
                        selectedPartyId = entry.getKey();
                        break;
                    }
                }

                // Now you have both the key and value, you can use them as needed
                if (selectedPartyId != null) {
                    // Use selectedPartyId and selectedPartyName as needed
                    PARTY_NAME.setText(selectedPartyName);
                    PARTY_ID = selectedPartyId;

                    Toast.makeText(Receipt.this, PARTY_ID, Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

    }

    public void fetch(){
        String Party_id = PARTY_ID;
        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/customerprofile.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String Bill_no = jsonObject.getString("bill_no");
                                String Bill_date = jsonObject.getString("bill_date");
                                String Days = jsonObject.getString("days");
                                String Amount = jsonObject.getString("amount");
                                String Rpt_amount = jsonObject.getString("rpt_amount");

                                ReceiptModel receiptModel =new ReceiptModel(Bill_no,Bill_date,Days,Amount,Rpt_amount);
                                RECEIPT.add(receiptModel);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RPT_ADAPTER = new ReceiptAdapter(Receipt.this,RECEIPT);
                        RPT_RECYCLER.setAdapter(RPT_ADAPTER);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Receipt.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("party_id", Party_id);
                return params;
            }
        };
        // Set a custom retry policy for the request
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    public void insert(){
        String Receipt_no = RECEIPT_NO.getText().toString().trim();
        String Receipt_date = RECEIPT_DATE.getText().toString().trim();
        String Party_id = PARTY_ID;
        String Discount = DISCOUNT.getText().toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-android/add_cart_prod.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("Insert Successfully")){
                            Toast.makeText(Receipt.this, "Insert Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Receipt.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Receipt.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rpt_no",Receipt_no);
                params.put("rpt_date",Receipt_date);
                params.put("party_id",Party_id);
                params.put("discount",Discount);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Receipt.this);
        requestQueue.add(request);
    }

    public void pdfCrate(){
        Toast.makeText(this, "Pdf Created Successfully", Toast.LENGTH_SHORT).show();
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void changeStatusBarColour(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }
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
            ActivityCompat.requestPermissions(Receipt.this,PERMISSION,30);
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
                        Toast.makeText(Receipt.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(Receipt.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Receipt.this, "You Denied Permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}