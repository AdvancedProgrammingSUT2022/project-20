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

    public boolean unitMoveTo(Civilization civilization, Tile tile)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;
        if (unit.isCivilian()) {
            if (tile.getNonCombatUnit() != null)
                return false;
            tile.setNonCombatUnit(unit);
        } else {
            if (tile.getCombatUnit() != null)
                return false;
            tile.setCombatUnit(unit);
        }
        unit.setTile(tile);
        unit.setUnitAction(UnitType.UnitAction.MOVETO);
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

        unit.setUnitAction(UnitType.UnitAction.FORTIFY);
        return true;
    }

    public boolean unitGarrison(Civilization civilization)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;

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

    public boolean unitAttack(Civilization civilization)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;

        unit.setUnitAction(UnitType.UnitAction.ATTACK);
        return true;
    }

    public boolean unitFoundCity(Civilization civilization)
    {
        Unit unit = civilization.getSelectedUnit();
        if(unit == null) return false;

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
