package connect;

import data.CachedWeatherInfo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class CloudConnector {
    private final static String API_URL_FORMAT = "http://api.weatherstack.com/current?access_key=%s&query=%s";
    private final static String ACCESS_KEY = System.getenv("ACCESS_KEY");

    public Optional<CompletableFuture<HttpResponse<Supplier<CachedWeatherInfo>>>> getFromCloud(String city) {
        try {
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder(URI
                    .create(String.format(API_URL_FORMAT, ACCESS_KEY, city)))
                    .build();
            return Optional.of(client.sendAsync(request, new JsonBodyHandler<>(CachedWeatherInfo.class)));
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }
}
