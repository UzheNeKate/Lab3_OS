package data;

public class CachedWeather extends AbstractWeather {
    int temperature;
    int feelslike;
    int visibility;
    int wind_speed;
    int humidity;

    public int getTemperature() {
        return temperature;
    }

    public int getFeelsLike() {
        return feelslike;
    }

    public int getVisibility() {
        return visibility;
    }

    public int getWindSpeed() {
        return wind_speed;
    }

    public int getHumidity() {
        return humidity;
    }
}
