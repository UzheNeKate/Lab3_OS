package data;

import lombok.Getter;

public class TemperatureRequestInfo extends AbstractWeatherInfo {
    @Getter
    Temperature current;

    public TemperatureRequestInfo(CachedWeatherInfo cachedWeatherInfo) {
        this.current = new Temperature(cachedWeatherInfo.current);
        this.location = new Location(cachedWeatherInfo.getLocation());
        this.fromCache = cachedWeatherInfo.getFromCache();
    }
}
