package parser;

import Data.WeatherInfo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    //возвращает значение по ключу
    public String getField(String json, String field){
        Gson gson = new Gson();
        WeatherInfo weather = gson.fromJson( json, WeatherInfo.class );
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
