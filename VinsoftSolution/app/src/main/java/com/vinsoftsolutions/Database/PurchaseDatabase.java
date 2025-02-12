package com.vinsoftsolutions.Database;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vinsoftsolutions.Adapter.PurchaseAdapter;
import com.vinsoftsolutions.Models.PurchaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PurchaseDatabase {
    private Context context;
    private List<PurchaseModel> PURCHASE = new ArrayList<>();
    private PurchaseAdapter PURCHASE_ADAPTER;

    public PurchaseDatabase(Context context) {
        this.context = context;
    }

    public void fetch(RecyclerView PAYMENT_RECYCLER, String from_date,String to_date, TextView total) {

//      String url = BaseUrl.BASE_URL + "/purchase/" + from_date + "/" + to_date;
        String url = "http://192.168.250.112:8080/purchase/" + from_date + "/" + to_date;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                PURCHASE.clear();
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    double totalAmt = 0;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String type = object.getString("type");
                        double amount = object.getDouble("net_amt");
                        String formattedAmount = String.format("%.2f", amount);

                        totalAmt += amount;

                        PurchaseModel purchaseModel = new PurchaseModel(type, formattedAmount);
                        PURCHASE.add(purchaseModel);
                    }
                    String formattedTotal = String.format("%.2f", totalAmt);
                    total.setText(formattedTotal);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                PURCHASE_ADAPTER = new PurchaseAdapter(context, PURCHASE);
                PAYMENT_RECYCLER.setAdapter(PURCHASE_ADAPTER);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(context, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
}