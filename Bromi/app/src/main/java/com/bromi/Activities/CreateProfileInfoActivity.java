package com.bromi.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bromi.R;

public class CreateProfileInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile_info);
    }

    //Called upon clicking the "Next" button
    public void initProfileQuestions(View view) {
        Intent profileQuestions = new Intent(this, CreateProfileActivity.class);
        startActivity(profileQuestions);
    }
}
