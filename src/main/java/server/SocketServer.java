package server;

import data.Request;
import parser.JsonParser;
import parser.RequestParser;
import parser.ResponseHeader;
import request.RequestDistributor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

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


    private void readInput() throws Throwable {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String request;
        while ((request = reader.readLine()) != null) {
            Request parsed = RequestParser.parse(request);
            RequestDistributor distributor = new RequestDistributor();
            var handler = distributor.findHandler(parsed);

            if (handler == null) {
                writeResponse(new JsonParser<Request>(Request.class).getJson(parsed));
            } else {
                writeResponse(handler.call());
            }
        }
    }


    private void writeResponse(String responseData) throws Throwable {
        var responseHeader = new ResponseHeader("HTTP/1.1", "200 OK",
                "WeatherServer", "text/html", responseData.length(), "keep-alive");
        outputStream.write((responseHeader.toString() + responseData).getBytes());
        outputStream.flush();
    }

}