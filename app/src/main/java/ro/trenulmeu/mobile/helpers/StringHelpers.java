package ro.trenulmeu.mobile.helpers;

/**
 * Collection of Helper methods for String manipulation.
 */
public class StringHelpers {

    public static String normalize(String in) {
        return in.toLowerCase()
                .replaceAll(".+ăâá", "a")
                .replaceAll("é", "e")
                .replaceAll(".+îí", "i")
                .replaceAll("ş", "s")
                .replaceAll(".+öóő", "o")
                .replaceAll(".+üúű", "u")
                .replaceAll("ţ", "t");
    }

    /**
     * Static Class.
     */
    private StringHelpers() {}

}
