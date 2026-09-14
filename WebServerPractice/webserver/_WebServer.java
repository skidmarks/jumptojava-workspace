package webserver;

import java.net.ServerSocket;
import java.net.Socket;

public class _WebServer {
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {
        int port = (args == null || args.length == 0) ? DEFAULT_PORT : Integer.parseInt(args[0]);

        try (ServerSocket listenSocket = new ServerSocket(port)) {
            System.out.println("Web Application Server started " + port + " port.");

            while (true) {
                Socket connection = listenSocket.accept();
                Thread thread = new Thread(new RequestHandler(connection));
                thread.start();
            }
        }
    }
}
