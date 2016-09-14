package com.bromi.util;

public class constants {

    // JSON profile data filename on device's storage
    public static final String PROFILE_DATA_FILENAME = "PROFILE";

    // JSON Object names for profile
    public static final String PROFILE_NAME = "name";
    public static final String PROFILE_GENDER = "gender";
    public static final String PROFILE_COUNTRY = "country";
    public static final String PROFILE_AVATAR = "avatar";
    public static final String STAT_LEVELS_DONE = "levels_done";
    public static final String STAT_VOCABULARIES_DONE = "vocabularies_done";
    public static final String STAT_CORRECT_VOCABULARIES = "correct_vocabularies";
    public static final String STAT_WRONG_VOCABULARIES = "wrong_vocabularies";
    public static final String STAT_USER_EXPERIENCE = "experience_points";
    public static final String STAT_USER_LEVEL = "user_level";

    // Android bundle names
    public static final String BUNDLE_PROFILE = "profileData";
    public static final String BUNDLE_MODE_ID = "modeId";
    public static final String BUNDLE_LEVEL_ID = "levelId";
    public static final String BUNDLE_LANGUAGE_ID = "languageId";
    public static final String BUNDLE_IS_NEW_LEVEL = "isNewLevel";
    public static final String BUNDLE_OPENED_FROM = "openedFrom";

    // Activity IDs used to identify what activity/menu to return to if a quit button is pressed on an activity that can be accesses from multiple instances (e.g. profile)
    public static final int MAIN_MENU_ID = 0;
    public static final int PRACTICE_LEVEL_SELECT_ID = 1;

    // EXP thresholds and values
    public static final int EXP_FOR_CORRECT_ANSWER = 10;
    public static final int EXP_FOR_ALL_CORRECT = 50;
    public static final int EXP_FOR_COMPLETING_LEVEL = 10;
    public static final int EXP_REQUIRED_FOR_ONE_LEVEL = 500;

    // Mode IDs
    public static final int PRACTICE_MODE_ID = 0;
    public static final int CHALLENGE_MODE_ID = 1;
    public static final int ENDLESS_MODE_ID = 2;

    // Language IDs
    public static final int LANGUAGE_ID_ENGLISH = 0;
    public static final int LANGUAGE_ID_GERMAN = 1;
    public static final int LANGUAGE_ID_TURKISH = 2;
    public static final int LANGUAGE_ID_FRENCH = 3;
    public static final int LANGUAGE_ID_SPANISH = 4;
    public static final int LANGUAGE_ID_CHINESE_SIMPLIFIED = 5;
    public static final int LANGUAGE_ID_CHINESE_TRADITIONAL = 6;
    public static final int LANGUAGE_ID_JAPANESE = 7;
    public static final int LANGUAGE_ID_KOREAN = 8;
    public static final int LANGUAGE_ID_RUSSIAN = 9;

    // Other IDs
    public static final int STRING_SIZE_LIMIT = 3;
    public static final int ANSWER_POSSIBILITY_SIZE = 4;
}
