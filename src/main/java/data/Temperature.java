package data;

import lombok.Getter;

public class Temperature extends AbstractWeather {
    @Getter
    int temperature;
    @Getter
    int feelslike;

    public Temperature(CachedWeather current) {
        this.temperature = current.getTemperature();
        this.feelslike = current.getFeelslike();
        this.observation_time = current.getObservation_time();
    }
}
