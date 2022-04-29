package ir.ap.controller;

import java.io.FileReader;
import java.io.Reader;

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
        // TODO
        return JSON_FALSE;
    }

    public JsonObject getAllCivilizations() {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject getAllPlayers() {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject newGame(String[] players) {
        gameArea = new GameArea(System.currentTimeMillis());
        int cnt = 0;
        for (String username : players) {
            User curUser = User.getUser(username);
            if (curUser == null || !curUser.isLogin())
                return messageToJsonObj(Message.USER_NOT_LOGGED_IN, false);
            Civilization curCiv = new Civilization(cnt++, curUser.getNickname() + ".civ", null);
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

    public JsonObject nextTurn(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject infoResearch(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject infoUnits(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject infoCities(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject infoVictory(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject infoDemographics(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject infoNotifications(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject infoMilitary(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject infoEconomic(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject infoDiplomatic(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject infoDeals(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject selectCombatUnit(String username, int tileId) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject selectNonCombatUnit(String username, int tileId) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject selectCity(String username, int tileId) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject selectCity(String username, String cityName) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject unitMoveTo(String username, int tileId) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject unitSleep(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject unitAlert(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject unitFortify(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject unitFortifyUntilHeal(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject unitGarrison(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject unitSetupRanged(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject unitAttack(String username, int tileId) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject unitFoundCity(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject unitCancelMission(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject unitWake(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject unitDelete(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject unitBuildRoad(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject unitBuildRailRoad(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject unitBuildBridge(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject unitBuildImprovement(String username, int imprId) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject unitRemoveJungle(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject unitRemoveForest(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject unitRemoveMarsh(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject unitRemoveRoute(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject unitRepair(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject mapShow(String username, int tileId) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject mapShow(String username, String cityName) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject mapMove(String username, int dirId, int count) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject mapMove(String username, int dirId) {
        return mapMove(username, dirId, 1);
    }

    public JsonObject cityAddCitizenToWorkOnTile(String username, int tileId) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject cityRemoveCitizenFromWorkOnTile(String username, int tileId) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject cityGetOutput(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject cityGetUnemployedCitizens(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject cityGetBuildings(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject cityPurchaseTile(String username, int tileId) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject cityGetCurrentProduction(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject citySetCurrentProduction(String username, int prodId) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject cityBuyProduction(String username, int prodId) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject cityGetAllAvailableProductions(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject cityGetWorkingTiles(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject cityDestroy(String username, int tileId) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject cityAnnex(String username, int tileId) {
        // TODO
        return JSON_FALSE;
    }
}
