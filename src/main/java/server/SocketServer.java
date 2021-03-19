package server;

import com.google.gson.Gson;
import com.google.gson.internal.bind.util.ISO8601Utils;
import data.CachedWeatherInfo;
import data.Request;
import parser.RequestParser;
import parser.ResponseHeader;
import request.RequestDistributor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static data.Request.BAD_REQUEST;

public class SocketServer implements Runnable {
    private final Socket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;


    public SocketServer(Socket socket) throws Throwable {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String request;
        while ((request = reader.readLine()) != null) {
            Request parsed = RequestParser.parse(request);
            RequestDistributor distributor = new RequestDistributor();
            var handler = distributor.findHandler(parsed);

            if (handler == null) {
                writeResponse(new Gson().toJson(BAD_REQUEST, Request.class), 400);
            } else {
                writeResponse(pool.submit(handler).get(), 200);
            }
        }
        //reader.close();
    }


    private void writeResponse(String responseData, int httpCode) throws Throwable {
        var responseHeader = new ResponseHeader("HTTP/1.1", httpCode,
                "WeatherServer", "text/html", responseData.length(), "close");
        outputStream.write((responseHeader + responseData + "\r\n\r").getBytes(StandardCharsets.UTF_8));
       // outputStream.close();
        System.out.println(responseData);
    }

}