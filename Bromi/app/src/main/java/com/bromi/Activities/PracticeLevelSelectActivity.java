package com.bromi.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.bromi.R;
import com.bromi.util.methods;

public class PracticeLevelSelectActivity extends AppCompatActivity {

    private int modeId;
    private int languageId;
    private int levelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_level_select);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            modeId = extras.getInt("modeId");
            languageId = extras.getInt("languageId");
        }

        setLanguageStringOnTextView();
    }

    private void setLanguageStringOnTextView() {
        String lang = methods.getLanguageFromId(languageId, this);

        if (lang != null) {
            ((TextView) findViewById(R.id.language_view)).setText(lang);
        }
        else {
            ((TextView) findViewById(R.id.language_view)).setText("");
        }
    }

    private void levelTransition(final Button b) {
        final Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        final Intent practiceLevel = new Intent(this, PracticeLevelActivity.class);

        b.startAnimation(fade);
        fade.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                b.setVisibility(View.INVISIBLE);
                practiceLevel.putExtra("modeId", modeId);
                practiceLevel.putExtra("languageId", languageId);
                practiceLevel.putExtra("levelId", levelId);
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
}
