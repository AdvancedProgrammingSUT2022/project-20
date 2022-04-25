package ir.ap.controller;

import java.io.FileReader;
import java.io.Reader;
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

    private static final String CITY_NAMES_FILE = "citynames.json";

    public GameController() {
        super();
    }

    public GameController(boolean readData) {
        super();
        if (readData) {
            readCityNames();
        }
    }

    @Override
    public void close() {
    }

    public boolean readCityNames() {
        try {
            Reader namesReader = new FileReader(CITY_NAMES_FILE);
            String[] curNames = GSON.fromJson(namesReader, String[].class);
            for (String name : curNames) {
                City.addCityName(name);
            }
            namesReader.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public JsonObject getCivilizationByUsername(String username) {
        return JSON_FALSE;
    }

    public JsonObject getAllCivilizations() {
        return JSON_FALSE;
    }

    public JsonObject getAllPlayers() {
        return JSON_FALSE;
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
        mapController.initCivilizationPositions();
        return messageToJsonObj(Message.GAME_STARTED, true);
    }

    public JsonObject nextTurn() {
        return JSON_FALSE;
    }
}
