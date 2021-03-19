package data;

import lombok.Getter;

public class Humidity extends AbstractWeather{
    @Getter
    int humidity;

    public Humidity(CachedWeather current) {
        this.humidity =  current.getHumidity();
        this.observation_time = current.getObservation_time();
    }
}
