package request;

import com.google.gson.Gson;
import data.HumidityRequestInfo;
import data.AsynchronousWeatherCache;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/humidity")
public class HumidityAsynchronousRequestHandler implements AsynchronousRequestHandler {
    static AsynchronousWeatherCache cache = new AsynchronousWeatherCache(HumidityRequestInfo.class, 5);

    @Override
    public Optional<CompletableFuture<String>> handle(@RequestParam(value = "city") String city) {
        var fromCache = cache.get(city);
        if (fromCache.isEmpty())
            return Optional.empty();
        var futureString = fromCache.get().handle((info, ex) -> {
            var humidity = new HumidityRequestInfo(info);
            return new Gson().toJson(humidity, HumidityRequestInfo.class);
        });
        return Optional.of(futureString);
    }
}