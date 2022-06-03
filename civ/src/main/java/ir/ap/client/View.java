package ir.ap.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ir.ap.controller.GameController;
import ir.ap.controller.UserController;

public abstract class View {
    protected static final UserController USER_CONTROLLER = new UserController(true);
    protected static final GameController GAME_CONTROLLER = new GameController(true);
    protected static final Gson GSON = new Gson();

    protected static String currentUsername = null;

    protected static boolean responseOk(String response) {
        return response != null && GSON.fromJson(response, JsonObject.class)
                .get("ok").getAsBoolean();
    }

    protected static boolean responseOk(JsonObject response) {
        return response != null && response.has("ok") && response.get("ok").getAsBoolean();
    }

    protected static <T> T getField(JsonObject response, String fieldName, Class<T> classOfT) {
        if (response == null)
            return null;
        JsonElement field = response.get(fieldName);
        if (field == null)
            return null;
        if (classOfT.equals(String.class))
            return classOfT.cast(field.getAsString());
        if (classOfT.equals(Integer.class))
            return classOfT.cast(field.getAsInt());
        if (classOfT.equals(Boolean.class))
            return classOfT.cast(field.getAsBoolean());
        if (classOfT.equals(Double.class))
            return classOfT.cast(field.getAsDouble());
        throw new RuntimeException();
    }

    protected static boolean isLogin() {
        return currentUsername != null;
    }
}
