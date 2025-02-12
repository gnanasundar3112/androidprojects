package in.jgssolution.study.Api;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.jgssolution.study.Models.ExamQuestionModel;

public class GetQuestionsApi {
    private Context context;

    // Define a callback interface
    public interface GetQuestionsCallback {
        void onQuestionsReceived(List<ExamQuestionModel> examQuestions);
        void onError(String errorMessage);
    }

    public GetQuestionsApi(Context context) {
        this.context = context;
    }

    public void getExamQuestion(String ROLL_NO, final GetQuestionsCallback callback) {
        String url = RequestUrl.QUESTION_URL + "?rollno=" + ROLL_NO;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<ExamQuestionModel> examQuestionModels = new ArrayList<>();

                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            if ("OK".equals(status)) {
                                String message = object.optString("message");
                                callback.onError(message);
                                return;
                            }

                            // Parsing exam question data
                            String examQuestionId = object.optString("examquestionid");
                            String questionId = object.optString("questionid");
                            String question = object.optString("question");
                            String optionalOne = object.optString("optionalone");
                            String optionalTwo = object.optString("optionaltwo");
                            String optionalThree = object.optString("optionalthree");
                            String optionalFour = object.optString("optionalfoure");
                            String answer = object.optString("answer");
                            String assessmentNo = object.optString("assesmentno");
                            String imageName = object.optString("imagename");
                            String imageType = object.optString("imagetype");
                            String images = object.optString("images");
                            String batchNo = object.optString("batchno");
                            String batchName = object.optString("batchname");
                            String examStatus = object.optString("examstatus");
                            String answerStatus = object.optString("answerstatus");
                            String imageFiles = object.optString("imagefiles");

                            String questiontypeno = object.optString("questiontypeno");
                            String noofquestion = object.optString("noofquestion");

                            ExamQuestionModel examQuestionModel = new ExamQuestionModel(
                                    examQuestionId, questionId, question,
                                    optionalOne, optionalTwo, optionalThree, optionalFour, answer, assessmentNo, imageType,
                                    imageName, images, batchNo, batchName, examStatus, answerStatus, imageFiles,questiontypeno,
                                    noofquestion
                            );
                            examQuestionModels.add(examQuestionModel);

                            // Call the callback with the list of questions
                            callback.onQuestionsReceived(examQuestionModels);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError("Error parsing response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
                            callback.onError("Server error");
                        } else {
                            callback.onError("Network error: " + error.getMessage());
                        }
                    }
                }
        );

        // Add request to the Volley request queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
}
