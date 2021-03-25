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
        return toJsonSubscriber(this.targetClass);
    }


    public static <W> HttpResponse.BodySubscriber<Supplier<W>> toJsonSubscriber(Class<W> targetType) {
        HttpResponse.BodySubscriber<InputStream> upstream = HttpResponse.BodySubscribers.ofInputStream();
        return HttpResponse.BodySubscribers.mapping(
                upstream,
                inputStream -> toJsonSupplier(inputStream, targetType)
        );
    }

    public static <W> Supplier<W> toJsonSupplier(InputStream inputStream, Class<W> targetType) {
        return () -> {
            try (InputStream stream = inputStream) {
                var responseString = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                return new Gson().fromJson(responseString, targetType);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }
}