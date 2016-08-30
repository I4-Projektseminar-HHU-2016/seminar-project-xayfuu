package com.bromi.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bromi.R;
import com.bromi.db.*;
import com.bromi.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class PracticeLevelActivity extends AppCompatActivity {

    private int modeId;
    private int languageId;
    private int levelId;
    private int vocabularyCount;
    private int vocabularyAnsweredCount;

    private ArrayList<String> vocabularyUsed;
    private HashMap<String,String> currentLevel;

    private TextView level_indicator_text, level_progress_indicator, selected_language, current_vocabulary;
    private Button answer1, answer2, answer3, answer4;

    private LanguageLevelDbHelper langDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_level);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            modeId = extras.getInt("modeId");
            languageId = extras.getInt("languageId");
            levelId = extras.getInt("levelId");
        }

        langDb = new LanguageLevelDbHelper(getApplicationContext());

        level_indicator_text = (TextView) findViewById(R.id.level_indicator_text);
        selected_language = (TextView) findViewById(R.id.language_view);
        level_progress_indicator = (TextView) findViewById(R.id.level_progress_indicator);
        current_vocabulary = (TextView) findViewById(R.id.current_vocabulary);

        answer1 = (Button) findViewById(R.id.answer_possibility_1);
        answer2 = (Button) findViewById(R.id.answer_possibility_2);
        answer3 = (Button) findViewById(R.id.answer_possibility_3);
        answer4 = (Button) findViewById(R.id.answer_possibility_4);

        vocabularyAnsweredCount = 0;
        vocabularyUsed = new ArrayList<>();

        loadLevelData();
        setLevelIndicatorText();
        setSelectedLanguage();
        setLevelProgressIndicator();
        runLevel();
    }

    private void loadLevelData() {
        currentLevel = langDb.readWordPairsAsMap(langDb.getLevel(levelId));
        vocabularyCount = currentLevel.size();

        System.out.println("Level read: " + levelId);
        System.out.println("Level data: " + currentLevel.toString());
        System.out.println("Level size: " + vocabularyCount);
    }

    private void setLevelIndicatorText() {
        level_indicator_text.setText(" Lv." + String.valueOf(levelId + 1));
    }

    private void setSelectedLanguage() {
        String lang = methods.getLanguageFromId(languageId, this);

        if (lang != null) {
            selected_language.setText(lang);
        }
        else {
            selected_language.setText("");
        }
    }

    private void setLevelProgressIndicator() {
        if (currentLevel != null) {
            level_progress_indicator.setText(String.valueOf(vocabularyAnsweredCount) + "/" + String.valueOf(currentLevel.size()));
        }
        else {
            level_progress_indicator.setText(String.valueOf(vocabularyAnsweredCount));
        }
    }

    private void runLevel() {
        if (currentLevel != null) {
            String newVocabulary = getVocabulary();
            System.out.println("Vocabulary generated: " + newVocabulary);

            if (!methods.isEmpty(newVocabulary)) {
                addUsedVocabulary(newVocabulary);
            }
            else {
                // Finish level
            }

            setVocabularyText(newVocabulary);
            setAnswerText(generateAnswerButtonCaptionOrder(currentLevel.get(newVocabulary)));
        }
    }

    private ArrayList<String> generateAnswerButtonCaptionOrder(String newVoc) {
        ArrayList<String> order = new ArrayList<>();
        Random rand = new Random();
        String toAdd;

        for (int i = 0; i < constants.ANSWER_POSSIBILITY_SIZE; i++) {
            toAdd = LanguageLevelData.words[rand.nextInt(LanguageLevelData.words.length)];

            for (int j = 0; j < order.size(); j++) {

                if (j >= 1) {
                    if (toAdd.equals(order.get(j - 1))) {
                        order.remove(j - 1);
                    }
                }
            }
            order.add(toAdd);
        }
        int newVocPosition = rand.nextInt(order.size());
        order.remove(newVocPosition);
        order.add(newVocPosition, newVoc);

        return order;
    }


    private void addUsedVocabulary(String s) {
        vocabularyUsed.add(s);
    }

    private String getVocabulary() {
        ArrayList<String> alreadyGenerated = new ArrayList<>();
        return getVocabulary(alreadyGenerated);
    }

    // http://stackoverflow.com/questions/929554/is-there-a-way-to-get-the-value-of-a-hashmap-randomly-in-java
    private String getVocabulary(ArrayList<String> alreadyGenerated) {
        String randomVocabulary = "";
        Random generator = new Random();
        HashMap<String, String> tmp = currentLevel;

        if (alreadyGenerated.size() == vocabularyCount) {
            return randomVocabulary;
        }

        if (!alreadyGenerated.isEmpty()) {

            for (int j = 0; j < alreadyGenerated.size(); j++) {
                tmp.remove(alreadyGenerated.get(j));
            }
        }

        if (!vocabularyUsed.isEmpty()) {

            if (alreadyGenerated.size() != vocabularyUsed.size()) {
                randomVocabulary = (String) tmp.keySet().toArray()[generator.nextInt(tmp.size())];

                if (!notUsedYet(randomVocabulary)) {
                    return randomVocabulary;
                }
                else {
                    alreadyGenerated.add(randomVocabulary);
                    getVocabulary(alreadyGenerated);        // Recursion
                }
            }
        }
        else {
            return (String) tmp.keySet().toArray()[generator.nextInt(tmp.size())];
        }

        return randomVocabulary;
    }

    private boolean notUsedYet(String s) {
        for (int i = 0; i < vocabularyUsed.size(); i++) {
            if (s.equals(vocabularyUsed.get(i))) {
                return true;
            }
        }
        return false;
    }

    private void setVocabularyText(String s) {
        current_vocabulary.setText(s);
    }

    private void setAnswerText(ArrayList<String> order) {
        String s;
        int i = 0;

        System.out.println(order.toString());

        while (i != order.size()) {
            s = order.get(i);

            if (i == 0) {
                answer1.setText(order.get(i));
                adjustTextSize(s, answer1);
            }
            else if (i == 1) {
                answer2.setText(order.get(i));
                adjustTextSize(s, answer2);
            }
            else if (i == 2) {
                answer3.setText(order.get(i));
                adjustTextSize(s, answer3);
            }
            else {
                answer4.setText(order.get(i));
                adjustTextSize(s, answer4);
            }
            i++;
        }
    }

    private void adjustTextSize(String s, Button b) {
        if (s.length() >= 15) {
            float newSize = b.getTextSize() - (s.length() % 2);
            b.setTextSize(newSize);
        }
    }

    public void returnToLevelSelectScreen(View view) {
        Intent levelSelectScreen = new Intent(this, PracticeLevelSelectActivity.class);
        levelSelectScreen.putExtra("modeId", modeId);
        levelSelectScreen.putExtra("languageId", languageId);
        startActivity(levelSelectScreen);
    }
}
