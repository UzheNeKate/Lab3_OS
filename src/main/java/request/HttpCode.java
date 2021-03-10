package request;

public class HttpCode {
    final private int code;
    final private String message;

    public HttpCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
