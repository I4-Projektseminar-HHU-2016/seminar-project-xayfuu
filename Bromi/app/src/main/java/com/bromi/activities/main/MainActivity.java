package com.bromi.activities.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.bromi.activities.menus.StartScreenActivity;
import com.bromi.R;
import com.bromi.audio.BackgroundMusic;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tapView = (TextView) findViewById(R.id.startButton);

        BackgroundMusic.start(this, R.raw.walterwarm_summer_love, true);

        Animation fade = AnimationUtils.loadAnimation(this, R.anim.slow_fade_in);
        tapView.startAnimation(fade);

        final Intent start = new Intent(this, StartScreenActivity.class);

        tapView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(start);
            }
        });
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
