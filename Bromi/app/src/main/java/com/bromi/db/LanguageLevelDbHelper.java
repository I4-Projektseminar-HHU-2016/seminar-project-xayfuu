package com.bromi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.bromi.util.constants;

import java.util.HashMap;

/**
 * http://www.tutorialspoint.com/android/android_sqlite_database.htm
 * https://developer.android.com/training/basics/data-storage/databases.html
 */
public class LanguageLevelDbHelper extends SQLiteOpenHelper {

    public static class Entries implements BaseColumns {

        public static final String TABLE_GERMAN = "german";
        // public static final String TABLE_WRONG_SET_OF_WORDS = "wrong words";
        public static final String COLUMN_LEVEL_ID = "level_id";
        public static final String COLUMN_QUESTION_TYPE_ID = "question_type";
        public static final String COLUMN_FOREIGN_WORD = "foreign_word";
        public static final String COLUMN_TRANSLATED_WORD = "answer_word";
        // public static final String COLUMN_WRONG_WORD = "wrong";
    }

    public static final String DATABASE_NAME = "LanguageLevels.db";
    public static final String TEXT_TYPE = " TEXT";
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String COMMA_SEP = ",";

    // The SQL command for creating the table "german" with a bunch of columns
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXIST" + Entries.TABLE_GERMAN + " (" + Entries._ID + " INTEGER PRIMARY KEY" + COMMA_SEP + Entries.COLUMN_LEVEL_ID + INTEGER_TYPE + COMMA_SEP + Entries.COLUMN_QUESTION_TYPE_ID + TEXT_TYPE + COMMA_SEP + Entries.COLUMN_FOREIGN_WORD + TEXT_TYPE + COMMA_SEP + Entries.COLUMN_TRANSLATED_WORD + TEXT_TYPE + ")";

    // Delete command
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Entries.TABLE_GERMAN;

    public static final int DATABASE_VERSION = 1;

    // Constructor
    public LanguageLevelDbHelper(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        onUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }

    // Insert level data into the database
    public void insertLevelData() {
        SQLiteDatabase db = this.getReadableDatabase();

        for (int i = 0; i < LanguageLevelData.level_count; i++) {
            for (int j = 0; j < LanguageLevelData.level_length; j++) {
                db.execSQL("INSERT INTO " + Entries.TABLE_GERMAN + " (" + Entries.COLUMN_LEVEL_ID + ", " + Entries.COLUMN_FOREIGN_WORD + ", " + Entries.COLUMN_TRANSLATED_WORD + ") " + "VALUES (" + i + ", '" + LanguageLevelData.levels_german[i][j] + "', '" + LanguageLevelData.levels_english[i][j] + "');");
            }
        }
    }

    // Get a level from the databse by levelId
    // Note: First level has levelId 0, second level has levelId 1, third has LevelId 2 and so on
    public Cursor getLevel(int levelId) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("SELECT " + Entries.COLUMN_FOREIGN_WORD + "," + Entries.COLUMN_TRANSLATED_WORD + " FROM " + Entries.TABLE_GERMAN + " WHERE " + Entries.COLUMN_LEVEL_ID + " = ?", new String[] { String.valueOf(levelId) } );
    }

    // Return a row from a Cursor object (of e.g. getLevel()) as a HashMap
    public HashMap<String, String> readWordPairsAsMap(Cursor c) {
        HashMap<String, String> pairs = new HashMap<>();

        if (c.moveToFirst()) {
            do {

                String foreign_word = c.getString(c.getColumnIndex(Entries.COLUMN_FOREIGN_WORD));
                String translated_word = c.getString(c.getColumnIndex(Entries.COLUMN_TRANSLATED_WORD));

                pairs.put(foreign_word, translated_word);

            } while (c.moveToNext());
        }

        return pairs;
    }
}
