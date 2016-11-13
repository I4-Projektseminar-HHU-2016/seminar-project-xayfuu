package com.bromi.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.bromi.activities.menus.MainMenuActivity;
import com.bromi.R;
import com.bromi.audio.BackgroundMusic;
import com.bromi.db.LanguageDbAdapter;
import com.bromi.lib.ProfileManager;
import com.bromi.util.*;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;


public class LogInActivity extends Activity {

    private LanguageDbAdapter langDb;
    private HashMap<String, String> profileMap;
    public TextView load_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        BackgroundMusic.start(this, R.raw.walterwarm_summer_love, false);

        load_message = (TextView) findViewById(R.id.loading_text);

        new Loader(this, load_message).execute();

    }

    private class Loader extends AsyncTask<String, String, String> {
        Context c;
        TextView load_message;

        Loader(Context c, TextView load_message) {
            this.c = c;
            this.load_message = load_message;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            load_message.setText("Checking level data...");
        }

        @Override
        protected String doInBackground(String... params) {
            /**
             * Initiate level database
             */

            langDb = new LanguageDbAdapter(getApplicationContext());

            publishProgress("Setting up data...");
            langDb.createDatabase();
            //langDb.open();
            //langDb.runTest();

            publishProgress("Loading profile data...");
            readProfile();

            return "Complete!";
        }

        @Override
        protected void onProgressUpdate(String... s) {
            super.onProgressUpdate(s[0]);
            load_message.setText(s[0]);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            load_message.setText(result);

            Intent mainMenu = new Intent(getApplicationContext(), MainMenuActivity.class);
            mainMenu.putExtra(constants.BUNDLE_PROFILE, profileMap.toString());
            startActivity(mainMenu);
        }
    }

    /**
     * Obligatory check if profile exists. If it does, read the profile data.
     *
     * TODO: Send profile data to a UserManager class or something so it's always accessible
     *
     * @throws IOException
     * @throws JSONException
     */
    private void readProfile() {
        try {
            profileMap = ProfileManager.readProfileAsHashMap(getApplicationContext());
        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
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