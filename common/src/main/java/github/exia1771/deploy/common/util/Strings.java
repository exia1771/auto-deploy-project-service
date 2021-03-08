package github.exia1771.deploy.common.util;

public abstract class Strings {

    public static Boolean isLengthBetween(String str, int min, int max) {
        int length = 0;
        if (str == null) {
            return length >= min;
        } else {
            length = str.length();
            return min <= length && length <= max;
        }
    }

}
