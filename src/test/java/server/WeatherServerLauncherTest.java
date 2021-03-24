package server;

import com.google.gson.Gson;
import data.HumidityRequestInfo;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Random;

class WeatherServerLauncherTest {
    @Test
    void parseWeatherMinsk(){
        var part = "/weather?city=Minsk";
        var parts = part.split("[/?=&]");
        System.out.println(parts.length);
        Arrays.stream(parts).forEach(System.out::println);
    }

    static String[] requests = {"temperature", "humidity", "wrong"};

    static String[] cities = {"Minsk", "New York", "Brest", "Moscow", "London", "Warsaw",
            "Kiev", "Moscow", "Berlin", "Rome", "Paris"};

    static String getRandomRequest(){
        Random rnd = new Random();
        return String.format("%s?city=%s", requests[rnd.nextInt(requests.length)],
                cities[rnd.nextInt(cities.length)]);
    }

    @Test
    void integrityClientTest() {
        String hostname = "localhost";
        int port = 8080;
        System.out.println("Connecting to " + hostname + ":" + port);
        System.out.println();
        for (int i = 0; i < 10; i++) {
            var randomRequest = getRandomRequest();
            System.out.println("Request : " + randomRequest);

            try (Socket socket = new Socket(hostname, port)) {
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                PrintWriter writer = new PrintWriter(output, true);
                writer.println("GET /" + randomRequest);

                int k = 0;
                StringBuilder str = new StringBuilder();
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));
                String line;
                while (! (line = in.readLine()).equals("")) {
                    System.out.println(line);
                }
                reader.close();
            } catch (UnknownHostException ex) {
                System.out.println("Server not found: " + ex.getMessage());
            } catch (IOException ex) {
                System.out.println("I/O error: " + ex.getMessage());
            }
        }
    }
}