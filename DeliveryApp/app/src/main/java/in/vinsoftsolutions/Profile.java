package in.vinsoftsolutions;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    private FloatingActionButton BACK_PRESS;
    TextView APPBAR_TITLE,MUSER_ID,MUSER_NAME,MUSER_PHONE;
    private CircleImageView MUSER_IMG;
    ImageView USER_IMAGE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BACK_PRESS = findViewById(R.id.fab_backPress);
        APPBAR_TITLE = findViewById(R.id.txt_appbarTitle);

        MUSER_ID = findViewById(R.id.profile_Id);
        MUSER_NAME = findViewById(R.id.profile_name);
        MUSER_PHONE = findViewById(R.id.profile_mobile);
        MUSER_IMG = findViewById(R.id.profile_img);

        APPBAR_TITLE.setText("Profile");
        //back press activity
        BACK_PRESS.setOnClickListener(view -> Profile.super.onBackPressed());

        MUSER_IMG.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Profile.this, R.style.AlertDialogTheme);
                View view1 = LayoutInflater.from(Profile.this).inflate(R.layout.image_view_dialog,
                        (LinearLayout) findViewById(R.id.view_img_dialog));

                builder.setView(view1);

                final android.app.AlertDialog alertDialog = builder.create();
                USER_IMAGE = view1.findViewById(R.id.dialog_pro_img);

                Drawable USER_IMG = MUSER_IMG.getDrawable();
                Glide.with(Profile.this).load(USER_IMG).into(USER_IMAGE);

                view1.findViewById(R.id.dialog_pro_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }
        });
        fetchProfile();
        localVariyable();
    }

    private void fetchProfile() {
        SharedPreferences preferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String USER_ID = preferences.getString("USER_ID", "");

        StringRequest request = new StringRequest(Request.Method.POST, "https://vinsoftsolutions.in/jaya-delivery-app/deli_user_fetch.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String USER_ID = jsonObject.getString("user_id");
                                String USER_NAME = jsonObject.getString("user_name");
                                String USER_MOBILE = jsonObject.getString("user_phone");
                                String USER_IMG = jsonObject.getString("user_img");

                                MUSER_ID.setText(USER_ID);
                                MUSER_NAME.setText(USER_NAME);
                                MUSER_PHONE.setText(USER_MOBILE);
                                Glide.with(Profile.this).load(USER_IMG).into(MUSER_IMG);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(Profile.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", USER_ID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void localVariyable(){
        String USER_ID = MUSER_ID.getText().toString();
        String USER_NAME = MUSER_NAME.getText().toString();

        // this is user name store var local variyable
        SharedPreferences sharedPreferences = getSharedPreferences("delivery_user_id_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_id",USER_ID);
        editor.putString("user_name",USER_NAME);
        editor.apply();
    }
}