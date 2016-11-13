package com.bromi.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class LanguageDbAdapter {

    private final Context mContext;
    private SQLiteDatabase mDataBase;
    private LanguageDbHelper mDataBaseHelper;

    private final String DEFAULT_TABLE_NAME = LanguageDbHelper.Entries.TABLE_ENGLISH;
    public static final String ID = LanguageDbHelper.Entries.COLUMN_WORD_ID;
    public static final String ENGLISH_WORD = LanguageDbHelper.Entries.COLUMN_WORD;
    public static final String ENGLISH_DEFINITION = LanguageDbHelper.Entries.COLUMN_WORD_DEFINITION;
    public static final String GERMAN_TRANSLATION = LanguageDbHelper.Entries.COLUMN_WORD_TRANSLATION_GERMAN;

    public LanguageDbAdapter(Context c) {
        mContext = c;
        mDataBaseHelper = new LanguageDbHelper(c);
    }

    public LanguageDbAdapter createDatabase() throws SQLException {
        mDataBaseHelper.createDataBase();

        return this;
    }

    public LanguageDbAdapter open() throws SQLException {
        mDataBaseHelper.openDatabase();
        mDataBase = mDataBaseHelper.getReadableDatabase();

        return this;
    }

    public ArrayList<String> getAllWords() {
        String[] projection = {ENGLISH_WORD};
        Cursor c = mDataBase.query(DEFAULT_TABLE_NAME, projection, null, null, null, null, null);
        ArrayList<String> ret = new ArrayList<>();

        if (c.moveToFirst()) {

            while (!c.isAfterLast()) {
                String word = c.getString(c.getColumnIndex(ENGLISH_WORD));
                ret.add(word);
                c.moveToNext();
            }
        }

        return ret;
    }

    public ArrayList<String> getAllDefinitions() {
        String[] projection = {ENGLISH_DEFINITION};
        Cursor c = mDataBase.query(DEFAULT_TABLE_NAME, projection, null, null, null, null, null);
        ArrayList<String> ret = new ArrayList<>();

        if (c.moveToFirst()) {

            while (!c.isAfterLast()) {
                String word = c.getString(c.getColumnIndex(ENGLISH_DEFINITION));
                ret.add(word);
                c.moveToNext();
            }
        }

        return ret;
    }

    public ArrayList<String> getAllTranslatedWords(String column) {
        String[] projection = {column};
        Cursor c = mDataBase.query(DEFAULT_TABLE_NAME, projection, null, null, null, null, null);
        ArrayList<String> ret = new ArrayList<>();

        if (c.moveToFirst()) {

            while (!c.isAfterLast()) {
                String word = c.getString(c.getColumnIndex(column));
                ret.add(word);
                c.moveToNext();
            }
        }

        return ret;
    }

    /**
     * Get one word from a word dictionary
     * @param indx: 0 <= indx <= Table.length - 1
     * @return Word, "" if nothing was found
     */
    public String getWordAt(int indx) {
        String ret = "";
        Cursor c = mDataBase.rawQuery("SELECT * FROM " + DEFAULT_TABLE_NAME + " WHERE " + ID + " = ?", new String[] {String.valueOf(indx)});

        if (c.moveToFirst()) {
            ret = c.getString(c.getColumnIndex(ENGLISH_WORD));
        }

        return ret;
    }

    /**
     * Get range of words (replaces getLevel() method)
     * @param min: minimum 0
     * @param max: maximum LanguageData.english_dictionary.length - 1
     * @return Word ArrayList, ArrayList will be empty if nothing was found
     */
    public ArrayList<String> getWordsFromRange(int min, int max) {
        ArrayList<String > ret = new ArrayList<>();
        Cursor c = mDataBase.rawQuery("SELECT * FROM " + DEFAULT_TABLE_NAME + " limit " + String.valueOf(min) + ", " + String.valueOf(max), null);

        if (c.moveToFirst()) {

            while (!c.isAfterLast()) {
                String word = c.getString(c.getColumnIndex(ENGLISH_WORD));
                ret.add(word);
                c.moveToNext();
            }
        }

        return ret;
    }

    public String getDefinitionAt(int indx) {
        String ret = "";
        Cursor c = mDataBase.rawQuery("SELECT * FROM " + DEFAULT_TABLE_NAME + " WHERE " + ID + " = ?", new String[] {String.valueOf(indx)});

        if (c.moveToFirst()) {
            ret = c.getString(c.getColumnIndex(ENGLISH_DEFINITION));
        }

        return ret;
    }

    public ArrayList<String> getDefinitionsFromRange(int min, int max) {
        ArrayList<String > ret = new ArrayList<>();
        Cursor c = mDataBase.rawQuery("SELECT * FROM " + DEFAULT_TABLE_NAME + " LIMIT " + String.valueOf(min) + ", " + String.valueOf(max), null);

        if (c.moveToFirst()) {

            while (!c.isAfterLast()) {
                String word = c.getString(c.getColumnIndex(ENGLISH_DEFINITION));
                ret.add(word);
                c.moveToNext();
            }
        }

        return ret;

    }

    public String getDefinitionOf(String englishWord) {
        String ret = "";
        Cursor c = mDataBase.rawQuery("SELECT * FROM " + DEFAULT_TABLE_NAME + " WHERE " + ENGLISH_DEFINITION + " = ?", new String[] {englishWord});

        if (c.moveToFirst()) {
            ret = c.getString(c.getColumnIndex(ENGLISH_DEFINITION));
        }

        return ret;
    }

    /**
     * Get Translation to an English word within ENGLISH TABLE from TRANSLATION column (via foreign key column)
     *
     * maximum ID = LanguageData.array.length - 1
     * minimum ID = 0
     */
    public String getTranslationOf(String englishWord, String translateTo) {
        String ret = "";
        Cursor c = mDataBase.rawQuery("SELECT * FROM " + DEFAULT_TABLE_NAME + " WHERE " + ENGLISH_WORD + " = ?", new String[] {englishWord});

        if (c.moveToFirst()) {
            ret = c.getString(c.getColumnIndex(translateTo));
        }

        return ret;
    }

    public ArrayList<String> getTranslationsFromRange(int min, int max, String translateTo) {
        ArrayList<String > ret = new ArrayList<>();
        Cursor c = mDataBase.rawQuery("SELECT * FROM " + DEFAULT_TABLE_NAME + " LIMIT " + String.valueOf(min) + ", " + String.valueOf(max), null);

        if (c.moveToFirst()) {

            while (!c.isAfterLast()) {
                String word = c.getString(c.getColumnIndex(translateTo));
                ret.add(word);
                c.moveToNext();
            }
        }

        return ret;
    }

    public boolean dataBaseExists() {
        return mDataBaseHelper.databaseExists(mContext);
    }

    public void runTest() {
        //Random r = new Random();

        //ENGLISH TABLE TESTS

        /**
        System.out.println(getAllWords(LanguageDbHelper.Entries.TABLE_ENGLISH).toString());
        System.out.println(getAllDefinitions(LanguageDbHelper.Entries.TABLE_ENGLISH).toString());
        System.out.println(getWordAt(r.nextInt((LanguageData.english_dictionary.length - 0) + 1), LanguageDbHelper.Entries.TABLE_ENGLISH));
        System.out.println(getWordAt(0, LanguageDbHelper.Entries.TABLE_ENGLISH));
        System.out.println(getWordAt(LanguageData.english_dictionary.length-1, LanguageDbHelper.Entries.TABLE_ENGLISH));
        System.out.println(getDefinitionAt(0, LanguageDbHelper.Entries.TABLE_ENGLISH));
        System.out.println(getDefinitionAt(LanguageData.english_dictionary.length-1, LanguageDbHelper.Entries.TABLE_ENGLISH));
        System.out.println(getWordsFromRange(0, 20, LanguageDbHelper.Entries.TABLE_ENGLISH));
        System.out.println(getDefinitionsFromRange(0, 20, LanguageDbHelper.Entries.TABLE_ENGLISH));
        System.out.println(getDefinitionOf(getWordAt(0, LanguageDbHelper.Entries.TABLE_ENGLISH), LanguageDbHelper.Entries.TABLE_ENGLISH));
         */


        //GERMAN COLUMN TESTS

        /**
        System.out.println(getAllTranslatedWords(GERMAN_TRANSLATION));
        System.out.println(getTranslationOf(getWordAt(3), GERMAN_TRANSLATION));
        System.out.println(getTranslationOf(getWordAt(LanguageData.german_translations.length-1), GERMAN_TRANSLATION));
        System.out.println(getTranslationsFromRange(0, 10, GERMAN_TRANSLATION).toString());
         */
    }
}
