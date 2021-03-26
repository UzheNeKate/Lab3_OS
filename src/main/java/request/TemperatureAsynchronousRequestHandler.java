package request;

import com.google.gson.Gson;
import data.HumidityRequestInfo;
import data.TemperatureRequestInfo;
import data.AsynchronousWeatherCache;
import data.CachedWeatherInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.ConnectException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/temperature")
public class TemperatureAsynchronousRequestHandler implements AsynchronousRequestHandler {
    static AsynchronousWeatherCache cache = new AsynchronousWeatherCache(CachedWeatherInfo.class, 5);

    @Override
    @GetMapping
    public Optional<CompletableFuture<String>> handle(@RequestParam(value = "city") String city) {
        var fromCache = cache.get(city);
        if (fromCache.isEmpty())
            return Optional.empty();
        var futureString = fromCache.get().handle((info, ex) -> {
            var temperature = new TemperatureRequestInfo(info);
            return new Gson().toJson(temperature, TemperatureRequestInfo.class);
        });
        return Optional.of(futureString);
    }
}
