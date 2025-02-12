package in.jgssolution.study;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import in.jgssolution.study.Api.RequestUrl;
import in.jgssolution.study.Internet.NetworkChangeListener;
import in.jgssolution.study.Models.ExamQuestionModel;
import in.jgssolution.study.Api.GetQuestionsApi;
import in.jgssolution.study.Api.ValidQuestionsApi;
import in.jgssolution.study.Service.BitmapImages;
import in.jgssolution.study.Service.OptionHighlightColors;
import in.jgssolution.study.Service.TimerSetting;

public class QuestionActivity extends AppCompatActivity{
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private ImageView BACK_PRESS;
    private LinearLayout LINEAR,QUESTION_LINEAR;
    private TextView APPBAR_TITTLE, QUESTION_TIMER, QUESTION, OPTION_A, OPTION_B, OPTION_C, OPTION_D;
    private ImageView QUESTION_IMG;
    private LinearLayout IMAGE_LAYER;
    private AppCompatButton QUESTION_SUBMIT;
    private String ROLL_NO;
    private String ANSWER_OPTION,SELECTED_ANSWER,QUESTION_ID,BATCH_NAME,ASSESMENT_NO,QUESTION_TYPE_NO,NO_OF_QUESTION;
    private TimerSetting TIMER_SETTING;
    private GetQuestionsApi GET_QUESTIONS;
    private ValidQuestionsApi VALID_QUESTIONS;
    private OptionHighlightColors OPTION_HIGHLIGHT_COLORS;;
    private int QuestionCount = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // Retrieve the values from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String rollNo = sharedPreferences.getString("rollNo", "");
        ROLL_NO = rollNo;

        // Initialize views
        BACK_PRESS = findViewById(R.id.backPress);
        APPBAR_TITTLE = findViewById(R.id.appbarTitle);
        LINEAR = findViewById(R.id.linear);
        QUESTION_LINEAR = findViewById(R.id.question_linear);
        QUESTION_TIMER = findViewById(R.id.question_timer);
        QUESTION = findViewById(R.id.question);
        QUESTION_IMG = findViewById(R.id.question_img);
        IMAGE_LAYER = findViewById(R.id.image_layer);
        OPTION_A = findViewById(R.id.option_a);
        OPTION_B = findViewById(R.id.option_b);
        OPTION_C = findViewById(R.id.option_c);
        OPTION_D = findViewById(R.id.option_d);
        QUESTION_SUBMIT = findViewById(R.id.question_submit);

        APPBAR_TITTLE.setText("Exam Question");
        APPBAR_TITTLE.setPadding(50,0,0,0);
        BACK_PRESS.setVisibility(View.GONE);

        //GET TIMER START AND STOP
        TIMER_SETTING = new TimerSetting(QuestionActivity.this,QUESTION_TIMER);

        //GET QUESTION FROM URL
        GET_QUESTIONS = new GetQuestionsApi(QuestionActivity.this);
        getExamQuestion();

        //VALID THE QUESTION
        VALID_QUESTIONS = new ValidQuestionsApi(QuestionActivity.this);

        //HIGHLIGHT OPTIONS COLORS
        OPTION_HIGHLIGHT_COLORS = new OptionHighlightColors(QuestionActivity.this,OPTION_A,OPTION_B,OPTION_C,OPTION_D);

        // Common OnClickListener for all options
        View.OnClickListener optionClickListener = v -> {
            SELECTED_ANSWER = ((TextView) v).getText().toString().trim();
            OPTION_HIGHLIGHT_COLORS.SelectedOption(SELECTED_ANSWER);
        };

        // Set OnClickListeners for all options
        OPTION_A.setOnClickListener(optionClickListener);
        OPTION_B.setOnClickListener(optionClickListener);
        OPTION_C.setOnClickListener(optionClickListener);
        OPTION_D.setOnClickListener(optionClickListener);

