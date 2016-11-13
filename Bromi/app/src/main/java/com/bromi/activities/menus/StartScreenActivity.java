package com.bromi.activities.menus;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bromi.activities.profile.CreateProfileInfoActivity;
import com.bromi.activities.LogInActivity;
import com.bromi.R;
import com.bromi.audio.BackgroundMusic;
import com.bromi.lib.ProfileManager;

public class StartScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        BackgroundMusic.start(this, R.raw.walterwarm_summer_love, false);
    }

    // Called when clicked on "Create Profile" button
    public void buttonCreateProfile(View view) {
        Intent createProfile = new Intent(this, CreateProfileInfoActivity.class);
        startActivity(createProfile);
    }

    // Called when clicked on "Log In" button
    public void buttonLogIn(View view) {
        if (ProfileManager.profileExists(getApplicationContext())) {
            Intent logIn = new Intent(this, LogInActivity.class);
            startActivity(logIn);
        }
        else {
            /**
             * Show alert that no profile exists and ask user to create one
             * (returns user to profile creation activity
             */
            final Intent create = new Intent(this, CreateProfileInfoActivity.class);

            new AlertDialog.Builder(this)
                    .setMessage(R.string.dialog_create_profile)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(create);
                        }

                    }).setCancelable(false)
                    .create()
                    .show();
        }
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
