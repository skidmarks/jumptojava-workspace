package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {

    private static final Logger log = System.getLogger(RequestHandler.class.getName());
    private static final String WEBAPP_PATH = "./webapp";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.log(Level.DEBUG, "New Client connnect! connected IP : {0}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String requestLine = br.readLine();
            if (requestLine == null) {
                return;
            }
            log.log(Level.DEBUG, requestLine);

            String method = requestLine.split(" ")[0];

            Map<String, String> headers = new HashMap<>();
            String line = requestLine;
            while (!"".equals(line)) {
                line = br.readLine();
                if (line == null) {
                    return;
                }
                log.log(Level.DEBUG, line);
                int colonIndex = line.indexOf(": ");
                if (colonIndex != -1) {
                    headers.put(line.substring(0, colonIndex), line.substring(colonIndex + 2));
                }
            }

            String url = HttpRequestUtils.getPath(requestLine);
            int index = url.indexOf("?");
            String path = index == -1 ? url : url.substring(0, index);

            if ("/user/create".equals(path)) {
                String queryString;
                if ("POST".equals(method)) {
                    int contentLength = Integer.parseInt(headers.get("Content-Length"));
                    queryString = IOUtils.readData(br, contentLength);
                } else {
                    queryString = url.substring(index + 1);
                }
                Map<String, String> params = HttpRequestUtils.parseQueryString(queryString);
                User user = new User(params.get("userId"), params.get("password"), params.get("name"),
                        params.get("email"));
                DataBase.addUser(user);
                log.log(Level.INFO, "회원가입: " + user);

                DataOutputStream dos = new DataOutputStream(out);
                response302Header(dos, "/index.html");
                return;
            }

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = "/user/list".equals(path)
                    ? createUserListHtml().getBytes(StandardCharsets.UTF_8)
                    : Files.readAllBytes(Path.of(WEBAPP_PATH, path));
            response200Header(dos, body.length);
            responseBody(dos, body);

        } catch (IOException e) {
            log.log(Level.ERROR, e.getMessage());
        }
    }

    private String createUserListHtml() {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset=\"utf-8\"><title>회원목록</title></head><body>");
        html.append("<h1>회원목록</h1>");
        html.append("<table border=\"1\"><tr><th>아이디</th><th>이름</th><th>이메일</th></tr>");
        for (User user : DataBase.findAll()) {
            html.append("<tr><td>").append(user.getUserId()).append("</td><td>")
                    .append(user.getName()).append("</td><td>")
                    .append(user.getEmail()).append("</td></tr>");
        }
        html.append("</table></body></html>");
        return html.toString();
    }

    private void response302Header(DataOutputStream dos, String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Location: " + location + "\r\n");
            dos.writeBytes("\r\n");

        } catch (IOException e) {
            log.log(Level.ERROR, e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {

        try {
            dos.writeBytes("HTTP/1.1 200 OK\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");

        } catch (IOException e) {
            log.log(Level.ERROR, e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.log(Level.ERROR, e.getMessage());
        }
    }
}
