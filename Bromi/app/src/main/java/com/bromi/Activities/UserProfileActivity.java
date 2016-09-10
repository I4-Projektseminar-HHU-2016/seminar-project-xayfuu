package com.bromi.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bromi.Custom.ExperienceBar;
import com.bromi.R;
import com.bromi.util.constants;
import com.bromi.util.methods;

import java.util.HashMap;

public class UserProfileActivity extends AppCompatActivity {

    private HashMap<String, String> profileData;

    private int openedFrom;
    private int modeId;
    private int languageId;

    private TextView userName, vocabulary_answered, levels_played, wrong_vocabularies, correct_vocabularies, current_exp, user_level;
    private ImageView userAvatar;
    private ExperienceBar exp_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            profileData = methods.stringToHashMap(extras.getString(constants.BUNDLE_PROFILE));
            openedFrom = extras.getInt(constants.BUNDLE_OPENED_FROM);

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

    private void setUserName() {
        if (profileData != null) {
            userName.setText(profileData.get(constants.PROFILE_NAME));
        }
        else {
            userName.setText("User 31");
        }
    }

    private void setUserAvatar() {
        if (profileData != null) {
            userAvatar.setImageResource(methods.getImageResourceId(profileData.get(constants.PROFILE_AVATAR)));
        }
        else {
            userAvatar.setImageResource(methods.getImageResourceId("default"));
        }
    }

    private void setLevelsCompleted() {
        if (profileData != null) {
            levels_played.setText(profileData.get(constants.STAT_LEVELS_DONE));
        }
        else {
            levels_played.setText("-");
        }
    }

    private void setVocabulariesAnswered() {
        if (profileData != null) {
            vocabulary_answered.setText(profileData.get(constants.STAT_VOCABULARIES_DONE));
        }
        else {
            vocabulary_answered.setText("-");
        }
    }

    private void setCorrectVocabularies() {
        if (profileData != null) {
            correct_vocabularies.setText(profileData.get(constants.STAT_CORRECT_VOCABULARIES));
        }
        else {
            correct_vocabularies.setText("-");
        }
    }

    private void setWrongVocabularies() {
        if (profileData != null) {
            wrong_vocabularies.setText(profileData.get(constants.STAT_WRONG_VOCABULARIES));
        }
        else {
            wrong_vocabularies.setText("-");
        }
    }

    private void drawXpRectangle() {
        float ratio = (float) Integer.parseInt(profileData.get(constants.STAT_USER_EXPERIENCE))/constants.EXP_REQUIRED_FOR_ONE_LEVEL;
        float length = ratio * exp_bar.getXpBarLength();

        exp_bar.setXpBarLength(length);

        //System.out.println(length);
    }

    private void setCurrentExp() {
        if (profileData != null) {
            current_exp.setText(profileData.get(constants.STAT_USER_EXPERIENCE));
        }
        else {
            wrong_vocabularies.setText("-");
        }
    }

    private void setUserLevel() {
        if (profileData != null) {
            user_level.setText(user_level.getText().toString().replace("0", String.valueOf(profileData.get(constants.STAT_USER_LEVEL))));
        }
        else {
            user_level.setText("-");
        }
    }

    public void returnToMenu(View view) {
        Intent returnMenu = methods.getIntentFromId(openedFrom, this.getApplicationContext());
        returnMenu.putExtra(constants.BUNDLE_PROFILE, profileData.toString());

        if(openedFrom == constants.PRACTICE_LEVEL_SELECT_ID) {
            returnMenu.putExtra(constants.BUNDLE_LANGUAGE_ID, languageId);
            returnMenu.putExtra(constants.BUNDLE_MODE_ID, modeId);
        }

        startActivity(returnMenu);
    }
}
