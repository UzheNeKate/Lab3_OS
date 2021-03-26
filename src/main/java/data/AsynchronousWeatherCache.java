package data;

import com.google.gson.Gson;
import connect.CloudConnector;

import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class AsynchronousWeatherCache {
    private final int size;
    private final ConcurrentLinkedQueue<CompletableFuture<CachedWeatherInfo>> recordsQueue =
            new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<String, CompletableFuture<CachedWeatherInfo>> weatherByCity =
            new ConcurrentHashMap<>();
    private final CloudConnector cloudConnector;

    public AsynchronousWeatherCache(Type type, int size) {
        this.cloudConnector = new CloudConnector();
        if (size < 0) {
            throw new IllegalArgumentException("Size cannot be negative");
        }
        this.size = size;
    }

    public Optional<CompletableFuture<CachedWeatherInfo>> get(String key) {
        if (weatherByCity.get(key) == null) {
            return update(key);
        }

        var futureInfo = weatherByCity.get(key);
        futureInfo.handle((info, ex) -> {
            info.setWrittenToCache();
            return true;
        });
        return Optional.of(futureInfo);
    }

    private Optional<CompletableFuture<CachedWeatherInfo>> update(String key) {
        var futureOptional = cloudConnector.getFromCloud(key);
        if (futureOptional.isEmpty()) {
            return Optional.empty();
        }
        var futureWeatherInfo =
                futureOptional.get().handle((response, ex) -> response.body().get());

        if (recordsQueue.size() == size) {
            var removed = recordsQueue.poll();
            if (removed != null) {
                removed.handle((info, ex) -> weatherByCity.remove(info.getLocation().getName()));
            }
        }
        recordsQueue.add(futureWeatherInfo);
        weatherByCity.put(key, futureWeatherInfo);
        return Optional.of(futureWeatherInfo);
    }
}