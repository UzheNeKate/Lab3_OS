package request;

import java.util.function.Function;

public class RequestDistributor {
    public Function<String, String> findHandler(String requestData){
        //TODO: по полученной от парсера информации вернуть обработчик
        //например, такой:
        return request -> new WeatherRequestHandler().handle(request);
    }
}
