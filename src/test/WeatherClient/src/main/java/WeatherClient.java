import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class WeatherClient {
    static String[] requests = {"weather", "humidity", "wrong"};

    static String[] cities = {"Minsk", "New York", "Brest", "Moscow", "London", "Warsaw",
            "Kiev", "Moscow", "Berlin", "Rome", "Paris"};

    static String getRandomRequest(){
        Random rnd = new Random();
        return String.format("%s?city=%s", requests[rnd.nextInt(requests.length - 1)],
                cities[rnd.nextInt(cities.length - 1)]);
    }

    public static void main(String[] args) {
        if (args.length < 1) return;

        String hostname = "localhost";
        int port = 8080;

        try (Socket socket = new Socket(hostname, port)) {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line;

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            for (int i = 0; i < 5; i++)
                writer.println(getRandomRequest());

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}
