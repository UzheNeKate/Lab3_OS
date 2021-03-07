package request;

public interface RequestHandler {

    /**
     * @param request string containing request
     * @return string with response
     */
    String handle(String request);
}
