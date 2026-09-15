package util;

public class HttpRequestUtils {

    public static String getPath(String requestLine) {
        String[] tokens = requestLine.split(" ");
        return tokens[1];
    }
}
