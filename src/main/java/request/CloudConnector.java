package request;

import Data.AbstractWeatherInfo;
import parser.JsonParser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CloudConnector<T extends AbstractWeatherInfo> {

    private final static String API_URL_FORMAT = System.getenv("API_URL_FORMAT");
    private final static String ACCESS_KEY = System.getenv("ACCESS_KEY");

    public HttpConnectionInfo  getFromCloud(String city, Type type){
        HttpURLConnection con;
        try {
            URL url = new URL(String.format(API_URL_FORMAT, ACCESS_KEY, city));
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(Request.GET.toString());
            JsonParser<T> parser = new JsonParser<T>();
            T info = parser.getFromJson(new String(con.getInputStream().readAllBytes(), StandardCharsets.UTF_8), type);
            return new HttpConnectionInfo(200,  " OK ", info);
        } catch (IOException e) {
            e.printStackTrace();
            return new HttpConnectionInfo(400, "Bad Request", null);
        }
        //opens a connection with the cloud server
        //returns received string
    }
}
