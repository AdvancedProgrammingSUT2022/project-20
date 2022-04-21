package ir.ap.controller;

import com.google.gson.JsonObject;

import ir.ap.model.GameArea;

public class GameController extends AbstractController {
    private GameArea gameArea = null;

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
        return JSON_FALSE;
    }
}
