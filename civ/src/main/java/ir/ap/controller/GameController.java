package ir.ap.controller;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ir.ap.model.*;
import ir.ap.model.TerrainType.TerrainFeature;
import ir.ap.model.Tile.TileKnowledge;

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

        INVALID_REQUEST("request is invalid"),
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

    public JsonObject serializeTile(Tile tile, Civilization civ) {
        if (tile == null || civ == null)
            return null;
        JsonObject tileJsonObj = new JsonObject();
        tileJsonObj.addProperty("index", tile.getIndex());
        tileJsonObj.addProperty("x", tile.getMapX());
        tileJsonObj.addProperty("y", tile.getMapY());
        tileJsonObj.addProperty("knowledge", gameArea.getTileKnowledgeByCivilization(civ, tile).name());
        if (gameArea.getTileKnowledgeByCivilization(civ, tile) != TileKnowledge.FOG_OF_WAR) {
            tileJsonObj.addProperty("terrainTypeId", tile.getTerrainType().getId());
            if (tile.getTerrainFeature() != null) {
                tileJsonObj.addProperty("terrainFeatureId", tile.getTerrainFeature().getId());
            }
            JsonObject hasRiver = new JsonObject();
            hasRiver.addProperty("up", tile.getHasRiverOnSide(Direction.UP));
            hasRiver.addProperty("upRight", tile.getHasRiverOnSide(Direction.UP_RIGHT));
            hasRiver.addProperty("downRight", tile.getHasRiverOnSide(Direction.DOWN_RIGHT));
            hasRiver.addProperty("down", tile.getHasRiverOnSide(Direction.DOWN));
            hasRiver.addProperty("downLeft", tile.getHasRiverOnSide(Direction.DOWN_LEFT));
            hasRiver.addProperty("upLeft", tile.getHasRiverOnSide(Direction.UP_LEFT));
            tileJsonObj.add("hasRiver", hasRiver);
        }
        if (gameArea.getTileKnowledgeByCivilization(civ, tile) == TileKnowledge.VISIBLE) {
            if (tile.getImprovement() != null) {
                tileJsonObj.addProperty("improvementId", tile.getImprovement().getId());
            }
            if (tile.getNonCombatUnit() != null) {
                JsonObject nonCombatUnit = new JsonObject();
                nonCombatUnit.addProperty("unitTypeId", tile.getNonCombatUnit().getUnitType().getId());
                nonCombatUnit.addProperty("civId", tile.getNonCombatUnit().getCivilization().getIndex());
                tileJsonObj.add("nonCombatUnit", nonCombatUnit);
            }
            if (tile.getCombatUnit() != null) {
                JsonObject combatUnit = new JsonObject();
                combatUnit.addProperty("unitTypeId", tile.getCombatUnit().getUnitType().getId());
                combatUnit.addProperty("civId", tile.getCombatUnit().getCivilization().getIndex());
                tileJsonObj.add("combatUnit", combatUnit);
            }
            if (tile.getOwnerCity() != null) {
                tileJsonObj.addProperty("ownerCivId", tile.getOwnerCity().getCivilization().getIndex());
            }
            if (tile.getCity() != null) {
                tileJsonObj.addProperty("cityInTile", tile.getCity().getName());
            }
        }
        return tileJsonObj;
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

    public JsonObject serializeProduction(Production production){
        JsonObject productionObj = new JsonObject();
        productionObj.addProperty("name", production.getName() );
        productionObj.addProperty("id", production.getId());
        productionObj.addProperty("cost", production.getCost());
        return productionObj;
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
            if (curUser == null) // TODO login in server PHASE2
                return messageToJsonObj(Message.USER_NOT_LOGGED_IN, false);
            Civilization curCiv = new Civilization(cnt++, curUser.getNickname() + ".civ", null);;
            gameArea.addUser(curUser, curCiv);
        }
        if (gameArea.getUserCount() < 2 || gameArea.getUserCount() > 8)
            return messageToJsonObj("at least 2 players and at most 8 should be in game", false);
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
        JsonObject response = new JsonObject();
        response.addProperty("msg", Message.GAME_STARTED.toString());
        setOk(response, true);
        response.add("civs", new JsonArray());
        for (String username : players) {
            JsonArray civsJson = response.get("civs").getAsJsonArray();
            JsonObject curCivJson = new JsonObject();
            Civilization curCiv = civController.getCivilizationByUsername(username);
            if (curCiv.getUnits().size() > 0) {
                int initX = curCiv.getUnits().get(0).getTile().getMapX();
                int initY = curCiv.getUnits().get(0).getTile().getMapY();
                JsonObject initPosJson = new JsonObject();
                initPosJson.addProperty("x", initX);
                initPosJson.addProperty("y", initY);
                curCivJson.add("initPos", initPosJson);
            }
            civsJson.add(curCivJson);
        }
        return response;
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
    // TODO: test
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
        if (!unit.getCivilization().equals(civilization))
            return messageToJsonObj("unit does not belong to your civ", false);
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
        if (!unit.getCivilization().equals(civilization))
            return messageToJsonObj("unit does not belong to your civ", false);
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
        if (!city.getCivilization().equals(civilization))
            return messageToJsonObj("city does not belong to your civ", false);
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
        if (!city.getCivilization().equals(civilization))
            return messageToJsonObj("city does not belong to your civ", false);
        civilization.setSelectedCity(city);
        return messageToJsonObj("city selected", true);
    }

    public JsonObject unitMoveTo(String username, int tileId, boolean cheat) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        Tile tile = gameArea.getMap().getTileByIndex(tileId);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (tile == null)
            return messageToJsonObj("invalid tileId", false);
        if (unitController.unitMoveTo(civilization, tile, cheat) == false)
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

    public JsonObject unitAttack(String username, int tileId, boolean cheat) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        Tile tile = gameArea.getMap().getTileByIndex(tileId);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (tile == null)
            return messageToJsonObj("invalid tileId", false);
        if (unitController.unitAttack(civilization, tile, cheat) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit Attacked", true);
    }

    public JsonObject unitFoundCity(String username, boolean cheat) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unitController.unitFoundCity(civilization, cheat) == false)
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

    public JsonObject unitBuildRoad(String username, boolean cheat) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unitController.unitBuildRoad(civilization, cheat) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit built road", true);
    }

    public JsonObject unitBuildRailRoad(String username, boolean cheat) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unitController.unitBuildRailRoad(civilization, cheat) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit built rail road", true);
    }
    // TODO: unit removes and build Improvement are not complete due to changes on weights and dists
    public JsonObject unitBuildImprovement(String username, int imprId, boolean cheat) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        Improvement improvement = Improvement.getImprovementById(imprId);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (improvement == null)
            return messageToJsonObj("invalid imprID", false);
        if (unitController.unitBuildImprovement(civilization, improvement, cheat) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit built improvement", true);
    }

    public JsonObject unitRemoveJungle(String username, boolean cheat) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unitController.unitRemoveJungle(civilization, cheat) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit removed jungle", true);
    }

    public JsonObject unitRemoveForest(String username, boolean cheat) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unitController.unitRemoveForest(civilization, cheat) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit removed forest", true);
    }

    public JsonObject unitRemoveMarsh(String username, boolean cheat) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unitController.unitRemoveMarsh(civilization, cheat) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit removed marsh", true);
    }

    public JsonObject unitRemoveRoute(String username, boolean cheat) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unitController.unitRemoveRoute(civilization, cheat) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit removed route", true);
    }

    public JsonObject unitRepair(String username, boolean cheat) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        if (unitController.unitRepair(civilization, cheat) == false)
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("unit repaired", true);
    }

    public JsonObject increaseGold(String username, int amount) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        int last_gold = civilization.getGold();
        civilization.setGold(last_gold+amount);        
        return messageToJsonObj("gold added successfully", true);
    }

    public JsonObject increaseTurn(String username, int amount) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject increaseHappiness(String username, int amount) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        civilization.addToHappiness(amount);        
        return messageToJsonObj("Happiness added successfully", true);
        // TODO: Happiness in civilization
    }

    public JsonObject getTileIndexByXY(int x, int y) {
        Tile tile = mapController.getTileByIndices(x, y);
        if (tile == null)
            return messageToJsonObj("No such tile", false);
        JsonObject response = new JsonObject();
        response.addProperty("id", tile.getIndex());
        return setOk(response, true);
    }

    public JsonObject getTerrainTypeNameById(int id) {
        TerrainType[] terrainTypes = TerrainType.values();
        for (TerrainType terrainType : terrainTypes) {
            if (terrainType.getId() == id) {
                JsonObject response = new JsonObject();
                response.addProperty("terrainTypeId", terrainType.getId());
                response.addProperty("terrainTypeName", terrainType.name());
                return setOk(response, true);
            }
        }
        return JSON_FALSE;
    }

    public JsonObject getAllTerrainTypeIds() {
        TerrainType[] terrainTypes = TerrainType.values();
        ArrayList<Integer> ids = new ArrayList<>();
        for (TerrainType terrainType : terrainTypes) {
            ids.add(terrainType.getId());
        }
        JsonObject response = new JsonObject();
        response.add("terrainTypeIds", GSON.fromJson(GSON.toJson(ids), JsonArray.class));
        return setOk(response, true);
    }

    public JsonObject getTerrainFeatureNameById(int id) {
        TerrainFeature[] terrainFeatures = TerrainFeature.values();
        for (TerrainFeature terrainFeature : terrainFeatures) {
            if (terrainFeature.getId() == id) {
                JsonObject response = new JsonObject();
                response.addProperty("terrainFeatureId", terrainFeature.getId());
                response.addProperty("terrainFeatureName", terrainFeature.name());
                return setOk(response, true);
            }
        }
        return JSON_FALSE;
    }

    public JsonObject getAllTerrainFeatureIds() {
        TerrainFeature[] terrainFeatures = TerrainFeature.values();
        ArrayList<Integer> ids = new ArrayList<>();
        for (TerrainFeature terrainFeature : terrainFeatures) {
            ids.add(terrainFeature.getId());
        }
        JsonObject response = new JsonObject();
        response.add("terrainFeatureIds", GSON.fromJson(GSON.toJson(ids), JsonArray.class));
        return setOk(response, true);
    }

    public JsonObject getUnitTypeNameById(int id) {
        UnitType[] unitTypes = UnitType.values();
        for (UnitType unitType : unitTypes) {
            if (unitType.getId() == id) {
                JsonObject response = new JsonObject();
                response.addProperty("unitTypeId", unitType.getId());
                response.addProperty("unitTypeName", unitType.name());
                return setOk(response, true);
            }
        }
        return JSON_FALSE;
    }

    public JsonObject getAllUnitTypeIds() {
        UnitType[] unitTypes = UnitType.values();
        ArrayList<Integer> ids = new ArrayList<>();
        for (UnitType unitType : unitTypes) {
            ids.add(unitType.getId());
        }
        JsonObject response = new JsonObject();
        response.add("unitTypeIds", GSON.fromJson(GSON.toJson(ids), JsonArray.class));
        return setOk(response, true);
    }

    public JsonObject getResourceNameById(int id) {
        Resource[] resources = Resource.values();
        for (Resource resource : resources) {
            if (resource.getId() == id) {
                JsonObject response = new JsonObject();
                response.addProperty("resourceId", resource.getId());
                response.addProperty("resourceName", resource.name());
                return setOk(response, true);
            }
        }
        return JSON_FALSE;
    }

    public JsonObject getAllResourceIds() {
        Resource[] resources = Resource.values();
        ArrayList<Integer> ids = new ArrayList<>();
        for (Resource resource : resources) {
            ids.add(resource.getId());
        }
        JsonObject response = new JsonObject();
        response.add("resourceIds", GSON.fromJson(GSON.toJson(ids), JsonArray.class));
        return setOk(response, true);
    }

    public JsonObject getImprovementNameById(int id) {
        Improvement[] improvements = Improvement.values();
        for (Improvement improvement : improvements) {
            if (improvement.getId() == id) {
                JsonObject response = new JsonObject();
                response.addProperty("improvementId", improvement.getId());
                response.addProperty("improvementName", improvement.name());
                return setOk(response, true);
            }
        }
        return JSON_FALSE;
    }

    public JsonObject getAllImprovementIds() {
        Improvement[] improvements = Improvement.values();
        ArrayList<Integer> ids = new ArrayList<>();
        for (Improvement improvement : improvements) {
            ids.add(improvement.getId());
        }
        JsonObject response = new JsonObject();
        response.add("improvementIds", GSON.fromJson(GSON.toJson(ids), JsonArray.class));
        return setOk(response, true);
    }

    public JsonObject getCivilizationNameById(int id) {
        ArrayList<Civilization> civilizations = civController.getAllCivilizations();
        for (Civilization civilization : civilizations) {
            if (civilization.getIndex() == id) {
                JsonObject response = new JsonObject();
                response.addProperty("civId", civilization.getIndex());
                response.addProperty("civName", civilization.getName());
                return setOk(response, true);
            }
        }
        return JSON_FALSE;
    }

    public JsonObject getAllCivilizationIds() {
        ArrayList<Civilization> civs = civController.getAllCivilizations();
        ArrayList<Integer> ids = new ArrayList<>();
        for (Civilization civ : civs) {
            ids.add(civ.getIndex());
        }
        JsonObject response = new JsonObject();
        response.add("civIds", GSON.fromJson(GSON.toJson(ids), JsonArray.class));
        return setOk(response, true);
    }

    public JsonObject mapShow(String username, int tileId) {
        Civilization civ = civController.getCivilizationByUsername(username);
        if (civ == null)
            return messageToJsonObj(Message.USER_NOT_ON_GAME, false);
        Tile tile = mapController.getTileById(tileId);
        if (tile == null)
            return messageToJsonObj(Message.INVALID_REQUEST, false);
        JsonObject response = new JsonObject();
        int height = 7, width = 9;
        response.addProperty("height", height);
        response.addProperty("width", width);
        response.add("map", new JsonArray());
        int tileX = tile.getMapX(), tileY = tile.getMapY();
        response.addProperty("focusId", tileId);
        response.addProperty("focusX", tileX);
        response.addProperty("focusY", tileY);
        int upLeftX = Math.max(0, tileX - height / 2);
        int upLeftY = Math.max(0, tileY - width / 2);
        upLeftX = Math.min(upLeftX, gameArea.getMap().getMapH() - height);
        upLeftY = Math.min(upLeftY, gameArea.getMap().getMapW() - width);
        for (int i = upLeftX; i < upLeftX + height; i++) {
            JsonArray row = new JsonArray();
            for (int j = upLeftY; j < upLeftY + width; j++) {
                row.add(serializeTile(mapController.getTileByIndices(i, j), civ));
            }
            ((JsonArray) response.get("map")).add(row);
        }
        return setOk(response, true);
    }

    public JsonObject mapShow(String username, String cityName) {
        City city = City.getCityByName(cityName);
        if (city == null || city.getTile() == null)
            return null;
        return mapShow(username, city.getTile().getIndex());
    }

    public JsonObject cityAddCitizenToWorkOnTile(String username, int tileId) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        City city = civilization.getSelectedCity();
        if ( city == null )
            return messageToJsonObj("no city selected", false);
        Tile tile = mapController.getTileById(tileId);
        if ( tile == null )
            return messageToJsonObj("invalid tileId", false);
        if ( cityController.cityAddCitizenToWorkOnTile(city, tile) == false )
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("citizen successfully added to work on tile", true);
    }

    public JsonObject cityRemoveCitizenFromWorkOnTile(String username, int tileId) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        City city = civilization.getSelectedCity();
        if ( city == null )
            return messageToJsonObj("no city selected", false);
        Tile tile = mapController.getTileById(tileId);
        if ( tile == null )
            return messageToJsonObj("invalid tileId", false);
        if ( cityController.cityRemoveCitizenFromWork(city, tile) == false )
            return messageToJsonObj("something is invalid", false);        
        return messageToJsonObj("citizen successfully removed from working on tile", true);
    }

    public JsonObject cityGetOutput(String username) {
        // TODO
        return JSON_FALSE;
    }

    public JsonObject cityGetUnemployedCitizens(String username) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        City city = civilization.getSelectedCity();
        if ( city == null )
            return messageToJsonObj("no city selected", false);
        if ( city.getPopulation() < city.getWorkingTiles().size() ) // Ehtemalan etefagh neofteh vali serfan baray etminan
            return messageToJsonObj("something is wrong in the city", false);
        JsonObject jsonObject =  new JsonObject();
        jsonObject.addProperty("unemployedCitizens", city.getPopulation()-city.getWorkingTiles().size());
        jsonObject.addProperty("ok", true);
        return jsonObject;
    }

    public JsonObject cityGetBuildings(String username) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        City city = civilization.getSelectedCity();
        if ( city == null )
            return messageToJsonObj("no city selected", false);        
        // TODO PHASE2
        return JSON_FALSE;
    } 
 
    public JsonObject cityPurchaseTile(String username, int tileId, boolean cheat) { 
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        City city = civilization.getSelectedCity();
        if ( city == null )
            return messageToJsonObj("no city selected", false);        
        Tile tile = mapController.getTileById(tileId);
        if ( tile == null )
            return messageToJsonObj("invalid tileId", false);
        if( cheat == true ){
            if( cityController.cityPurchaseTile(city, tile) == false )
                return messageToJsonObj("something is invalid", false);
        }
        else{
            if( civilization.getGold() < City.GOLD_NEEDED_TO_PURCHASE_TILE )
                return messageToJsonObj("the gold isn't enough", false);
            if( cityController.cityPurchaseTile(city, tile) == false )
                return messageToJsonObj("something is invalid", false);
            civilization.addToGold(-City.GOLD_NEEDED_TO_PURCHASE_TILE);
        }
        return messageToJsonObj("Tile succesfully added to city", true);
    }

    public JsonObject cityGetCurrentProduction(String username) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        City city = civilization.getSelectedCity();
        if ( city == null )
            return messageToJsonObj("no city selected", false);        
        Production production = city.getCurrentProduction();
        if( production == null )
            return messageToJsonObj("there is no production", false);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("production", serializeProduction(production));
        jsonObject.addProperty("ok", true);        
        return jsonObject;
    }

    public JsonObject citySetCurrentProduction(String username, int prodId, boolean cheat) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        City city = civilization.getSelectedCity();
        if ( city == null )
            return messageToJsonObj("no city selected", false);        
        Production[] allProductions = Production.getAllProductions();
        if( prodId >= allProductions.length )
            return messageToJsonObj("prodId is invalid", false);
        Production production = allProductions[ prodId ];
        if( cityController.cityChangeCurrentProduction(city, production, cheat) == false )
            return messageToJsonObj("something is invalid", false);
        return messageToJsonObj("current production changed successfully", true);
    }

    public JsonObject cityBuyProduction(String username, int prodId, boolean cheat) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        City city = civilization.getSelectedCity();
        if ( city == null )
            return messageToJsonObj("no city selected", false);        
        Production[] allProductions = Production.getAllProductions();
        if ( prodId >= allProductions.length )
            return messageToJsonObj("proId is invalid", false);
        Production production = allProductions[ prodId ];
        if ( cheat == true ){ 
            if ( cityController.cityChangeCurrentProduction(city, production, true) == false )
                return messageToJsonObj("something is invalid", false);        
        }
        else{
            if ( production.getCost() > civilization.getGold() )
                return messageToJsonObj("not enough gold", false);
            if ( cityController.cityChangeCurrentProduction(city, production, true) == false )
                return messageToJsonObj("something is invalid", false);
            int last_gold = civilization.getGold();
            civilization.setGold(last_gold-production.getCost());        
        }
        return messageToJsonObj("production successfully bought", true);
    }

    public JsonObject cityGetAllAvailableProductions(String username, boolean cheat) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        City city = civilization.getSelectedCity();
        if ( city == null )
            return messageToJsonObj("no city selected", false);        
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("productions", new JsonArray());
        if( cheat == true ){
            for (Production production : Production.getAllProductions()){
                ((JsonArray) jsonObject.get("productions")).add(serializeProduction(production));
            }
        }   
        else{
            for (Production production : Production.getAllProductions()){
                if( civilization.getTechnologyReached(production.getTechnologyRequired()) == true ){
                    ((JsonArray) jsonObject.get("productions")).add(serializeProduction(production));
                }
            }
        }
        jsonObject.addProperty("ok", true);     
        return jsonObject;
    }

    public JsonObject cityGetWorkingTiles(String username) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        City city = civilization.getSelectedCity();
        if ( city == null )
            return messageToJsonObj("no city selected", false);        
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("workingTiles", new JsonArray());    
        for ( Tile tile : city.getWorkingTiles() ){
            ((JsonArray) jsonObject.get("workingTiles")).add(serializeTile(tile, civilization));
        }
        jsonObject.addProperty("ok", true);
        return jsonObject;
    }

    public JsonObject cityDestroy(String username, int tileId) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        Tile tile = mapController.getTileById(tileId);
        if ( tile == null )
            return messageToJsonObj("invalid tileId", false);
        City city = tile.getCity();
        if ( city == null )
            return messageToJsonObj("no city selected", false);        
        if ( city.getCivilization() == civilization )        
            return messageToJsonObj("city is invalid", false);
        if ( cityController.cityDestroy(city, civilization) == false )
            return messageToJsonObj("something is invalid", false);  
        return messageToJsonObj("city destroyed successfully", true);
        // TODO: check if city is attacked and get by civilization (city.isDead()?)
    }

    public JsonObject cityAnnex(String username, int tileId) {
        Civilization civilization = civController.getCivilizationByUsername(username);
        if (civilization == null)
            return messageToJsonObj("invalid civUsername", false);
        Tile tile = mapController.getTileById(tileId);
        if ( tile == null )
            return messageToJsonObj("invalid tileId", false);
        City city = tile.getCity();
        if ( city == null )
            return messageToJsonObj("no city selected", false);
        if ( city.getCivilization() == civilization )        
            return messageToJsonObj("city is invalid", false);
        if ( cityController.cityAnnex(city, civilization) == false )
            return messageToJsonObj("something is invalid", false);  
        return messageToJsonObj("city Annexed successfully", true);
        // TODO: check if city is attacked and get by civilization (city.isDead)
    }
}
