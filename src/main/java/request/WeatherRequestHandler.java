package request;

import data.WeatherCache;
import data.WeatherInfo;
import connect.ConnectionFailedException;
import parser.JsonParser;

public class WeatherRequestHandler implements RequestHandler {

    static WeatherCache<WeatherInfo> cache = new WeatherCache<>(WeatherInfo.class, 5);

    @Override
    public String handle(String city) {
        WeatherInfo info;
        try {
            info = cache.get(city);
        } catch (ConnectionFailedException e) {
            return e.getMessage();
        }
        JsonParser<WeatherInfo> parser = new JsonParser<>(WeatherInfo.class);
        return parser.getJson(info);
    }
}
