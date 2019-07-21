package string;

public class BetterStringUtil {

    public static String truncate(String value, int len) {
        if (len < 0) {
            return "";
        } else if (len > value.length()) {
            return value;
        }
        return value.substring(0, len) + "...";
    }
}
