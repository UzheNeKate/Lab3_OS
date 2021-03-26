package server;

import com.google.gson.Gson;
import data.Request;
import parser.RequestParser;
import parser.ResponseHeader;
import request.RequestDistributor;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        socket.read(buffer).get();

        String request = new String(buffer.array(), StandardCharsets.UTF_8).trim();
        System.out.println("New request: " + request);
        Request parsed = RequestParser.parse(request);
        RequestDistributor distributor = new RequestDistributor();

        var handler = distributor.findHandler(parsed);
        if (handler.isEmpty()) {
            writeResponse(new Gson().toJson(BAD_REQUEST, Request.class), 400);
        } else {
            writeResponse(pool.submit(handler.get()).get(), 200);
        }
    }

    private void writeResponse(String responseData, int httpCode) {
        var responseHeader = new ResponseHeader("HTTP/1.1", httpCode,
                "WeatherServer", "text/html", responseData.length());
        var response = responseHeader.toString() + responseData;
        socket.write(ByteBuffer.wrap(response.getBytes(StandardCharsets.UTF_8)));
        System.out.println("Response: " + response);
    }
}