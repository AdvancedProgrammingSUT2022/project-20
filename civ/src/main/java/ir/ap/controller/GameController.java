package ir.ap.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import ir.ap.model.*;
import org.w3c.dom.html.HTMLImageElement;

import java.net.UnknownServiceException;
import java.util.ArrayList;

public class GameController extends AbstractController {

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
        USER_NOT_LOGGED_IN("user is not logged in"),

        GAME_STARTED("game started"),

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

    private GameArea gameArea = null;

    public JsonObject getCivilizationByUsername(String username)
    {
        Civilization civilization = gameArea.getCivilizationByUser(gameArea.getUserByUsername(username));

        Gson gson = new Gson();
        String x = gson.toJson(civilization);
        return gson.fromJson(x,JsonObject.class);
    }

    public JsonObject getAllCivilizations()
    {
        ArrayList<Civilization> civilizations = new ArrayList<Civilization>();
        for(Civilization civilization : gameArea.getCiv2user().keySet())
            civilizations.add(civilization);

        Gson gson = new Gson();
        String x = gson.toJson(civilizations);
        return gson.fromJson(x,JsonObject.class);
    }

    public JsonObject getAllPlayers()
    {
        ArrayList<User> Users = new ArrayList<User>();
        for(User user : gameArea.getCiv2user().values())
            Users.add(user);

        Gson gson = new Gson();
        String x = gson.toJson(Users);
        return gson.fromJson(x,JsonObject.class);
    }

    public JsonObject newGame(String[] players)
    {
        gameArea = new GameArea((int)System.currentTimeMillis());
        for (String username : players) {
            User user = User.getUser(username);
            if (user == null || !user.isLogin())
                return messageToJsonObj(Message.USER_NOT_LOGGED_IN, false);
        }
        return messageToJsonObj(Message.GAME_STARTED,true);
    }

    public JsonObject nextTurn(String username)
    {
        return JSON_FALSE;
    }

    public JsonObject selectCity(String username, String cityName)
    {
        Civilization civilization = gameArea.getCivilizationByName(username);
        City city = gameArea.getCityByName(cityName);
        civilization.setSelectedCity(city);
        return messageToJsonObj("city has benn selected succesfully",true);
    }

    public JsonObject selectCity(String username, int tileID)
    {
        Civilization civilization = gameArea.getCivilizationByName(username);
        City city = gameArea.getCityByTileID(tileID);
        civilization.setSelectedCity(city);
        return messageToJsonObj("city has benn selected succesfully",true);
    }

    public JsonObject ()
    {
        return ;
    }
}
