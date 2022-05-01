package ir.ap.view;

import java.util.regex.Matcher;

import com.google.gson.JsonObject;

public class GameMenuView extends AbstractMenuView {

    public enum Command {
        ENTER_MENU("menu enter (?<menuName>\\w+)"),
        EXIT_MENU("menu exit"),
        SHOW_MENU("menu show-current"),
        INFO("info (?<arg>\\S+)"),
        SELECT_UNIT("select unit (?<type>combat|noncombat) (?<tileId>\\d+))"),
        SELECT_CITY("select city (?<nameOrId>\\S+)");

        private final String regex;

        Command(String regex) {
            this.regex = regex;
        }

        @Override
        public String toString() {
            return "\\s*" + regex.replace(" ", "\\s+") + "\\s*";
        }
    }

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
        MENU_NAVIGATION_IMPOSSIBLE("menu navigation is not possible"),

        ARG_INVALID("argument %s invalid"),
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

    public Enum<?>[] getCommands() {
        return Command.values();
    }

    public Menu enterMenu(Matcher matcher) {
        return responseAndGo(Message.MENU_NAVIGATION_IMPOSSIBLE, Menu.GAME);
    }

    public Menu exitMenu(Matcher matcher) {
        return responseAndGo(null, Menu.MAIN);
    }

    public Menu showMenu(Matcher matcher) {
        return responseAndGo("Game Menu", Menu.GAME);
    }

    public Menu info(Matcher matcher) {
        String arg = matcher.group("arg");
        JsonObject response = getInfoResponse(arg);
        if (response == null)
            return responseAndGo(Message.ARG_INVALID.toString().replace("%s", arg), Menu.GAME);
        if (!responseOk(response))
            return responseAndGo(Message.E500, Menu.GAME);
        String msg = getField(response, "msg", String.class);
        return responseAndGo(msg, Menu.GAME);
    }

    public Menu selectUnit(Matcher matcher) {
        String type = matcher.group("type");
        int tileId = Integer.parseInt(matcher.group("tileId"));
        JsonObject response = (type.equals("combat")
                ? GAME_CONTROLLER.selectCombatUnit(currentUsername, tileId)
                : GAME_CONTROLLER.selectNonCombatUnit(currentUsername, tileId));
        String msg = getField(response, "msg", String.class);
        return responseAndGo(msg, Menu.GAME);
    }

    public Menu selectCity(Matcher matcher) {
        String nameOrId = matcher.group("nameOrId");
        JsonObject response;
        if (nameOrId.matches("\\d+")) {
            response = GAME_CONTROLLER.selectCity(currentUsername, Integer.parseInt(nameOrId));
        } else {
            response = GAME_CONTROLLER.selectCity(currentUsername, nameOrId);
        }
        String msg = getField(response, "msg", String.class);
        return responseAndGo(msg, Menu.GAME);
    }

    public Menu unitAction(Matcher matcher) {
        // TODO
        return responseAndGo(null, Menu.GAME);
    }

    public Menu cityAction(Matcher matcher) {
        // TODO
        return responseAndGo(null, Menu.GAME);
    }

    public Menu showMap(Matcher matcher) {
        // TODO
        return responseAndGo(null, Menu.GAME);
    }

    public Menu moveMap(Matcher matcher) {
        // TODO
        return responseAndGo(null, Menu.GAME);
    }

    public JsonObject getInfoResponse(String arg) {
        switch (arg) {
            case "research":
                return GAME_CONTROLLER.infoResearch(currentUsername);

            case "units":
                return GAME_CONTROLLER.infoUnits(currentUsername);

            case "cities":
                return GAME_CONTROLLER.infoCities(currentUsername);

            case "diplomacy":
                return GAME_CONTROLLER.infoDiplomacy(currentUsername);

            case "victory":
                return GAME_CONTROLLER.infoVictory(currentUsername);

            case "demographics":
                return GAME_CONTROLLER.infoDemographics(currentUsername);

            case "notifications":
                return GAME_CONTROLLER.infoNotifications(currentUsername);

            case "military":
                return GAME_CONTROLLER.infoMilitary(currentUsername);

            case "economic":
                return GAME_CONTROLLER.infoEconomic(currentUsername);

            case "diplomatic":
                return GAME_CONTROLLER.infoDiplomatic(currentUsername);

            case "deals":
                return GAME_CONTROLLER.infoDeals(currentUsername);

            default:
                return null;
        }
    }
}
