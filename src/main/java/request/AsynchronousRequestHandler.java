package request;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface AsynchronousRequestHandler {

    /**
     * @param request string containing request
     * @return string with response
     */
    Optional<CompletableFuture<String>> handle(String request);
}
