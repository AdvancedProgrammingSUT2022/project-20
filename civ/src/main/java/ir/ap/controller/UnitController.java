package ir.ap.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import ir.ap.model.*;
import ir.ap.model.Tile.TileKnowledge;

public class UnitController extends AbstractGameController {
    public UnitController(GameArea gameArea) {
        super(gameArea);
    }

    public Set<Tile> getUnitVisitingTilesInRange(Unit unit, int range) {
        if (unit == null) return null;
        Tile tile = unit.getTile();
        if (tile == null) return null;
        Set<Tile> retTiles = new HashSet<>();
        HashMap<Tile, Boolean> visited = new HashMap<>();
        HashMap<Tile, Integer> dist = new HashMap<>();
        Queue<Tile> queue = new LinkedList<>();
        queue.add(tile);
        dist.put(tile, 0);
        while (!queue.isEmpty()) {
            Tile adjTile = queue.poll();
            if (visited.get(adjTile) != null) continue;
            visited.put(adjTile, true);
            if (dist.get(adjTile) == null || dist.get(adjTile) > range) continue;
            retTiles.add(adjTile);
            if (!adjTile.isBlock()) {
                for (Tile tileInDepth : adjTile.getNeighbors()) {
                    queue.add(tileInDepth);
                }
            }
        }
        return retTiles;
    }

    public Set<Tile> getUnitVisitingTiles(Unit unit) {
        return getUnitVisitingTilesInRange(unit, unit.getVisitingRange());
    }

    public boolean addUnit(Civilization civilization, Tile tile, UnitType unitType){
        Unit unit = new Unit(unitType, civilization, tile);
        civilization.addUnit(unit);
        return addUnitToMap(unit);
    }

    public boolean removeUnit(Unit unit) {
        Civilization civ = unit.getCivilization();
        if (civ != null)
            civ.removeUnit(unit);
        return removeUnitFromMap(unit);
    }

    public boolean changeUnitOwner(Unit unit, Civilization newCiv) {
        if (unit == null || newCiv == null) return false;
        removeUnit(unit);
        addUnit(newCiv, unit.getTile(), unit.getUnitType());
        // TODO: XP?
        return true;
    }

    public boolean addUnitToMap(Unit unit) {
        if (unit == null)
            return false;
        Tile tile = unit.getTile();
        if (tile == null)
            return false;
        if (unit.isCivilian()) {
            tile.setNonCombatUnit(unit);
        } else {
            tile.setCombatUnit(unit);
        }
        for (Tile visitingTile : getUnitVisitingTiles(unit)) {
            visitingTile.addVisitingUnit(unit);
            gameArea.setTileKnowledgeByCivilization(unit.getCivilization(), visitingTile, TileKnowledge.VISIBLE);
        }
        return true;
    }

    public boolean removeUnitFromMap(Unit unit) {
        if (unit == null) return false;
        Tile tile = unit.getTile();
        if (tile == null) return false;
        if (unit.isCivilian()) {
            tile.setNonCombatUnit(null);
        } else {
            tile.setCombatUnit(null);
        }
        for (Tile visitingTile : getUnitVisitingTiles(unit)) {
            visitingTile.removeVisitingUnit(unit);
            if (!visitingTile.civilizationIsVisiting(unit.getCivilization())) {
                gameArea.setTileKnowledgeByCivilization(unit.getCivilization(), visitingTile, TileKnowledge.REVEALED);
            }
        }
        return true;
    }

    public boolean unitMoveTo(Civilization civilization, Tile target)
    {
        if (civilization == null || target == null) return false;
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;
        Tile tile = unit.getTile();
        if (tile == null) return false;
        int dist = gameArea.getWeightedDistance(tile, target);
        if (unit.getMp() == 0 || dist >= unit.getMp() + UnitType.MAX_MOVEMENT)
            return false;
        if ((unit.isCivilian() && target.getNonCombatUnit() != null) ||
            (!unit.isCivilian() && target.getCombatUnit() != null))
            return false;
        removeUnitFromMap(unit);
        unit.addToMp(-dist);
        unit.setTile(target);
        unit.setUnitAction(UnitType.UnitAction.MOVETO);
        addUnitToMap(unit);
        return true;
    }

