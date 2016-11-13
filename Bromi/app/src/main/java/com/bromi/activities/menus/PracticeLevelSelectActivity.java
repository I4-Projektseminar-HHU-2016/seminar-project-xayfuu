package com.bromi.activities.menus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bromi.activities.game.PracticeLevelActivity;
import com.bromi.activities.profile.UserProfileActivity;
import com.bromi.R;
import com.bromi.audio.BackgroundMusic;
import com.bromi.util.constants;
import com.bromi.util.methods;

import java.util.HashMap;

public class PracticeLevelSelectActivity extends AppCompatActivity {

    /**
     * Current mode selected
     */
    private int modeId;

    /**
     * Current language selected
     */
    private int languageId;

    /**
     * Current level selected
     */
    private int levelId;

    /**
     * Holds the profile data
     */
    private HashMap<String, String> profileData;

    /**
     * ImageView object of the userAvatar
     */
    private ImageView userAvatar;

    /**
     * TextView objects used in this activity
     */
    private TextView userName, language_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_level_select);

        BackgroundMusic.start(this, R.raw.walterwarm_summer_love, false);

        Bundle extras = getIntent().getExtras();    // receive Bundle from previous activity

        if (extras != null) {
            modeId = extras.getInt(constants.BUNDLE_MODE_ID);
            languageId = extras.getInt(constants.BUNDLE_LANGUAGE_ID);
            profileData = methods.stringToHashMap(extras.getString(constants.BUNDLE_PROFILE));
        }

        userAvatar = (ImageView) findViewById(R.id.user_avatar_clickable);
        userName = (TextView) findViewById(R.id.user_name);
        language_view = (TextView) findViewById(R.id.language_view);

        setLanguageStringOnTextView();
        initProfileClickListeners();
        setUserName();
        setUserAvatar();
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
     * Set up clickListeners for userName and userAvatar so they act as a button
     * - opens userprofile if onClick() is invoked
     */
    private void initProfileClickListeners() {
        final Intent profile = new Intent(this, UserProfileActivity.class);
        profile.putExtra(constants.BUNDLE_PROFILE, profileData.toString());
        profile.putExtra(constants.BUNDLE_OPENED_FROM, constants.PRACTICE_LEVEL_SELECT_ID);
        profile.putExtra(constants.BUNDLE_MODE_ID, modeId);
        profile.putExtra(constants.BUNDLE_LANGUAGE_ID, languageId);

        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(profile);
            }
        });

        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(profile);
            }
        });
    }

    /**
     * Set language view to show the language that was selected
     */
    private void setLanguageStringOnTextView() {
        String lang = methods.getLanguageFromId(languageId, this);

        if (lang != null) {
            language_view.setText(lang);
        }
        else {
            language_view.setText("");
        }
    }

    /**
     * Small animation when a level has been selected.
     * - Initiates level gameplay when animation is done
     * @param b
     */
    private void levelTransition(final Button b) {
        final Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slow_fade_out);
        final Intent practiceLevel = new Intent(this, PracticeLevelActivity.class);

        b.startAnimation(fade);

        fade.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                b.setVisibility(View.INVISIBLE);
                practiceLevel.putExtra(constants.BUNDLE_MODE_ID, modeId);
                practiceLevel.putExtra(constants.BUNDLE_LANGUAGE_ID, languageId);
                practiceLevel.putExtra(constants.BUNDLE_LEVEL_ID, levelId);
                practiceLevel.putExtra(constants.BUNDLE_IS_NEW_LEVEL, true);
                practiceLevel.putExtra(constants.BUNDLE_PROFILE, profileData.toString());
                startActivity(practiceLevel);
                b.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    /**
     * Called when any level has been clicked.
     * - Identifies the level that was clicked by it's viewID in the android ressources and instantiates levelId
     * @param view
     */
    public void startLevelClicked(View view) {
        Button b = (Button) findViewById(view.getId());
        switch (view.getId()) {
            case (R.id.level_1):
                //Start level 1
                levelTransition(b);
                levelId = 1;
                break;

            case (R.id.level_2):
                //Start level 2
                levelTransition(b);
                levelId = 2;
                break;

            case (R.id.level_3):
                //Start level 3
                levelTransition(b);
                levelId = 3;
                break;

            case (R.id.level_4):
                //Start level 4
                levelTransition(b);
                levelId = 4;
                break;

            case (R.id.level_5):
                //Start level 5
                levelTransition(b);
                levelId = 5;
                break;

            case (R.id.level_6):
                //Start level 6
                levelTransition(b);
                levelId = 6;
                break;

            case (R.id.level_7):
                //Start level 7
                levelTransition(b);
                levelId = 7;
                break;

            case (R.id.level_8):
                //Start level 8
                levelTransition(b);
                levelId = 8;
                break;

            case (R.id.level_exam):
                //Start exam
                levelTransition(b);
                levelId = 42;
                break;

            default:
                levelId = -1;
        }
    }

    /**
     * Quit-Button method to return to language select
     * @param view
     */
    public void returnToLanguageSelect(View view) {
        Intent lvlSelect = new Intent(this, LanguageSelectActivity.class);
        lvlSelect.putExtra(constants.BUNDLE_MODE_ID, modeId);
        lvlSelect.putExtra(constants.BUNDLE_PROFILE, profileData.toString());
        startActivity(lvlSelect);
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
