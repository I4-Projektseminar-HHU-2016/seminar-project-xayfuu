package com.bromi.db;

public class LanguageLevelData {

    public static final int level_length = 5;
    public static final int level_count = 8;

    public static final String[][] levels_german = {
            {"gut", "Essen", "Haus", "ich", "Person"},                  //Lvl 1
            {"Polizei", "denken", "sagen", "der Arzt", "mein"},         //Lvl 2
            {"ist", "Maus", "Restaurant", "Krankenhaus", "Name"},       //Lvl 3
            {"sehen", "verstehen", "ja", "gehen", "trinken"},           //Lvl 4
            {"Straße", "links", "rechts", "das Auto", "Straßenbahn"},   //Lvl 5
            {"Computer", "Buch", "Schuhe", "schön", "vielleicht"},      //Lvl 6
            {"Apfel", "Pause", "kaputt", "machen", "Stift"},            //Lvl 7
            {"Danke", "Baum", "grün", "Raum", "Wald"}                   //Lvl 8
    };

    public static final String[][] levels_english = {
            {"good", "food", "house", "I", "person"},                   //Lvl 1
            {"police", "think", "say", "doctor", "mine"},               //Lvl 2
            {"is", "mouse", "restaurant", "hospital", "name"},          //Lvl 3
            {"see", "understand", "yes", "go", "drink"},                //Lvl 4
            {"street", "left", "right", "car", "tram"},                 //Lvl 5
            {"computer", "book", "shoes", "beautiful", "maybe"},        //Lvl 6
            {"apple", "break", "broken", "make", "pen"},                //Lvl 7
            {"thanks", "tree", "green", "room", "forest"}               //Lvl 8
    };

    // Wrong words
    public static final String[] words = {
            "good", "food", "house", "I", "person", "police", "think", "say", "doctor", "mine", "is", "mouse", "restaurant", "hospital", "name", "see", "understand", "yes", "go","drink", "street", "left",
            "right", "car", "tram", "maybe", "use", "large", "water", "epic", "still", "clear", "with", "get", "somewhere", "again", "game", "could", "work", "support", "from", "theme", "support", "look",
            "bottom", "enable", "on", "screen", "where", "who", "when", "how", "that", "then", "this", "way", "it", "he", "she", "exactly", "what", "find", "something", "adapt", "fine", "my", "off", "because",
            "thing", "up", "down", "the", "enter", "do", "normal", "toggle", "back", "shortcut", "tape", "paper", "which", "taxing", "well", "make", "mode", "barely", "anything", "in", "just", "want", "brother",
            "sister", "disable", "information", "element", "expect", "a", "useless", "advice", "possible", "impossible", "bad", "color", "along", "bunch", "text", "only", "such", "much", "many", "try",
            "please", "spam", "hold", "kill", "step", "stair", "star", "tool", "tip", "death", "until", "about", "as", "graph", "at", "least", "render", "update", "rate", "actually", "reality", "difference",
            "loop", "count", "set", "fast", "number", "serious", "all", "of", "those", "cause", "even", "usually", "but", "notice", "crash", "by", "image", "cancel", "cute", "congratulations", "one", "correct",
            "strength", "too", "to", "hand", "foot", "head", "finger", "whack", "sorry", "apologize", "interrupt", "manage", "almost", "throw", "face", "obviously", "match", "here", "alright", "let", "boy", "girl",
            "choke", "computer", "book", "shoes", "beautiful", "apple", "break", "pen", "thanks", "tree", "green", "room", "forest", "broken"
    };
}
