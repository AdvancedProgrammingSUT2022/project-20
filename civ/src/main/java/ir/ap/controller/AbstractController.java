package ir.ap.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public abstract class AbstractController {
    protected static final Gson GSON = new Gson();

    protected static final JsonObject JSON_EMPTY = GSON.fromJson("{}", JsonObject.class);
    protected static final JsonObject JSON_FALSE = GSON.fromJson("{\"ok\":false}", JsonObject.class);
    protected static final JsonObject JSON_TRUE = GSON.fromJson("{\"ok\":true}", JsonObject.class);

    protected static String toJsonStr(JsonObject jsonObj) {
        if (jsonObj == null)
            jsonObj = new JsonObject();
        return GSON.toJson(jsonObj);
    }

    protected static JsonObject toJsonObj(String jsonStr) {
        if (jsonStr == null)
            jsonStr = new String();
        return GSON.fromJson(jsonStr, JsonObject.class);
    }

    protected static JsonObject setOk(JsonObject jsonObj, boolean value) {
        if (jsonObj == null)
            jsonObj = new JsonObject();
        jsonObj.addProperty("ok", value);
        return jsonObj;
    }
}
