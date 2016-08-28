package com.bromi.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bromi.R;
import com.bromi.db.LanguageLevelData;
import com.bromi.db.LanguageLevelDbHelper;
import com.bromi.util.*;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;


public class LogInActivity extends AppCompatActivity {

    private LanguageLevelDbHelper langDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        try {
            checkProfile();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        loadLanguageLevelDb();

    }

    /**
     * Obligatory check if profile exists. If it does, read the profile data.
     *
     * TODO: Send profile data to a UserManager class or something so it's always accessible
     *
     * @throws IOException
     * @throws JSONException
     */
    private void checkProfile() throws IOException, JSONException {

        if (profileExists()) {

            FileInputStream fis = openFileInput(constants.PROFILE_DATA_FILENAME);
            BufferedInputStream bis = new BufferedInputStream(fis);
            StringBuilder buffer = new StringBuilder();

            while (bis.available() != 0) {
                char c = (char) bis.read();
                buffer.append(c);
            }
            fis.close();
            bis.close();

            HashMap<String, String> profileMap = methods.readProfileFromJSONBuffer(buffer);

            Intent mainMenu = new Intent(this, MainMenuActivity.class);
            mainMenu.putExtra("Profile Data", profileMap.toString());
            startActivity(mainMenu);
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

                    }).create().show();
        }
    }

    private void loadLanguageLevelDb() {
        langDb = new LanguageLevelDbHelper(this);
        langDb.insertLevelData();

        // Test
        Cursor l1 = langDb.getLevel(0);
        Cursor l2 = langDb.getLevel(1);
        Cursor l3 = langDb.getLevel(2);
        Cursor l4 = langDb.getLevel(3);
        Cursor l5 = langDb.getLevel(4);
        HashMap<String, String> level1 = langDb.readWordPairsAsMap(l1);
        HashMap<String, String> level2 = langDb.readWordPairsAsMap(l2);
        HashMap<String, String> level3 = langDb.readWordPairsAsMap(l3);
        HashMap<String, String> level4 = langDb.readWordPairsAsMap(l4);
        HashMap<String, String> level5 = langDb.readWordPairsAsMap(l5);
        System.out.println(level1.toString());
        System.out.println(level2.toString());
        System.out.println(level3.toString());
        System.out.println(level4.toString());
        System.out.println(level5.toString());
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
