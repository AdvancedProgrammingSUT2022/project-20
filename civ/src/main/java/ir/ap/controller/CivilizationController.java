package ir.ap.controller;

import com.google.gson.JsonObject;
import ir.ap.model.*;
import ir.ap.model.UnitType.UnitAction;

import java.util.ArrayList;

public class CivilizationController extends AbstractGameController {
    public CivilizationController(GameArea gameArea) {
        super(gameArea);
    }

    public Civilization getCivilizationByUsername(String username)
    {
        Civilization civilization = gameArea.getCivilizationByUser(gameArea.getUserByUsername(username));
        return civilization;
    }

    public ArrayList<Civilization> getAllCivilizations()
    {
        ArrayList<Civilization> civilizations = new ArrayList<Civilization>();
        for(Civilization civilization : gameArea.getCiv2user().keySet())
            civilizations.add(civilization);
        return civilizations;
    }

    public ArrayList<User> getAllPlayers() {
        ArrayList<User> Users = new ArrayList<User>();
        for (User user : gameArea.getCiv2user().values())
            Users.add(user);
        return Users;
    }

    public boolean nextTurn(Civilization civilization)
    {
        // TODO: check knoim aslan mishe raft turn baadi?!
        civilization.addToGold(civilization.getGoldYield());
        civilization.addToScience(civilization.getScienceYield());
        if (civilization.getGold() < 0) {
            civilization.addToGold(2);
            civilization.addToScience(-2);
        }
        if (civilization.getCurrentResearch() != null && civilization.getScience() > 0) {
            civilization.addToScienceSpentForCurrentResearch(civilization.getScience());
            civilization.setScience(0);
            if (civilization.getScienceLeftForResearchFinish() <= 0) {
                civilization.finishResearch();
            }
        }
        for(Unit unit : civilization.getUnits())
        {
            if (unit.getUnitAction() == UnitAction.MOVETO) {
                unitController.moveUnitTowardsTarget(unit);
            }

            unit.resetMp();

            if(unit.getUnitAction() == UnitType.UnitAction.ALERT) {
                Tile tile = unit.getTile();
                for(Tile enemyTile : tile.getNeighbors()) {
                    if (enemyTile == null) continue;
                    Unit enemyUnit = enemyTile.getCombatUnit();
                    if(enemyUnit == null) enemyUnit = enemyTile.getNonCombatUnit();
                    if(enemyUnit != null) {
                        unitController.unitWake(civilization);
                    }
                }
            }

            if (unit.getUnitType() == UnitType.WORKER) {
                Tile tile = unit.getTile();
                if (unit.getUnitAction() == UnitType.UnitAction.REPAIR) {
                    if (tile.hasImprovement()) {
                        Improvement improvement = tile.getImprovement();
                        improvement.setIsDead(false);
                        tile.setImprovement(improvement);
                        unit.setHowManyTurnWeKeepAction(0);
                        unit.setUnitAction(null);
                    }
                    if (tile.hasCity()) {
                        City city = tile.getCity();
                        city.setHp(Math.min(city.getHp() + 3, 20));
                        tile.setCity(city);
                        if (city.getHp() >= 20) {
                            unit.setHowManyTurnWeKeepAction(0);
                            unit.setUnitAction(null);;
                        }
                    }
                }
                if (unit.getUnitAction() == UnitType.UnitAction.FORTIFY_HEAL) {
                    unit.setHp(Math.min(unit.getHp() + 3, Unit.getMaxHp()));
                    if (unit.getHp() >= Unit.getMaxHp()) {
                        unit.setHowManyTurnWeKeepAction(0);
                        unit.setUnitAction(null);
                    }
                }

                if (unit.getHowManyTurnWeKeepAction() >= 6) {
                    if (unit.getUnitAction() == UnitType.UnitAction.BUILD_CAMP) {
                        mapController.addImprovement(tile, Improvement.CAMP);
                        unit.setHowManyTurnWeKeepAction(0);
                    }
                    if (unit.getUnitAction() == UnitType.UnitAction.BUILD_FARM) {
                        mapController.addImprovement(tile, Improvement.FARM);
                        unit.setHowManyTurnWeKeepAction(0);
                    }
                    if (unit.getUnitAction() == UnitType.UnitAction.BUILD_LUMBERMILL) {
                        mapController.addImprovement(tile, Improvement.LUMBER_MILL);
                        unit.setHowManyTurnWeKeepAction(0);
                    }
                    if (unit.getUnitAction() == UnitType.UnitAction.BUILD_MINE) {
                        mapController.addImprovement(tile, Improvement.MINE);
                        unit.setHowManyTurnWeKeepAction(0);
                    }
                    if (unit.getUnitAction() == UnitType.UnitAction.BUILD_PASTURE) {
                        mapController.addImprovement(tile, Improvement.PASTURE);
                        unit.setHowManyTurnWeKeepAction(0);
                    }
                    if (unit.getUnitAction() == UnitType.UnitAction.BUILD_PLANTATION) {
                        mapController.addImprovement(tile, Improvement.PLANTATION);
                        unit.setHowManyTurnWeKeepAction(0);
                    }
                    if (unit.getUnitAction() == UnitType.UnitAction.BUILD_QUARRY) {
                        mapController.addImprovement(tile, Improvement.QUARRY);
                        unit.setHowManyTurnWeKeepAction(0);
                    }
                    if (unit.getUnitAction() == UnitType.UnitAction.BUILD_TRADINGPOST) {
                        mapController.addImprovement(tile, Improvement.TRADING_POST);
                        unit.setHowManyTurnWeKeepAction(0);
                    }
                } else
                    unit.setHowManyTurnWeKeepAction(unit.getHowManyTurnWeKeepAction() + 1);

                if (unit.getUnitAction() == UnitType.UnitAction.BUILD_RAILROAD) {
                    if (unit.getHowManyTurnWeKeepAction() >= 3) {
                        mapController.addRailRoad(tile);
                        unit.setHowManyTurnWeKeepAction(0);
                    } else
                        unit.setHowManyTurnWeKeepAction(unit.getHowManyTurnWeKeepAction() + 1);
                }
                if (unit.getUnitAction() == UnitType.UnitAction.BUILD_ROAD) {
                    if (unit.getHowManyTurnWeKeepAction() >= 3) {
                        mapController.addRoad(tile);
                        unit.setHowManyTurnWeKeepAction(0);
                    } else
                        unit.setHowManyTurnWeKeepAction(unit.getHowManyTurnWeKeepAction() + 1);
                }
                if (unit.getUnitAction() == UnitType.UnitAction.REMOVE_ROUTE) {
                    mapController.removeRoad(tile);
                    mapController.removeRailRoad(tile);
                    unit.setHowManyTurnWeKeepAction(0);
                }
                if (unit.getUnitAction() == UnitType.UnitAction.REMOVE_FOREST) {
                    if (unit.getHowManyTurnWeKeepAction() >= 4) {
                        mapController.removeTerrainFeature(tile);
                        unit.setHowManyTurnWeKeepAction(0);
                    } else
                        unit.setHowManyTurnWeKeepAction(unit.getHowManyTurnWeKeepAction() + 1);
                }
                if (unit.getUnitAction() == UnitType.UnitAction.REMOVE_JUNGLE) {
                    if (unit.getHowManyTurnWeKeepAction() >= 7) {
                        mapController.removeTerrainFeature(tile);
                        unit.setHowManyTurnWeKeepAction(0);
                    } else
                        unit.setHowManyTurnWeKeepAction(unit.getHowManyTurnWeKeepAction() + 1);
                }
                if (unit.getUnitAction() == UnitType.UnitAction.REMOVE_MARSH) {
                    if (unit.getHowManyTurnWeKeepAction() >= 6) {
                        mapController.removeTerrainFeature(tile);
                        unit.setHowManyTurnWeKeepAction(0);
                    } else
                        unit.setHowManyTurnWeKeepAction(unit.getHowManyTurnWeKeepAction() + 1);
                }
            }
        }
        for (City city : civilization.getCities()) {
            cityController.nextTurn(city);
        }
        gameArea.nextTurn();
        return gameArea.end();
    }

    public void selectCity(Civilization civilization, City city)
    {
        civilization.setSelectedUnit(null);
        civilization.setSelectedCity(city);
    }

    public void selectUnit(Civilization civilization, Unit unit)
    {
        civilization.setSelectedCity(null);
        civilization.setSelectedUnit(unit);
    }
}
