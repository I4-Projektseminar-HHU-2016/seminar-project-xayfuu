package com.bromi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.bromi.util.constants;

/**
 * http://www.tutorialspoint.com/android/android_sqlite_database.htm
 * https://developer.android.com/training/basics/data-storage/databases.html
 */
public class LanguageLevelDbHelper extends SQLiteOpenHelper {

    public static class Entries implements BaseColumns {

        public static final String LANGUAGE_TABLE_NAME = "german";
        public static final String LANGUAGE_TABLE_WRONG_SET_OF_WORDS = "wrong words";
        public static final String LANGUAGE_COLUMN_LEVEL_ID = "level_id";
        public static final String LANGUAGE_COLUMN_QUESTION_TYPE_ID = "question_type";
        public static final String LANGUAGE_COLUMN_FOREIGN_WORD = "foreign_word";
        public static final String LANGUAGE_COLUMN_TRANSLATED_WORD = "answer_word";
        public static final String LANGUAGE_COLUMN_WRONG_WORD = "wrong";
    }

    public static final String DATABASE_NAME = "LanguageLevels.db";
    public static final String TEXT_TYPE = " TEXT";
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Entries.LANGUAGE_TABLE_NAME + " (" +
                    Entries._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    Entries.LANGUAGE_COLUMN_LEVEL_ID + INTEGER_TYPE + COMMA_SEP +
                    Entries.LANGUAGE_COLUMN_QUESTION_TYPE_ID + TEXT_TYPE + COMMA_SEP +
                    Entries.LANGUAGE_COLUMN_FOREIGN_WORD + TEXT_TYPE + COMMA_SEP +
                    Entries.LANGUAGE_COLUMN_TRANSLATED_WORD + TEXT_TYPE + ")";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Entries.LANGUAGE_TABLE_NAME;

    public static final int DATABASE_VERSION = 1;


    public LanguageLevelDbHelper(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

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

    public void insertLevelData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Entries.LANGUAGE_COLUMN_LEVEL_ID, 1);
        cv.put(Entries.LANGUAGE_COLUMN_QUESTION_TYPE_ID, constants.SINGLE_CHOICE_IDENTIFIER);
        cv.put(Entries.LANGUAGE_COLUMN_FOREIGN_WORD, "gut");
        cv.put(Entries.LANGUAGE_COLUMN_TRANSLATED_WORD, "good");

        db.insert(Entries.LANGUAGE_TABLE_NAME, null, cv);
    }

    public Cursor getLevel(int levelId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String [] projection = {Entries._ID, Entries.LANGUAGE_COLUMN_FOREIGN_WORD, Entries.LANGUAGE_COLUMN_TRANSLATED_WORD};

        // Filter results WHERE "title" = 'My Title'
        String selection = Entries.LANGUAGE_COLUMN_LEVEL_ID + " = " + levelId;

        Cursor ret = db.query(
                Entries.LANGUAGE_TABLE_NAME,            // The table to query
                projection,                             // The columns to return
                selection,                              // The columns for the WHERE clause
                null,                                   // The values for the WHERE clause
                null,                                   // Don't group the rows
                null,                                   // Don't filter by row groups
                null                                    // Sort order
        );
        ret.moveToFirst();

        return ret;
    }
}
