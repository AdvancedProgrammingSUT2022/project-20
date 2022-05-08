package ir.ap.controller;

import ir.ap.model.*;
import ir.ap.model.TerrainType.TerrainFeature;
import ir.ap.model.Tile.TileKnowledge;

public class UnitController extends AbstractGameController {
    public UnitController(GameArea gameArea) {
        super(gameArea);
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
        for (Tile visitingTile : mapController.getUnitVisitingTiles(unit)) {
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
        for (Tile visitingTile : mapController.getUnitVisitingTiles(unit)) {
            visitingTile.removeVisitingUnit(unit);
            if (!visitingTile.civilizationIsVisiting(unit.getCivilization())) {
                gameArea.setTileKnowledgeByCivilization(unit.getCivilization(), visitingTile, TileKnowledge.REVEALED);
            }
        }
        return true;
    }

    public boolean unitMoveTo(Civilization civilization, Tile target, boolean cheat)
    {
        //TODO
        // TODO: moveto OCEAN?!!
        if (civilization == null || target == null) return false;
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;
        unit.setHowManyTurnWeKeepAction(0);
        Tile tile = unit.getTile();
        if (tile == null) return false;
        int dist = mapController.getWeightedDistance(tile, target);
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
        unit.setHowManyTurnWeKeepAction(0);
        unit.setUnitAction(UnitType.UnitAction.SLEEP);
        return true;
    }

    public boolean unitFortify(Civilization civilization)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;
        unit.setHowManyTurnWeKeepAction(0);
        if(unit.getUnitType().getCombatType() == UnitType.CombatType.CIVILIAN) return false;
        if(unit.getUnitType().getCombatType() == UnitType.CombatType.MOUNTED)  return false;
        if(unit.getUnitType().getCombatType() == UnitType.CombatType.ARMORED) return false;
        unit.setUnitAction(UnitType.UnitAction.FORTIFY);
        // combat Strengh ziad mishe va lahaz shode to unit.getCombatStrengh
        return true;
    }
    public boolean unitFortifyHeal(Civilization civilization)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;
        if(unit.getCombatType() == UnitType.CombatType.CIVILIAN) return false;
        unit.setHowManyTurnWeKeepAction(0);
        unit.setUnitAction(UnitType.UnitAction.FORTIFY_HEAL);
        return true;
    }

    public boolean unitGarrison(Civilization civilization)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;
        unit.setHowManyTurnWeKeepAction(0);
        City city = unit.getTile().getCity();
        if (city == null || city.getCivilization() != civilization || city.getCombatUnit() != unit) return false;
        // GARISSON to tabe city.getCombatStrength lahaz shode
        unit.setUnitAction(UnitType.UnitAction.GARRISON);
        return true;
    }
    public boolean unitSetupForRangedAttack(Civilization civilization)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;
        unit.setHowManyTurnWeKeepAction(0);

        unit.setUnitAction(UnitType.UnitAction.SETUP_RANGED);
        return true;
    }

    public boolean unitAttack(Civilization civilization, Tile target, boolean cheat)
    {
        // TODO: khodi ha!!!!
        if (civilization == null || target == null) return false;
        Unit unit = civilization.getSelectedUnit();
        if (unit == null) return false;
        unit.setHowManyTurnWeKeepAction(0);
        City city = civilization.getSelectedCity();
        Unit enemyUnit = target.getCombatUnit();
        City enemyCity = target.getCity();
        if (enemyUnit == null) enemyUnit = target.getNonCombatUnit();
        Tile curTile = (unit == null ? (city == null ? null : city.getTile()) : unit.getTile());
        if((unit == null && city == null) || (enemyUnit == null && enemyCity == null) || curTile == null) return false;
        Civilization otherCiv = (enemyUnit == null ? (enemyCity == null ? null : enemyCity.getCivilization()) : enemyUnit.getCivilization());
        if (otherCiv == null) return false;

        int dist = mapController.getDistanceInTiles(curTile, target);
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
                            unitMoveTo(civilization, target, cheat);
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
                        unitMoveTo(civilization, target, cheat);
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

            if(unit.getTile() == target) {
                if(target.hasImprovement() && target.getOwnerCity().getCivilization() != civilization){
                    Improvement improvement = target.getImprovement();
                    improvement.setIsDead(true);
                    target.setImprovement(improvement);
                }
            }
            unit.setUnitAction(UnitType.UnitAction.ATTACK);
        }
        return false;
    }

    public boolean unitFoundCity(Civilization civilization, boolean cheat)
    {
        if (civilization == null) return false;
        Unit unit = civilization.getSelectedUnit();
        if (unit == null) return false;
        unit.setHowManyTurnWeKeepAction(0);
        if (unit == null || !(unit.getUnitType() == UnitType.SETTLER))
            return false;

        Tile target = unit.getTile();
        if (target == null || target.hasOwnerCity())
            return false;
        City city;
        int cnt = 10;
        do {
            city = new City(City.getCityName(RANDOM.nextInt()), civilization, target);
        } while (!cityController.addCity(city) && cnt --> 0);
        if (cnt < 0)
            return false;
        unit.setUnitAction(UnitType.UnitAction.FOUND_CITY);
        return true;
    }

    public boolean unitCancelMission(Civilization civilization)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;
        unit.setHowManyTurnWeKeepAction(0);

        unit.setUnitAction(UnitType.UnitAction.CANCEL_MISSION);
        return true;
    }

    public boolean unitBuildRoad(Civilization civilization, boolean cheat)
    {
        if (civilization == null) return false;
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;
        Tile tile = unit.getTile();
        if (tile == null || tile.getHasRoad()) return false;
        unit.setHowManyTurnWeKeepAction(0);
        if(unit.getUnitType() != UnitType.WORKER) return false;
        if(civilization.getTechnologyReached(Technology.THE_WHEEL) == false) return false;

        unit.setUnitAction(UnitType.UnitAction.BUILD_ROAD);
        return true;
    }

    public boolean unitBuildRailRoad(Civilization civilization, boolean cheat)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;
        Tile tile = unit.getTile();
        if (tile == null || tile.getHasRailRoad()) return false;
        unit.setHowManyTurnWeKeepAction(0);
        if(unit.getUnitType() != UnitType.WORKER) return false;
        if(civilization.getTechnologyReached(Technology.RAILROAD) == false) return false;

        unit.setUnitAction(UnitType.UnitAction.BUILD_RAILROAD);
        return true;
    }

    public boolean unitBuildImprovement(Civilization civilization, Improvement improvement, boolean cheat)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;
        unit.setHowManyTurnWeKeepAction(0);
        Tile tile = unit.getTile();
        if (tile == null || tile.hasImprovement()) return false;

        if(civilization.getTechnologyReached(improvement.getTechnologyRequired()) == false)
            return false;
        if(tile.getTerrainFeature() == TerrainFeature.FOREST) return false;
        if(tile.getTerrainFeature() == TerrainFeature.JUNGLE) return false;
        if(tile.getTerrainFeature() == TerrainFeature.MARSH) return false;

        if(improvement == Improvement.FARM) {
            if(tile.getTerrainFeature() == TerrainFeature.ICE) return false;
            unit.setUnitAction(UnitType.UnitAction.BUILD_FARM);
        }
        if(improvement == Improvement.MINE){
            if(tile.getTerrainType() != TerrainType.HILL) return false;
            unit.setUnitAction(UnitType.UnitAction.BUILD_MINE);
        }
        if(improvement == Improvement.TRADING_POST) {
            if(civilization.getTechnologyReached(Technology.TRAPPING) == false) return false;
            unit.setUnitAction(UnitType.UnitAction.BUILD_TRADINGPOST);
        }
        if(improvement == Improvement.LUMBER_MILL) {
            if(civilization.getTechnologyReached(Technology.CONSTRUCTION) == false) return false;
            unit.setUnitAction(UnitType.UnitAction.BUILD_LUMBERMILL);
        }
        if(improvement == Improvement.PASTURE) {
            if(civilization.getTechnologyReached(Technology.ANIMAL_HUSBANDRY) == false) return false;
            unit.setUnitAction(UnitType.UnitAction.BUILD_PASTURE);
        }
        if(improvement == Improvement.CAMP) {
            if(civilization.getTechnologyReached(Technology.TRAPPING) == false) return false;
            unit.setUnitAction(UnitType.UnitAction.BUILD_CAMP);
        }
        if(improvement == Improvement.PLANTATION) {
            if(civilization.getTechnologyReached(Technology.CALENDAR) == false) return false;
            unit.setUnitAction(UnitType.UnitAction.BUILD_PLANTATION);
        }
        if(improvement == Improvement.QUARRY) {
            if(civilization.getTechnologyReached(Technology.ENGINEERING) == false) return false;
            unit.setUnitAction(UnitType.UnitAction.BUILD_QUARRY);
        }
        return true;
    }
    public boolean unitRemoveJungle(Civilization civilization, boolean cheat)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;
        Tile tile = unit.getTile();
        if (tile == null || tile.getTerrainFeature() != TerrainFeature.JUNGLE) return false;
        unit.setHowManyTurnWeKeepAction(0);
        if(civilization.getTechnologyReached(Technology.BRONZE_WORKING) == false) return false;

        unit.setUnitAction(UnitType.UnitAction.REMOVE_JUNGLE);
        return true;
    }
    public boolean unitRemoveForest(Civilization civilization, boolean cheat)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;
        Tile tile = unit.getTile();
        if (tile == null || tile.getTerrainFeature() != TerrainFeature.FOREST) return false;
        unit.setHowManyTurnWeKeepAction(0);
        if(civilization.getTechnologyReached(Technology.MINING) == false) return false;

        unit.setUnitAction(UnitType.UnitAction.REMOVE_FOREST);
        return true;
    }

    public boolean unitRemoveMarsh(Civilization civilization, boolean cheat) {
        Unit unit = civilization.getSelectedUnit();
        if (unit == null) return false;
        unit.setHowManyTurnWeKeepAction(0);
        Tile tile = unit.getTile();
        if (tile == null || tile.getTerrainFeature() != TerrainFeature.MARSH) return false;
        if(civilization.getTechnologyReached(Technology.MASONRY) == false) return false;

        unit.setUnitAction(UnitType.UnitAction.REMOVE_MARSH);
        return true;
    }

    public boolean unitRemoveRoute(Civilization civilization, boolean cheat)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;
        Tile tile = unit.getTile();
        if (tile == null || (!tile.getHasRoad() && !tile.getHasRailRoad())) return false;
        unit.setHowManyTurnWeKeepAction(0);

        unit.setUnitAction(UnitType.UnitAction.REMOVE_ROUTE);
        return true;
    }

    public boolean unitRepair(Civilization civilization, boolean cheat)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;
        Tile tile = unit.getTile();
        if (tile == null || (!tile.hasCity() && !tile.hasBuilding() && !tile.hasImprovement())) return false;
        unit.setHowManyTurnWeKeepAction(0);

        // TODO: repair building PHASE2

        unit.setUnitAction(UnitType.UnitAction.REPAIR);
        return true;
    }

    public boolean unitWake(Civilization civilization)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;
        unit.setHowManyTurnWeKeepAction(0);
        unit.setUnitAction(UnitType.UnitAction.WAKE);
        return true;
    }

    public boolean unitAlert(Civilization civilization)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;
        unit.setHowManyTurnWeKeepAction(0);
        unit.setUnitAction(UnitType.UnitAction.ALERT);
        return true;
    }

}
