package ir.ap.controller;

import ir.ap.model.*;

public class UnitController extends AbstractGameController {
    public UnitController(GameArea gameArea) {
        super(gameArea);
    }

    public void addUnit(Civilization civilization, Tile tile, UnitType unitType){
        Unit unit = new Unit(unitType, civilization, tile);
        civilization.addUnit(unit);
        if( unit.isCivilian() == true ){
            tile.setNonCombatUnit( unit );
        } 
        else{
            tile.setCombatUnit( unit );
        }
    }

    public boolean unitMoveTo(Civilization civilization, Tile target)
    {
        if (civilization == null || target == null) return false;
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;
        Tile tile = unit.getTile();
        if (tile == null) return false;
        int dist = gameArea.getWeightedDistance(tile, target);
        if (dist - target.getMovementCost() + 1 > unit.getMp())
            return false;
        if (unit.isCivilian()) {
            if (target.getNonCombatUnit() != null)
                return false;
            target.setNonCombatUnit(unit);
            tile.setNonCombatUnit(null);
        } else {
            if (target.getCombatUnit() != null)
                return false;
            target.setCombatUnit(unit);
            tile.setCombatUnit(null);
        }
        unit.addToMp(-dist);
        unit.setTile(target);
        unit.setUnitAction(UnitType.UnitAction.MOVETO);
        // TODO: tileknowledge
        // TODO: handle rivers
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

    public boolean unitAttack(Civilization civilization, Tile tile)
    {
        if (civilization == null || tile == null) return false;
        Unit unit = civilization.getSelectedUnit();
        Unit enemyUnit = tile.getCombatUnit();
        Tile curTile = unit.getTile();
        Civilization civilization1 = enemyUnit.getCivilization();
        City enemyCity = tile.getCity();
        City city = civilization.getSelectedCity();
        if(civilization1 == null && (city.getTile() == tile)) civilization1 = enemyCity.getCivilization();
        // TODO: Non Combat Units!! DID
        if((unit == null && city == null) || (enemyUnit == null && enemyCity.getTile() != tile) || curTile == null) return false;
         // TODO: City!! DID
        // TODO: River!!

        if(city != null) {
            if (enemyCity.getTile() == tile) {
                if (gameArea.getDistance > city.getRange()) return false;
                enemyCity.setHp(enemyCity.getHp() - city.getCombatStrength());

                if (enemyCity.getHp() <= 0) {
                    enemyCity.setHp(0);
                }

                return true;
            }
            if(enemyUnit != null){
                if (gameArea.getDistance > city.getRange()) return false;
                enemyUnit.setHp(enemyUnit.getHp() - city.getCombatStrength());

                if (enemyUnit.getHp() <= 0) {
                    civilization1.removeUnit(enemyUnit);
                }

                return true;
            }
        }

        if(unit != null) {
            if(unit.getUnitType().getCombatType() == UnitType.CombatType.CIVILIAN) return false;
            if(enemyUnit != null) {
                if (unit.getCombatType() == UnitType.CombatType.ARCHERY || unit.getCombatType() == UnitType.CombatType.SIEGE) {
                    if (gameArea.getDistanceInTiles(curTile, tile) > unit.getRange()) return false;
                    if (unit.getCombatType() == UnitType.CombatType.SIEGE && unit.getUnitAction() != UnitType.UnitAction.SETUP_RANGED)
                        return false;
                    enemyUnit.setHp(enemyUnit.getHp() - unit.getCombatStrength());
                    if (enemyUnit.isDead()) {
                        civilization1.removeUnit(enemyUnit);
                        tile.setCombatUnit(null);
                    }
                    if (unit.isDead()) {
                        civilization.removeUnit(unit);
                        curTile.setCombatUnit(null);
                    }
                    return true;
                    // in this type of attack we will kill worker
                }

                if (unit.getCombatType() == UnitType.CombatType.MOUNTED || unit.getCombatType() == UnitType.CombatType.MELEE || unit.getCombatType() == UnitType.CombatType.GUNPOWDER || unit.getCombatType() == UnitType.CombatType.ARMORED || unit.getCombatType() == UnitType.CombatType.RECON) {
                    if (gameArea.getDistance > unit.getMp()) return false;
                    if (enemyUnit.getUnitType().getCombatType() == UnitType.CombatType.CIVILIAN) {
                        if (enemyCity.getTile() != tile) {
                            civilization1.removeUnit(enemyUnit);
                            civilization.addUnit(enemyUnit);
                            return true;
                        }
                    } else {
                        enemyUnit.setHp(enemyUnit.getHp() - unit.getCombatStrength());
                        unit.setHp(unit.getHp() - enemyUnit.getCombatStrength());
                        if (enemyUnit.getHp() <= 0) {
                            unitMoveTo(civilization, tile);
                            civilization1.removeUnit(enemyUnit);
                        }
                        if (unit.getHp() <= 0) {
                            civilization.removeUnit(unit);
                        }
                        return true;
                    }
                    // in this type of attack we got worker if it is not city
                }
            }
            if (enemyCity.getTile() == tile) {
                if (unit.getCombatType() == UnitType.CombatType.ARCHERY || unit.getCombatType() == UnitType.CombatType.SIEGE) {
                    if (gameArea.getDistance > unit.getRange()) return false;
                    if (unit.getCombatType() == UnitType.CombatType.SIEGE && unit.getUnitAction() != UnitType.UnitAction.SETUP_RANGED)
                        return false;
                    enemyCity.setHp(enemyCity.getHp() - unit.getCombatStrength());
                    unit.setHp(unit.getHp() - enemyCity.getCombatStrength());

                    if (enemyCity.getHp() <= 0) {
                        enemyCity.setHp(0);
                    }
                    if (unit.getHp() <= 0) {
                        civilization.removeUnit(unit);
                    }
                    return true;
                }

                if (unit.getCombatType() == UnitType.CombatType.MOUNTED || unit.getCombatType() == UnitType.CombatType.MELEE || unit.getCombatType() == UnitType.CombatType.GUNPOWDER || unit.getCombatType() == UnitType.CombatType.ARMORED || unit.getCombatType() == UnitType.CombatType.RECON) {
                    if (gameArea.getDistance > unit.getMovement()) return false;
                    enemyCity.setHp(enemyCity.getHp() - unit.getCombatStrength());
                    unit.setHp(unit.getHp() - enemyCity.getCombatStrength());
                    if (enemyCity.getHp() <= 0) {
                        unitMoveTo(civilization, tile);
                        civilization1.removeCity(enemyCity);
                        civilization.addCity(enemyCity);
                        if(enemyUnit.getUnitType().getCombatType() == UnitType.CombatType.CIVILIAN)
                        {
                            civilization1.removeUnit(enemyUnit);
                            civilization.addUnit(enemyUnit);
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
                unit.setMp(unit.getMp() - gameArea.getDistance);


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
        Tile tile = unit.getTile();
        if (tile == null || tile.getCity() != null)
            return false;
        City city;
        int cnt = 10;
        do {
            city = new City(City.getCityName(RANDOM.nextInt()), civilization, tile);
        } while (!cityController.addCity(city) && cnt --> 0);
        unit.setUnitAction(UnitType.UnitAction.FOUND_CITY);
        return true;
    }

    public boolean unitCancelMission(Civilization civilization)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;

        unit.setUnitAction(UnitType.UnitAction.CANCEL_MISSION);
        return true;
    }

    public boolean unitBuildRoad(Civilization civilization)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;

        unit.setUnitAction(UnitType.UnitAction.BUILD_ROAD);
        return true;
    }

    public boolean unitBuildRailRoad(Civilization civilization)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;

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
