package com.bromi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
    }

    // Called when clicked on "Create Profile" button
    public void buttonCreateProfile(View view) {
        Intent createProfile = new Intent(this, CreateProfileInfoActivity.class);
        startActivity(createProfile);
    }
}
