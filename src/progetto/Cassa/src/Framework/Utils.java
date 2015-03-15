package Framework;

/**
 *
 * @author alex
 */
public class Utils {

    public static boolean dangerousName(String festivalName) {
        // divide la stringa ogni volta che incontra un carattere che non è una lettera, un digit, uno spazio oppure un _
        return festivalName.split("[^a-zA-Z0-9_]|\\S").length != 0;
    }

    public static String underscore(String festivalName) {
        return festivalName.trim().replaceAll("\\s+", "_");
    }

    public static String deUnderscore(String festivalName) {
        return festivalName.replaceAll("_", " ");
    }

}
