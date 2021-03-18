package request;

import com.google.gson.Gson;
import data.TemperatureRequestInfo;
import data.WeatherCache;
import data.CachedWeatherInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.ConnectException;

@RestController
@RequestMapping("/temperature")
public class TemperatureRequestHandler implements RequestHandler {
    static WeatherCache cache = new WeatherCache(CachedWeatherInfo.class, 5);

    @Override
    @GetMapping
    public String handle(@RequestParam(value = "city") String city) {
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
