package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RequestHandler implements Runnable {
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        System.out.println("New Client Connect! Connected IP : " + connection.getInetAddress()
                + ", Port : " + connection.getPort());

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
             OutputStream out = connection.getOutputStream()) {

            String line = br.readLine();
            if (line == null) {
                return;
            }
            System.out.println("request line : " + line);

            while (!(line = br.readLine()).isEmpty()) {
                System.out.println("header : " + line);
            }

            byte[] body = "<h1>Hello florakid!너</h1>".getBytes(StandardCharsets.UTF_8);
            response200Header(out, body.length);
            responseBody(out, body);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void response200Header(OutputStream out, int lengthOfBodyContent) throws IOException {
        out.write("HTTP/1.1 200 OK \r\n".getBytes());
        out.write("Content-Type: text/html;charset=utf-8\r\n".getBytes());
        out.write(("Content-Length: " + lengthOfBodyContent + "\r\n").getBytes());
        out.write("\r\n".getBytes());
    }

    private void responseBody(OutputStream out, byte[] body) throws IOException {
        out.write(body);
        out.flush();
    }
}
