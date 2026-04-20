package utils;

import java.util.HashMap;
import java.util.Map;

public class LanguageManager {

    public enum Language { EN, NO }

    private static Language currentLanguage = Language.EN;

    private static final Map<String, String> EN = new HashMap<>();
    private static final Map<String, String> NO = new HashMap<>();

    static {
        // Settings
        EN.put("settings",  "Settings");
        EN.put("theme",     "Theme");
        EN.put("language",  "Language");
        EN.put("close",     "Close");
        EN.put("english",     "English");
        EN.put("norwegian",     "Norwegian");

        NO.put("settings",  "Innstillinger");
        NO.put("theme",     "Tema");
        NO.put("language",  "Språk");
        NO.put("close",     "Lukk");
        NO.put("english", "Engelsk");
        NO.put("norwegian",     "Norsk");

        // DriverTripPanel
        EN.put("no_active_deliveries", "No active deliveries");
        EN.put("min_left",             "min left");
        EN.put("min_late",             "min late");
        EN.put("preorderFor:", "Preorder for: ");

        NO.put("no_active_deliveries", "Ingen aktive leveringer");
        NO.put("min_left",             "min igjen");
        NO.put("min_late",             "min forsinket");
        NO.put("preorderFor:", "Forordre til: ");


        // Driver statuses
        EN.put("AVAILABLE",    "Available");
        EN.put("ON_TRIP",      "On a trip");
        EN.put("COMING_BACK",  "Coming back");
        EN.put("ON_BREAK",     "On break");

        NO.put("AVAILABLE",    "Tilgjengelig");
        NO.put("ON_TRIP",      "På tur");
        NO.put("COMING_BACK",  "På vei tilbake");
        NO.put("ON_BREAK",     "På pause");

        // DeliveryListPanel
        EN.put("in_transit",  "In Transit");
        EN.put("waiting",     "Waiting");
        EN.put("unassigned",  "Unassigned");

        NO.put("in_transit",  "Under levering");
        NO.put("waiting",     "Venter");
        NO.put("unassigned",  "Ikke tildelt");

        //
    }

    public static void setLanguage(Language lang) {
        currentLanguage = lang;
    }

    public static Language getLanguage() {
        return currentLanguage;
    }

    public static String get(String key) {
        Map<String, String> map = currentLanguage == Language.NO ? NO : EN;
        return map.getOrDefault(key, key); // falls back to the key itself if missing
    }
}