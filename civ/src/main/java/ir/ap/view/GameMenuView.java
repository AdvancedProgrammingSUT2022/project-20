package ir.ap.view;

import java.util.regex.Matcher;

import com.google.gson.JsonObject;

public class GameMenuView extends AbstractMenuView {

    public enum Command {
        ENTER_MENU("menu enter (?<menuName>\\w+)"),
        EXIT_MENU("menu exit"),
        SHOW_MENU("menu show-current"),
        INFO("info (?<arg>\\S+)"),
        SELECT_UNIT("select unit (?<type>combat|noncombat) (?<tileId>\\d+)"),
        SELECT_CITY("select city (?<nameOrId>\\S+)"),
        UNIT_ACTION("unit (?<args>.*)"),
        CITY_ACTION("city (?<args>.*)"),
        NEXT_TURN("next turn"),
        INCREASE("increase (?<args>.*)");

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
        ARG_GOLD("--gold"),
        ARG_TURN("--turn"),
        ARG_HAPPINESS("--happiness");

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

        INVALID_REQUEST("invalid request"),
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

    private String[] usersInGame;
    private String[] civsInGame;
    private int currentTurnId;
    private String currentPlayer;
    private String currentCiv;
    // TODO: cheat

    public Enum<?>[] getCommands() {
        return Command.values();
    }

    public void init(String[] users) {
        usersInGame = users;
        civsInGame = new String[usersInGame.length];
        for (int i = 0; i < users.length; i++) {
            civsInGame[i] = getField(GAME_CONTROLLER.getCivilizationByUsername(users[i]),
                    "civName", String.class);
        }
        currentTurnId = 0;
        currentPlayer = usersInGame[currentTurnId];
        currentCiv = civsInGame[currentTurnId];
        System.out.format("Turn: %s\n", currentCiv);
    }

    public Menu nextTurn(Matcher matcher) {
        JsonObject response = GAME_CONTROLLER.nextTurn(currentPlayer);
        String msg = getField(response, "msg", String.class);
        if (responseOk(response)) {
            ++currentTurnId;
            currentTurnId %= usersInGame.length;
            currentPlayer = usersInGame[currentTurnId];
            currentCiv = civsInGame[currentTurnId];
            if (getField(response, "end", Boolean.class) == true) {
                return responseAndGo(msg, Menu.MAIN);
            }
            System.out.format("Turn: %s\n", currentCiv);
        }
        return responseAndGo(msg, Menu.GAME);
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
                ? GAME_CONTROLLER.selectCombatUnit(currentPlayer, tileId)
                : GAME_CONTROLLER.selectNonCombatUnit(currentPlayer, tileId));
        if (response == null)
            return responseAndGo(Message.INVALID_REQUEST, Menu.GAME);
        String msg = getField(response, "msg", String.class);
        return responseAndGo(msg, Menu.GAME);
    }

    public Menu selectCity(Matcher matcher) {
        String nameOrId = matcher.group("nameOrId");
        JsonObject response;
        if (nameOrId.matches("\\d+")) {
            response = GAME_CONTROLLER.selectCity(currentPlayer, Integer.parseInt(nameOrId));
        } else {
            response = GAME_CONTROLLER.selectCity(currentPlayer, nameOrId);
        }
        if (response == null)
            return responseAndGo(Message.INVALID_REQUEST, Menu.GAME);
        String msg = getField(response, "msg", String.class);
        return responseAndGo(msg, Menu.GAME);
    }

    public Menu unitAction(Matcher matcher) {
        String[] args = matcher.group("args").trim().split("\\s+");
        boolean cheat = false;
        for (String arg : args) {
            cheat |= arg.matches("(--cheat|-c)");
        }
        JsonObject response = getUnitActionResponse(args, cheat);
        if (response == null)
            return responseAndGo(Message.INVALID_REQUEST, Menu.GAME);
        String msg = getField(response, "msg", String.class);
        return responseAndGo(msg, Menu.GAME);
    }

    public Menu cityAction(Matcher matcher) {
        String[] args = matcher.group("args").trim().split("\\s+");
        boolean cheat = false;
        for (String arg : args) {
            cheat |= arg.matches("(--cheat|-c)");
        }
        JsonObject response = getCityActionResponse(args, cheat);
        if (response == null)
            return responseAndGo(Message.INVALID_REQUEST, Menu.GAME);
        String msg = getField(response, "msg", String.class);
        return responseAndGo(msg, Menu.GAME);
    }

    public Menu showMap(Matcher matcher) {
        // TODO
        return responseAndGo(null, Menu.GAME);
    }

    public Menu moveMap(Matcher matcher) {
        // TODO
        return responseAndGo(null, Menu.GAME);
    }

