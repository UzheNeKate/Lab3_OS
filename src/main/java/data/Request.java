package data;

import request.RequestType;

public class Request {
    public static final Request BAD_REQUEST = new Request(RequestType.GET, "400 Bad Request", null);
    RequestType httpRequestType;
    String request;
    String city;

    public Request(RequestType httpRequestType, String request, String city) {
        this.httpRequestType = httpRequestType;
        this.request = request;
        this.city = city;
    }

    public RequestType getHttpRequestType() {
        return httpRequestType;
    }

    public String getRequest() {
        return request;
    }

    public String getCity() {
        return city;
    }
}
