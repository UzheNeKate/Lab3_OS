package request;

import Data.AbstractWeatherInfo;

public class HttpConnectionInfo {
    final private int code;
    final private String message;
    final private AbstractWeatherInfo weatherInfo;

    public HttpConnectionInfo(int code, String message, AbstractWeatherInfo weatherInfo) {
        this.code = code;
        this.message = message;
        this.weatherInfo = weatherInfo;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public AbstractWeatherInfo getWeatherInfo() {
        return weatherInfo;
    }
}
