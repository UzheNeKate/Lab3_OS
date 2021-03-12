package connect;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public class JsonBodyHandler<T> implements HttpResponse.BodyHandler<Supplier<T>> {
    private final Class<T> targetClass;

    public JsonBodyHandler(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public HttpResponse.BodySubscriber<Supplier<T>> apply(HttpResponse.ResponseInfo responseInfo) {
        return asJson(this.targetClass);
    }


    public static <W> HttpResponse.BodySubscriber<Supplier<W>> asJson(Class<W> targetType) {
        HttpResponse.BodySubscriber<InputStream> upstream = HttpResponse.BodySubscribers.ofInputStream();

        return HttpResponse.BodySubscribers.mapping(
                upstream,
                inputStream -> toSupplier(inputStream, targetType)
        );
    }

    public static <W> Supplier<W> toSupplier(InputStream inputStream, Class<W> targetType) {
        return () -> {
            try (InputStream stream = inputStream) {
                var responseString = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                var response = new Gson().fromJson(responseString, targetType);
                return response;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }
}