package parser;

import com.google.gson.Gson;

import java.lang.reflect.Type;


public class JsonParser<T> {
    private final Type parsingClass;

    public JsonParser(Type parsingClass) {
        this.parsingClass = parsingClass;
    }

    public T getFromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, parsingClass);
    }

    public String getJson(T obj){
        Gson gson = new Gson();
        return gson.toJson(obj);
    }
}
