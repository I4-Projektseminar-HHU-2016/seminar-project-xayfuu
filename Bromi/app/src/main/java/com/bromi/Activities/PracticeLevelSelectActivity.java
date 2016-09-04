package com.bromi.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bromi.R;
import com.bromi.util.constants;
import com.bromi.util.methods;

import org.w3c.dom.Text;

import java.util.HashMap;

public class PracticeLevelSelectActivity extends AppCompatActivity {

    private int modeId;
    private int languageId;
    private int levelId;

    private HashMap<String, String> profileData;

    private ImageView userAvatar;
    private TextView userName, language_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_level_select);

        Bundle extras = getIntent().getExtras();
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

    private void setLanguageStringOnTextView() {
        String lang = methods.getLanguageFromId(languageId, this);

        if (lang != null) {
            language_view.setText(lang);
        }
        else {
            language_view.setText("");
        }
    }

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

    public void startLevelClicked(View view) {
        Button b = (Button) findViewById(view.getId());
        switch (view.getId()) {
            case (R.id.level_1):
                //Start level 1
                levelTransition(b);
                levelId = 0;
                break;
            case (R.id.level_2):
                //Start level 2
                levelTransition(b);
                levelId = 1;
                break;
            case (R.id.level_3):
                //Start level 3
                levelTransition(b);
                levelId = 2;
                break;
            case (R.id.level_4):
                //Start level 4
                levelTransition(b);
                levelId = 3;
                break;
            case (R.id.level_5):
                //Start level 5
                levelTransition(b);
                levelId = 4;
                break;
            case (R.id.level_6):
                //Start level 6
                levelTransition(b);
                levelId = 5;
                break;
            case (R.id.level_7):
                //Start level 7
                levelTransition(b);
                levelId = 6;
                break;
            case (R.id.level_8):
                //Start level 8
                levelTransition(b);
                levelId = 7;
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

    public void returnToLanguageSelect(View view) {
        Intent lvlSelect = new Intent(this, LanguageSelectActivity.class);
        lvlSelect.putExtra(constants.BUNDLE_MODE_ID, modeId);
        lvlSelect.putExtra(constants.BUNDLE_PROFILE, profileData.toString());
        startActivity(lvlSelect);
    }
}
