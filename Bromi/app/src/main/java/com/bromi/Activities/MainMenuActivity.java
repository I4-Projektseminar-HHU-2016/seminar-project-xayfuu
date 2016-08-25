package com.bromi.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bromi.R;
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
}
