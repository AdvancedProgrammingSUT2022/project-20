package ir.ap.controller;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import ir.ap.model.City;
import ir.ap.model.Civilization;
import ir.ap.model.GameArea;
import ir.ap.model.User;

public class GameController extends AbstractGameController implements JsonResponsor, AutoCloseable {

    public enum Validator {
        ;

        private final String regex;

        Validator(String regex) {
            this.regex = regex;
        }

        @Override
        public String toString() {
            return regex;
        }
    }

    public enum Message {
        GAME_STARTED("game started successfully"),
        USER_NOT_LOGGED_IN("user is not logged in"),

        E500("Server error");

        private final String msg;

        Message(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            return msg;
        }
    }

    public GameController() {
    }

    @Override
    public void close() {
    }

    public JsonObject newGame(String[] players) {
        gameArea = new GameArea(System.currentTimeMillis());
        for (String username : players) {
            User curUser = User.getUser(username);
            if (curUser == null || !curUser.isLogin())
                return messageToJsonObj(Message.USER_NOT_LOGGED_IN, false);
            Civilization curCiv = new Civilization(curUser.getNickname() + ".civ", null);
            gameArea.addUser(curUser, curCiv);
        }
        civController = new CivilizationController(gameArea);
        mapController = new MapController(gameArea);
        unitController = new UnitController(gameArea);
        cityController = new CityController(gameArea);
        AbstractGameController[] controllers = new AbstractGameController[] {
                this, civController, mapController, unitController, cityController };
        for (AbstractGameController controller : controllers) {
            controller.setGameController(this);
            controller.setCivilizationController(civController);
            controller.setMapController(mapController);
            controller.setUnitController(unitController);
            controller.setCityController(cityController);
        }
        
        return messageToJsonObj(Message.GAME_STARTED, true);
    }

    public JsonObject nextTurn() {
        return JSON_FALSE;
    }
}
