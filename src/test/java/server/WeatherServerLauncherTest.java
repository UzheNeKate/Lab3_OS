package server;

import data.HumidityInfo;
import org.junit.jupiter.api.Test;
import parser.JsonParser;

import java.util.Arrays;

class WeatherServerLauncherTest {
    @Test
    void parse()
    {
        String json =
                "{\"request\":{\"type\":\"City\",\"query\":\"New York, United States of America\",\"language\":\"en\",\"unit\":\"m\"},\"location\":{\"name\":\"New York\",\"country\":\"United States of America\",\"region\":\"New York\",\"lat\":\"40.714\",\"lon\":\"-74.006\",\"timezone_id\":\"America/New_York\",\"localtime\":\"2021-03-01 13:33\",\"localtime_epoch\":1614605580,\"utc_offset\":\"-5.0\"},\"current\":{\"observation_time\":\"06:33 PM\",\"temperature\":9,\"weather_code\":122,\"weather_icons\":[\"https://assets.weatherstack.com/images/wsymbols01_png_64/wsymbol_0004_black_low_cloud.png\"],\"weather_descriptions\":[\"Overcast\"],\"wind_speed\":0,\"wind_degree\":247,\"wind_dir\":\"WSW\",\"pressure\":1003,\"precip\":3,\"humidity\":71,\"cloudcover\":100,\"feelslike\":7,\"uv_index\":2,\"visibility\":16,\"is_day\":\"yes\"}}";
        parser.JsonParser<HumidityInfo> parser = new JsonParser<>(HumidityInfo.class);
        var info = parser.getFromJson(json);
        String str = parser.getJson(info);
        System.out.println(str);
    }

    @Test
    void doIt(){
        var part = "/weather?city=Minsk";
        var parts = part.split("[/?=&]");
        System.out.println(parts.length);
        Arrays.stream(parts).forEach(System.out::println);
    }

}