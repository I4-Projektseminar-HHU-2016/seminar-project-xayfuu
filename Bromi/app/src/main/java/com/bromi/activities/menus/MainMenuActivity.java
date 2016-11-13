package com.bromi.activities.menus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bromi.activities.profile.UserProfileActivity;
import com.bromi.R;
import com.bromi.audio.BackgroundMusic;
import com.bromi.util.constants;
import com.bromi.util.methods;

import java.util.HashMap;

public class MainMenuActivity extends AppCompatActivity {

    /**
     * Profile Data Map
     */
    private HashMap<String, String> profileData;

    /**
     * ImageView object of the userAvatar
     */
    private ImageView userAvatar;

    /**
     * TextView object of the userName
     */
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Bundle extras = getIntent().getExtras();    // receive bundle from previous activity

        if (extras != null) {
            profileData = methods.stringToHashMap(extras.getString(constants.BUNDLE_PROFILE));
        }

        BackgroundMusic.start(this, R.raw.walterwarm_summer_love, false);

        userAvatar = (ImageView) findViewById(R.id.user_avatar_clickable);
        userName = (TextView) findViewById(R.id.user_name);

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
        profile.putExtra(constants.BUNDLE_OPENED_FROM, constants.MAIN_MENU_ID);

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
     * Button callback for "Practice"-button
     * TODO: Challenge and Endless buttons
     * @param view
     */
    public void buttonPractice(View view) {
        int modeId = constants.PRACTICE_MODE_ID;

        Intent languageSelect = new Intent(this, LanguageSelectActivity.class);
        languageSelect.putExtra(constants.BUNDLE_MODE_ID, modeId);
        languageSelect.putExtra(constants.BUNDLE_PROFILE, profileData.toString());
        startActivity(languageSelect);
    }

    /**
     * Quit Button method call; returns to startScreen
     */
    public void returnToStartScreen(View view) {
        Intent startScreen = new Intent(this, StartScreenActivity.class);
        startActivity(startScreen);
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
