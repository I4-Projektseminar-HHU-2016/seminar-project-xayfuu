package com.bromi.Activities;

import android.app.Dialog;
import android.content.Context;
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

import com.bromi.R;
import com.bromi.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;


public class CreateProfileActivity extends AppCompatActivity {

    private EditText name;
    private AutoCompleteTextView countryEdit;
    private Spinner genderSpinner;

    private String[] countries;

    private boolean nameIsValid = false;
    private boolean countryIsValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        name = (EditText) findViewById(R.id.editName);
        countryEdit = (AutoCompleteTextView) findViewById(R.id.autocomplete_country);
        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);

        countries = getResources().getStringArray(R.array.countries_array);

        name.addTextChangedListener(nameWatcher);
        countryEdit.addTextChangedListener(countryWatcher);

        initAutoCountryAdapter();
        initGenderSpinnerAdapter();
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
                        else { nameIsValid = true; }
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
     */
    public void submitProfileData(View view) {
        String nameValue = name.getText().toString();
        String countryValue = countryEdit.getText().toString();
        String genderValue = genderSpinner.getSelectedItem().toString();

        name.clearFocus();
        countryEdit.clearFocus();

        /**
         System.out.println(nameValue);
         System.out.println(countryValue);
         System.out.println(genderValue);
         **/

        if (countryIsValid && nameIsValid) {
            try {
                createProfile(nameValue, countryValue, genderValue);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
        else {

            if (!countryIsValid) {
                methods.showToast("Please make sure you enter a legitimate country!", getApplicationContext());
            }

            if (!nameIsValid) {
                methods.showToast("Please make sure your name is at least 3 symbols long!", getApplicationContext());
            }
        }
    }

    /**
     * This method creates one basic profile saved as a JSON on the internal storage of the device
     *
     * [
     *   {
     *     "name": name
     *     "gender": gender
     *     "country": country
     *     "avatar": default.png
     *   }
     * ]
     *
     * @param name - the name the user entered
     * @param country - the country the user entered
     * @param gender - the gender the user gave
     */
    private void createProfile(final String name, final String country, final String gender) throws IOException, JSONException {

        if (!profileExists()){

            FileOutputStream fos = openFileOutput(constants.PROFILE_DATA_FILENAME, Context.MODE_PRIVATE);

            // Create JSON
            JSONArray data = createProfileJSONObject(name, country, gender);

            // Write onto device's storage
            fos.write(data.toString().getBytes());
            fos.close();

            /** Test to make sure it worked
             FileInputStream fis = openFileInput(constants.PROFILE_DATA_FILENAME);
             BufferedInputStream bis = new BufferedInputStream(fis);
             StringBuilder buffer = new StringBuilder();

             while (bis.available() != 0) {
             char c = (char) bis.read();
             buffer.append(c);
             }
             fis.close();
             bis.close();

             methods.readProfileFromJSONBuffer(buffer);
             */

            methods.showToast("Profile created!", getBaseContext());

            Intent logIn = new Intent(this, LogInActivity.class);
            startActivity(logIn);
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
                        createProfile(name, country, gender);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

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

    /**
     * Creates a Profile. Data must be gathered beforehand for this to work.
     * @param name - the name a user entered
     * @param country - the country a user entered
     * @param gender - the gender a user entered
     * @return the JSONArray
     * @throws JSONException
     */
    private JSONArray createProfileJSONObject(String name, String country, String gender) throws JSONException {
        JSONArray data = new JSONArray();
        JSONObject profile = new JSONObject();
        profile.put(constants.PROFILE_NAME, name);
        profile.put(constants.PROFILE_GENDER, gender);
        profile.put(constants.PROFILE_COUNTRY, country);
        profile.put(constants.PROFILE_AVATAR, "default_avatar");
        profile.put(constants.STAT_LEVELS_DONE, 0);
        profile.put(constants.STAT_VOCABULARIES_DONE, 0);
        profile.put(constants.STAT_CORRECT_VOCABULARIES, 0);
        profile.put(constants.STAT_WRONG_VOCABULARIES, 0);
        data.put(profile);

        return data;
    }

    /**
     * Checks if a profile exists by looking for its name within the file storage of the device
     * @return - true if the profile data exists, false if otherwise
     */
    private boolean profileExists() {
        String[] files = fileList();
        for (String file : files) {
            System.out.println(file);
            if (file.equals(constants.PROFILE_DATA_FILENAME)) {
                return true;
            }
        }
        return false;
    }
}