package request;

import data.HumidityInfo;
import data.WeatherCache;
import connect.ConnectionFailedException;
import parser.JsonParser;

public class HumidityRequestHandler implements RequestHandler {

    static WeatherCache<HumidityInfo> cache = new WeatherCache<>(HumidityInfo.class, 5);

    @Override
    public String handle(String city) {
        HumidityInfo info;
        try {
            info = cache.get(city);
        } catch (ConnectionFailedException e) {
            return e.getMessage();
        }
        JsonParser<HumidityInfo> parser = new JsonParser<>(HumidityInfo.class);
        return parser.getJson(info);
    }
}
