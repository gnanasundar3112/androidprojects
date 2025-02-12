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
import com.vinsoftsolutions.Adapter.BankAdapter;
import com.vinsoftsolutions.Models.BankModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BankDatabase {
    private Context context;
    private List<BankModel> BANK =  new ArrayList<>();
    private BankAdapter BANK_ADAPTER;
    public BankDatabase(Context context){
        this.context = context;
    }

    public void fetch(RecyclerView BANK_RECYCLER, String date, TextView total){

//        String url = BaseUrl.BASE_URL + "/cash/"+date;
        String url = "http://192.168.250.112:8080/bank/"+date;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                BANK.clear();
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    double totalAmt = 0;

                    for (int i=0; i<jsonArray.length() ;i++){
                        JSONObject object = jsonArray.getJSONObject(i);

                        String type = object.getString("acnum");
                        String ac_name = object.getString("ac_name");
                        double amount = object.getDouble("amount");
                        totalAmt += amount;

                        BankModel bankModel = new BankModel(type,ac_name,amount);
                        BANK.add(bankModel);
                    }

                    total.setText(""+totalAmt);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                BANK_ADAPTER = new BankAdapter(context, BANK);
                BANK_RECYCLER.setAdapter(BANK_ADAPTER);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(context,"Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);

    }
}
