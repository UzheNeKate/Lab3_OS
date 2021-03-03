package request;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GetWeatherProcessor implements RequestProcessor{
    private final static String API_URL_FORMAT = System.getenv("API_URL_FORMAT");
    private final static String ACCESS_KEY = System.getenv("ACCESS_KEY");

    @Override
    public String process(String request) {
        String weatherRequestFormat = "/weather?city=";
        if (request.startsWith(weatherRequestFormat)) {
            var city = request.substring(weatherRequestFormat.length());
            HttpURLConnection con;
            try {
                URL url = new URL(String.format(API_URL_FORMAT, ACCESS_KEY, city));
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod(Request.GET.toString());
                return new String(con.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
                return "Wrong request";
            }
        } else {
            return "Wrong request";
        }
    }
}
