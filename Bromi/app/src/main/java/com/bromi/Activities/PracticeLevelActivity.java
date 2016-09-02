package com.bromi.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.bromi.R;
import com.bromi.db.*;
import com.bromi.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class PracticeLevelActivity extends AppCompatActivity {

    private int modeId = -1;                 // Current mode selected
    private int languageId = -1;             // Current language selected
    private int levelId = -1;                // Current level selected
    private int vocabularyTotalCount;   // Total vocabularies (equal to level_size)
    private int vocabularyUsedCount;    // Incremented value that keeps track of how many vocabularies have been tested (Level progression counter)

    private ArrayList<String> vocabularyUsed;       // Holds the strings of the vocabularies that were used already
    private HashMap<String,String> currentLevel;    // Holds the level data
    private ArrayList<String> answersGiven;         // Holds the user answers (from buttons answer1, 2, 3, 4)
    private ArrayList<String> correctAnswersGiven;  // For each answer the user has given, either a true or false will be added to this List depending on if the answer is correct or not; this helps distinguishing true correct answers and wrong answers that are identical to another vocabulary of the same level but are actually not the answer

    private boolean isNewLevel = true;

    private TextView level_indicator_text, level_progress_indicator, selected_language, current_vocabulary;     // Textviews from level layout
    private Button answer1, answer2, answer3, answer4;                                                          // Answer buttons

    private LanguageLevelDbHelper langDb;   // Level DB Adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_level);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            modeId = extras.getInt("modeId");
            languageId = extras.getInt("languageId");
            levelId = extras.getInt("levelId");
            isNewLevel = extras.getBoolean("isNewLevel");
        }

        if (!isNewLevel && extras != null) {
            vocabularyUsed = extras.getStringArrayList("vocabularyUsed");
            currentLevel = methods.stringToHashMap(extras.getString("currentLevel"));
            vocabularyTotalCount = currentLevel.size();

        }
        else {
            vocabularyUsed = new ArrayList<>();
            langDb = new LanguageLevelDbHelper(getApplicationContext());

            loadLevelData();
        }

        // System.out.println(levelId);
        // System.out.println(languageId);
        // System.out.println(levelId);

        level_indicator_text = (TextView) findViewById(R.id.level_indicator_text);
        selected_language = (TextView) findViewById(R.id.language_view);
        level_progress_indicator = (TextView) findViewById(R.id.level_progress_indicator);
        current_vocabulary = (TextView) findViewById(R.id.current_vocabulary);

        answer1 = (Button) findViewById(R.id.answer_possibility_1);
        answer2 = (Button) findViewById(R.id.answer_possibility_2);
        answer3 = (Button) findViewById(R.id.answer_possibility_3);
        answer4 = (Button) findViewById(R.id.answer_possibility_4);

        vocabularyUsedCount = 0;
        answersGiven = new ArrayList<>();
        correctAnswersGiven = new ArrayList<>();

        setLevelIndicatorText();
        setSelectedLanguage();
        setLevelProgressIndicator();
        runLevel();
    }

    /**
     * Load level data from levelId from databank
     */
    private void loadLevelData() {
        if (levelId != 42 && levelId != -1) {
            currentLevel = langDb.readWordPairsAsMap(langDb.getLevel(levelId));
            vocabularyTotalCount = currentLevel.size();
        }

        /**
        System.out.println("Level read: " + levelId);
        System.out.println("Level data: " + currentLevel.toString());
        System.out.println("Level size: " + vocabularyTotalCount);
         */
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
     * Set level counter text on layout
     */
    private void setLevelProgressIndicator() {
        if (currentLevel != null) {
            level_progress_indicator.setText(String.valueOf(vocabularyUsedCount) + "/" + String.valueOf(currentLevel.size()));
        }
        else {
            level_progress_indicator.setText(String.valueOf(vocabularyUsedCount));
        }
    }

    /**
     * Set vocabulary text on layout
     * @param s
     */
    private void setVocabularyText(String s) {
        current_vocabulary.setText(s);
        adjustText(s, current_vocabulary);
    }

    /**
     * Run the level
     * - Increments vocabularyUsedCount
     * - Adds used vocabulary to vocabularyUsed
     * - Updates level screen and moves forward
     * - A level is finished if newVocabulary equals "" and vocabularyUsedCount equals vocabularyTotalCount
     */
    private void runLevel() {

        if (currentLevel != null) {

            if (isNewLevel) {

                String newVocabulary = getVocabulary();
                // System.out.println("Vocabulary generated: " + newVocabulary);

                if (!newVocabulary.equals("")) {

                    if (vocabularyUsedCount != vocabularyTotalCount) {
                        addUsedVocabulary(newVocabulary);

                        setVocabularyText(newVocabulary);
                        setAnswerText(generateAnswerButtonCaptionOrder(currentLevel.get(newVocabulary)));
                        setLevelProgressIndicator();

                        vocabularyUsedCount++;
                    }
                    else {
                        System.out.println("Level done");
                    }
                }
                else {
                    // System.out.println(vocabularyUsedCount + " " + vocabularyTotalCount);

                    if (vocabularyUsedCount == vocabularyTotalCount) {

                        setLevelProgressIndicator();

                        //System.out.println("Level done");

                        initResultScreen();
                    }
                    else {
                        runLevel();
                    }
                }
            }
            else {

                /**
                System.out.println(vocabularyUsedCount);
                System.out.println(vocabularyTotalCount);
                System.out.println(vocabularyUsed.toString());
                System.out.println(answersGiven.toString());
                System.out.println(correctAnswersGiven.toString());
                System.out.println(currentLevel.toString());
                 */

                if (vocabularyUsedCount != vocabularyTotalCount) {

                    String nextVocabulary = vocabularyUsed.get(vocabularyUsedCount);

                    setVocabularyText(nextVocabulary);
                    setAnswerText(generateAnswerButtonCaptionOrder(currentLevel.get(nextVocabulary)));
                    setLevelProgressIndicator();

                    vocabularyUsedCount++;
                }
                else {
                    setLevelProgressIndicator();
                    initResultScreen();
                }
            }
        }
    }

    /**
     * Generates a random word order which the answer buttons will be set up to
     * - all words are randomized together with the actual answer to the current vocabulary
     * @param newVoc
     * @return
     */
    private ArrayList<String> generateAnswerButtonCaptionOrder(String newVoc) {
        ArrayList<String> order = new ArrayList<>();
        Random rand = new Random();
        String toAdd;

        for (int i = 0; i < constants.ANSWER_POSSIBILITY_SIZE; i++) {
            toAdd = LanguageLevelData.words[rand.nextInt(LanguageLevelData.words.length)];

            for (int j = 0; j < order.size(); j++) {

                if (j >= 1) {

                    if (toAdd.equals(order.get(j - 1))) {       // Prevent doubles
                        order.remove(j - 1);
                    }
                }
            }
            if (!toAdd.equals(newVoc)) {        // Prevent string identical to current vocabulary
                order.add(toAdd);
            }
            else {
                i--;
            }
        }

        int newVocPosition = rand.nextInt(order.size());        // Add correct answer
        order.remove(newVocPosition);
        order.add(newVocPosition, newVoc);

        return order;
    }

    /**
     * Adds a string to the vocabularyUsed arraylist
     * @param s
     */
    private void addUsedVocabulary(String s) {
        vocabularyUsed.add(s);
    }

    /**
     * Generates a vocabulary from the data retrieved from the databank
     * - random order of vocabularies
     * - keeps track of which vocabularies were used already thanks to vocabularyUsed arraylist and is called recursively accordingly if this case occurs inside runLevel() implicitly
     * - returns "" as a sort of null-string if either all vocabularies were used already or some other problem occurs (problem is caught in runLevel() implicitly by simply calling runLevel() recursively)
     * - quasi-level-manager so to speak
     * @return
     */
    // http://stackoverflow.com/questions/929554/is-there-a-way-to-get-the-value-of-a-hashmap-randomly-in-java
    private String getVocabulary() {
        Random generator = new Random();
        HashMap<String, String> tmp = currentLevel;

        // System.out.println(vocabularyUsed.toString());
        // System.out.println(tmp.toString());

        if (vocabularyUsed.size() == vocabularyUsedCount - 1) {     // If only one word is left for use

            for (int i = 0; i < vocabularyUsed.size(); i++) {

                if (tmp.containsKey(vocabularyUsed.get(i))) {
                    tmp.remove(vocabularyUsed.get(i));
                }
            }
        }

        if (!vocabularyUsed.isEmpty()) {    // If level is going midway

            String randomVocabulary = (String) tmp.keySet().toArray()[generator.nextInt(tmp.size())];

            if (!vocabularyUsed.contains(randomVocabulary)) {
                return randomVocabulary;
            }
        }
        else {      // If first vocabulary
            return (String) tmp.keySet().toArray()[generator.nextInt(tmp.size())];
        }

        return "";      // If none of the above apply, which means that the level is done
    }

    /**
     * Set answerButton text to what has been generated by generateAnswerButtonCaptionOrder
     * @param order
     */
    private void setAnswerText(ArrayList<String> order) {
        String s;
        int i = 0;

        // System.out.println(order.toString());

        while (i != order.size()) {
            s = order.get(i);

            if (i == 0) {
                answer1.setText(s);
                adjustText(s, answer1);
            }
            else if (i == 1) {
                answer2.setText(s);
                adjustText(s, answer2);
            }
            else if (i == 2) {
                answer3.setText(s);
                adjustText(s, answer3);
            }
            else {
                answer4.setText(s);
                adjustText(s, answer4);
            }
            i++;
        }
        enableAllButtons();
    }

    /**
     * Quit button
     * @param view
     */
    public void returnToLevelSelectScreen(View view) {
        Intent levelSelectScreen = new Intent(this, PracticeLevelSelectActivity.class);
        levelSelectScreen.putExtra("modeId", modeId);
        levelSelectScreen.putExtra("languageId", languageId);
        startActivity(levelSelectScreen);
    }

    /**
     * Called when any of the 4 answer buttons were pressed
     * - all buttons are disabled immediately if this is called to prevent spam clicking a button and retrieving undesired data, which will lead to a crash
     * - buttons are reenabled in setAnswerText(), i.e. when everything that must be processed by the program is done
     * - adds answer to answersGiven
     * @param view
     */
    public void submitAnswer(View view) {
        final Button pressed = (Button) findViewById(view.getId());
        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fast_short_shake);

        disableAllButtons();

        pressed.startAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                answersGiven.add(pressed.getText().toString());
                correctAnswersGiven.add(compareAnswers(pressed.getText().toString(), currentLevel.get(current_vocabulary.getText().toString())));
                System.out.println(answersGiven.toString());
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                runLevel();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * Checks if the user answers equals the solution. Primitive Boolean is not used here because ArrayList does not support primitive types.
     * @param answer: Answer given by the user
     * @param solution: Current vocabulary
     * @return The boolean corresponding to whether the answer is right or wrong
     */
    private String compareAnswers(String answer, String solution) {
        String ret;
        if (answer.equals(solution)) {
            ret = Boolean.TRUE.toString();
        }
        else {
            ret = Boolean.FALSE.toString();
        }
        return ret;
    }

    /**
     * Enables all answer buttons on level screen
     */
    private void enableAllButtons() {
        answer1.setEnabled(true);
        answer2.setEnabled(true);
        answer3.setEnabled(true);
        answer4.setEnabled(true);
    }

    /**
     * Disables all answer buttons on level screen
     */
    private void disableAllButtons() {
        answer1.setEnabled(false);
        answer2.setEnabled(false);
        answer3.setEnabled(false);
        answer4.setEnabled(false);
    }

    /**
     * Attempt to automatically adjust text size of certain TextView objects depending on char length of s
     * @param s
     * @param tv
     */
    private void adjustText(String s, TextView tv) {
        int newSize;

        if (tv.getId() == current_vocabulary.getId()) {
            newSize = 60;   // Default

            if (s.length() > 13) {
                newSize = 45;
            }
            else if (s.length() > 18) {
                newSize = 30;
            }

            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, newSize);
        }
        else if (tv.getId() == answer1.getId() || tv.getId() == answer2.getId() || tv.getId() == answer3.getId() || tv.getId() == answer4.getId()) {
            newSize = 20;   // Default

            if (s.length() > 14) {
                newSize = 15;
            }
            else if (s.length() > 19) {
                newSize = 10;
            }

            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, newSize);
        }
    }

    private void initResultScreen() {
        final Intent results = new Intent(this, PracticeResultScreenActivity.class);
        results.putExtra("languageId", languageId);
        results.putExtra("modeId", modeId);
        results.putExtra("levelId", levelId);
        results.putExtra("answersGiven", answersGiven);
        results.putExtra("levelData", currentLevel.toString());
        results.putExtra("correctAnswersGiven", correctAnswersGiven);
        results.putExtra("vocabularyUsed", vocabularyUsed);

        new AlertDialog.Builder(this)
                .setMessage("Level Complete!")
                .setPositiveButton("Get Your Results ->", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(results);
                    }

                }).create().show();
    }
}
