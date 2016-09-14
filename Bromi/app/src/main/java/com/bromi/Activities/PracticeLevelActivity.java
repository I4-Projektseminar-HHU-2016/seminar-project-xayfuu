package com.bromi.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
     * Total amount of vocabularies used in level (equals a level size counter)
     */
    private int vocabularyTotalCount;

    /**
     * Integer that keeps track of how many vocabularies have been used so far in the level (equals a level progression counter)
     * - this value is incremented by 1 everytime a vocabulary has been successfully tested within the runLevel() method
     */
    private int vocabularyUsedCount;

    /**
     * Holds the strings of all vocabularies that were used already. This is used for the randomizing method getVocabulary(), so that no
     * duplicates are used.
     */
    private ArrayList<String> vocabularyUsed;

    /**
     * Holds the level data gotten from the level database.
     * - pair structure:
     * -
     * -            foreign_word : correct_answer
     * -                key      :    value
     */
    private HashMap<String,String> currentLevel;

    /**
     * Holds the profile data
     */
    private HashMap<String,String> profileData;

    /**
     * Holds the answers given by the users.
     * - Literally holds the words retrieved from the answer buttons the user has pressed
     */
    private ArrayList<String> answersGiven;

    /**
     * For each answer the user has given, compareAnswers() will add a boolean string "true" or "false" to this AL depending on wether the answer is correct or not.
     * - This is to help distinguish true correct answers and wrong answers
     *
     * TODO: Merge answersGiven and correctAnswersGiven into a Map, where the key is the answer given as a word and the value the boolean value that says whether the key is the answer or not
     */
    private ArrayList<String> correctAnswersGiven;

    /**
     * This boolean describes two game conditions that need to be distinguished between in this entire class.
     * - If isNewLevel is true, then that means a level has been initiated without any previously given data.
     * - If isNewLevel is false, then that means the level has been restarted from the resultScreenActivity (via the redoLevel() method) and must retrieve the data used in the level before
     */
    private boolean isNewLevel = true;

    /**
     * All TextView objects used in this activity
     */
    private TextView level_indicator_text, level_progress_indicator, selected_language, current_vocabulary;

    /**
     * The answer buttons used in this activity
     */
    private Button answer1, answer2, answer3, answer4;

    /**
     * DB object to enable interaction with the App's SQLite DB
     */
    private LanguageLevelDbHelper langDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_level);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            modeId = extras.getInt(constants.BUNDLE_MODE_ID);
            languageId = extras.getInt(constants.BUNDLE_LANGUAGE_ID);
            levelId = extras.getInt(constants.BUNDLE_LEVEL_ID);
            isNewLevel = extras.getBoolean(constants.BUNDLE_IS_NEW_LEVEL);
            profileData = methods.stringToHashMap(extras.getString(constants.BUNDLE_PROFILE));
        }

        // If level has been restarted, retrieve used data from resultScreenActivity.java
        if (!isNewLevel && extras != null) {
            vocabularyUsed = extras.getStringArrayList("vocabularyUsed");
            currentLevel = methods.stringToHashMap(extras.getString("currentLevel"));
            vocabularyTotalCount = currentLevel.size();

        }

        // If level is completely new, instantiate normally
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
     * Load level data for the levelId from the DB
     */
    private void loadLevelData() {
        if (levelId != 42 && levelId != -1) {
            currentLevel = langDb.readWordPairsAsMap(langDb.getLevel(levelId));
            vocabularyTotalCount = currentLevel.size();
        }

        /**     Tests
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

                // If level is started without any previously given data, generate vocabulary
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

                    // If level is restarted, simply get the vocabulary from the vocabularyUsed AL
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
            }
            else if (i == 1) {
                answer2.setText(s);
            }
            else if (i == 2) {
                answer3.setText(s);
            }
            else {
                answer4.setText(s);
            }
            i++;
        }
        enableAllButtons();
    }

    /**
     * Quit button, returns to level select
     * @param view
     */
    public void returnToLevelSelectScreen(View view) {
        Intent levelSelectScreen = new Intent(this, PracticeLevelSelectActivity.class);
        levelSelectScreen.putExtra(constants.BUNDLE_MODE_ID, modeId);
        levelSelectScreen.putExtra(constants.BUNDLE_LANGUAGE_ID, languageId);
        levelSelectScreen.putExtra(constants.BUNDLE_PROFILE, profileData.toString());

        setProfileStats();

        startActivity(levelSelectScreen);
    }

    /**
     * Called when any of the 4 answer buttons were pressed
     * - all buttons are disabled immediately if this is called to prevent spam clicking a button and retrieving undesired data, which will lead to a crash
     * - buttons are reenabled in setAnswerText(), i.e. when everything that must be processed by the program is done
     * - adds answer to answersGiven
     * - invokes compareAnswers()
     * - runs next vocabulary by calling runLevel()
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
     * Saves profile statistics gained by playing the level (no matter up to what point) to profile hashmap
     */
    private void setProfileStats() {
        String[] keys = {constants.STAT_LEVELS_DONE, constants.STAT_VOCABULARIES_DONE, constants.STAT_CORRECT_VOCABULARIES, constants.STAT_WRONG_VOCABULARIES};
        String[] newValues = {
                String.valueOf(((Integer.parseInt(profileData.get(constants.STAT_LEVELS_DONE))) + 1)),
                String.valueOf(((Integer.parseInt(profileData.get(constants.STAT_VOCABULARIES_DONE))) + correctAnswersGiven.size())),
                String.valueOf(((Integer.parseInt(profileData.get(constants.STAT_CORRECT_VOCABULARIES))) + countTrue())),
                String.valueOf(((Integer.parseInt(profileData.get(constants.STAT_WRONG_VOCABULARIES))) + countFalse()))

        };
        System.out.println(profileData.toString());
        profileData = methods.editProfileStats(keys, newValues, profileData);
        System.out.println(profileData.toString());
    }

    /**
     * Counts correctly answered vocabulary
     * @return amount gathered from correctAnsersGiven
     */
    private int countTrue() {
        int ret = 0;

        for (int i = 0; i < correctAnswersGiven.size(); i++) {

            if (correctAnswersGiven.get(i).equals(Boolean.TRUE.toString())) {
                ret++;
            }
        }
        return ret;
    }

    /**
     * Counts wrongly answered vocabulary
     * @return amount gathered from correctAnsersGiven
     */
    private int countFalse() {
        int ret = 0;

        for (int i = 0; i < correctAnswersGiven.size(); i++) {

            if (correctAnswersGiven.get(i).equals(Boolean.FALSE.toString())) {
                ret++;
            }
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
     * Called when player has finished level. Puts every bit of data gained throughout the play of this level into the Bundle and starts the results screen activity
     */
    private void initResultScreen() {
        setProfileStats();
        final Intent results = new Intent(this, PracticeResultScreenActivity.class);
        results.putExtra(constants.BUNDLE_LANGUAGE_ID, languageId);
        results.putExtra(constants.BUNDLE_MODE_ID, modeId);
        results.putExtra(constants.BUNDLE_LEVEL_ID, levelId);
        results.putExtra(constants.BUNDLE_PROFILE, profileData.toString());
        results.putExtra("answersGiven", answersGiven);
        results.putExtra("levelData", currentLevel.toString());
        results.putExtra("correctAnswersGiven", correctAnswersGiven);
        results.putExtra("vocabularyUsed", vocabularyUsed);

        /**
         * Pop up to stop the level and force the player to go to the level screen
         */
        new AlertDialog.Builder(this)
                .setMessage("Level Complete!")
                .setPositiveButton("Get Your Results ->", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(results);
                    }

                }).setCancelable(false)
                    .create()
                    .show();
    }
}
