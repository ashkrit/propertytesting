package string;

import java.util.StringTokenizer;

public class BetterStringUtil {

    public static String truncate(String value, int len) {
        if (len < 0) {
            return "";
        } else if (len > value.length()) {
            return value;
        }
        return value.substring(0, len) + "...";
    }

    public static boolean contains(String value, String checkValue) {
        return value.indexOf(checkValue) > -1;
    }


    public static String[] tokens(String value, String deliminator) {

        StringTokenizer tokenItr = new StringTokenizer(value, deliminator);
        String[] tokens = new String[tokenItr.countTokens()];
        int index = 0;
        while (tokenItr.hasMoreTokens()) {
            tokens[index++] = tokenItr.nextToken();
        }
        return tokens;
    }
}
