package server;

import request.Request;
import request.RequestHandler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class SocketServer implements Runnable {
    private final Socket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private RequestHandler getProcessor;

    //TODO: move to ResponseParser
    String DEFAULT_RESPONSE_FORMAT = """
            HTTP/1.1 200 OK\r
            Server: YarServer/2009-09-09\r
            Content-Type: text/html\r
            Content-Length: %d\r
            Connection: close\r
            \r
            """;

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
        while (true) {
            //TODO:
            //1. добавляет запрос в очередь к пулу (потом)
            //2. отдается парсеру на съедение
            //3. то, что выплюнул парсер, отдаётся распределителю (RequestDistributor)
            //4. запускается handle класса, который был определён распределителем
            var request = reader.readLine();
            if (request == null || request.trim().length() == 0) {
                break;
            }
            if (request.startsWith(Request.GET.toString())) {
                processGetRequest(request.split(" ")[1]);
            }
        }
    }

    //TODO: redundant, move to RequestDistributor
    private void processGetRequest(String request) throws Throwable {
        writeResponse(getProcessor.handle(request));
    }

    private void writeResponse(String responseData) throws Throwable {
        var responseHeader = String.format(DEFAULT_RESPONSE_FORMAT, responseData.length());
        outputStream.write((responseHeader + responseData).getBytes());
        outputStream.flush();
    }

    //TODO: redundant
    public void setGetProcessor(RequestHandler getProcessor) {
        this.getProcessor = getProcessor;
    }
    //TODO: redundant
    public RequestHandler getGetProcessor(RequestHandler getProcessor) {
        return getProcessor;
    }
}