    public boolean unitSleep(Civilization civilization)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;
        unit.setUnitAction(UnitType.UnitAction.SLEEP);
        return true;
    }

    public boolean unitFortify(Civilization civilization)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;
        if(unit.getUnitType().getCombatType() == UnitType.CombatType.CIVILIAN) return false;
        if(unit.getUnitType().getCombatType() == UnitType.CombatType.MOUNTED)  return false;
        if(unit.getUnitType().getCombatType() == UnitType.CombatType.ARMORED) return false;
        unit.setUnitAction(UnitType.UnitAction.FORTIFY);
        return true;
    }

    public boolean unitGarrison(Civilization civilization)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;
        City city = unit.getTile().getCity();
        if (city == null || city.getCivilization() != civilization || city.getCombatUnit() != unit) return false;
        unit.setUnitAction(UnitType.UnitAction.GARRISON);
        return true;
    }
    public boolean unitSetupForRangedAttack(Civilization civilization)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;

        unit.setUnitAction(UnitType.UnitAction.SETUP_RANGED);
        return true;
    }

    public boolean unitAttack(Civilization civilization, Tile target)
    {
        if (civilization == null || target == null) return false;
        Unit unit = civilization.getSelectedUnit();
        City city = civilization.getSelectedCity();
        Unit enemyUnit = target.getCombatUnit();
        City enemyCity = target.getCity();
        if (enemyUnit == null) enemyUnit = target.getNonCombatUnit();
        Tile curTile = (unit == null ? (city == null ? null : city.getTile()) : unit.getTile());
        if((unit == null && city == null) || (enemyUnit == null && enemyCity == null) || curTile == null) return false;
        Civilization otherCiv = (enemyUnit == null ? (enemyCity == null ? null : enemyCity.getCivilization()) : enemyUnit.getCivilization());
        if (otherCiv == null) return false;

        int dist = gameArea.getDistanceInTiles(curTile, target);
        if(city != null) {
            if (enemyCity != null) {
                if (dist > city.getTerritoryRange()) return false;
                enemyCity.setHp(enemyCity.getHp() - city.getCombatStrength());

                if (enemyCity.isDead()) {
                    cityController.changeCityOwner(enemyCity, civilization);
                }

                return true;
            }
            else if(enemyUnit != null){
                if (dist > city.getTerritoryRange()) return false;
                enemyUnit.setHp(enemyUnit.getHp() - city.getCombatStrength());

                if (enemyUnit.getHp() <= 0) {
                    removeUnit(enemyUnit);
                }

                return true;
            }
        }
        
        if(unit != null) {
            if(unit.getUnitType().getCombatType() == UnitType.CombatType.CIVILIAN) return false;
            if(enemyUnit != null) {
                if (unit.getCombatType() == UnitType.CombatType.ARCHERY || unit.getCombatType() == UnitType.CombatType.SIEGE) {
                    if (dist > unit.getRange()) return false;
                    if (unit.getCombatType() == UnitType.CombatType.SIEGE && unit.getUnitAction() != UnitType.UnitAction.SETUP_RANGED)
                        return false;
                    enemyUnit.setHp(enemyUnit.getHp() - unit.getCombatStrength());
                    if (enemyUnit.isDead()) {
                        removeUnit(enemyUnit);
                    }
                    if (unit.isDead()) {
                        removeUnit(unit);
                    }
                    return true;
                    // in this type of attack we will kill worker
                }

                if (unit.getCombatType() == UnitType.CombatType.MOUNTED || unit.getCombatType() == UnitType.CombatType.MELEE || unit.getCombatType() == UnitType.CombatType.GUNPOWDER || unit.getCombatType() == UnitType.CombatType.ARMORED || unit.getCombatType() == UnitType.CombatType.RECON) {
                    if (dist > 1) return false;
                    if (enemyUnit.getUnitType().getCombatType() == UnitType.CombatType.CIVILIAN) {
                        if (enemyCity == null) {
                            changeUnitOwner(enemyUnit, civilization);
                            return true;
                        }
                    } else {
                        enemyUnit.setHp(enemyUnit.getHp() - unit.getCombatStrength());
                        unit.setHp(unit.getHp() - enemyUnit.getCombatStrength());
                        if (enemyUnit.getHp() <= 0) {
                            removeUnit(enemyUnit);
                            unitMoveTo(civilization, target);
                        }
                        if (unit.getHp() <= 0) {
                            removeUnit(unit);
                        }
                        return true;
                    }
                    // in this type of attack we got worker if it is not city
                }
            }
            if (enemyCity != null) {
                if (unit.getCombatType() == UnitType.CombatType.ARCHERY || unit.getCombatType() == UnitType.CombatType.SIEGE) {
                    if (dist > unit.getRange()) return false;
                    if (unit.getCombatType() == UnitType.CombatType.SIEGE && unit.getUnitAction() != UnitType.UnitAction.SETUP_RANGED)
                        return false;
                    enemyCity.setHp(enemyCity.getHp() - unit.getCombatStrength());
                    unit.setHp(unit.getHp() - enemyCity.getCombatStrength());

                    if (unit.getHp() <= 0) {
                        removeUnit(unit);
                    }
                    return true;
                }

                if (unit.getCombatType() == UnitType.CombatType.MOUNTED || unit.getCombatType() == UnitType.CombatType.MELEE || unit.getCombatType() == UnitType.CombatType.GUNPOWDER || unit.getCombatType() == UnitType.CombatType.ARMORED || unit.getCombatType() == UnitType.CombatType.RECON) {
                    if (dist > 1) return false;
                    enemyCity.setHp(enemyCity.getHp() - unit.getCombatStrength());
                    unit.setHp(unit.getHp() - enemyCity.getCombatStrength());
                    if (enemyCity.getHp() <= 0) {
                        unitMoveTo(civilization, target);
                        cityController.changeCityOwner(enemyCity, civilization);
                        if(enemyUnit.getUnitType().getCombatType() == UnitType.CombatType.CIVILIAN)
                        {
                            changeUnitOwner(enemyUnit, civilization);
                        }
                    }
                    if (unit.getHp() <= 0) {
                        civilization.removeUnit(unit);
                    }
                    return true;
                }
            }
            if (unit.getUnitType().getCombatType() != UnitType.CombatType.ARMORED && unit.getUnitType().getCombatType() != UnitType.CombatType.MOUNTED)
                unit.setMp(0);
            else
                unit.setMp(unit.getMp() - dist);


            unit.setUnitAction(UnitType.UnitAction.ATTACK);
        }
        return false;
    }

    public boolean unitFoundCity(Civilization civilization)
    {
        if (civilization == null) return false;
        Unit unit = civilization.getSelectedUnit();
        if (unit == null || !(unit.getUnitType() == UnitType.SETTLER))
            return false;
        Tile target = unit.getTile();
        if (target == null || target.getCity() != null)
            return false;
        City city;
        int cnt = 10;
        do {
            city = new City(City.getCityName(RANDOM.nextInt()), civilization, target);
        } while (!cityController.addCity(city) && cnt --> 0);
        unit.setUnitAction(UnitType.UnitAction.FOUND_CITY);
        return true;
    }

    public boolean unitCancelMission(Civilization civilization)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;

        unit.setUnitAction(UnitType.UnitAction.CANCEL_MISSION);
        // TODO: kar dige i lazeme anjam bedim?
        return true;
    }

    public boolean unitBuildRoad(Civilization civilization)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;
        if(unit.getUnitType() != UnitType.WORKER) return false;
        Tile tile = unit.getTile();
        if(tile.getHasRoad() == true)
            return false; // we can't build road twice
        if(unit.getHowManyTurnWeKeepBuildRoadAction() == 3){
            unit.setHowManyTurnWeKeepBuildRoadAction(0);
            tile.setHasRoad(true);
            unit.setUnitAction(null);
            return true;
        }
        unit.setHowManyTurnWeKeepBuildRoadAction(unit.getHowManyTurnWeKeepBuildRoadAction()+1);
        unit.setUnitAction(UnitType.UnitAction.BUILD_ROAD);
        return true;
    }

    public boolean unitBuildRailRoad(Civilization civilization)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;
        if(unit.getUnitType() != UnitType.WORKER) return false;
        Tile tile = unit.getTile();
        if(tile.getHasRailRoad() == true)
            return false; // we can't build Railroad twice
        if(unit.getHowManyTurnWeKeepBuildRailRoadAction() == 3){
            unit.setHowManyTurnWeKeepBuildRailRoadAction(0);
            tile.setHasRailRoad(true);
            unit.setUnitAction(null);
            return true;
        }

        unit.setHowManyTurnWeKeepBuildRailRoadAction(unit.getHowManyTurnWeKeepBuildRailRoadAction()+1);
        unit.setUnitAction(UnitType.UnitAction.BUILD_RAILROAD);
        return true;
    }

    public boolean unitBuildImprovement(Civilization civilization, Improvement improvement)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;

        if(improvement == Improvement.FARM)
            unit.setUnitAction(UnitType.UnitAction.BUILD_FARM);
        if(improvement == Improvement.MINE)
            unit.setUnitAction(UnitType.UnitAction.BUILD_MINE);
        if(improvement == Improvement.TRADING_POST)
            unit.setUnitAction(UnitType.UnitAction.BUILD_TRADINGPOST);
        if(improvement == Improvement.LUMBER_MILL)
            unit.setUnitAction(UnitType.UnitAction.BUILD_LUMBERMILL);
        if(improvement == Improvement.PASTURE)
            unit.setUnitAction(UnitType.UnitAction.BUILD_PASTURE);
        if(improvement == Improvement.CAMP)
            unit.setUnitAction(UnitType.UnitAction.BUILD_CAMP);
        if(improvement == Improvement.PLANTATION)
            unit.setUnitAction(UnitType.UnitAction.BUILD_PLANTATION);
        if(improvement == Improvement.QUARRY)
            unit.setUnitAction(UnitType.UnitAction.BUILD_QUARRY);
        return true;
    }
    public boolean unitRemoveJungle(Civilization civilization)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;

        unit.setUnitAction(UnitType.UnitAction.REMOVE_JUNGLE);
        return true;
    }
    public boolean unitRemoveForest(Civilization civilization)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;

        unit.setUnitAction(UnitType.UnitAction.REMOVE_FOREST);
        return true;
    }
    public boolean unitRemoveRoute(Civilization civilization)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;

        unit.setUnitAction(UnitType.UnitAction.REMOVE_ROUTE);
        return true;
    }

    public boolean unitRepair(Civilization civilization)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;

        unit.setUnitAction(UnitType.UnitAction.REPAIR);
        return true;
    }
}
