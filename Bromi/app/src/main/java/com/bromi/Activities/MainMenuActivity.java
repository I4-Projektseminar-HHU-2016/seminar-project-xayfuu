package com.bromi.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bromi.R;
import com.bromi.util.constants;
import com.bromi.util.methods;

import java.util.HashMap;

public class MainMenuActivity extends AppCompatActivity {

    private HashMap<String, String> profileData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String s = extras.getString("Profile Data");
            profileData = methods.stringToHashMap(s);
        }
    }

    public void buttonPractice(View view) {
        int modeId = constants.PRACTICE_MODE_ID;
        Intent languageSelect = new Intent(this, LanguageSelectActivity.class);
        languageSelect.putExtra("ModeId", modeId);
        startActivity(languageSelect);
    }
}
