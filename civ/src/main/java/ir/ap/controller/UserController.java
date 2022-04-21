package ir.ap.controller;

import com.google.gson.JsonObject;

public class UserController extends AbstractController {

    public JsonObject register(String username, String nickname, String password) {
        return JSON_FALSE;
    }

    public JsonObject login(String username, String password) {
        return JSON_FALSE;
    }

    public JsonObject logout(String username) {
        return JSON_FALSE;
    }

    public JsonObject changeNickname(String username, String newNickname) {
        return JSON_FALSE;
    }

    public JsonObject changePassword(String username, String newPassword) {
        return JSON_FALSE;
    }
}
