package com.bromi.lib;

import android.content.Context;

import com.bromi.util.constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class ProfileManager {

    public static StringBuilder readProfileAsStringBuilder(Context ctx) throws IOException {
        FileInputStream fis = ctx.openFileInput(constants.PROFILE_DATA_FILENAME);   // read from the internal storage
        BufferedInputStream bis = new BufferedInputStream(fis);
        StringBuilder buffer = new StringBuilder();

        while (bis.available() != 0) {
            char c = (char) bis.read();
            buffer.append(c);
        }

        fis.close();
        bis.close();

        return buffer;
    }

    /**
     * Reads a Profile from a JSON Buffered String
     * @return a HashMap with all elements of the JSONObject as key;value pairs
     * @throws JSONException
     */
    public static HashMap<String, String> readProfileAsHashMap(Context ctx) throws JSONException, IOException {

        StringBuilder buffer = readProfileAsStringBuilder(ctx);

        // http://stackoverflow.com/questions/4307118/jsonarray-to-hashmap
        JSONArray readData = new JSONArray(buffer.toString());
        HashMap<String, String> profileData = new HashMap<>();

        for (int i = 0; i < readData.length(); i++) {
            JSONObject toPut = readData.optJSONObject(i);
            Iterator iter = toPut.keys();

            while (iter.hasNext()) {
                String key = iter.next().toString();
                profileData.put(key, toPut.getString(key));
            }

            /**     Tests
             System.out.println(profileData);
             System.out.println(profileData.size());
             System.out.println(profileData.get("name"));
             System.out.println(profileData.get("gender"));
             System.out.println(profileData.get("country"));
             **/
        }

        return profileData;
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
     *     ...
     *   }
     * ]
     *
     * @param name - the name the user entered
     * @param country - the country the user entered
     * @param gender - the gender the user gave
     * @param c - the Context needed to do operations on the android device
     */
    public static void createProfile(String name, String country, String gender, Context c) throws IOException, JSONException {

        FileOutputStream fos = c.openFileOutput(constants.PROFILE_DATA_FILENAME, Context.MODE_PRIVATE);

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
    }

    /**
     * Creates the JSON Profile object used for the entire app.
     * @param name - the name a user entered
     * @param country - the country a user entered
     * @param gender - the gender a user entered
     * @return the JSONArray
     * @throws JSONException
     */
    private static JSONArray createProfileJSONObject(String name, String country, String gender) throws JSONException {
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
        profile.put(constants.STAT_USER_EXPERIENCE, 0);
        profile.put(constants.STAT_USER_LEVEL, 1);
        data.put(profile);

        return data;
    }

    /**
     * Checks if a profile exists by looking for its name within the file storage of the device
     * @param c - the Context needed to do android operations
     * @return - true if the profile data exists, false if otherwise
     */
    public static boolean profileExists(Context c) {
        String[] files = c.fileList();
        for (String file : files) {
            if (file.equals(constants.PROFILE_DATA_FILENAME)) {
                return true;
            }
        }
        return false;
    }
}
