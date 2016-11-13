package com.bromi.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.io.File;

/**
 * http://www.tutorialspoint.com/android/android_sqlite_database.htm
 * https://developer.android.com/training/basics/data-storage/databases.html
 */
public class LanguageDbHelper extends SQLiteOpenHelper {

    /**
     * Inner Class used to gain access to _ID via BaseColumns interface
     * - Includes all necessary column names of the DB
     */
    public static class Entries implements BaseColumns {

        public static final String TABLE_ENGLISH = "English";
        public static final String COLUMN_WORD_ID = "word_id";
        public static final String COLUMN_WORD = "word";
        public static final String COLUMN_WORD_DEFINITION = "word_definition";
        public static final String COLUMN_WORD_TRANSLATION_GERMAN = "german_translation";
    }

    public static final String DATABASE_NAME = "LanguageDictionary.db";
    public static final String TEXT_TYPE = " TEXT";
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String COMMA_SEP = ",";

    private SQLiteDatabase mDataBase;
    private File mDataBasePath;
    private final Context mContext;

    /**
     * The SQLite command for creating the table "german" with a bunch of columns
     */
    public static final String SQL_CREATE_ENTRIES_ENGLISH =
            "CREATE TABLE IF NOT EXISTS " + Entries.TABLE_ENGLISH
                    + " ("
                    + Entries._ID + " INTEGER PRIMARY KEY" + COMMA_SEP
                    + Entries.COLUMN_WORD_ID + INTEGER_TYPE + COMMA_SEP
                    + Entries.COLUMN_WORD + TEXT_TYPE + COMMA_SEP
                    + Entries.COLUMN_WORD_DEFINITION + TEXT_TYPE + COMMA_SEP
                    + Entries.COLUMN_WORD_TRANSLATION_GERMAN + TEXT_TYPE
                    + ");";

    /**
     * SQLite delete command as suggested by Google Android Docs (never used)
      */
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Entries.TABLE_ENGLISH;

    public static final int DATABASE_VERSION = 1;

    /**
     * Constructor
      */
    public LanguageDbHelper(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
        mDataBasePath = c.getDatabasePath(DATABASE_NAME);
        mContext = c;
    }

    /**
     * Create database
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES_ENGLISH);
    }

    /**
     * As suggested by Google Android Docs
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    /**
     * As suggested by Google Android Docs
     */
    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        onUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }

    /**
     * Insert level data into the database
     */
    public void createDataBase() {
        if (!databaseExists(mContext)) {

            SQLiteDatabase db= this.getWritableDatabase();
            int iterationsForTranslations = 0;
            String query;

            for (int set_num = 0; set_num < LanguageData.english_dictionary.length; set_num++) {
                iterationsForTranslations++;

                if (iterationsForTranslations < LanguageData.german_translations.length + 1) {
                    query = "INSERT INTO " + Entries.TABLE_ENGLISH
                            + " (" + Entries.COLUMN_WORD_ID + ", "
                            + Entries.COLUMN_WORD + ", "
                            + Entries.COLUMN_WORD_DEFINITION + ", "
                            + Entries.COLUMN_WORD_TRANSLATION_GERMAN + ") "
                            + "VALUES (" + set_num + ", \"" + LanguageData.english_dictionary[set_num][0] + "\", \"" + LanguageData.english_dictionary[set_num][1] + "\", \"" + LanguageData.german_translations[iterationsForTranslations - 1] + "\");";
                }
                else {
                    query = "INSERT INTO " + Entries.TABLE_ENGLISH
                            + " (" + Entries.COLUMN_WORD_ID + ", "
                            + Entries.COLUMN_WORD + ", "
                            + Entries.COLUMN_WORD_DEFINITION + ", "
                            + Entries.COLUMN_WORD_TRANSLATION_GERMAN + ") "
                            + "VALUES (" + set_num + ", \"" + LanguageData.english_dictionary[set_num][0] + "\", \"" + LanguageData.english_dictionary[set_num][1] + "\", \"None\");";
                }
                db.execSQL(query);
            }
        }
    }

    public boolean openDatabase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase(mDataBasePath.toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    public boolean databaseExists(Context c) {
        File dbFile = c.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }

}
