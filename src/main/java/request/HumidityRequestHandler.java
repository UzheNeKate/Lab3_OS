package request;

import com.google.gson.Gson;
import data.HumidityRequestInfo;
import data.WeatherCache;

import java.net.ConnectException;

public class HumidityRequestHandler implements RequestHandler {

    static WeatherCache cache = new WeatherCache(HumidityRequestInfo.class, 5);

    @Override
    public String handle(String city) {
        HumidityRequestInfo info;
        try {
            info = new HumidityRequestInfo(cache.get(city));
        } catch (ConnectException e) {
            return e.getMessage();
        }
        Gson parser = new Gson();
        return parser.toJson(info, HumidityRequestInfo.class);
    }
}
