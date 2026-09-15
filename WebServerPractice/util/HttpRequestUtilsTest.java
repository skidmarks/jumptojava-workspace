package util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpRequestUtilsTest {

    @Test
    public void getPath_요청라인에서_경로를_추출한다() {
        String requestLine = "GET /index.html HTTP/1.1";
        assertEquals("/index.html", HttpRequestUtils.getPath(requestLine));
    }

    @Test
    public void getPath_루트_경로도_추출한다() {
        String requestLine = "GET / HTTP/1.1";
        assertEquals("/", HttpRequestUtils.getPath(requestLine));
    }

    @Test
    public void getPath_쿼리스트링이_있어도_경로만_추출한다() {
        String requestLine = "GET /user/create?name=hong HTTP/1.1";
        assertEquals("/user/create?name=hong", HttpRequestUtils.getPath(requestLine));
    }
}
