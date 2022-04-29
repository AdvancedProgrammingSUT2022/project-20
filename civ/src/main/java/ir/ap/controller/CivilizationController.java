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

    public void nextTurn(Civilization civilization)
    {
        if (civilization == null) return;
        for(Unit unit : civilization.getUnits())
            if(unit.getUnitType() == UnitType.WORKER)
            {
                Tile tile = unit.getTile();
                if (unit.getUnitAction() == UnitType.UnitAction.REPAIR) {
                    // TODO: safe 28 Game.pdf
                }
                if (unit.getUnitAction() == UnitType.UnitAction.FORTIFY_HEAL) {
                    // TODO: safe 29 Game.pdf
                }
                if(unit.getHowManyTurnWeKeepAction() == 3){
                    if(unit.getUnitAction() == UnitType.UnitAction.BUILD_BRIDGE){
                        ///TODO:kojaro bayad tagir bedim?
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.BUILD_CAMP){
                        mapController.addImprovement(tile, Improvement.CAMP);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.BUILD_FARM){
                        mapController.addImprovement(tile, Improvement.FARM);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.BUILD_LUMBERMILL){
                        mapController.addImprovement(tile, Improvement.LUMBER_MILL);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.BUILD_MINE){
                        mapController.addImprovement(tile, Improvement.MINE);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.BUILD_PASTURE){
                        mapController.addImprovement(tile, Improvement.PASTURE);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.BUILD_PLANTATION){
                        mapController.addImprovement(tile, Improvement.PLANTATION);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.BUILD_QUARRY){
                        mapController.addImprovement(tile, Improvement.QUARRY);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.BUILD_RAILROAD){
                        mapController.addRailRoad(tile);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.BUILD_ROAD){
                        mapController.addRoad(tile);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.BUILD_TRADINGPOST){
                        mapController.addImprovement(tile, Improvement.TRADING_POST);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.REMOVE_FOREST){
                        mapController.removeTerrainFeature(tile);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.REMOVE_JUNGLE){
                        mapController.removeTerrainFeature(tile);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.REMOVE_ROUTE){
                        mapController.removeRoad(tile);
                        mapController.removeRailRoad(tile);
                    }
                    if(unit.getUnitAction() == UnitType.UnitAction.REMOVE_MARSH){
                        mapController.removeTerrainFeature(tile);
                    }
                    unit.setHowManyTurnWeKeepAction(0);
                }
                else{
                    unit.setHowManyTurnWeKeepAction(unit.getHowManyTurnWeKeepAction()+1);
                }
            }
        for (City city : civilization.getCities()) {
            cityController.nextTurn(city);
        }
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
