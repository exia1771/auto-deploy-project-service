package github.exia1771.deploy.common.util;

import org.apache.http.HttpHost;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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


    public static HttpHost[] parse(String uriList, String delimiter) {
        String[] uris = uriList.split(delimiter);
        HttpHost[] httpHosts = new HttpHost[uris.length];

        for (int i = 0; i < uris.length; i++) {
            httpHosts[i] = parseUri(uris[i]);
        }

        return httpHosts;
    }

    private static HttpHost parseUri(String uri) {
        // 去除URL最后一个/字符
        if (uri.lastIndexOf("/") == (uri.length() - 1)) {
            uri = uri.substring(0, uri.lastIndexOf("/"));
        }
        // 对带有路径前缀主机的解析
        if (uri.lastIndexOf("/") != (uri.indexOf("//") + 1)) {
            String text = uri;
            String scheme = null;
            int schemeIdx = uri.indexOf("://");
            if (schemeIdx > 0) {
                scheme = uri.substring(0, schemeIdx);
                text = uri.substring(schemeIdx + 3);
            }

            int port = -1;
            int portIdx = text.lastIndexOf(":");
            if (portIdx > 0) {
                try {
                    port = Integer.parseInt(text.substring(portIdx + 1));
                } catch (NumberFormatException var7) {
                    throw new IllegalArgumentException("Invalid HTTP host: " + text);
                }
                text = text.substring(0, portIdx);
            }

            if (text.contains("/")) {
                String[] split = text.split("/");
                text = split[0];
            }
            return new HttpHost(text, port, scheme);
        } else {
            return HttpHost.create(uri);
        }
    }

}
