package com.bromi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class LogInActivity extends AppCompatActivity {

    String test = "This is some test text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        /**
        FileOutputStream fos = null;
        fos = openFileOutput(PROFILE_DATA_FILENAME, Context.MODE_PRIVATE);
        fos.write(test.getBytes());
        fos.close();
         **/

    }

    private void checkProfiles() {

        /**
         FileInputStream fis = null;
         int bytes;
         String content = "";

         fis = openFileInput(PROFILE_DATA_FILENAME);
         while ((bytes = fis.read()) != -1) {
         content = content + Character.toString((char) bytes);
         }
         fis.close();

         System.out.println(content);
         **/
    }
}
