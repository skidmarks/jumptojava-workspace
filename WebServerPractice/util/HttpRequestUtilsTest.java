package util;

import java.util.Map;

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
    public void getPath_쿼리스트링을_포함한_URL을_추출한다() {
        String requestLine = "GET /user/create?name=hong HTTP/1.1";
        assertEquals("/user/create?name=hong", HttpRequestUtils.getPath(requestLine));
    }

    @Test
    public void parseQueryString_여러_파라미터를_맵으로_파싱한다() {
        String queryString = "userId=javajigi&password=password&name=JaeSung&email=javajigi%40slipp.net";
        Map<String, String> params = HttpRequestUtils.parseQueryString(queryString);

        assertEquals("javajigi", params.get("userId"));
        assertEquals("password", params.get("password"));
        assertEquals("JaeSung", params.get("name"));
        assertEquals("javajigi%40slipp.net", params.get("email"));
    }

    @Test
    public void parseQueryString_파라미터가_하나여도_파싱한다() {
        Map<String, String> params = HttpRequestUtils.parseQueryString("userId=javajigi");
        assertEquals("javajigi", params.get("userId"));
    }

    @Test
    public void parseQueryString_빈_문자열이면_빈_맵을_반환한다() {
        assertEquals(0, HttpRequestUtils.parseQueryString("").size());
    }
}
