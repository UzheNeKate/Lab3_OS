import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class WeatherServer {

    public static void main(String[] args) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(8080);
            while (true) {
                Socket socket = serverSocket.accept();
                new SocketProcessor(socket).run();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static class SocketProcessor implements Runnable {
        private final Socket socket;
        private final InputStream inputStream;
        private final OutputStream outputStream;
        
        private final static String API_URL_FORMAT = "http://api.weatherstack.com/current?access_key=%s&query=%s";
        private final static String ACCESS_KEY = "bed96a1dacd8f47c3035e322b8b32089";
        String DEFAULT_RESPONSE_FORMAT = """
                HTTP/1.1 200 OK\r
                Server: YarServer/2009-09-09\r
                Content-Type: text/html\r
                Content-Length: %d\r
                Connection: close\r
                \r
                """;

        private SocketProcessor(Socket socket) throws Throwable {
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
                var request = reader.readLine();
                if (request == null || request.trim().length() == 0) {
                    break;
                }
                if (request.startsWith("GET")){
                    processGetRequest(request.split(" ")[1]);
                }
            }
        }

        private void processGetRequest(String request) throws Throwable {
            String weatherRequestFormat = "/weather?city=";
            if (request.startsWith(weatherRequestFormat)) {
                var city = request.substring(weatherRequestFormat.length());
                URL url = new URL(String.format(API_URL_FORMAT, ACCESS_KEY, city));
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                writeResponse(new String(con.getInputStream().readAllBytes(), StandardCharsets.UTF_8));
            } else {
                writeResponse("Wrong request");
            }
        }

        private void writeResponse(String responseData) throws Throwable {
            var responseHeader= String.format(DEFAULT_RESPONSE_FORMAT, responseData.length());
            outputStream.write((responseHeader + responseData).getBytes());
            outputStream.flush();
        }
    }
}