package ir.ap.controller;

import com.google.gson.JsonObject;

import ir.ap.model.GameArea;

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
