package data;

import lombok.Getter;
import request.RequestType;

public class Request {
    public static final Request BAD_REQUEST = new Request(RequestType.GET, "400 Bad Request", null);
    @Getter
    RequestType httpRequestType;
    @Getter
    String request;
    @Getter
    String city;

    public Request(RequestType httpRequestType, String request, String city) {
        this.httpRequestType = httpRequestType;
        this.request = request;
        this.city = city;
    }
}
