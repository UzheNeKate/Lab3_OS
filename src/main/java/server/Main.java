package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    static ExecutorService pool = Executors.newFixedThreadPool(8);

    public static void main(String[] args) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(8080);
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                var server = new SocketServer(socket);
                pool.submit(server);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
