package com.bromi.Activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bromi.R;
import com.bromi.util.methods;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class PracticeResultScreenActivity extends AppCompatActivity {

    private int modeId = -1;
    private int languageId = -1;
    private int levelId = -1;
    private String levelDataString = "";

    private ArrayList<String> answersGiven;
    private ArrayList<String> correctAnswerGiven;
    private HashMap<String, String> levelData;
    private ArrayList<String> vocabularyOrder;

    private TextView level_indicator_text, selected_language, level_results_text, exp_gained_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_result_screen);

        Bundle extras = getIntent().getExtras();

        answersGiven = new ArrayList<>();
        correctAnswerGiven = new ArrayList<>();
        levelData = new HashMap<>();
        vocabularyOrder = new ArrayList<>();

        if (extras != null) {
            modeId = extras.getInt("modeId");
            languageId = extras.getInt("languageId");
            levelId = extras.getInt("levelId");
            answersGiven = extras.getStringArrayList("answersGiven");
            levelDataString = extras.getString("levelData");
            correctAnswerGiven = extras.getStringArrayList("correctAnswersGiven");
            vocabularyOrder = extras.getStringArrayList("vocabularyUsed");
        }

        if (levelDataString != null && !levelDataString.equals("")) {
            levelData = methods.stringToHashMap(levelDataString);
        }

        level_indicator_text = (TextView) findViewById(R.id.level_indicator_text);
        selected_language = (TextView) findViewById(R.id.language_view);
        level_results_text = (TextView) findViewById(R.id.level_results_text);
        exp_gained_text = (TextView) findViewById(R.id.exp_gained_text);
        level_results_text = (TextView) findViewById(R.id.level_results_text);
        exp_gained_text = (TextView) findViewById(R.id.exp_gained_text);

        setLevelIndicatorText();
        setSelectedLanguage();
        showResults();
    }

    /**
     * Set level text on layout
     */
    private void setLevelIndicatorText() {
        level_indicator_text.setText(" Lv." + String.valueOf(levelId + 1));
    }

    /**
     * Set language text on layout
     */
    private void setSelectedLanguage() {
        String lang = methods.getLanguageFromId(languageId, this);

        if (lang != null) {
            selected_language.setText(lang);
        }
        else {
            selected_language.setText("");
        }
    }

    private void showResults() {
        LinearLayout linLay = (LinearLayout) findViewById(R.id.vocabulary_results_layout);

        /**
        System.out.println(correctAnswerGiven.toString());
        System.out.println(levelId);
        System.out.println(levelData.toString());
         */

        int correctAmt = 0;

        if (levelId != 42 && levelId != -1) {

            if (!correctAnswerGiven.isEmpty() && !levelData.isEmpty() && !answersGiven.isEmpty() && !vocabularyOrder.isEmpty()) {

                for (int i = 0; i < correctAnswerGiven.size(); i++) {

                    if (correctAnswerGiven.get(i).equals(Boolean.TRUE.toString())) {
                        correctAmt++;
                        createVocabularyTextView(i, linLay, "#2fb62f");
                    }
                    else {
                        createVocabularyTextView(i, linLay, "#ff0000");
                    }
                }
            }
            showGrade(correctAmt);
            startVocabularyResultAnimation(linLay, 0);
        }
    }

    private void createVocabularyTextView(int itemNum, LinearLayout linLay, String color) {
        TextView tv = new TextView(this);

        tv.setText(vocabularyOrder.get(itemNum) + ": " + answersGiven.get(itemNum));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        tv.setTextColor(Color.parseColor(color));
        tv.setVisibility(View.INVISIBLE);

        linLay.addView(tv);
    }

    private void showGrade(int correctAmt) {
        if (correctAmt > answersGiven.size()/2) {
            level_results_text.setText("You Passed!!");

            startLevelResultAnimationPositive();
        }
        else {
            level_results_text.setText("You Failed...");

            startLevelResultAnimationNegative();
        }
    }

    /*********************
     * A N I M A T I O N *
     *********************/

    private void startVocabularyResultAnimation(final LinearLayout linLay, final int toAnimate) {
        final Animation fade_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fast_fade_in);
        final TextView tv;

        if (toAnimate <= linLay.getChildCount()) {

            if (linLay.getChildAt(toAnimate) instanceof TextView) {
                tv = (TextView) linLay.getChildAt(toAnimate);
                linLay.getChildAt(toAnimate).setVisibility(View.VISIBLE);

                tv.startAnimation(fade_in);

                tv.postDelayed(new Runnable() {
                    public void run() {
                        startVocabularyResultAnimation(linLay, toAnimate + 1);
                    }
                }, 500);
            }
        }
    }

    private void startLevelResultAnimationNegative() {
        Animation fade_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slow_fade_in);
        final Animation fast_shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fast_short_shake);
        level_results_text.startAnimation(fade_in);
        fade_in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                level_results_text.startAnimation(fast_shake);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fast_shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                fast_shake.setStartOffset(10000);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void startLevelResultAnimationPositive() {
        Animation bouncy = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        final Animation slow_shake_start = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slow_shake_start_degree);
        final Animation slow_shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slow_shake);

        level_results_text.startAnimation(bouncy);

        bouncy.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                level_results_text.startAnimation(slow_shake_start);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        slow_shake_start.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                level_results_text.startAnimation(slow_shake);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
