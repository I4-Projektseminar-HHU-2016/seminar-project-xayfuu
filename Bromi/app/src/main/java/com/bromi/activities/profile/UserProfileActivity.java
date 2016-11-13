package com.bromi.activities.profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bromi.audio.BackgroundMusic;
import com.bromi.custom.ExperienceBar;
import com.bromi.R;
import com.bromi.util.constants;
import com.bromi.util.methods;

import java.util.HashMap;

public class UserProfileActivity extends AppCompatActivity {

    /**
     * Profile data map
     */
    private HashMap<String, String> profileData;

    /**
     * This integer is used to identify the activity the userProfile has been opened from.
     * (See ../util/constants.java:
     *      public static final int MAIN_MENU_ID = 0;
     *      public static final int PRACTICE_LEVEL_SELECT_ID = 1;
     *      )
     */
    private int openedFrom;

    /**
     * Current mode selected
     * - this exists to make sure the modeId doesn't get lost when userProfile was opened from LevelSelect activity
     */
    private int modeId;

    /**
     * Current language selected
     * - this exists to make sure the languageId doesn't get lost when userProfile was opened from LevelSelect activity
     */
    private int languageId;

    /**
     * TextView objects used in this activity
     */
    private TextView userName, vocabulary_answered, levels_played, wrong_vocabularies, correct_vocabularies, current_exp, user_level;

    /**
     * The userAvatar picture
     */
    private ImageView userAvatar;

    /**
     * ExperienceBar object used to interact with custom ExperienceBar View
     */
    private ExperienceBar exp_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Bundle extras = getIntent().getExtras();

        BackgroundMusic.start(this, R.raw.walterwarm_summer_love, false);

        if (extras != null) {
            profileData = methods.stringToHashMap(extras.getString(constants.BUNDLE_PROFILE));
            openedFrom = extras.getInt(constants.BUNDLE_OPENED_FROM);

            // receive data according to the activity the profile has been opened from
            if (openedFrom == constants.PRACTICE_LEVEL_SELECT_ID) {
                modeId = extras.getInt(constants.BUNDLE_MODE_ID);
                languageId = extras.getInt(constants.BUNDLE_LANGUAGE_ID);
            }
        }

        userName = (TextView) findViewById(R.id.user_name);
        vocabulary_answered = (TextView) findViewById(R.id.vocabulary_answered);
        levels_played = (TextView) findViewById(R.id.levels_played);
        wrong_vocabularies = (TextView) findViewById(R.id.wrong_vocabularies);
        correct_vocabularies = (TextView) findViewById(R.id.correct_vocabularies);
        current_exp = (TextView) findViewById(R.id.current_exp);
        exp_bar = (ExperienceBar) findViewById(R.id.exp_bar);
        user_level = (TextView) findViewById(R.id.user_level);

        userAvatar = (ImageView) findViewById(R.id.user_avatar);

        setUserName();
        setUserAvatar();
        setLevelsCompleted();
        setVocabulariesAnswered();
        setCorrectVocabularies();
        setWrongVocabularies();
        drawXpRectangle();
        setCurrentExp();
        setUserLevel();
    }

    /**
     * Set text of userName TextView object to the username stored within profile map
     */
    private void setUserName() {
        if (profileData != null) {
            userName.setText(profileData.get(constants.PROFILE_NAME));
        }
        else {
            userName.setText("ProfileManager 31");
        }
    }

    /**
     * Set avatar of userAvatar ImageView object to the username stored within profile map (only one working avatar at the moment)
     */
    private void setUserAvatar() {
        if (profileData != null) {
            userAvatar.setImageResource(methods.getImageResourceId(profileData.get(constants.PROFILE_AVATAR)));
        }
        else {
            userAvatar.setImageResource(methods.getImageResourceId("default"));
        }
    }

    /**
     * Set levels_completed statistic text
     */
    private void setLevelsCompleted() {
        if (profileData != null) {
            levels_played.setText(profileData.get(constants.STAT_LEVELS_DONE));
        }
        else {
            levels_played.setText("-");
        }
    }

    /**
     * Set vocabulary_answered statistic text
     */
    private void setVocabulariesAnswered() {
        if (profileData != null) {
            vocabulary_answered.setText(profileData.get(constants.STAT_VOCABULARIES_DONE));
        }
        else {
            vocabulary_answered.setText("-");
        }
    }

    /**
     * Set correct_vocabularies statistic text
     */
    private void setCorrectVocabularies() {
        if (profileData != null) {
            correct_vocabularies.setText(profileData.get(constants.STAT_CORRECT_VOCABULARIES));
        }
        else {
            correct_vocabularies.setText("-");
        }
    }

    /**
     * set wrong_vocabularies statistic text
     */
    private void setWrongVocabularies() {
        if (profileData != null) {
            wrong_vocabularies.setText(profileData.get(constants.STAT_WRONG_VOCABULARIES));
        }
        else {
            wrong_vocabularies.setText("-");
        }
    }

    /**
     * Draw the experience bar onto the activity view relative to the user's amount of current experience needed until the next level up
     */
    private void drawXpRectangle() {
        float ratio = (float) Integer.parseInt(profileData.get(constants.STAT_USER_EXPERIENCE))/constants.EXP_REQUIRED_FOR_ONE_LEVEL;
        float length = ratio * exp_bar.getXpBarLength();

        exp_bar.setXpBarLength(length);

        //System.out.println(length);
    }

    /**
     * Set current_exp text
     */
    private void setCurrentExp() {
        if (profileData != null) {
            current_exp.setText(profileData.get(constants.STAT_USER_EXPERIENCE));
        }
        else {
            current_exp.setText("-");
        }
    }

    /**
     * Set user_level text
     */
    private void setUserLevel() {
        if (profileData != null) {
            user_level.setText(user_level.getText().toString().replace("0", String.valueOf(profileData.get(constants.STAT_USER_LEVEL))));
        }
        else {
            user_level.setText("-");
        }
    }

    /**
     * Quit Button method, returns to the activity the profile has been opened from
     * @param view
     */
    public void returnToMenu(View view) {
        Intent returnMenu = methods.getIntentFromId(openedFrom, this.getApplicationContext());
        returnMenu.putExtra(constants.BUNDLE_PROFILE, profileData.toString());

        // don't forget to add languageId and modeId to the bundle if returning to the levelSelect activity
        if(openedFrom == constants.PRACTICE_LEVEL_SELECT_ID) {
            returnMenu.putExtra(constants.BUNDLE_LANGUAGE_ID, languageId);
            returnMenu.putExtra(constants.BUNDLE_MODE_ID, modeId);
        }

        startActivity(returnMenu);
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
