package com.bromi.activities.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.bromi.activities.LogInActivity;
import com.bromi.activities.menus.StartScreenActivity;
import com.bromi.R;
import com.bromi.audio.BackgroundMusic;
import com.bromi.lib.ProfileManager;
import com.bromi.util.*;

import org.json.JSONException;

import java.io.IOException;


public class CreateProfileActivity extends AppCompatActivity {

    /**
     * Stores the name the user gave
     */
    private EditText name;

    /**
     * Stores the country the user gave
     */
    private AutoCompleteTextView countryEdit;

    /**
     * DropDown menu for gender question
     */
    private Spinner genderSpinner;

    /**
     * String Array of all countries available in strings.xml
     */
    private String[] countries;

    /**
     * Boolean to ensure name entered is valid or not
     */
    private boolean nameIsValid = false;

    /**
     * Boolean to ensure country entered is valid or not
     */
    private boolean countryIsValid = false;

    /**
     * Make this value >true< to skip profile creation for development/test purposes
     */
    public static boolean dummyProfile = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!dummyProfile) {
            setContentView(R.layout.activity_create_profile);

            BackgroundMusic.start(this, R.raw.walterwarm_summer_love, false);

            name = (EditText) findViewById(R.id.editName);
            countryEdit = (AutoCompleteTextView) findViewById(R.id.autocomplete_country);
            genderSpinner = (Spinner) findViewById(R.id.genderSpinner);

            countries = getResources().getStringArray(R.array.countries_array);

            name.addTextChangedListener(nameWatcher);
            countryEdit.addTextChangedListener(countryWatcher);

            initAutoCountryAdapter();
            initGenderSpinnerAdapter();
        }
        else {
            try {
                ProfileManager.createProfile("Dummy", "Germany", "Male", getApplicationContext());
                initLogIn();
            }
            catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Text Watcher for name edit field
     * - shows if the desired name has been entered wrong upon finishing entering text
     */
    private final TextWatcher nameWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            final String enteredString = editable.toString();

            /**
             * Add a FocusListener to make sure Toast doesn't pop up with every symbol the user enters...
             */
            name.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View view, boolean hasFocus) {

                    if (!hasFocus) {

                        if (methods.isEmpty(enteredString) && enteredString.length() < constants.STRING_SIZE_LIMIT) {
                            methods.showToast("Please make sure your name is at least 3 symbols long!", getApplicationContext());
                        }
                        else {
                            nameIsValid = true;
                        }
                    }
                }
            });
        }
    };

    /**
     * Text Watcher for country edit field
     * - complains if no legitimate country has been entered(relative to R.id.country_array)
     */
    private final TextWatcher countryWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            final String enteredString = editable.toString();

            /**
             * Add a FocusListener to make sure Toast doesn't pop up with every symbol the user enters...
             */
            countryEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View view, boolean hasFocus) {

                    if (!hasFocus) {

                        for (String country : countries) {

                            if (enteredString.toLowerCase().equals(country.toLowerCase())) {
                                countryIsValid = true;
                            }
                        }
                        if (!countryIsValid) {
                            methods.showToast("Please make sure you enter a legitimate country!", getApplicationContext());
                        }
                    }
                }
            });
        }
    };

    /**
     * https://developer.android.com/guide/topics/ui/controls/text.html
     * Automatic Country suggestions
     */
    protected void initAutoCountryAdapter() {
        ArrayAdapter<String> auto_country_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countries);
        countryEdit.setAdapter(auto_country_adapter);
    }

    /**
     * https://developer.android.com/guide/topics/ui/controls/spinner.html
     * Gender Spinner/Drop Down Menu
     */
    protected void initGenderSpinnerAdapter() {
        String[] genders = getResources().getStringArray(R.array.gender_array);
        ArrayAdapter<CharSequence> gender_spinner_adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, genders);
        genderSpinner.setAdapter(gender_spinner_adapter);
    }

    /**
     * Quit Button method call
     */
    public void returnToStartScreen(View view) {
        Intent startScreen = new Intent(this, StartScreenActivity.class);
        startActivity(startScreen);
    }

    /**
     * Submit Button method call
     * - gets every string the user entered within the textViews
     */
    public void submitProfileData(View view) {
        final String nameValue = name.getText().toString();
        final String countryValue = countryEdit.getText().toString();
        final String genderValue = genderSpinner.getSelectedItem().toString();

        // clear focuses of textViews to force TextWatchers to invoke afterTextChanged() methods
        name.clearFocus();
        countryEdit.clearFocus();

        /**
         System.out.println(nameValue);
         System.out.println(countryValue);
         System.out.println(genderValue);
         **/

        if (countryIsValid && nameIsValid) {

            /**
             * If no profile exists, create one
             */
            if (!ProfileManager.profileExists(getApplicationContext())) {

                try {
                    ProfileManager.createProfile(nameValue, countryValue, genderValue, getApplicationContext());
                    initLogIn();
                }
                catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            else {

                /**
                 * If a profile exists, show a dialog alert that asks the user if they want to delete their old profile
                 */
                new AlertDialog.Builder(this)
                        .setMessage(R.string.dialog_delete_profile)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteFile(constants.PROFILE_DATA_FILENAME);    // Delete Profile
                                methods.showToast("Profile deleted.", getApplicationContext());

                                try {
                                    ProfileManager.createProfile(nameValue, countryValue, genderValue, getApplicationContext());
                                }
                                catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                }

                                initLogIn();

                            }

                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Do Nothing
                    }
                }).setCancelable(false)
                        .create()
                        .show();
            }
        }
        else {

            if (!countryIsValid) {
                methods.showToast("Please make sure you chose a legitimate country!", getApplicationContext());
            }

            if (!nameIsValid) {
                methods.showToast("Please make sure your name is at least 3 symbols long!", getApplicationContext());
            }
        }
    }

    private void initLogIn() {
        Intent logIn = new Intent(getApplicationContext(), LogInActivity.class);
        startActivity(logIn);
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