package data;

import com.google.gson.Gson;
import connect.CloudConnector;

import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class WeatherCache {
    private final int size;
    private final ConcurrentLinkedQueue<CachedWeatherInfo> recordsQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<String, CachedWeatherInfo> weatherByCity = new ConcurrentHashMap<>();
    private final CloudConnector cloudConnector;

    public WeatherCache(Type type, int size) {
        this.cloudConnector = new CloudConnector();
        if (size < 0) {
            throw new IllegalArgumentException("Size cannot be negative");
        }
        this.size = size;
    }

    public CachedWeatherInfo get(String key) throws ConnectException {
        if (weatherByCity.get(key) == null) {
            return update(key);
        }

        var info = weatherByCity.get(key);
        info.setWrittenToCache();
        return info;
    }

    private CachedWeatherInfo update(String key) throws ConnectException {
        var future = cloudConnector.getFromCloud(key);
        var receivedOptionalInfo = parse(future);

        if (receivedOptionalInfo.isEmpty()) {
            throw new ConnectException("400 Bad Request");
        }

        var receivedInfo = receivedOptionalInfo.get();
        if (recordsQueue.size() == size) {
            var removed = recordsQueue.poll();
            if (removed != null) {
                weatherByCity.remove(removed.getLocation().getName());
            }
        }
        recordsQueue.add(receivedInfo);
        weatherByCity.put(key, receivedInfo);
        return receivedInfo;
    }
    private Optional<CachedWeatherInfo> parse(CompletableFuture<HttpResponse<Supplier<CachedWeatherInfo>>> future){
        CachedWeatherInfo response = null;
        try {
            response = future.get().body().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        Gson parser = new Gson();
        System.out.println(parser.toJson(response, CachedWeatherInfo.class));
        return Optional.of(response);
    }
}
