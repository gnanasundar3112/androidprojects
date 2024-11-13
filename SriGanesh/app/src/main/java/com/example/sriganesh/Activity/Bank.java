package com.example.sriganesh.Activity;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.example.sriganesh.Adapter.BankAdapter;
import com.example.sriganesh.Adapter.PaymentAdapter;
import com.example.sriganesh.Model.BankModel;
import com.example.sriganesh.Model.PaymentModel;
import com.example.sriganesh.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Bank extends AppCompatActivity {

    private RecyclerView recyclerViewBank;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager manager;
    private List<BankModel> models;
    private ProgressBar progressBar;

    private EditText BANK_DATE;
    private Button BANK_PROCESS,BANK_PDF;
    private FloatingActionButton backPress;
    private TextView appbarTitle,BANK_TOTAL;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        progressBar = findViewById(R.id.ProgressBar1);
        recyclerViewBank = findViewById(R.id.bank_recycler);
        manager = new GridLayoutManager(Bank.this,1);
        recyclerViewBank.setLayoutManager(manager);
        models = new ArrayList<>();

        BANK_DATE = findViewById(R.id.bank_date);
        BANK_PROCESS = findViewById(R.id.bank_process);
        BANK_PDF = findViewById(R.id.bank_pdf);
        BANK_TOTAL = findViewById(R.id.bank_total);
        backPress = findViewById(R.id.fab_backPress);
        appbarTitle = findViewById(R.id.txt_appbarTitle);


        ActivityCompat.requestPermissions(this,new String[]{
                WRITE_EXTERNAL_STORAGE},PackageManager.PERMISSION_GRANTED);
        //pdf();

        calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                getDate();
            }
        };
        BANK_DATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Bank.this,date,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });
        BANK_PROCESS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchDetails();

            }
        });
        BANK_PDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Bank.this,Chart_Activity.class);
                startActivity(intent);
            }
        });

        appbarTitle.setText("Bank");
        //back press activity
        backPress.setOnClickListener(view -> Bank.super.onBackPressed());
    }

    private void getDate() {
        String DateFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat, Locale.US);
        BANK_DATE.setText(dateFormat.format(calendar.getTime()));
    }

    private void pdf() {
        BANK_PDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                PdfDocument pdfDocument = new PdfDocument();
                Paint paint = new Paint();
                Paint TablePaint = new Paint();

                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(250,400,1).create();
                PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                Canvas canvas = page.getCanvas();
                File file = new File(Environment.getExternalStorageDirectory(),"/BankPDF.pdf");

                canvas.drawText("Sri Ganesh",100,20,paint);
                paint.setTextAlign(Paint.Align.CENTER);
                pdfDocument.finishPage(page);

                paint.setStrokeWidth(2f);

                 canvas.drawLine(180,790,180,840,paint);
                 canvas.drawLine(680,790,180,840,paint);
                 canvas.drawLine(880,790,180,840,paint);
                 canvas.drawLine(1030,790,180,840,paint);

               // paint.setTextAlign(Paint.Align.LEFT);
                //paint.setStyle(Paint.Style.FILL);
                //canvas.drawText("DATE",40,550,TablePaint);
                //canvas.drawText("NAME",200,550,TablePaint);
                //canvas.drawText("AMOUNT",500,550,TablePaint);


                try {
                    pdfDocument.writeTo(new FileOutputStream(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pdfDocument.close();
                Toast.makeText(Bank.this, "pdf created", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchDetails() {
        String date = BANK_DATE.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/admin/getcartproducts.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    double item_total = 0;

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String Bank_Type = jsonObject.getString("id");
                        String Bank_Name = jsonObject.getString("eng_name");
                        double Bank_Amount = jsonObject.getDouble("amount");

                        BankModel bankModel = new BankModel(Bank_Type,Bank_Name,Bank_Amount);
                        models.add(bankModel);

                        item_total += Bank_Amount;
                        BANK_TOTAL.setText("₹"+ item_total);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                mAdapter = new BankAdapter(Bank.this, models);
                recyclerViewBank.setAdapter(mAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Bank.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id",date);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

}
