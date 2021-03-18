package request;

import com.google.gson.Gson;
import data.HumidityRequestInfo;
import data.WeatherCache;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.ConnectException;

@RestController
@RequestMapping("/humidity")
public class HumidityRequestHandler implements RequestHandler {

    static WeatherCache cache = new WeatherCache(HumidityRequestInfo.class, 5);

    @Override
    public String handle(@RequestParam(value = "city") String city) {
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
