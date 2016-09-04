package com.bromi.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.bromi.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tapView = (TextView) findViewById(R.id.startButton);

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
}
