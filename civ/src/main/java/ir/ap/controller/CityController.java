package ir.ap.controller;

import ir.ap.model.City;
import ir.ap.model.Civilization;
import ir.ap.model.GameArea;
import ir.ap.model.Tile;
import ir.ap.model.Tile.TileKnowledge;

public class CityController extends AbstractGameController {
    public CityController(GameArea gameArea) {
        super(gameArea);
    }

    public boolean addCityToMap(City city) {
        Tile tile = city.getTile();
        Civilization civ = city.getCivilization();
        if (tile == null || tile.getCity() != null)
            return false;
        if (!City.addCity(city))
            return false;
        tile.setCity(city);
        civ.addCity(city);
        for (Tile territoryTile : gameArea.getTilesInRange(city, city.getTerritoryRange())) {
            if (territoryTile.getOwnerCity() == null) {
                city.addToTerritory(territoryTile);
                territoryTile.setOwnerCity(city);
                gameArea.setTileKnowledgeByCivilization(civ, territoryTile, TileKnowledge.VISIBLE);
            }
        }
        return true;
    }

    public boolean removeCityFromMap(City city) {

    }

    public boolean addCity(City city) {

    }

    public boolean removeCity(City city) {

    }

    public boolean changeCityOwner(City city, Civilization newCiv) {
        removeCity(city);
        city.setCivilization(newCiv);
        addCity(city);
    }
}
