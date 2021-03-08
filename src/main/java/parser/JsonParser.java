package parser;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    //возвращает значение по ключу
    String getField(String json, String field){
        //TODO: create
        return null;
    }

    //возвращает массив значений по массиву ключей
    List<String> getFields(String json, List<String> fields){
        var jsonValues = new ArrayList<String>();
        for (var field : fields){
            jsonValues.add(getField(json, field));
        }
        return jsonValues;
    }

    String getJson(List<String> keys, List<String> values){
        //TODO: create
        return null;
    }
}