    public Menu increase(Matcher matcher) {
        String[] args = matcher.group("args").trim().split("\\s+");
        for (int i = 0; i < args.length; i++) {
            if (args[i].matches(Validator.ARG_GOLD.toString())) {
                if (i + 1 == args.length || !args[i + 1].matches("-?\\d+")) {
                    return responseAndGo(Message.INVALID_REQUEST, Menu.GAME);
                } else {
                    int amount = Integer.parseInt(args[i + 1]);
                    JsonObject response = GAME_CONTROLLER.increaseGold(currentPlayer, amount);
                    if (!responseOk(response)) {
                        System.out.println(Message.E500);
                        continue;
                    }
                    String msg = getField(response, "msg", String.class);
                    System.out.println(msg);
                }
            } else if (args[i].matches(Validator.ARG_TURN.toString())) {
                if (i + 1 == args.length || !args[i + 1].matches("\\d+")) {
                    return responseAndGo(Message.INVALID_REQUEST, Menu.GAME);
                } else {
                    int amount = Integer.parseInt(args[i + 1]);
                    JsonObject response = GAME_CONTROLLER.increaseTurn(currentPlayer, amount);
                    if (!responseOk(response)) {
                        System.out.println(Message.E500);
                        continue;
                    }
                    String msg = getField(response, "msg", String.class);
                    System.out.println(msg);
                }
            } else if (args[i].matches(Validator.ARG_HAPPINESS.toString())) {
                if (i + 1 == args.length || !args[i + 1].matches("\\d+")) {
                    return responseAndGo(Message.INVALID_REQUEST, Menu.GAME);
                } else {
                    int amount = Integer.parseInt(args[i + 1]);
                    JsonObject response = GAME_CONTROLLER.increaseHappiness(currentPlayer, amount);
                    if (!responseOk(response)) {
                        System.out.println(Message.E500);
                        continue;
                    }
                    String msg = getField(response, "msg", String.class);
                    System.out.println(msg);
                }
            } else {
                return responseAndGo(Message.ARG_INVALID.toString()
                        .replace("%s", args[i]), Menu.GAME);
            }
        }
        return responseAndGo(null, Menu.GAME);
    }

    public JsonObject getInfoResponse(String arg) {
        switch (arg) {
            case "research":
                return GAME_CONTROLLER.infoResearch(currentPlayer);
            case "units":
                return GAME_CONTROLLER.infoUnits(currentPlayer);
            case "cities":
                return GAME_CONTROLLER.infoCities(currentPlayer);
            case "diplomacy":
                return GAME_CONTROLLER.infoDiplomacy(currentPlayer);
            case "victory":
                return GAME_CONTROLLER.infoVictory(currentPlayer);
            case "demographics":
                return GAME_CONTROLLER.infoDemographics(currentPlayer);
            case "notifications":
                return GAME_CONTROLLER.infoNotifications(currentPlayer);
            case "military":
                return GAME_CONTROLLER.infoMilitary(currentPlayer);
            case "economic":
                return GAME_CONTROLLER.infoEconomic(currentPlayer);
            case "diplomatic":
                return GAME_CONTROLLER.infoDiplomatic(currentPlayer);
            case "deals":
                return GAME_CONTROLLER.infoDeals(currentPlayer);
            default:
                return null;
        }
    }

    public JsonObject getUnitActionResponse(String[] args, boolean cheat) {
        int tileId;
        switch (args[0]) {
            case "moveto":
                try {
                    tileId = Integer.parseInt(args[1]);
                } catch (Exception ex) {
                    return null;
                }
                return GAME_CONTROLLER.unitMoveTo(currentPlayer, tileId, cheat);
            case "sleep":
                return GAME_CONTROLLER.unitSleep(currentPlayer);
            case "alert":
                return GAME_CONTROLLER.unitAlert(currentPlayer);
            case "fortify":
                if (args.length > 1 && args[1].equals("heal"))
                    return GAME_CONTROLLER.unitFortifyUntilHeal(currentPlayer);
                return GAME_CONTROLLER.unitFortify(currentPlayer);
            case "garrison":
                return GAME_CONTROLLER.unitGarrison(currentPlayer);
            case "setup":
                if (args.length > 1 && args[1].equals("ranged"))
                    return GAME_CONTROLLER.unitSetupRanged(currentPlayer);
                return null;
            case "attack":
                try {
                    tileId = Integer.parseInt(args[1]);
                } catch (Exception ex) {
                    return null;
                }
                return GAME_CONTROLLER.unitAttack(currentPlayer, tileId, cheat);
            case "found":
                if (args.length > 1 && args[1].equals("city"))
                    return GAME_CONTROLLER.unitFoundCity(currentPlayer, cheat);
                return null;
            case "cancel":
                if (args.length > 1 && args[1].equals("mission"))
                    return GAME_CONTROLLER.unitCancelMission(currentPlayer);
                return null;
            case "wake":
                return GAME_CONTROLLER.unitWake(currentPlayer);
            case "delete":
                return GAME_CONTROLLER.unitDelete(currentPlayer);
            case "build":
                return getUnitBuildResponse(args, cheat);
            case "remove":
                if (args.length == 1)
                    return null;
                switch (args[1]) {
                    case "jungle":
                        return GAME_CONTROLLER.unitRemoveJungle(currentPlayer, cheat);
                    case "forest":
                        return GAME_CONTROLLER.unitRemoveForest(currentPlayer, cheat);
                    case "marsh":
                        return GAME_CONTROLLER.unitRemoveMarsh(currentPlayer, cheat);
                    case "route":
                        return GAME_CONTROLLER.unitRemoveRoute(currentPlayer, cheat);
                    default:
                        return null;
                }
            case "repair":
                return GAME_CONTROLLER.unitRepair(currentPlayer, cheat);
            default:
                return null;
        }
    }

