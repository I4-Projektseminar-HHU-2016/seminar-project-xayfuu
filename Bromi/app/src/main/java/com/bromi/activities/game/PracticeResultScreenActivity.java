package com.bromi.activities.game;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.bromi.activities.menus.PracticeLevelSelectActivity;
import com.bromi.R;
import com.bromi.audio.BackgroundMusic;
import com.bromi.db.LanguageData;
import com.bromi.lib.LevelManager;
import com.bromi.util.constants;
import com.bromi.util.methods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PracticeResultScreenActivity extends AppCompatActivity {

    /**
     * Current mode selected
     * - set to -1 incase something goes wrong or the ID gets lost
     */
    private int modeId = -1;

    /**
     * Current language selected
     * - set to -1 incase something goes wrong or the ID gets lost
     */
    private int languageId = -1;

    /**
     * Current level selected
     * - set to -1 incase something goes wrong or the ID gets lost
     */
    private int levelId = -1;

    /**
     * This String holds the level HashMap of the previous PracticeLevelActivity class retrieved from the db as a string.
     * - Will be later converted to levelData
     * - exists to prevent null error when calling methods.stringToHashMap()
     */
    private String levelDataString = "";

    /**
     * Experience Gained amount. Will be initiated with the base amount of experience a user gets for simply completing the level.
     */
    private int experienceGained = constants.EXP_FOR_COMPLETING_LEVEL;

    /**
     * answersGiven ArrayList retrieved from PracticeLevelActivity.java
     */
    private ArrayList<String> answersGiven;

    /**
     * correctAnswersGiven ArrayList retrieved from PracticeLevelActivity.java
     */
    private ArrayList<String> correctAnswersGiven;

    /**
     * levelData HashMap retrieved from PracticeLevelActivity.java
     */
    private HashMap<String, String> levelData;

    /**
     * Profile Data Map
     */
    private HashMap<String, String> profileData;

    /**
     * vocabularyUsed ArrayList retrieved from PracticeLevelActivity.java
     */
    private ArrayList<String> vocabularyOrder;

    private TextView level_indicator_text, selected_language, level_results_text, exp_gained_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_result_screen);

        BackgroundMusic.start(this, R.raw.walterwarm_summer_love, false);

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
        computeAndShowResults();
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

    /**
     * This method counts the amount of true's within the correctAnswersGiven ArrayList.
     */
    private void computeAndShowResults() {
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
                        createVocabularyTextView(i, linLay, "#2fb62f");     // Generate TextView with green text to imply "this vocabulary is correct"
                    }
                    else {
                        createVocabularyTextView(i, linLay, "#ff0000");     // Generate TextView with red text ti imply "this vocabulary is incorrect"
                    }
                }
            }
            showGrade(correctAmt);
            computeExperience(correctAmt);
            startVocabularyResultAnimation(linLay, 0);
        }
    }

    /**
     * This method procedurally adds a new TextView to linLay.
     * @param itemNum
     * @param linLay
     * @param color
     */
    private void createVocabularyTextView(int itemNum, LinearLayout linLay, String color) {
        TextView tv = new TextView(this);

        tv.setText(vocabularyOrder.get(itemNum) + ": " + answersGiven.get(itemNum));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        tv.setTextColor(Color.parseColor(color));
        tv.setVisibility(View.INVISIBLE);

        linLay.addView(tv);
    }

    /**
     * Sets result text according to the user's performance.
     * - 50% correct answers counts as pass.
     * @param correctAmt
     */
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

    /**
     * This method computes the EXP a user gains upon completing a level.
     * @param correctAmt
     */
    private void computeExperience(int correctAmt) {

        // Gain 10 EXP for each correct vocabulary
        experienceGained = experienceGained + (correctAmt * constants.EXP_FOR_CORRECT_ANSWER);

        if (correctAmt == correctAnswersGiven.size()) {

            // Gain 50 EXP if every vocabulary is correct
            experienceGained = experienceGained + constants.EXP_FOR_ALL_CORRECT;
        }

        exp_gained_text.setText(exp_gained_text.getText().toString().replace("0", String.valueOf(experienceGained)));

        // Check if a level up occurred.
        if(exceedsLevelRequirement()) {

            // If level up occured, substract the exp needed for a level up from the user's current exp
            int xpRemaining = (experienceGained + Integer.parseInt(profileData.get(constants.STAT_USER_EXPERIENCE))) - constants.EXP_REQUIRED_FOR_ONE_LEVEL;

            // Write remaining exp to user profile map
            methods.editProfileStats(constants.STAT_USER_EXPERIENCE, String.valueOf(xpRemaining), profileData);

            // Increment user level
            methods.editProfileStats(constants.STAT_USER_LEVEL, String.valueOf((Integer.parseInt(profileData.get(constants.STAT_USER_LEVEL))) + 1), profileData);

            // Show a little dialog when level up occurred
            new AlertDialog.Builder(this)
                    .setMessage("Congratulations, you leveled up!")
                    .setPositiveButton(">> Press to continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).setCancelable(false)
                        .create()
                        .show();
        }

        // If no level up occurred, simply add the experience gained to the user's current amount of exp
        else {
            methods.editProfileStats(constants.STAT_USER_EXPERIENCE, String.valueOf((Integer.parseInt(profileData.get(constants.STAT_USER_EXPERIENCE))) + experienceGained), profileData);
        }
    }

    /**
     * This method checks if the level up threshold has been passed.
     * @return
     */
    private boolean exceedsLevelRequirement() {
        return (Integer.parseInt(profileData.get(constants.STAT_USER_EXPERIENCE)) + experienceGained) > constants.EXP_REQUIRED_FOR_ONE_LEVEL;
    }

    /**
     * Saves data stored within the HashMap to the JSON-Profile stored within the phone's internal device so the user's progress doesn't get lost
     * - TODO: Have more locations where the profile saves instead of just after a level has been completed.
     * @throws IOException
     * @throws JSONException
     */
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
        jsonObject.put(constants.STAT_USER_EXPERIENCE, (Integer.parseInt(profileData.get(constants.STAT_USER_EXPERIENCE))));
        jsonObject.put(constants.STAT_USER_LEVEL, (Integer.parseInt(profileData.get(constants.STAT_USER_LEVEL))));

        json.put(jsonObject);

        FileOutputStream fos = openFileOutput(constants.PROFILE_DATA_FILENAME, Context.MODE_PRIVATE);

        fos.write(json.toString().getBytes());
        fos.close();

        methods.showToast("Profile saved.", this);

    }

    /*****************
     * B U T T O N S *
     *****************/

    /**
     * Return to level select screen
     * @param view
     */
    public void returnToLevelSelectScreen(View view) {
        try {
            saveProfileToJSON();
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        Intent levelSelect = new Intent(this, PracticeLevelSelectActivity.class);
        levelSelect.putExtra(constants.BUNDLE_MODE_ID, modeId);
        levelSelect.putExtra(constants.BUNDLE_LANGUAGE_ID, languageId);
        levelSelect.putExtra(constants.BUNDLE_PROFILE, profileData.toString());
        startActivity(levelSelect);
    }

    /**
     * Redo level
     * @param view
     */
    public void redoLevel(View view) {
        try {
            saveProfileToJSON();
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }

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

    /**
     * Go to next level
     * @param view
     */
    public void nextLevel(View view) {
        try {
            saveProfileToJSON();
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        int next = levelId + 1;
        final Intent nextLevel = new Intent(this, PracticeLevelActivity.class);

        if (next < constants.TRANSLATION_LEVEL_COUNT) {
            nextLevel.putExtra(constants.BUNDLE_LEVEL_ID, next);
            nextLevel.putExtra(constants.BUNDLE_LANGUAGE_ID, languageId);
            nextLevel.putExtra(constants.BUNDLE_MODE_ID, modeId);
            nextLevel.putExtra(constants.BUNDLE_IS_NEW_LEVEL, true);
            nextLevel.putExtra(constants.BUNDLE_PROFILE, profileData.toString());
            startActivity(nextLevel);
        }
        else {

            // If all levels have been completed and the user is at the end, show a pop up that informs the user
            new AlertDialog.Builder(this)
                    .setMessage("This was the last available level for this language! Do you wish to return to the first level?")
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            nextLevel.putExtra(constants.BUNDLE_LEVEL_ID, 0);   // Start first level again if user presses yes
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

    @Override
    public void onResume() {
        super.onResume();
        BackgroundMusic.start(this, R.raw.walterwarm_summer_love, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        BackgroundMusic.pause();
    }
}
