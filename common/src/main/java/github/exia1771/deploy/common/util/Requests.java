package github.exia1771.deploy.common.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Requests {

    private static final String EQUAL_DELIMITER = "=";
    private static final String CONCAT_DELIMITER = "&";

    public static String concat(String urlPrefix, Map<String, Object> params, Set<String> excluded) {
        StringBuilder builder = new StringBuilder(urlPrefix);
        boolean isConcat = false;

        Set<String> keySet = params.keySet();
        if (keySet.size() != 0) {
            isConcat = true;
        }

        for (String key : keySet) {
            Object v = params.get(key);
            if (v == null || excluded.contains(key)) {
                continue;
            }
            builder.append(key).append(EQUAL_DELIMITER).append(v).append(CONCAT_DELIMITER);
        }

        if (isConcat) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public static String concat(String urlPrefix, Map<String, Object> params) {
        return concat(urlPrefix, params, new HashSet<>());
    }


    public static String concat(String urlPrefix, String... other) {
        StringBuilder stringBuilder = new StringBuilder(urlPrefix);
        boolean isConcat = false;
        for (String s : other) {
            if (s == null) {
                continue;
            }
            if (!isConcat) {
                isConcat = true;
            }
            stringBuilder.append(CONCAT_DELIMITER).append(s);
        }

        return stringBuilder.toString();
    }
}
