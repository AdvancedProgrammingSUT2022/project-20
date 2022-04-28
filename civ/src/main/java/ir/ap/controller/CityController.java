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
        if (city == null)
            return false;
        Tile tile = city.getTile();
        Civilization civ = city.getCivilization();
        if (tile == null || civ == null || tile.hasCity())
            return false;
        tile.setCity(city);
        for (Tile territoryTile : mapController.getTilesInRange(city, city.getTerritoryRange())) {
            if (territoryTile.getOwnerCity() == null) {
                city.addToTerritory(territoryTile);
                territoryTile.setOwnerCity(city);
                gameArea.setTileKnowledgeByCivilization(civ, territoryTile, TileKnowledge.VISIBLE);
            }
        }
        return true;
    }

    public boolean removeCityFromMap(City city) {
        if (city == null)
            return false;
        Tile tile = city.getTile();
        Civilization civ = city.getCivilization();
        if (tile != null) {
            tile.setCity(null);
            for (Tile territoryTile : city.getTerritory()) {
                territoryTile.setOwnerCity(null);
                if (!territoryTile.civilizationIsVisiting(civ)) {
                    gameArea.setTileKnowledgeByCivilization(civ, territoryTile, TileKnowledge.REVEALED);
                }
            }
        }
        city.resetTerritory();
        return true;
    }

    public boolean addCity(City city) {
        if (!City.addCity(city))
            return false;
        if (!addCityToMap(city)) {
            City.removeCity(city);
            return false;
        }
        city.getCivilization().addCity(city);
        return true;
    }

    public boolean removeCity(City city) {
        City.removeCity(city);
        Civilization civ = city.getCivilization();
        if (civ != null)
            civ.removeCity(city);
        return removeCityFromMap(city);
    }

    public boolean changeCityOwner(City city, Civilization newCiv) {
        removeCity(city);
        city.setCivilization(newCiv);
        return addCity(city);
    }
}
