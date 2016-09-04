package com.bromi.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.widget.Toast;

import com.bromi.Activities.MainMenuActivity;
import com.bromi.Activities.PracticeLevelActivity;
import com.bromi.Activities.PracticeLevelSelectActivity;
import com.bromi.R;
import com.bromi.db.LanguageLevelData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

public class methods {

    /**
     * Check if a string is empty
     * @param str - the string to be checked
     * @return - true if empty, false if otherwise
     */
    public static boolean isEmpty(String str) {
        return str != null && !str.isEmpty() && str.length() != 0;
    }

    /**
     * Shows a so called "Toast" pop-up within the context
     * @param message - the message to be displayed
     * @param c - the context to be popped up in
     */
    public static void showToast(String message, Context c) {
        Toast toast = Toast.makeText(c, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Reads a Profile from a JSON Buffered String
     * @param buffered - the String created upon reading a JSON String from the device's internal storage beforehand
     * @return a HashMap with all elements of the JSONObject as key;value pairs
     * @throws JSONException
     */
    public static HashMap<String, String> readProfileFromJSONBuffer(StringBuilder buffered) throws JSONException {

        // http://stackoverflow.com/questions/4307118/jsonarray-to-hashmap
        JSONArray readData = new JSONArray(buffered.toString());
        HashMap<String, String> profileData = new HashMap<>();

        for (int i = 0; i < readData.length(); i++) {
            JSONObject toPut = readData.optJSONObject(i);
            Iterator iter = toPut.keys();

            while (iter.hasNext()) {
                String key = iter.next().toString();
                profileData.put(key, toPut.getString(key));
            }

            /**
             System.out.println(profileData);
             System.out.println(profileData.size());
             System.out.println(profileData.get("name"));
             System.out.println(profileData.get("gender"));
             System.out.println(profileData.get("country"));
             **/

        }

        return profileData;
    }

    // http://stackoverflow.com/questions/19945411/android-java-how-can-i-parse-a-local-json-file-from-assets-folder-into-a-listvi
    public static String loadJsonFromAssets(Context c) throws IOException {
        FileInputStream profile = c.openFileInput(constants.PROFILE_DATA_FILENAME);
        byte[] stringBytes = new byte[profile.available()];
        profile.read(stringBytes);
        profile.close();

        return new String(stringBytes, "UTF-8");
    }

    /**
     * Used to convert the profile string that's being carried between activities back to a HashMap.
     * @param mapString - the string that needs to be converted, i.e. {name=fdghsdfhds, gender=Male, country=Germany}
     * @return the hashMap object
     * http://stackoverflow.com/questions/26485964/how-to-convert-string-into-hashmap-in-java
     */
    public static HashMap<String, String> stringToHashMap(String mapString) {
        if (mapString != null) {

            if (!mapString.isEmpty() && !mapString.equals("")) {
                HashMap<String, String> ret = new HashMap<>();
                mapString = mapString.substring(1, mapString.length() - 1);
                String[] keyValuePairs = mapString.split(",");

                for (String pair : keyValuePairs) {
                    String[] entry = pair.split("=");
                    ret.put(entry[0].trim(), entry[1].trim());
                }
                return ret;
            }
        }

        return null;
    }

    /**
     * Converts a language ID to the string it refers to
     * @param id - the desired language ID
     * @param c - used solely for the purpose of having strings.xml available for use
     * @return - Language String or null if it wasn't found
     */
    public static String getLanguageFromId(int id, Context c) {
        switch(id) {
            case(constants.LANGUAGE_ID_GERMAN):
                return " " + c.getResources().getString(R.string.GER);
            case(constants.LANGUAGE_ID_TURKISH):
                return " " + c.getResources().getString(R.string.TRK);
            case(constants.LANGUAGE_ID_FRENCH):
                return " " + c.getResources().getString(R.string.FR);
            case(constants.LANGUAGE_ID_SPANISH):
                return " " + c.getResources().getString(R.string.SPN);
            case(constants.LANGUAGE_ID_CHINESE_SIMPLIFIED):
                return " " + c.getResources().getString(R.string.CHNs);
            case(constants.LANGUAGE_ID_CHINESE_TRADITIONAL):
                return " " + c.getResources().getString(R.string.CHNt);
            case(constants.LANGUAGE_ID_JAPANESE):
                return " " + c.getResources().getString(R.string.JPN);
            case(constants.LANGUAGE_ID_KOREAN):
                return " " + c.getResources().getString(R.string.KOR);
            case(constants.LANGUAGE_ID_ENGLISH):
                return " " + c.getResources().getString(R.string.ENG);
            case(constants.LANGUAGE_ID_RUSSIAN):
                return " " + c.getResources().getString(R.string.RUS);
            default:
                return null;
        }
    }

    /**
     * Returns the resource ID of an image
     * @param img
     * @return
     */
    public static int getImageResourceId(String img) {
        switch(img) {
            case ("test_avatar"):
                return R.drawable.test_avatar;
            default:
                return R.drawable.default_avatar;

        }
    }

    public static Intent getIntentFromId(int id, Context c) {
        Intent ret;
        switch(id) {
            case(constants.PRACTICE_LEVEL_SELECT_ID):
                ret = new Intent(c, PracticeLevelSelectActivity.class);
                break;
            default:
                ret = new Intent(c, MainMenuActivity.class);
                break;
        }

        return ret;
    }

    public static HashMap<String, String> editProfileStats(String[] keys, String[] newValues, HashMap<String, String> profile) {
        for (int i = 0; i < keys.length; i++) {
            profile.put(keys[i], newValues[i]);
        }

        return profile;
    }
}

