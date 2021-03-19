package data;

import lombok.Getter;

public class CachedWeather extends AbstractWeather {
    @Getter
    int temperature;
    @Getter
    int feelslike;
    @Getter
    int visibility;
    @Getter
    int wind_speed;
    @Getter
    int humidity;
}
