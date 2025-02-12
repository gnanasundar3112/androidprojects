package in.jgssolution.study.Api;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.jgssolution.study.CallbackMessage.ResponseCallback;
import in.jgssolution.study.LoginActivity;

public class LoginApi {

    private final Context context;

    public LoginApi(Context context) {
        this.context = context;
    }

    public void validUserNameAndPassword(String studName, String studPass, ResponseCallback callback) {
        // Create JSON request body
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", studName);
            requestBody.put("password", studPass);
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onFailure("Invalid request data");
            return;
        }

        // Create JSON request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RequestUrl.LOGIN_URL, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Extract response data
                            String id = response.getString("id");
                            String username = response.getString("username");
                            String email = response.getString("email");
                            String roll_no = response.getString("studentrollno");

                            saveToSharedPreferences("rollNo", roll_no);

                            callback.onSuccess("Login successfully!");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure("Error parsing login response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                // Parse error response
                                String errorResponse = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                                JSONObject errorJson = new JSONObject(errorResponse);
                                String errorMessage = errorJson.optString("message", "Unknown error");

                                callback.onFailure(errorMessage);
                            } else {
                                callback.onFailure("Network error: " + error.getMessage());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.onFailure("Error handling server response");
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + RequestUrl.AUTH_TOKEN);
                return headers;
            }
        };
        // Add request to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    private void saveToSharedPreferences(String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
