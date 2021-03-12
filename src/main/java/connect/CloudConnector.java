package connect;

import com.google.gson.Gson;
import data.CachedWeatherInfo;
import request.RequestType;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class CloudConnector {
    private final static String API_URL_FORMAT = "http://api.weatherstack.com/current?access_key=%s&query=%s";
    private final static String ACCESS_KEY = System.getenv("ACCESS_KEY");

    public Optional<CachedWeatherInfo> getFromCloud(String city) {
        HttpURLConnection con;
        try {
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder(URI
                    .create(String.format(API_URL_FORMAT, ACCESS_KEY, city)))
                    .build();

            var response =
                    client.send(request, new JsonBodyHandler<>(CachedWeatherInfo.class)).body().get();
            Gson parser = new Gson();
            System.out.println(parser.toJson(response, CachedWeatherInfo.class));
            return Optional.of(response);
        } catch (IOException | InterruptedException | NullPointerException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
