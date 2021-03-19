package data;

import lombok.Getter;

public class HumidityRequestInfo extends AbstractWeatherInfo {

    @Getter
    Humidity current;

    public HumidityRequestInfo(CachedWeatherInfo cachedWeatherInfo) {
        this.current = new Humidity(cachedWeatherInfo.current);
        this.location = new Location(cachedWeatherInfo.getLocation());
        this.fromCache = cachedWeatherInfo.getFromCache();
    }
}
