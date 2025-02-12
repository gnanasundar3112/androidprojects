package in.jgssolution.study;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.jgssolution.study.Api.RequestUrl;
import in.jgssolution.study.Internet.NetworkChangeListener;

public class RegisterActivity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private TextInputEditText STUDENT_NAME, STUDENT_REGISTER, STUDENT_EMAIL;
    private Spinner STUDENT_PASS_YEAR;
    private ArrayAdapter<String> YEAR_OF_PASS_ADAPTER;
    private AppCompatButton REGISTER;
    private MaterialTextView LOGIN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        STUDENT_NAME = findViewById(R.id.stud_name);
        STUDENT_REGISTER = findViewById(R.id.stud_register);
        STUDENT_EMAIL = findViewById(R.id.stud_email);
        STUDENT_PASS_YEAR = findViewById(R.id.stud_pass_year);
        REGISTER = findViewById(R.id.register);
        LOGIN = findViewById(R.id.reg_login);

        YEAR_OF_PASS_ADAPTER = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.year_of_pass));
        YEAR_OF_PASS_ADAPTER.setDropDownViewResource(R.layout.item_drop_down);
        STUDENT_PASS_YEAR.setAdapter(YEAR_OF_PASS_ADAPTER);

        REGISTER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentName = STUDENT_NAME.getText().toString();
                String studentRegister = STUDENT_REGISTER.getText().toString();
                String userEmail = STUDENT_EMAIL.getText().toString();
                String userPassYear = STUDENT_PASS_YEAR.getSelectedItem().toString();

                if (studentName.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please enter User Name", Toast.LENGTH_SHORT).show();
                }else if(studentRegister.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please enter Register Number", Toast.LENGTH_SHORT).show();
                }else if(userEmail.isEmpty() || userEmail.endsWith("@gmail.com") == false){
                   Toast.makeText(RegisterActivity.this, "Please enter Correct Email", Toast.LENGTH_SHORT).show();
                }else if(userPassYear.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please enter Register Number", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(RegisterActivity.this, "Not Available", Toast.LENGTH_SHORT).show();
//                    registerStudent();
                }
            }
        });

        LOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public void registerStudent(){

        String studentName = STUDENT_NAME.getText().toString();
        String studentRegister = STUDENT_REGISTER.getText().toString();
        String userEmail = STUDENT_EMAIL.getText().toString();
        String userPassYear = STUDENT_PASS_YEAR.getSelectedItem().toString();

        StringRequest request = new StringRequest(Request.Method.POST, RequestUrl.REGISTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String rollno = object.getString("rollno");
                        String studentname = object.getString("studentname");
                        String email = object.getString("email");
                        String yearofpass = object.getString("yearofpass");
                        String score = object.getString("score");
                        String active = object.getString("active");
                        String examStatus = object.getString("examStatus");
                        Toast.makeText(RegisterActivity.this, rollno, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
//                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                    Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "ENetwork error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("studentid","");
                params.put("studentname", studentName);
                params.put("rollno", studentRegister);
                params.put("email", userEmail);
                params.put("yearofpass", userPassYear);
                params.put("active","Y");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
        requestQueue.add(request);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeListener);
    }
}