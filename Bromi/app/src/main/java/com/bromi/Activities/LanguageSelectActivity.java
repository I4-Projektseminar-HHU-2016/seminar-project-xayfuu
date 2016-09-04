package com.bromi.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bromi.R;
import com.bromi.util.*;

public class LanguageSelectActivity extends AppCompatActivity {

    private int modeId;
    private Intent initNextActivity;
    private int languageId = -1;

    private String profileData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_select);

        Bundle b = getIntent().getExtras();
        if (b!=null) {
            modeId = b.getInt(constants.BUNDLE_MODE_ID);
            profileData = b.getString(constants.BUNDLE_PROFILE);
        }
    }

    public void buttonGerman(View view) {
        languageId = constants.LANGUAGE_ID_GERMAN;

        if (modeId == constants.PRACTICE_MODE_ID) {
            initNextActivity = new Intent(this, PracticeLevelSelectActivity.class);
            initNextActivity
                    .putExtra(constants.BUNDLE_LANGUAGE_ID, languageId)
                    .putExtra(constants.BUNDLE_MODE_ID, modeId)
                    .putExtra(constants.BUNDLE_PROFILE, profileData);
            startActivity(initNextActivity);
        }
        else if (modeId == constants.CHALLENGE_MODE_ID) {
            // TODO
        }
        else if (modeId == constants.ENDLESS_MODE_ID) {
            // TODO
        }
        else {
            methods.showToast("Error while loading Level Select screen.", getBaseContext());
            initNextActivity = new Intent(this, MainMenuActivity.class);
            initNextActivity.putExtra(constants.BUNDLE_PROFILE, profileData);
            startActivity(initNextActivity);
        }
    }

    public void returnToMainMenu(View view) {
        Intent mainMenu = new Intent(this, MainMenuActivity.class);
        mainMenu.putExtra(constants.BUNDLE_PROFILE, profileData);
        startActivity(mainMenu);
    }
}
