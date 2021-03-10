package Data;

import request.CloudConnector;

import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WeatherCache<T extends AbstractWeatherInfo> {
    private final int size;
    private ConcurrentLinkedQueue<AbstractWeatherInfo> recordsQueue;
    private ConcurrentHashMap<String, AbstractWeatherInfo> weatherByCity;
    private final CloudConnector<T> cloudConnector;

    public WeatherCache(Type type, int size) {
        this.cloudConnector = new CloudConnector<>(type);
        if (size < 0) {
            throw new IllegalArgumentException("Size cannot be negative");
        }
        this.size = size;
    }

    public AbstractWeatherInfo get(String key) {
        while (weatherByCity.get(key) == null) {
            update(key);
        }
        return weatherByCity.get(key);
    }

    private void update(String key) {
        var connectionInfo = cloudConnector.getFromCloud(key);
        var data = connectionInfo.getWeatherInfo();
        if (recordsQueue.size() == size) {
            recordsQueue.poll();
            weatherByCity.remove(key);
        }

        recordsQueue.add(data);
        weatherByCity.put(key, data);
    }
}
