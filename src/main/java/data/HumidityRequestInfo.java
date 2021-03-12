package data;

public class HumidityRequestInfo extends AbstractWeatherInfo {
    Humidity current;

    public Humidity getHumidity() {
        return current;
    }

    public HumidityRequestInfo(CachedWeatherInfo cachedWeatherInfo) {
        this.current = new Humidity(cachedWeatherInfo.current);
        this.location = new Location(cachedWeatherInfo.getLocation());
        this.fromCache = cachedWeatherInfo.getFromCache();
    }
}
