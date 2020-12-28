package external.tplink.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class KspJson {
    public static String convertToString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    public static JsonObject convertToObj(String str){
        Gson gson = new Gson();
        return gson.fromJson(str, JsonObject.class);
    }

}
