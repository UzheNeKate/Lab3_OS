package request;

public interface RequestProcessor {

    /**
     * @param request string containing request
     * @return string with response
     */
    String process(String request);
}
