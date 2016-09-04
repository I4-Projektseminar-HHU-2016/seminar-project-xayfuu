package com.bromi.Activities;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bromi.R;
import com.bromi.util.constants;
import com.bromi.util.methods;

import java.util.HashMap;

public class MainMenuActivity extends AppCompatActivity {

    private HashMap<String, String> profileData;
    private ImageView userAvatar;
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            profileData = methods.stringToHashMap(extras.getString(constants.BUNDLE_PROFILE));
        }

        userAvatar = (ImageView) findViewById(R.id.user_avatar_clickable);
        userName = (TextView) findViewById(R.id.user_name);

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

    public void buttonPractice(View view) {
        int modeId = constants.PRACTICE_MODE_ID;

        Intent languageSelect = new Intent(this, LanguageSelectActivity.class);
        languageSelect.putExtra(constants.BUNDLE_MODE_ID, modeId);
        languageSelect.putExtra(constants.BUNDLE_PROFILE, profileData.toString());
        startActivity(languageSelect);
    }
}
