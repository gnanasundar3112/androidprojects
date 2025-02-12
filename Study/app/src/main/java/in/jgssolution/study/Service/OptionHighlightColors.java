package in.jgssolution.study.Service;

import android.content.Context;
import android.widget.TextView;

import in.jgssolution.study.R;

public class OptionHighlightColors {

    private Context context;
    private TextView OPTION_A, OPTION_B, OPTION_C, OPTION_D;

    public OptionHighlightColors(Context context) {
        this.context = context;
    }

    public OptionHighlightColors(Context context, TextView OPTION_A, TextView OPTION_B, TextView OPTION_C, TextView OPTION_D){
        this.OPTION_A = OPTION_A;
        this.OPTION_B = OPTION_B;
        this.OPTION_C = OPTION_C;
        this.OPTION_D = OPTION_D;
        this.context = context;
    }

    public void SelectedOption(String SELECTED_ANSWER) {
        resetOptionsBackground();
        if (SELECTED_ANSWER.equals(OPTION_A.getText().toString().trim())) {
            OPTION_A.setBackgroundColor(context.getResources().getColor(R.color.blue_500));
            OPTION_A.setTextColor(context.getResources().getColor(R.color.white));
        } else if (SELECTED_ANSWER.equals(OPTION_B.getText().toString().trim())) {
            OPTION_B.setBackgroundColor(context.getResources().getColor(R.color.blue_500));
            OPTION_B.setTextColor(context.getResources().getColor(R.color.white));
        } else if (SELECTED_ANSWER.equals(OPTION_C.getText().toString().trim())) {
            OPTION_C.setBackgroundColor(context.getResources().getColor(R.color.blue_500));
            OPTION_C.setTextColor(context.getResources().getColor(R.color.white));
        } else if (SELECTED_ANSWER.equals(OPTION_D.getText().toString().trim())) {
            OPTION_D.setBackgroundColor(context.getResources().getColor(R.color.blue_500));
            OPTION_D.setTextColor(context.getResources().getColor(R.color.white));
        }
    }

    public void highlightCorrectAnswer(String ANSWER_OPTION) {
        if (ANSWER_OPTION.equals(OPTION_A.getText().toString().trim())) {
            OPTION_A.setBackgroundColor(context.getResources().getColor(R.color.green));
            OPTION_A.setTextColor(context.getResources().getColor(R.color.white));
        } else if (ANSWER_OPTION.equals(OPTION_B.getText().toString().trim())) {
            OPTION_B.setBackgroundColor(context.getResources().getColor(R.color.green));
            OPTION_B.setTextColor(context.getResources().getColor(R.color.white));
        } else if (ANSWER_OPTION.equals(OPTION_C.getText().toString().trim())) {
            OPTION_C.setBackgroundColor(context.getResources().getColor(R.color.green));
            OPTION_C.setTextColor(context.getResources().getColor(R.color.white));
        } else if (ANSWER_OPTION.equals(OPTION_D.getText().toString().trim())) {
            OPTION_D.setBackgroundColor(context.getResources().getColor(R.color.green));
            OPTION_D.setTextColor(context.getResources().getColor(R.color.white));
        }
    }
    // Function to highlight the incorrect answer
    public void highlightIncorrectAnswer(String SELECTED_ANSWER) {
        if (SELECTED_ANSWER.equals(OPTION_A.getText().toString().trim())) {
            OPTION_A.setBackgroundColor(context.getResources().getColor(R.color.red));
            OPTION_A.setTextColor(context.getResources().getColor(R.color.white));
        } else if (SELECTED_ANSWER.equals(OPTION_B.getText().toString().trim())) {
            OPTION_B.setBackgroundColor(context.getResources().getColor(R.color.red));
            OPTION_B.setTextColor(context.getResources().getColor(R.color.white));
        } else if (SELECTED_ANSWER.equals(OPTION_C.getText().toString().trim())) {
            OPTION_C.setBackgroundColor(context.getResources().getColor(R.color.red));
            OPTION_C.setTextColor(context.getResources().getColor(R.color.white));
        } else if (SELECTED_ANSWER.equals(OPTION_D.getText().toString().trim())) {
            OPTION_D.setBackgroundColor(context.getResources().getColor(R.color.red));
            OPTION_D.setTextColor(context.getResources().getColor(R.color.white));
        }
    }
    public void resetOptionsTextColor() {
        OPTION_A.setTextColor(context.getResources().getColor(R.color.black));
        OPTION_B.setTextColor(context.getResources().getColor(R.color.black));
        OPTION_C.setTextColor(context.getResources().getColor(R.color.black));
        OPTION_D.setTextColor(context.getResources().getColor(R.color.black));
    }
    public void resetOptionsBackground() {
        OPTION_A.setBackground(context.getResources().getDrawable(R.drawable.border));
        OPTION_B.setBackground(context.getResources().getDrawable(R.drawable.border));
        OPTION_C.setBackground(context.getResources().getDrawable(R.drawable.border));
        OPTION_D.setBackground(context.getResources().getDrawable(R.drawable.border));
        resetOptionsTextColor();
    }
}
