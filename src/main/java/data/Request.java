package data;

import request.RequestType;

public class Request {
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
