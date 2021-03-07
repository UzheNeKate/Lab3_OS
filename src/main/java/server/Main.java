package server;

import request.WeatherController;

import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(8080);
            while (true) {
                Socket socket = serverSocket.accept();
                var server = new SocketServer(socket);
                server.setGetProcessor(new WeatherController());
                server.run();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
