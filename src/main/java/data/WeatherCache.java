package data;

import connect.CloudConnector;
import connect.ConnectionFailedException;

import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WeatherCache<T extends AbstractWeatherInfo> {
    private final int size;
    private final ConcurrentLinkedQueue<T> recordsQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<String, T> weatherByCity = new ConcurrentHashMap<>();
    private final CloudConnector<T> cloudConnector;

    public WeatherCache(Type type, int size) {
        this.cloudConnector = new CloudConnector<>(type);
        if (size < 0) {
            throw new IllegalArgumentException("Size cannot be negative");
        }
        this.size = size;
    }

    public T get(String key) throws ConnectionFailedException {
        if (weatherByCity.get(key) == null) {
            return update(key);
        }

        var info = weatherByCity.get(key);
        info.setWrittenToCache();
        return info;
    }

    private T update(String key) throws ConnectionFailedException {
        var connectionInfo = cloudConnector.getFromCloud(key);

        if (connectionInfo == null) {
            throw new ConnectionFailedException(404, "Not Found");
        }
        if (connectionInfo.getWeatherInfo() == null) {
            throw new ConnectionFailedException(connectionInfo.getCode(), connectionInfo.getMessage());
        }

        var data = (T)connectionInfo.getWeatherInfo();
        if (recordsQueue.size() == size) {
            var removed = recordsQueue.poll();
            if (removed != null) {
                weatherByCity.remove(removed.getLocation().getName());
            }
        }

        recordsQueue.add(data);
        weatherByCity.put(key, data);
        return data;
    }
}
