package server;

import data.Request;
import parser.RequestParser;
import parser.ResponseHeader;
import request.RequestDistributor;
import request.RequestType;
import request.RequestHandler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.function.Function;

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
        var pool = Executors.newFixedThreadPool(8);
        while (true) {
            var request = reader.readLine();
            Request parsed = RequestParser.parse(request);
            RequestDistributor distributor = new RequestDistributor();
            var handler = distributor.findHandler(parsed);
            if (handler == null){
                writeResponse(parsed.getRequest());
            } else {
                writeResponse(pool.submit(handler).get());
            }
        }
    }


    private void writeResponse(String responseData) throws Throwable {
        var responseHeader = new ResponseHeader("HTTP/1.1", "200 OK",
                "WeatherServer/2009-09-09", "text/html", responseData.length(), "close");
        outputStream.write((responseHeader + responseData).getBytes());
        outputStream.flush();
    }

}