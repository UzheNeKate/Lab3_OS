package data;

public class TemperatureRequestInfo extends AbstractWeatherInfo {
    Temperature current;

    public Temperature getTemperature() {
        return current;
    }

    public TemperatureRequestInfo(CachedWeatherInfo cachedWeatherInfo) {
        this.current = new Temperature(cachedWeatherInfo.current);
        this.location = new Location(cachedWeatherInfo.getLocation());
        this.fromCache = cachedWeatherInfo.getFromCache();
    }
}
