package server;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

class WeatherServerLauncherTest {
    static String[] requests = {"temperature", "humidity", "wrong"};

    static String[] cities = {"Minsk", "New York", "Brest", "Moscow", "London", "Warsaw",
            "Kiev", "Moscow", "Berlin", "Rome", "Paris"};

    static String getRandomRequest() {
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
        for (int i = 0; i < 100; i++) {
            var randomRequest = getRandomRequest();
            System.out.println("Request : " + randomRequest);
            try (var socket = new Socket(hostname, port)) {
                ByteBuffer buffer = ByteBuffer.wrap(("GET /" + randomRequest).getBytes(StandardCharsets.UTF_8));
                socket.getOutputStream().write(buffer.array());

                ArrayList<Byte> bytes = new ArrayList<>();
                byte anotherByte;
                while ((anotherByte = (byte) socket.getInputStream().read()) != -1) {
                    bytes.add(anotherByte);
                }

                var byteArray = new byte[bytes.size()];
                for (int j = 0; j < bytes.size(); j++) {
                    byteArray[j] = bytes.get(j);
                }
                System.out.println(new String(byteArray, StandardCharsets.UTF_8));
            } catch (UnknownHostException ex) {
                System.out.println("Server not found: " + ex.getMessage());
            } catch (IOException ex) {
                System.out.println("I/O error: " + ex.getMessage());
            }
        }
    }
}