package com.bromi.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bromi.R;
import com.bromi.db.LanguageLevelData;
import com.bromi.util.constants;
import com.bromi.util.methods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PracticeResultScreenActivity extends AppCompatActivity {

    private int modeId = -1;
    private int languageId = -1;
    private int levelId = -1;
    private String levelDataString = "";

    private ArrayList<String> answersGiven;
    private ArrayList<String> correctAnswersGiven;
    private HashMap<String, String> levelData;
    private HashMap<String, String> profileData;
    private ArrayList<String> vocabularyOrder;

    private TextView level_indicator_text, selected_language, level_results_text, exp_gained_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_result_screen);

        Bundle extras = getIntent().getExtras();

        answersGiven = new ArrayList<>();
        correctAnswersGiven = new ArrayList<>();
        levelData = new HashMap<>();
        vocabularyOrder = new ArrayList<>();

        if (extras != null) {
            modeId = extras.getInt(constants.BUNDLE_MODE_ID);
            languageId = extras.getInt(constants.BUNDLE_LANGUAGE_ID);
            levelId = extras.getInt(constants.BUNDLE_LEVEL_ID);
            answersGiven = extras.getStringArrayList("answersGiven");
            levelDataString = extras.getString("levelData");
            correctAnswersGiven = extras.getStringArrayList("correctAnswersGiven");
            vocabularyOrder = extras.getStringArrayList("vocabularyUsed");
            profileData = methods.stringToHashMap(extras.getString(constants.BUNDLE_PROFILE));
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
        System.out.println(correctAnswersGiven.toString());
        System.out.println(levelId);
        System.out.println(levelData.toString());
         */

        int correctAmt = 0;

        if (levelId != 42 && levelId != -1) {

            if (!correctAnswersGiven.isEmpty() && !levelData.isEmpty() && !answersGiven.isEmpty() && !vocabularyOrder.isEmpty()) {

                for (int i = 0; i < correctAnswersGiven.size(); i++) {

                    if (correctAnswersGiven.get(i).equals(Boolean.TRUE.toString())) {
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

    private void saveProfileToJSON() throws IOException, JSONException {
        String jsonString = methods.loadJsonFromAssets(this.getApplicationContext());
        JSONArray json = new JSONArray(jsonString);
        JSONObject jsonObject = json.getJSONObject(0);

        /**
        int prevLevelsDone = (int) jsonObject.get(constants.STAT_LEVELS_DONE);
        int prevVocabulariesDone = (int) jsonObject.get(constants.STAT_VOCABULARIES_DONE);
        int prevCorrectVocabularies = (int) jsonObject.get(constants.STAT_CORRECT_VOCABULARIES);
        int prevWrongVocabularies = (int) jsonObject.get(constants.STAT_WRONG_VOCABULARIES);


        System.out.println(prevLevelsDone);
        System.out.println(prevVocabulariesDone);
        System.out.println(prevCorrectVocabularies);
        System.out.println(prevWrongVocabularies);
         */

        jsonObject.put(constants.STAT_LEVELS_DONE, (Integer.parseInt(profileData.get(constants.STAT_LEVELS_DONE))));
        jsonObject.put(constants.STAT_VOCABULARIES_DONE, (Integer.parseInt(profileData.get(constants.STAT_VOCABULARIES_DONE))));
        jsonObject.put(constants.STAT_CORRECT_VOCABULARIES, (Integer.parseInt(profileData.get(constants.STAT_CORRECT_VOCABULARIES))));
        jsonObject.put(constants.STAT_WRONG_VOCABULARIES, (Integer.parseInt(profileData.get(constants.STAT_WRONG_VOCABULARIES))));

        json.put(jsonObject);

        FileOutputStream fos = openFileOutput(constants.PROFILE_DATA_FILENAME, Context.MODE_PRIVATE);

        fos.write(json.toString().getBytes());
        fos.close();

    }

    /*****************
     * B U T T O N S *
     *****************/

    public void returnToLevelSelectScreen(View view) {
        try {
            saveProfileToJSON();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        Intent levelSelect = new Intent(this, PracticeLevelSelectActivity.class);
        levelSelect.putExtra(constants.BUNDLE_MODE_ID, modeId);
        levelSelect.putExtra(constants.BUNDLE_LANGUAGE_ID, languageId);
        levelSelect.putExtra(constants.BUNDLE_PROFILE, profileData.toString());
        startActivity(levelSelect);
    }

    public void redoLevel(View view) {
        Intent redoLvl = new Intent(this, PracticeLevelActivity.class);
        redoLvl.putExtra(constants.BUNDLE_LEVEL_ID, levelId);
        redoLvl.putExtra(constants.BUNDLE_LANGUAGE_ID, languageId);
        redoLvl.putExtra(constants.BUNDLE_MODE_ID, modeId);
        redoLvl.putExtra(constants.BUNDLE_IS_NEW_LEVEL, false);
        redoLvl.putExtra(constants.BUNDLE_PROFILE, profileData.toString());
        redoLvl.putExtra("vocabularyUsed", vocabularyOrder);
        redoLvl.putExtra("currentLevel", levelData.toString());
        startActivity(redoLvl);
    }

    public void nextLevel(View view) {
        int next = levelId + 1;
        final Intent nextLevel = new Intent(this, PracticeLevelActivity.class);

        if (next < LanguageLevelData.level_count) {
            nextLevel.putExtra(constants.BUNDLE_LEVEL_ID, next);
            nextLevel.putExtra(constants.BUNDLE_LANGUAGE_ID, languageId);
            nextLevel.putExtra(constants.BUNDLE_MODE_ID, modeId);
            nextLevel.putExtra(constants.BUNDLE_IS_NEW_LEVEL, true);
            nextLevel.putExtra(constants.BUNDLE_PROFILE, profileData.toString());
            startActivity(nextLevel);
        }
        else {
            new AlertDialog.Builder(this)
                    .setMessage("This was the last available level for this language! Do you wish to return to the first level?")
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            nextLevel.putExtra(constants.BUNDLE_LEVEL_ID, 0);
                            nextLevel.putExtra(constants.BUNDLE_LANGUAGE_ID, languageId);
                            nextLevel.putExtra(constants.BUNDLE_MODE_ID, modeId);
                            nextLevel.putExtra(constants.BUNDLE_IS_NEW_LEVEL, true);
                            nextLevel.putExtra(constants.BUNDLE_PROFILE, profileData.toString());
                            startActivity(nextLevel);
                        }

                    }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).setCancelable(false)
                        .create()
                        .show();
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
