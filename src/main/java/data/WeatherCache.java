package data;

import connect.CloudConnector;

import java.lang.reflect.Type;
import java.net.ConnectException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

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
        var receivedOptionalInfo = cloudConnector.getFromCloud(key);

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
}
