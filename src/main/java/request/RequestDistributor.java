package request;

import data.Request;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class RequestDistributor {
    public Optional<Callable<String>> findHandler(Request parsedRequest) {
        String className = parsedRequest.getRequest().substring(0, 1).toUpperCase() +
                parsedRequest.getRequest().substring(1) + "AsynchronousRequestHandler";

        AsynchronousRequestHandler handler;
        try {
            Class<?> handlerClass = Class.forName(AsynchronousRequestHandler.class.getPackageName() + "." + className);
            handler = (AsynchronousRequestHandler) handlerClass.getConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                IllegalAccessException | InvocationTargetException e) {
            return Optional.empty();
        }

        return handler
                .handle(parsedRequest.getCity())
                .map(stringCompletableFuture -> stringCompletableFuture::get);
    }
}