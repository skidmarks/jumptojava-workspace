package util;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestUtils {

    public static String getPath(String requestLine) {
        String[] tokens = requestLine.split(" ");
        return tokens[1];
    }

    public static Map<String, String> parseQueryString(String queryString) {
        Map<String, String> params = new HashMap<>();
        if (queryString == null || queryString.isEmpty()) {
            return params;
        }

        for (String pair : queryString.split("&")) {
            String[] tokens = pair.split("=");
            if (tokens.length == 2) {
                params.put(tokens[0], tokens[1]);
            }
        }
        return params;
    }
}
