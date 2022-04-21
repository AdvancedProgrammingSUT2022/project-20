package ir.ap.controller;

import com.google.gson.JsonObject;

public class UserController extends AbstractController {

    public enum Validator {
        USERNAME("\\w+"),
        NICKNAME("\\w+"),
        PASSWORD("\\S+");

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
        USER_CREATED("user created successfully"),
        USER_WITH_USERNAME_EXISTS("user with username %s already exists"),
        USER_WITH_NICKNAME_EXISTS("user with nickname %s already exists"),
        USERNAME_INVALID("username invalid"),
        NICKNAME_INVALID("nickname invalid"),
        PASSWORD_INVALID("password invalid"),

        USER_LOGGED_IN("user logged in successfully"),
        USER_NOT_LOGGED_IN("please login first"),
        INVALID_CREDENTIALS("Username and password didn't match"),

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

    public JsonObject changePassword(String username, String oldPassword, String newPassword) {
        return JSON_FALSE;
    }
}
