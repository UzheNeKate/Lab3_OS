package request;

import data.Request;
import parser.RequestParser;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class RequestDistributor {
    public Callable<String> findHandler(Request parsedRequest) {
        String className = parsedRequest.getRequest().substring(0, 1).toUpperCase() +
                parsedRequest.getRequest().substring(1) + "RequestHandler";

        RequestHandler handler;

        try {
            Class<?> handlerClass = Class.forName(RequestHandler.class.getPackageName() + "." + className);
            handler = (RequestHandler) handlerClass.getConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                IllegalAccessException | InvocationTargetException e){
            return null;
        }

        return () -> handler.handle(parsedRequest.getCity());
    }
}
