package ir.ap.controller;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;

import com.google.gson.JsonObject;

import ir.ap.model.User;

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

    private static final String PLAYERS_CONF_FILE = "players.json";

    public UserController() {
        super();
    }

    public UserController(boolean shouldReadUsers) {
        this();
        if (shouldReadUsers)
            readUsers();
    }

    public boolean readUsers() {
        try {
            Reader usersReader = new FileReader(PLAYERS_CONF_FILE);
            User[] curUsers = GSON.fromJson(usersReader, User[].class);
            for (User user : curUsers) {
                User.addUser(user);
            }
            usersReader.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean writeUsers() {
        try {
            Writer usersWriter = new FileWriter(PLAYERS_CONF_FILE);
            GSON.toJson(User.getUsers(), usersWriter);
            usersWriter.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public JsonObject register(String username, String nickname, String password) {
        if (!username.matches(Validator.USERNAME.toString()))
            return messageToJsonObj(Message.USERNAME_INVALID, false);
        if (!nickname.matches(Validator.NICKNAME.toString()))
            return messageToJsonObj(Message.NICKNAME_INVALID, false);
        if (!password.matches(Validator.PASSWORD.toString()))
            return messageToJsonObj(Message.PASSWORD_INVALID, false);

        if (User.usernameExists(username)) {
            return messageToJsonObj(Message.USER_WITH_USERNAME_EXISTS
                    .toString().replace("%s", username), false);
        }
        if (User.nicknameExists(nickname)) {
            return messageToJsonObj(Message.USER_WITH_NICKNAME_EXISTS
                    .toString().replace("%s", nickname), false);
        }

        User curUser = new User(username, nickname, password);
        if (User.addUser(curUser))
            return messageToJsonObj(Message.USER_CREATED, true);
        else
            return messageToJsonObj(Message.E500, false);
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
