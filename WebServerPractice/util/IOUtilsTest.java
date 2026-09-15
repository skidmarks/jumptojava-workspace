package util;

import java.io.BufferedReader;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IOUtilsTest {

    @Test
    public void readData_지정한_길이만큼_본문을_읽는다() throws Exception {
        String body = "userId=javajigi&password=password&name=JaeSung&email=javajigi%40slipp.net";
        BufferedReader br = new BufferedReader(new StringReader(body));

        assertEquals(body, IOUtils.readData(br, body.length()));
    }

    @Test
    public void readData_길이만큼만_읽고_나머지는_남겨둔다() throws Exception {
        BufferedReader br = new BufferedReader(new StringReader("userId=javajigi&extra"));

        assertEquals("userId=javajigi", IOUtils.readData(br, "userId=javajigi".length()));
    }
}
