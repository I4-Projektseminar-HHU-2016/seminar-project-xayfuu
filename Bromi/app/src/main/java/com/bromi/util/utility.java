package com.bromi.util;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;

public class utility {

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
     * Creates a Profile. Data must be gathered beforehand for this to work.
     * @param name - the name a user entered
     * @param country - the country a user entered
     * @param gender - the gender a user entered
     * @return the JSONArray
     * @throws JSONException
     */
    public static JSONArray createProfileJSONObject(String name, String country, String gender) throws JSONException {
        JSONArray data = new JSONArray();
        JSONObject profile = new JSONObject();
        profile.put("name", name);
        profile.put("gender", gender);
        profile.put("country", country);
        data.put(profile);

        return data;
    }

    /**
     * Reads a Profile from a JSON Buffered String
     * @param buffered - the String created upon reading a JSON String from the device's internal storage beforehand
     * @param baseArray - the JSONArray itself
     * @return a HashMap with all elements of the JSONObject as key;value pairs
     * @throws JSONException
     */
    public static HashMap<String, String> readProfileFromJSONBuffer(StringBuilder buffered, JSONArray baseArray) throws JSONException {

        // http://stackoverflow.com/questions/4307118/jsonarray-to-hashmap
        JSONArray readData = new JSONArray(buffered.toString());
        HashMap<String, String> profileData = new HashMap<>();

        for (int i = 0; i < baseArray.length(); i++) {
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
             */
        }

        return profileData;
    }
}

