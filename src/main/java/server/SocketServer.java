package server;

import com.google.gson.Gson;
import data.Request;
import parser.RequestParser;
import parser.ResponseHeader;
import request.RequestDistributor;
import request.RequestType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static data.Request.BAD_REQUEST;

public class SocketServer implements Runnable {
    private final AsynchronousSocketChannel socket;

    public SocketServer(AsynchronousSocketChannel socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            readInput();
            socket.close();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    static ExecutorService pool = Executors.newFixedThreadPool(16);

    private void readInput() throws Throwable {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        socket.read(buffer);
        buffer.get();

        String request = new String(buffer.array(), StandardCharsets.UTF_8).trim();
        Request parsed = RequestParser.parse(request);
        RequestDistributor distributor = new RequestDistributor();
        var handler = distributor.findHandler(parsed);

        buffer.flip();
        if (handler == null) {
            writeResponse(new Gson().toJson(BAD_REQUEST, Request.class), 400);
        } else {
            writeResponse(pool.submit(handler).get(), 200);
        }
    }


    private void writeResponse(String responseData, int httpCode) throws Throwable {
        var responseHeader = new ResponseHeader("HTTP/1.1", httpCode,
                "WeatherServer", "text/html", responseData.length(), "close");
        ByteBuffer buffer = ByteBuffer.wrap((responseHeader + responseData + "\r\n\r")
                .getBytes(StandardCharsets.UTF_8));

        Future<Integer> writeResult = socket.write(buffer);
        writeResult.get();

        buffer.clear();
        System.out.println("from server" + responseData);
        System.out.println();
    }
}