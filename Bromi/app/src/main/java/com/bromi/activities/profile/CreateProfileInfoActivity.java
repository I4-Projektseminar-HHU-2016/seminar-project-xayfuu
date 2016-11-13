package com.bromi.activities.profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bromi.R;
import com.bromi.audio.BackgroundMusic;

public class CreateProfileInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!CreateProfileActivity.dummyProfile) {
            setContentView(R.layout.activity_create_profile_info);

            BackgroundMusic.start(this, R.raw.walterwarm_summer_love, false);
        }
        else {
            Intent profileQuestions = new Intent(this, CreateProfileActivity.class);
            startActivity(profileQuestions);
        }

    }

    //Called upon clicking the "Next" button
    public void initProfileQuestions(View view) {
        Intent profileQuestions = new Intent(this, CreateProfileActivity.class);
        startActivity(profileQuestions);
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
