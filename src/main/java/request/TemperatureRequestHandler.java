package request;

import com.google.gson.Gson;
import data.TemperatureRequestInfo;
import data.WeatherCache;
import data.CachedWeatherInfo;

import java.net.ConnectException;

public class TemperatureRequestHandler implements RequestHandler {

    static WeatherCache cache = new WeatherCache(CachedWeatherInfo.class, 5);

    @Override
    public String handle(String city) {
        TemperatureRequestInfo info;
        try {
            info = new TemperatureRequestInfo(cache.get(city));
        } catch (ConnectException e) {
            return e.getMessage();
        }
        Gson parser = new Gson();
        return parser.toJson(info, TemperatureRequestInfo.class);
    }
}
