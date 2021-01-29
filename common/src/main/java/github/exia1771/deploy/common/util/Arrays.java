package github.exia1771.deploy.common.util;

import java.util.stream.Stream;

public abstract class Arrays {

    public static String join(String[] array, String delimiter) {

        StringBuilder stringBuilder = new StringBuilder();
        Stream.of(array).forEach(s -> {
            stringBuilder.append(s).append(delimiter);
        });

        return stringBuilder.toString();
    }

}
