package ir.ap.controller;

import static org.junit.Assert.assertTrue;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ir.ap.model.Civilization;
import ir.ap.model.User;

public class AbstractControllerTest {
    protected static final UserController USER_CONTROLLER = new UserController(true);
    protected static final GameController GAME_CONTROLLER = new GameController(true);

    protected static final User player1 = new User("user1", "nick1", "pass1");
    protected static final User player2 = new User("user2", "nick2", "pass2");
    protected static Civilization civ1;
    protected static Civilization civ2;

    protected static boolean login(User user) {
        JsonObject response = USER_CONTROLLER.register(user.getUsername(), user.getNickname(), user.getPassword());
        boolean result = response.get("ok").getAsBoolean();
        response = USER_CONTROLLER.login(user.getUsername(), user.getPassword());
        result |= response.get("ok").getAsBoolean();
        return result;
    }

    protected static boolean newGame() {
        JsonObject response = GAME_CONTROLLER.newGame(new String[] { player1.getUsername(), player2.getUsername() });
        return response.get("ok").getAsBoolean();
    }

    protected static UnitController getUnitController() {
        return GAME_CONTROLLER.getUnitController();
    }

    protected static CivilizationController getCivController() {
        return GAME_CONTROLLER.getCivilizationController();
    }

    protected static CityController getCityController() {
        return GAME_CONTROLLER.getCityController();
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

    protected static void assertOk(JsonObject response) {
        assertTrue(responseOk(response));
    }
}
