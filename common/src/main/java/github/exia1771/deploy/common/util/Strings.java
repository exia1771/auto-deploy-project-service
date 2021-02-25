package github.exia1771.deploy.common.util;

public abstract class Strings {

    public static Boolean isLengthBetween(String str, int min, int max) {
        String temp = str.trim();
        return temp.length() >= min && temp.length() <= max;
    }

}
