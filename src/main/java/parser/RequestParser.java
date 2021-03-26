package parser;

import data.Request;
import request.RequestType;

import static data.Request.BAD_REQUEST;

public class RequestParser {

    public static Request parse(String request) {
        if (request == null || request.trim().length() == 0) {
            return BAD_REQUEST;
        }
        var parts = request.split(" ");
        if (parts[0].equals(RequestType.GET.toString())) {
            return getCityWeatherRequest(parts[1]);
        } else {
            return BAD_REQUEST;
        }
    }

    private static Request getCityWeatherRequest(String part) {
        var parts = part.split(("[/?=&]"));
        if (parts.length < 4)
            return BAD_REQUEST;
        String city = parts[3];
        String type = parts[1];
        return new Request(RequestType.GET, type, city);
    }
}