        // Submit button click logic
        QUESTION_SUBMIT.setOnClickListener(v -> {
            if (SELECTED_ANSWER == null || SELECTED_ANSWER.isEmpty()) {
                Toast.makeText(QuestionActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                return;
            }else if (ANSWER_OPTION == null || ANSWER_OPTION.isEmpty()){
//                Toast.makeText(this, "Answer is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the selected answer is correct
            if (SELECTED_ANSWER.equals(ANSWER_OPTION)) {
                // Highlight the correct answer in green
                OPTION_HIGHLIGHT_COLORS.highlightCorrectAnswer(ANSWER_OPTION);
            } else {
                // Highlight the correct answer in green and selected answer in red
                OPTION_HIGHLIGHT_COLORS.highlightCorrectAnswer(ANSWER_OPTION);
                OPTION_HIGHLIGHT_COLORS.highlightIncorrectAnswer(SELECTED_ANSWER);
            }
            moveToNextQuestion();
        });
    }

    public void moveToNextQuestion() {
        // Disable the submit button during the transition
        QUESTION_SUBMIT.setEnabled(false);

        // Stop the current timer before starting a new one
        TIMER_SETTING.stopTimer();

        // Delay before moving to the next question
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {

                ValidQuestion();
                TIMER_SETTING.startTimer();

                // Enable the submit button again
                QUESTION_SUBMIT.setEnabled(true);

            }
        }, 1000);  // 1.5 second delay
    }


    public void getExamQuestion() {
        GET_QUESTIONS.getExamQuestion(ROLL_NO, new GetQuestionsApi.GetQuestionsCallback() {
            @Override
            public void onQuestionsReceived(List<ExamQuestionModel> examQuestions) {
                if (examQuestions == null || examQuestions.isEmpty()) {
                    Gone();
                } else {
                    Visible();
                    for (ExamQuestionModel examQuestionModel : examQuestions) {
                        String count = String.valueOf(QuestionCount);
                        QUESTION.setText(count + ". " + examQuestionModel.getQuestion());
                        OPTION_A.setText(examQuestionModel.getOptionalone());
                        OPTION_B.setText(examQuestionModel.getOptionaltwo());
                        OPTION_C.setText(examQuestionModel.getOptionalthree());
                        OPTION_D.setText(examQuestionModel.getOptionalfoure());
                        ANSWER_OPTION = examQuestionModel.getAnswer();
                        Bitmap bitmap = BitmapImages.decodeBase64(examQuestionModel.getImages());

                        if (bitmap != null) {
                            IMAGE_LAYER.setVisibility(View.VISIBLE);
                            QUESTION_IMG.setImageBitmap(bitmap);
                        } else {
                            IMAGE_LAYER.setVisibility(View.GONE);
                        }

                        QUESTION_ID = examQuestionModel.getQuestionid();
                        BATCH_NAME = examQuestionModel.getBatchname();
                        ASSESMENT_NO = examQuestionModel.getAssesmentno();
                        QUESTION_TYPE_NO = examQuestionModel.getQuestiontypeno();
                        NO_OF_QUESTION = examQuestionModel.getNoofquestion();

                        OPTION_HIGHLIGHT_COLORS.resetOptionsBackground();
                        QuestionCount++;
                    }
                }
            }

            @Override
            public void onError(String errorMessage) {
                if (errorMessage.equals("Server error")){
                    Gone();
                }else if (errorMessage.equals("Exam is Finished")){
                    Gone();
                    Toast.makeText(QuestionActivity.this, "Exam is Finished", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(QuestionActivity.this,LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void ValidQuestion() {
        TIMER_SETTING.stopTimer();

        // Pass the callback to handle the result asynchronously
        VALID_QUESTIONS.validQuestion(QUESTION_ID, SELECTED_ANSWER, ROLL_NO, ASSESMENT_NO, BATCH_NAME,QUESTION_TYPE_NO,NO_OF_QUESTION, new ValidQuestionsApi.ValidQuestionCallback() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    if (response.equals("Completed")) {
                        TIMER_SETTING.stopTimer();

                        Intent intent = new Intent(QuestionActivity.this, SuccessActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        SELECTED_ANSWER = null;
                        getExamQuestion();
                    }
                } else {
                    // Handle the case where response is null
                    Toast.makeText(QuestionActivity.this, "Error: Response is null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error message
                Toast.makeText(QuestionActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void Visible(){
        QUESTION_LINEAR.setVisibility(View.VISIBLE);
        QUESTION_SUBMIT.setVisibility(View.VISIBLE);
        LINEAR.setVisibility(View.GONE);
        QUESTION_TIMER.setTextColor(getResources().getColor(R.color.black));
    }
    public void Gone(){
        QUESTION_LINEAR.setVisibility(View.GONE);
        QUESTION_SUBMIT.setVisibility(View.GONE);
        LINEAR.setVisibility(View.VISIBLE);
        QUESTION_TIMER.setText("00:00s");
        QUESTION_TIMER.setTextColor(getResources().getColor(R.color.red));

        // STOP THE TIMER
        TIMER_SETTING.stopTimer();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        // Create an AlertDialog to confirm exit
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Confirm Exit");
        adb.setMessage("Are you sure you want to exit this exam?");
        adb.setCancelable(false);
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(QuestionActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = adb.create();
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);

        // START THE TIMER
        TIMER_SETTING.startTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeListener);
        TIMER_SETTING.stopTimer();
    }

}
