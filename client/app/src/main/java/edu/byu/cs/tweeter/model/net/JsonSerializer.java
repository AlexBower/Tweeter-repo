package edu.byu.cs.tweeter.model.net;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

public class JsonSerializer {

    public static String serialize(Object requestInfo) {
        Gson gson = new GsonBuilder()
                .setExclusionStrategies( new ImageBytesExclusionStrategy() )
                .create();
        String json = gson.toJson(requestInfo);
        return json;
    }

    public static <T> T deserialize(String value, Class<T> returnType) {
        return (new Gson()).fromJson(value, returnType);
    }
}

