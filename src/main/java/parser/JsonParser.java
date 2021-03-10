package parser;

import com.google.gson.Gson;
import java.lang.reflect.Type;

public class JsonParser<T> {
    public T getFromJson(String json, Type type){
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    public String getJson(T obj){
        Gson gson = new Gson();
        return gson.toJson(obj);
    }
}
