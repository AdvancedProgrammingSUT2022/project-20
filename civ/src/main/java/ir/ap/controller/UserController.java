package ir.ap.controller;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;

import com.google.gson.JsonObject;

import ir.ap.model.User;

public class UserController implements JsonResponsor, AutoCloseable {

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
        USER_NOT_LOGGED_IN("user is not logged in"),
        INVALID_CREDENTIALS("Username and password didn't match"),

        PASSWORD_CHANGED("password changed successfully"),
        NICKNAME_CHANGED("nickname changed successfully"),

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

    public UserController(boolean readData) {
        this();
        if (readData)
            readUsers();
    }

    @Override
    public void close() {
        writeUsers();
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
        User user = User.getUser(username);
        if (user == null || !user.checkPassword(password))
            return messageToJsonObj(Message.INVALID_CREDENTIALS, false);
        user.setLogin(true);
        return messageToJsonObj(Message.USER_LOGGED_IN, true);
    }

    public JsonObject logout(String username) {
        User user = User.getUser(username);
        if (user == null || !user.isLogin())
            return messageToJsonObj(Message.USER_NOT_LOGGED_IN, false);
        user.setLogin(false);
        return messageToJsonObj(Message.USER_LOGGED_IN, true);
    }

    public JsonObject changeNickname(String username, String newNickname) {
        User user = User.getUser(username);
        if (user == null || !user.isLogin())
            return messageToJsonObj(Message.USER_NOT_LOGGED_IN, false);
        if (User.nicknameExists(newNickname)) {
            return messageToJsonObj(Message.USER_WITH_NICKNAME_EXISTS
                    .toString().replace("%s", newNickname), false);
        }
        user.setNickname(newNickname);
        return messageToJsonObj(Message.NICKNAME_CHANGED, true);
    }

    public JsonObject changePassword(String username, String oldPassword, String newPassword) {
        User user = User.getUser(username);
        if (user == null || !user.isLogin())
            return messageToJsonObj(Message.USER_NOT_LOGGED_IN, false);
        if (!user.checkPassword(oldPassword))
            return messageToJsonObj(Message.INVALID_CREDENTIALS, false);
        user.setPassword(newPassword);
        return messageToJsonObj(Message.PASSWORD_CHANGED, true);
    }
}
