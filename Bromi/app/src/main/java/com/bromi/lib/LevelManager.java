package com.bromi.lib;

import android.content.Context;

import com.bromi.db.LanguageData;
import com.bromi.db.LanguageDbAdapter;
import com.bromi.util.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class LevelManager {

    /**
     * A "Level" will equal to the range of IDs starting from (levelId * LEVEL_COUNT) and the end of the range being (levelId * LEVEL_COUNT) + LEVEL_LENGTH.
     * The maximum amount of levels is thus LanguageData.english_dictionary.length/LEVEL_LENGTH or something
    */
    public static HashMap<String, String> getTranslationLevelAsHashMap(int levelId, String foreignLanguageDbTable, Context c) {
        HashMap<String, String> ret = new HashMap<>();
        ArrayList<String> nativeWords;
        ArrayList<String> foreignWords;

        LanguageDbAdapter db = new LanguageDbAdapter(c);
        db.open();

        int start = (levelId * constants.TRANSLATION_LEVEL_LENGTH) - 1;
        int end = start + constants.TRANSLATION_LEVEL_LENGTH;
        int idx = 0;

        nativeWords = db.getWordsFromRange(start, end);
        foreignWords = db.getTranslationsFromRange(start, end, foreignLanguageDbTable);

        do {
            ret.put(foreignWords.get(idx), nativeWords.get(idx));
            idx++;
        }
        while (idx != constants.TRANSLATION_LEVEL_LENGTH);

        return ret;
    }

    /**
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
     */
}
