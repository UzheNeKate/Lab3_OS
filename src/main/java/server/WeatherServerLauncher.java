package server;

import request.GetWeatherProcessor;

import java.net.ServerSocket;
import java.net.Socket;

public class WeatherServerLauncher {
    public static void launch() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(8080);
            while (true) {
                Socket socket = serverSocket.accept();
                var server = new SocketServer(socket);
                server.setGetProcessor(new GetWeatherProcessor());
                server.run();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
