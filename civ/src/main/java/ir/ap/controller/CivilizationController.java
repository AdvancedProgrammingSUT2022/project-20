package ir.ap.controller;

import ir.ap.model.*;

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

    public String nextTurn(String username)
    {
        Civilization civilization = getCivilizationByUsername(username);
        for(Unit unit : civilization.getUnits())
            if(unit.getUnitType() == UnitType.WORKER)
            {
                Tile tile = unit.getTile();
                if(unit.getHowManyTurnWeKeepAction() == 3){
                    if(unit.getUnitAction() == UnitType.UnitAction.BUILD_BRIDGE){
                        ///TODO:kojaro bayad tagir bedim?
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.BUILD_CAMP){
                        tile.setImprovement(Improvement.CAMP);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.BUILD_FARM){
                        tile.setImprovement(Improvement.FARM);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.BUILD_LUMBERMILL){
                        tile.setImprovement(Improvement.LUMBER_MILL);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.BUILD_MINE){
                        tile.setImprovement(Improvement.MINE);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.BUILD_PASTURE){
                        tile.setImprovement(Improvement.PASTURE);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.BUILD_PLANTATION){
                        tile.setImprovement(Improvement.PLANTATION);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.BUILD_QUARRY){
                        tile.setImprovement(Improvement.QUARRY);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.BUILD_RAILROAD){
                        tile.setHasRailRoad(true);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.BUILD_ROAD){
                        tile.setHasRoad(true);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.BUILD_TRADINGPOST){
                        tile.setImprovement(Improvement.TRADING_POST);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.REMOVE_FOREST){
                        tile.setTerrainFeature(null);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.REMOVE_JUNGLE){
                        tile.setTerrainFeature(null);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.REMOVE_ROUTE){
                        //TODO: what should i do?
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.REMOVE_MARSH){
                        tile.setTerrainFeature(null);
                    }
                    unit.setHowManyTurnWeKeepAction(0);
                }
                else{
                    unit.setHowManyTurnWeKeepAction(unit.getHowManyTurnWeKeepAction()+1);
                }
            }
        // TODO: turn badi nobat kie?
        return null;
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
