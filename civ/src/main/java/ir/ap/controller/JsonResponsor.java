package ir.ap.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public interface JsonResponsor {
    public static final Gson GSON = new Gson();

    public static final JsonObject JSON_EMPTY = GSON.fromJson("{}", JsonObject.class);
    public static final JsonObject JSON_FALSE = GSON.fromJson("{\"ok\":false}", JsonObject.class);
    public static final JsonObject JSON_TRUE = GSON.fromJson("{\"ok\":true}", JsonObject.class);

    default String toJsonStr(JsonObject jsonObj) {
        if (jsonObj == null)
            jsonObj = new JsonObject();
        return GSON.toJson(jsonObj);
    }

    default JsonObject toJsonObj(String jsonStr) {
        if (jsonStr == null)
            jsonStr = new String();
        return GSON.fromJson(jsonStr, JsonObject.class);
    }

    default JsonObject setOk(JsonObject jsonObj, boolean value) {
        if (jsonObj == null)
            jsonObj = new JsonObject();
        jsonObj.addProperty("ok", value);
        return jsonObj;
    }

    default boolean isOk(JsonObject jsonObj) {
        return jsonObj != null && jsonObj.has("ok") && jsonObj.get("ok").getAsBoolean();
    }

    default JsonObject messageToJsonObj(Object msg, boolean ok) {
        JsonObject jsonObj = new JsonObject();
        if (msg != null)
            jsonObj.addProperty("msg", msg.toString());
        jsonObj.addProperty("ok", ok);
        return jsonObj;
    }
}
