package data;

public class CachedWeatherInfo extends AbstractWeatherInfo{
    CachedWeather current;

    public AbstractWeather getCachedWeather() {
        return current;
    }
}