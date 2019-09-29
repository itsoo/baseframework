package com.github.webapp.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * GsonUtils
 *
 * @author zxy
 */
public class GsonUtils {

    private final static Gson GSON = new Gson();

    public static Gson getGson() {
        return GSON;
    }

    public static String toJson(Object o) {
        return GSON.toJson(o);
    }

    public static <T> T get(String json, Class<T> classOfT) {
        return GSON.fromJson(json, classOfT);
    }

    public static Map<String, String> getMap(String str) {
        return GSON.fromJson(str, new TypeToken<Map<String, String>>() {}.getType());
    }

    public static Map<String, Map<String, String>> getMapMap(String str) {
        return GSON.fromJson(str, new TypeToken<Map<String, Map<String, String>>>() {}.getType());
    }

    public static List<Map<String, String>> getMapList(String str) {
        return GSON.fromJson(str, new TypeToken<List<Map<String, String>>>() {}.getType());
    }
}
