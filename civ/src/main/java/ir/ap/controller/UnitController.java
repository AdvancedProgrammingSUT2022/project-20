package ir.ap.controller;

import ir.ap.model.Civilization;
import ir.ap.model.GameArea;
import ir.ap.model.Tile;
import ir.ap.model.Unit;
import ir.ap.model.UnitType;

public class UnitController extends AbstractGameController {
    public UnitController(GameArea gameArea) {
        super(gameArea);
    }

    public void addUnit(Civilization civilization, Tile tile, UnitType unitType){
        Unit unit = new Unit(unitType, civilization, tile);
        civilization.getUnits().add( unit );
        if( unit.isCivilian() == true ){
            tile.setNonCombatUnit( unit );
        } 
        else{
            tile.setCombatUnit( unit );
        }
    }
}
