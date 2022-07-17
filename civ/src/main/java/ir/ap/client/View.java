package ir.ap.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ir.ap.client.App;
import ir.ap.client.network.Request;
import ir.ap.client.network.RequestHandler;
import ir.ap.controller.GameController;
import ir.ap.controller.UserController;
import javafx.application.Platform;

import java.io.IOException;
import java.net.Socket;

public abstract class View {
    protected static Socket socket;
    protected static RequestHandler requestHandler;

    protected static final Gson GSON;

    protected static String currentUsername = null;

    static {
        GSON = new Gson();
        try {
            socket = new Socket("localhost", App.SERVER_PORT);
            requestHandler = new RequestHandler(socket);
        } catch (IOException e) {
            System.out.println("Unable to connect to the server");
        }
    }

    protected static JsonObject send(String methodName, Object... params) {
        Request request = new Request(methodName, params);
        JsonObject response = null;
        try {
            response = requestHandler.send(request);
        } catch (Exception e) {
            System.out.println("Connection lost with server");
        }
        return response;
    }

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

    public void exit() {
        App.exit();
    }

    public void logout() {
        currentUsername = null;
        enterLogin();
    }

    private void enterLogin() {
        try {
            App.setRoot("fxml/login-view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enterSettings() {

    }

    public void enterProfile() {
        try {
            App.setRoot("fxml/profile-view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enterMain() {
        try {
            App.setRoot("fxml/main-view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enterGame() {
        try {
            App.setRoot("fxml/game-view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
