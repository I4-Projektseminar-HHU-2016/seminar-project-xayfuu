package com.bromi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CreateProfileInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile_info);
    }

    //Called upon clicking the "Next" button
    public void initProfileQuestions(View view) {
        Intent profileQuestions = new Intent(this, CreateProfileQuestionsActivity.class);
        startActivity(profileQuestions);
    }
}