    public JsonObject getUnitBuildResponse(String[] args, boolean cheat) {
        if (args.length <= 1 || !args[0].equals("build"))
            return null;
        switch (args[1]) {
            case "road":
                return GAME_CONTROLLER.unitBuildRoad(currentPlayer, cheat);
            case "railroad":
                return GAME_CONTROLLER.unitBuildRailRoad(currentPlayer, cheat);
            case "camp":
                return GAME_CONTROLLER.unitBuildImprovement(currentPlayer, 1, cheat);
            case "farm":
                return GAME_CONTROLLER.unitBuildImprovement(currentPlayer, 2, cheat);
            case "lumbermill":
                return GAME_CONTROLLER.unitBuildImprovement(currentPlayer, 3, cheat);
            case "mine":
                return GAME_CONTROLLER.unitBuildImprovement(currentPlayer, 4, cheat);
            case "pasture":
                return GAME_CONTROLLER.unitBuildImprovement(currentPlayer, 5, cheat);
            case "plantation":
                return GAME_CONTROLLER.unitBuildImprovement(currentPlayer, 6, cheat);
            case "quarry":
                return GAME_CONTROLLER.unitBuildImprovement(currentPlayer, 7, cheat);
            case "tradingpost":
                return GAME_CONTROLLER.unitBuildImprovement(currentPlayer, 8, cheat);
            case "factory":
                return GAME_CONTROLLER.unitBuildImprovement(currentPlayer, 9, cheat);
            default:
                return null;
        }
    }

    public JsonObject getCityActionResponse(String[] args, boolean cheat) {
        int tileId;
        switch (args[0]) {
            case "work":
                try {
                    tileId = Integer.parseInt(args[1]);
                    return GAME_CONTROLLER.cityAddCitizenToWorkOnTile(currentPlayer, tileId);
                } catch (Exception ex) {
                }
                if (args.length <= 1)
                    return null;
                if (args[1].equals("delete")) {
                    try {
                        tileId = Integer.parseInt(args[2]);
                    } catch (Exception ex) {
                        return null;
                    }
                    return GAME_CONTROLLER.cityRemoveCitizenFromWorkOnTile(currentPlayer, tileId);
                } else if (args[1].equals("all")) {
                    return GAME_CONTROLLER.cityGetWorkingTiles(currentPlayer);
                } else {
                    return null;
                }
            case "output":
                return GAME_CONTROLLER.cityGetOutput(currentPlayer);
            case "unemployed":
                return GAME_CONTROLLER.cityGetUnemployedCitizens(currentPlayer);
            case "buildings":
                return GAME_CONTROLLER.cityGetBuildings(currentPlayer);
            case "purchase":
                if (args.length <= 1)
                    return null;
                if (args[1].equals("tile")) {
                    try {
                        tileId = Integer.parseInt(args[2]);
                    } catch (Exception ex) {
                        return null;
                    }
                    return GAME_CONTROLLER.cityPurchaseTile(currentPlayer, tileId, cheat);
                } else if (args[1].equals("production")) {
                    int prodId;
                    try {
                        prodId = Integer.parseInt(args[2]);
                    } catch (Exception ex) {
                        return null;
                    }
                    return GAME_CONTROLLER.cityBuyProduction(currentPlayer, prodId, cheat);
                } else {
                    return null;
                }
            case "production":
                if (args.length == 1)
                    return GAME_CONTROLLER.cityGetCurrentProduction(currentPlayer);
                if (args[1].equals("show")) {
                    return GAME_CONTROLLER.cityGetCurrentProduction(currentPlayer);
                } else if (args[1].equals("set")) {
                    int prodId;
                    try {
                        prodId = Integer.parseInt(args[2]);
                    } catch (Exception ex) {
                        return null;
                    }
                    return GAME_CONTROLLER.citySetCurrentProduction(currentPlayer, prodId, cheat);
                } else if (args[1].equals("all")) {
                    return GAME_CONTROLLER.cityGetAllAvailableProductions(currentPlayer, cheat);
                } else {
                    return null;
                }
            case "destroy":
                try {
                    tileId = Integer.parseInt(args[1]);
                    return GAME_CONTROLLER.cityDestroy(currentPlayer, tileId, cheat);
                } catch (Exception ex) {
                    return null;
                }
            case "annex":
                try {
                    tileId = Integer.parseInt(args[1]);
                    return GAME_CONTROLLER.cityAnnex(currentPlayer, tileId, cheat);
                } catch (Exception ex) {
                    return null;
                }
            default:
                return null;
        }
    }
}
