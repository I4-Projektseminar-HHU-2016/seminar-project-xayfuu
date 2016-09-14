package com.bromi.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bromi.R;
import com.bromi.util.*;

public class LanguageSelectActivity extends AppCompatActivity {

    /**
     * Mode identifier
     * (from ../util/constants.java:
     *      public static final int PRACTICE_MODE_ID = 0;
     *      public static final int CHALLENGE_MODE_ID = 1;
     *      public static final int ENDLESS_MODE_ID = 2;
     *      )
     */
    private int modeId;

    /**
     * Profile data (Saved as String in this case, since we don't need a HashMapped profile for this class)
     */
    private String profileData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_select);

        Bundle b = getIntent().getExtras();     // recieve bundled data from previous activity

        if (b!=null) {
            modeId = b.getInt(constants.BUNDLE_MODE_ID);
            profileData = b.getString(constants.BUNDLE_PROFILE);
        }
    }

    /**
     * Provisionary method for the only working language at the moment. Initiates next activity and initializes languageID
     * TODO: do a switch-case sort of thing for languageID if all languages were to be used at any point in the future
     */
    public void buttonGerman(View view) {
        int languageId = constants.LANGUAGE_ID_GERMAN;

        Intent initNextActivity;
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

    /**
     * Returns to the main menu
     * @param view
     */
    public void returnToMainMenu(View view) {
        Intent mainMenu = new Intent(this, MainMenuActivity.class);
        mainMenu.putExtra(constants.BUNDLE_PROFILE, profileData);
        startActivity(mainMenu);
    }
}
