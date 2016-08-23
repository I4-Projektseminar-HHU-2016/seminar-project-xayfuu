package com.bromi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

public class CreateProfileQuestionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile_questions);

        initAutoCountryAdapter();
        initGenderSpinnerAdapter();
    }

    // https://developer.android.com/guide/topics/ui/controls/text.html
    // Automatic Country suggestions
    protected void initAutoCountryAdapter() {
        AutoCompleteTextView autoView = (AutoCompleteTextView) findViewById(R.id.autocomplete_country);
        String[] countries = getResources().getStringArray(R.array.countries_array);
        ArrayAdapter<String> auto_country_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countries);
        autoView.setAdapter(auto_country_adapter);
    }

    // https://developer.android.com/guide/topics/ui/controls/spinner.html
    // Gender Spinner/Drop Down Menu
    protected void initGenderSpinnerAdapter() {
        Spinner spinner = (Spinner) findViewById(R.id.genderSpinner);
        String[] genders = getResources().getStringArray(R.array.gender_array);
        ArrayAdapter<CharSequence> gender_spinner_adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, genders);
        spinner.setAdapter(gender_spinner_adapter);
    }

    // Quit Button
    public void returnToStartScreen(View view) {
        Intent startScreen = new Intent(this, StartScreenActivity.class);
        startActivity(startScreen);
    }
}
