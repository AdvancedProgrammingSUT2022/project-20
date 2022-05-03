package ir.ap.controller;

import java.io.FileReader;
import java.io.Reader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ir.ap.model.*;

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
        USER_NOT_ON_GAME("user is not on this game"),

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

    public JsonObject serializeCity(City city, Civilization civ) {
        if (!mapController.civCanSee(civ, city))
            return null;
        JsonObject cityObj = new JsonObject();
        cityObj.addProperty("name", city.getName());
        cityObj.addProperty("civName", city.getCivilization().getName());
        cityObj.addProperty("tileId", city.getTile().getIndex());
        cityObj.add("territory", new JsonArray());
        for (Tile tile : city.getTerritory()) {
            ((JsonArray) cityObj.get("territory")).add(tile.getIndex());
        }
        return cityObj;
    }

    public JsonObject serializeCiv(Civilization civ, Civilization otherCiv) {
        JsonObject civObj = new JsonObject();
        civObj.addProperty("index", civ.getIndex());
        civObj.addProperty("name", civ.getName());
        if (mapController.civCanSee(otherCiv, civ.getCapital())) {
            civObj.add("capital", serializeCity(civ.getCapital(), otherCiv));
        }
        civObj.add("cities", new JsonArray());
        for (City city : civ.getCities()) {
            if (mapController.civCanSee(otherCiv, city)) {
                ((JsonArray) civObj.get("cities")).add(serializeCity(city, otherCiv));
            }
        }
        return civObj;
    }

    public JsonObject getCivilizationByUsername(String username) {
        Civilization civ = civController.getCivilizationByUsername(username);
        if (civ == null)
            return messageToJsonObj(Message.USER_NOT_ON_GAME, false);
        JsonObject response = new JsonObject();
        response.addProperty("civName", civ.getName());
        return setOk(response, true);
    }

    public JsonObject getAllCivilizations(String username) {
        Civilization civ = civController.getCivilizationByUsername(username);
        if (civ == null)
            return messageToJsonObj(Message.USER_NOT_ON_GAME, false);
        JsonObject response = new JsonObject();
        response.add("civs", new JsonArray());
        for (Civilization curCiv : civController.getAllCivilizations()) {
            ((JsonArray) response.get("civs")).add(serializeCiv(curCiv, civ));
        }
        return setOk(response, true);
    }

    public JsonObject newGame(String[] players) {
        gameArea = new GameArea(System.currentTimeMillis());
        int cnt = 0;
        for (String username : players) {
            if (gameArea.getUserByUsername(username) != null)
                return messageToJsonObj("duplicate users", false);
            User curUser = User.getUser(username);
            if (curUser == null) // TODO login in server
                return messageToJsonObj(Message.USER_NOT_LOGGED_IN, false);
            Civilization curCiv = new Civilization(cnt++, curUser.getNickname() + ".civ", null);
            gameArea.addUser(curUser, curCiv);
        }
        if (gameArea.getUserCount() < 2)
            return messageToJsonObj("at least 2 players should be in game", false);
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
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid username", false);
        boolean end = civController.nextTurn(civilization);
        JsonObject response = new JsonObject();
        response.addProperty("end", end);
        if (end)
            response.addProperty("msg", "Game Ended");
        setOk(response, true);
        return response;
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

    public JsonObject infoDiplomacy(String username) {
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
    // TODO: cheat
    public JsonObject selectCombatUnit(String username, int tileId) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        Tile tile = gameArea.getMap().getTileByIndex(tileId);
        if (tile == null)
            return messageToJsonObj("invalid tileID", false);
        Unit unit = tile.getCombatUnit();
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unit == null)
            return messageToJsonObj("selected tile doesn't have unit", false);
        civilization.setSelectedUnit(unit);
        return messageToJsonObj("unit selected", true);
    }

    public JsonObject selectNonCombatUnit(String username, int tileId) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        Tile tile = gameArea.getMap().getTileByIndex(tileId);
        if (tile == null)
            return messageToJsonObj("invalid tileID", false);
        Unit unit = tile.getNonCombatUnit();
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unit == null)
            return messageToJsonObj("selected tile doesn't have unit", false);
        civilization.setSelectedUnit(unit);
        return messageToJsonObj("unit selected", true);
    }

    public JsonObject selectCity(String username, int tileId) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        Tile tile = gameArea.getMap().getTileByIndex(tileId);
        if (tile == null)
            return messageToJsonObj("invalid tileID", false);
        City city = tile.getCity();
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (city == null)
            return messageToJsonObj("selected tile doesn't have city", false);
        civilization.setSelectedCity(city);
        return messageToJsonObj("city selected", true);
    }

    public JsonObject selectCity(String username, String cityName) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        City city = City.getCityByName(cityName);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (city == null)
            return messageToJsonObj("invalid cityName", false);
        civilization.setSelectedCity(city);
        return messageToJsonObj("city selected", true);
    }

    public JsonObject unitMoveTo(String username, int tileId) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        Tile tile = gameArea.getMap().getTileByIndex(tileId);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (tile == null)
            return messageToJsonObj("invalid tileId", false);
        if (unitController.unitMoveTo(civilization, tile) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit moved", true);
    }

    public JsonObject unitSleep(String username) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unitController.unitSleep(civilization) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit slept", true);
    }

    public JsonObject unitAlert(String username) {

        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unitController.unitAlert(civilization) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit Alerted", true);
    }

    public JsonObject unitFortify(String username) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unitController.unitFortify(civilization) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit fortified", true);
    }

    public JsonObject unitFortifyUntilHeal(String username) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unitController.unitFortifyHeal(civilization) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit fortified to heal", true);
    }

    public JsonObject unitGarrison(String username) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unitController.unitGarrison(civilization) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit has been garrison", true);
    }

    public JsonObject unitSetupRanged(String username) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unitController.unitSetupForRangedAttack(civilization) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit has been set up ranged", true);
    }

    public JsonObject unitAttack(String username, int tileId) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        Tile tile = gameArea.getMap().getTileByIndex(tileId);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (tile == null)
            return messageToJsonObj("invalid tileId", false);
        if (unitController.unitAttack(civilization, tile) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit Attacked", true);
    }

    public JsonObject unitFoundCity(String username) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unitController.unitFoundCity(civilization) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit found city", true);
    }

    public JsonObject unitCancelMission(String username) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unitController.unitCancelMission(civilization) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit mission canceled", true);
    }

    public JsonObject unitWake(String username) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unitController.unitWake(civilization) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit has been set up ranged", true);
    }

    public JsonObject unitDelete(String username) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        Unit unit = civilization.getSelectedUnit();
        if (unit == null)
            return messageToJsonObj("we don`t have selected unit", false);
        unitController.removeUnit(unit);
        return messageToJsonObj("has been deleted successfully", true);
    }

    public JsonObject unitBuildRoad(String username) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unitController.unitBuildRoad(civilization) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit built road", true);
    }

    public JsonObject unitBuildRailRoad(String username) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unitController.unitBuildRailRoad(civilization) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit built rail road", true);
    }

    public JsonObject unitBuildImprovement(String username, int imprId) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        Improvement improvement = Improvement.getImprovementById(imprId);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (improvement == null)
            return messageToJsonObj("invalid imprID", false);
        if (unitController.unitBuildImprovement(civilization, improvement) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit built improvement", true);
    }

    public JsonObject unitRemoveJungle(String username) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unitController.unitRemoveJungle(civilization) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit removed jungle", true);
    }

    public JsonObject unitRemoveForest(String username) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unitController.unitRemoveForest(civilization) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit removed forest", true);
    }

    public JsonObject unitRemoveMarsh(String username) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unitController.unitRemoveMarsh(civilization) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit removed marsh", true);
    }

    public JsonObject unitRemoveRoute(String username) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unitController.unitRemoveRoute(civilization) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit removed route", true);
    }

    public JsonObject unitRepair(String username) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unitController.unitRepair(civilization) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit repaired", true);
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
