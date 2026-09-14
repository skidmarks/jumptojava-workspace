package webserver;

import java.lang.System.Logger;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    private static final Logger log = System.getLogger(WebServer.class.getName());
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        }else {
            port = Integer.parseInt(args[0]);
        }

        // 서버 소켓을 생성한다. 웹 서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.log(Logger.Level.INFO, "Web Application Server started " + port + " port.");

            // 클라이언트가 연결될 때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                RequestHandler requestHandler = new RequestHandler(connection);
                new Thread(requestHandler).start();
            }
        }
    }
}
