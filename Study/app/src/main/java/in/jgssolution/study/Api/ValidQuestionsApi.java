package in.jgssolution.study.Api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ValidQuestionsApi {

    private Context context;

    public ValidQuestionsApi(Context context) {
        this.context = context;
    }

    // Callback interface to handle the result asynchronously
    public interface ValidQuestionCallback {
        void onResponse(String examStatus);
        void onError(String errorMessage);
    }

    public void validQuestion(String QUESTION_ID, String SELECTED_ANSWER, String ROLL_NO, String ASSESMENT_NO, String BATCH_NAME,
                              String QUESTION_TYPE_NO, String NO_OF_QUESTION,ValidQuestionCallback callback) {
        String url = RequestUrl.QUESTION_VALID + "?questionid=" + QUESTION_ID +
                "&answer=" + SELECTED_ANSWER +
                "&rollno=" + ROLL_NO +
                "&questiontypeno=" + QUESTION_TYPE_NO +
                "&noofquestion=" + NO_OF_QUESTION +
                "&assesmentno=" + ASSESMENT_NO +
                "&batchname=" + BATCH_NAME;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parse JSON response
                            JSONObject object = new JSONObject(response);

                            String examStatus = object.optString("examstatus");
                            // Pass the result to the callback
                            callback.onResponse(examStatus);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle error in parsing response
                            callback.onError("Error parsing JSON: " + e.getMessage());

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle network error
                        if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                            callback.onError("Server error");
                        } else {
                            callback.onError("Network error: " + error.getMessage());
                        }
                    }
                }
        );

        // Add request to queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
}
