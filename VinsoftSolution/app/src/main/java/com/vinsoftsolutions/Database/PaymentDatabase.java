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
import com.vinsoftsolutions.Adapter.PaymentAdapter;
import com.vinsoftsolutions.Models.PaymentModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PaymentDatabase {
    private Context context;
    private List<PaymentModel> PAYMENT = new ArrayList<>();
    private PaymentAdapter PAYMENT_ADAPTER;

    public PaymentDatabase(Context context) {
        this.context = context;
    }

    public void fetch(RecyclerView PAYMENT_RECYCLER, String from_date,String to_date, TextView total) {

//      String url = BaseUrl.BASE_URL + "/payment/" + from_date + "/" + to_date;
        String url = "http://192.168.250.112:8080/payment/" + from_date + "/" + to_date;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                PAYMENT.clear();
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    double totalAmt = 0;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String type = object.getString("type");
                        String ac_name = object.getString("ac_name");
                        double amount = object.getDouble("amount");
                        totalAmt += amount;

                        PaymentModel paymentModel = new PaymentModel(type,ac_name,amount);
                        PAYMENT.add(paymentModel);
                    }

                    total.setText(""+totalAmt);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                PAYMENT_ADAPTER = new PaymentAdapter(context, PAYMENT);
                PAYMENT_RECYCLER.setAdapter(PAYMENT_ADAPTER);
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
