package server;

import data.HumidityInfo;
import org.junit.jupiter.api.Test;
import parser.JsonParser;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Random;

class WeatherServerLauncherTest {
    @Test
    void parse()
    {
        String json =
                "{\"request\":{\"type\":\"City\",\"query\":\"New York, United States of America\",\"language\":\"en\",\"unit\":\"m\"},\"location\":{\"name\":\"New York\",\"country\":\"United States of America\",\"region\":\"New York\",\"lat\":\"40.714\",\"lon\":\"-74.006\",\"timezone_id\":\"America/New_York\",\"localtime\":\"2021-03-01 13:33\",\"localtime_epoch\":1614605580,\"utc_offset\":\"-5.0\"},\"current\":{\"observation_time\":\"06:33 PM\",\"temperature\":9,\"weather_code\":122,\"weather_icons\":[\"https://assets.weatherstack.com/images/wsymbols01_png_64/wsymbol_0004_black_low_cloud.png\"],\"weather_descriptions\":[\"Overcast\"],\"wind_speed\":0,\"wind_degree\":247,\"wind_dir\":\"WSW\",\"pressure\":1003,\"precip\":3,\"humidity\":71,\"cloudcover\":100,\"feelslike\":7,\"uv_index\":2,\"visibility\":16,\"is_day\":\"yes\"}}";
        parser.JsonParser<HumidityInfo> parser = new JsonParser<>(HumidityInfo.class);
        var info = parser.getFromJson(json);
        String str = parser.getJson(info);
        System.out.println(str);
    }

    @Test
    void doIt(){
        var part = "/weather?city=Minsk";
        var parts = part.split("[/?=&]");
        System.out.println(parts.length);
        Arrays.stream(parts).forEach(System.out::println);
    }

    static String[] requests = {"weather", "humidity", "wrong"};
    static String[] cities = {"Minsk", "New York", "Brest", "Moscow", "London", "Warsaw",
            "Kiev", "Moscow", "Berlin", "Rome", "Paris"};

    static String getRequest(){
        Random rnd = new Random();
        return String.format("%s?city=%s", requests[rnd.nextInt(requests.length - 1)],
                cities[rnd.nextInt(cities.length - 1)]);
    }

    @Test
    public static void main(String[] args) {
        if (args.length < 1) return;

        String domainName = args[0];

        String hostname = "localhost";
        int port = 8080;

        try (Socket socket = new Socket(hostname, port)) {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line;

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            for (int i = 0; i < 5; i++)
                writer.println(getRequest());

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