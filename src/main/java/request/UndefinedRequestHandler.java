package request;

public class UndefinedRequestHandler implements RequestHandler{

    @Override
    public String handle(String request) {
        return "400 Bad Request";
    }
}
