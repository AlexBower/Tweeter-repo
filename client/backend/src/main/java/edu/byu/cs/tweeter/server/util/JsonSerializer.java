package edu.byu.cs.tweeter.server.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonSerializer {

    public static String serialize(Object requestInfo) {
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(requestInfo);
        return json;
    }

    public static <T> T deserialize(String value, Class<T> returnType) {
        return (new Gson()).fromJson(value, returnType);
    }
}
