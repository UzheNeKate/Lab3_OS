package data;

public class Humidity extends AbstractWeather{
    int humidity;

    public Humidity(CachedWeather current) {
        this.humidity = current.getHumidity();
        this.observation_time = current.getObservationTime();
    }

    public int getHumidity() {
        return humidity;
    }
}
