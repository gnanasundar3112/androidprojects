package in.jgssolution.study.Service;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import in.jgssolution.study.QuestionActivity;

public class TimerSetting {
    private CountDownTimer countDownTimer;
    private Context context;
    private TextView QUESTION_TIMER;

    public TimerSetting(Context context, TextView QUESTION_TIMER) {
        this.context = context;
        this.QUESTION_TIMER = QUESTION_TIMER;
    }

    public void startTimer() {
        // Start a countdown of 60 seconds
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Format time to always show 2 digits for seconds
                int seconds = (int) (millisUntilFinished / 1000);
                QUESTION_TIMER.setText(String.format("00 : %02ds", seconds)); // Adding leading zero
            }

            @Override
            public void onFinish() {
                // Handle when time is up
                if (context instanceof Activity) {
                    View rootView = ((Activity) context).findViewById(android.R.id.content);
                    Snackbar.make(rootView, "Time is up!", Snackbar.LENGTH_SHORT).show();
                }
                // Optionally move to the next question after timer finishes
                if (context instanceof QuestionActivity) {
                    QuestionActivity questionActivity = (QuestionActivity) context;
                    questionActivity.moveToNextQuestion();
                }
            }
        }.start();
    }

    public void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
