package data;

public class Temperature extends AbstractWeather {
    int temperature;
    int feelslike;

    public Temperature(CachedWeather current) {
        this.temperature = current.getTemperature();
        this.feelslike = current.getFeelsLike();
        this.observation_time = current.getObservationTime();
    }

    public int getTemperature() {
        return temperature;
    }

    public int getFeelsLike() {
        return feelslike;
    }

}
