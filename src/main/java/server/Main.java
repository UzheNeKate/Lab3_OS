package server;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ServerApp {

    public static void main(String[] args) {
        AsynchronousServerSocketChannel serverSocket;
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(16);
            serverSocket = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(8080));
            while (serverSocket.isOpen()) {
                Future<AsynchronousSocketChannel> futureSocket = serverSocket.accept();
                executorService.submit(new SocketServer(futureSocket.get()));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}