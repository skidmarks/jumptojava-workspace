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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

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
                log.log(Level.INFO, "회원가입: " + user);
                path = "/index.html";
            }

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(Path.of(WEBAPP_PATH, path));
            response200Header(dos, body.length);
            responseBody(dos, body);

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
