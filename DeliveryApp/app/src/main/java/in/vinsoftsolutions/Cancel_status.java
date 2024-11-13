package in.vinsoftsolutions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.vinsoftsolutions.Adapter.CancelAdapter;
import in.vinsoftsolutions.Model.CancelModel;

public class Cancel_status extends AppCompatActivity {

    private TextView APPBAR_TITLE;
    private FloatingActionButton BACK_PRESS;

    // recyclerView id name declaration start
    private RecyclerView RECYCLER_CANCEL_STATUS;
    private RecyclerView.LayoutManager CANCEL_STATUS_MANAGER;
    private List<CancelModel> cancelModels;
    private CancelAdapter cancelAdapter;
    private ProgressBar PROGRESSBAR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_status);

        /* fetch  start*/
        PROGRESSBAR = findViewById(R.id.cancel_progress);
        RECYCLER_CANCEL_STATUS = findViewById(R.id.recycler_cancel_status);
        CANCEL_STATUS_MANAGER = new GridLayoutManager(Cancel_status.this,1);
        RECYCLER_CANCEL_STATUS.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
        cancelModels = new ArrayList<>();
        /* fetch  end*/

        BACK_PRESS = findViewById(R.id.fab_backPress);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);

        APPBAR_TITLE.setText("Delivery Status");
        //back press activity
        BACK_PRESS.setOnClickListener(view -> Cancel_status.super.onBackPressed());
    }
    private void cancel_status() {

        PROGRESSBAR.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-delivery-app/cancel_status.php",
                new Response.Listener<String>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(String response) {
                PROGRESSBAR.setVisibility(View.GONE);

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String Order_No = jsonObject.getString("order_no");
                        String Cancel_Date = jsonObject.getString("modi_date");
                        String Cancel_Status = jsonObject.getString("status");

                        CancelModel cancelModel = new CancelModel(Order_No,Cancel_Date,Cancel_Status);
                        cancelModels.add(cancelModel);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                cancelAdapter = new CancelAdapter(Cancel_status.this, cancelModels);
                RECYCLER_CANCEL_STATUS.setAdapter(cancelAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PROGRESSBAR.setVisibility(View.GONE);
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Cancel_status.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(Cancel_status.this);
        requestQueue.add(request);
    }

    @Override
    protected void onStart() {
        super.onStart();
        cancel_status();
    }
}