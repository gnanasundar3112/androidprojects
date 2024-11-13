package com.example.adminpanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.example.adminpanel.Adapter.ReportsAdapter;
import com.example.adminpanel.Adapter.ReportsDetailAdapter;
import com.example.adminpanel.Model.ReportsDetailModel;
import com.example.adminpanel.Model.ReportsModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportsDetail extends AppCompatActivity {
    private FloatingActionButton BACK;
    private TextView APPBAR_TITLE,ORDER_NO,ORDER_DATE,REPORT_TOTAL_AMOUNT;
    private RecyclerView RECYCLERVIEW_REPORTS_DETAIL;
    private RecyclerView.LayoutManager REPORTS_DETAIL_MANAGER;
    private List<ReportsDetailModel> reportsDetailModels;
    private ReportsDetailAdapter reportsDetailAdapter;
    private ProgressBar PROGRESSBAR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_detail);

        BACK = findViewById(R.id.fab_backPress);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);
        APPBAR_TITLE.setText("Reports Detail");
        BACK.setOnClickListener(view -> ReportsDetail.super.onBackPressed());

        RECYCLERVIEW_REPORTS_DETAIL = findViewById(R.id.report_detail_recycler);
        REPORTS_DETAIL_MANAGER = new GridLayoutManager(ReportsDetail.this, 1);
        RECYCLERVIEW_REPORTS_DETAIL.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        reportsDetailModels = new ArrayList<>();
        PROGRESSBAR = findViewById(R.id.report_detail_ProgressBar);
        ORDER_NO = findViewById(R.id.report_detail_order_no);
        ORDER_DATE = findViewById(R.id.report_detail_order_date);
        REPORT_TOTAL_AMOUNT = findViewById(R.id.report_total_amount);
        getOrderDetail();

    }

    private void getOrderDetail() {
        Intent intent = getIntent();
        String Order_no = intent.getStringExtra("order_no");
        String Order_date = intent.getStringExtra("order_date");

        if (intent != null) {
            ORDER_NO.setText(Order_no);
            ORDER_DATE.setText(Order_date);
        }
        PROGRESSBAR.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, "https://caustic-rinses.000webhostapp.com/adminpanel/report_detail.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PROGRESSBAR.setVisibility(View.GONE);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            double item_total = 0;

                            for (int i = 0; i<jsonArray.length();i++){

                                JSONObject object = jsonArray.getJSONObject(i);

                                String Report_prod_name = object.getString("prod_name");
                                String Report_size_name = object.getString("size_name");
                                String Report_rate = object.getString("rate");
                                String Report_qty = object.getString("qty");
                                double Report_amount = object.getDouble("amount");

                                ReportsDetailModel reportsDetailModel =new ReportsDetailModel(Report_prod_name,Report_size_name,Report_rate,Report_qty,Report_amount);
                                reportsDetailModels.add(reportsDetailModel);

                                item_total += Report_amount;
                                REPORT_TOTAL_AMOUNT.setText("₹"+ item_total);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        reportsDetailAdapter = new ReportsDetailAdapter(ReportsDetail.this,reportsDetailModels);
                        RECYCLERVIEW_REPORTS_DETAIL.setAdapter(reportsDetailAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PROGRESSBAR.setVisibility(View.GONE);

                Toast.makeText(ReportsDetail.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("order_no",Order_no);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}