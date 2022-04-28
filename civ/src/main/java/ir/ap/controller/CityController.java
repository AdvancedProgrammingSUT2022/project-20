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
        if (tile == null || tile.hasCity())
            return false;
        tile.setCity(city);
        for (Tile territoryTile : mapController.getTilesInRange(city, city.getTerritoryRange())) {
            addTileToTerritoryOfCity(city, territoryTile);
        }
        return true;
    }

    public boolean removeCityFromMap(City city) {
        if (city == null)
            return false;
        Tile tile = city.getTile();
        if (tile == null)
            return false;
        tile.setCity(null);
        for (Tile territoryTile : mapController.getTilesInRange(city, city.getTerritoryRange())) {
            removeTileFromTerritoryOfCity(city, territoryTile);
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

    public boolean tileIsNearTerritoryOfCity(City city, Tile tile) {
        if (city == null || tile == null)
            return false;
        for (Tile neighbor : tile.getNeighbors()) {
            if (neighbor.getOwnerCity().equals(city))
                return true;
        }
        return false;
    }

    public boolean addTileToTerritoryOfCity(City city, Tile tile) {
        if (city == null || tile == null)
            return false;
        City other = tile.getOwnerCity();
        if (other != null)
            return false;
        Civilization civ = city.getCivilization();
        city.addToTerritory(tile);
        tile.setOwnerCity(city);
        gameArea.setTileKnowledgeByCivilization(civ, tile, TileKnowledge.VISIBLE);
        for (Tile neighbor : tile.getNeighbors())
            gameArea.setTileKnowledgeByCivilization(civ, neighbor, TileKnowledge.VISIBLE);
        return true;
    }

    public boolean removeTileFromTerritoryOfCity(City city, Tile tile) {
        if (city == null || tile == null)
            return false;
        Civilization civ = city.getCivilization();
        if (civ == null)
            return false;
        tile.setOwnerCity(null);
        city.removeFromTerritory(tile);
        if (!tile.civilizationIsVisiting(civ)) {
            gameArea.setTileKnowledgeByCivilization(civ, tile, TileKnowledge.REVEALED);
        }
        for (Tile neighbor : tile.getNeighbors()) {
            if (!neighbor.civilizationIsVisiting(civ))
                gameArea.setTileKnowledgeByCivilization(civ, neighbor, TileKnowledge.REVEALED);
        }
        return true;
    }

    public boolean cityLockCitizenToTile(City city, Tile tile) {
        if (city == null || tile == null)
            return false;
        if (city.getWorkingTiles().size() >= city.getPopulation())
            return false;
        if (!city.getTerritory().contains(tile))
            return false;
        return city.addToWorkingTiles(tile);
    }

    public boolean cityRemoveCitizenFromWork(City city, Tile tile) {
        if (city == null || tile == null)
            return false;
        return city.removeFromWorkingTiles(tile);
    }

    public boolean cityPurchaseTile(City city, Tile tile) {
        if (!tileIsNearTerritoryOfCity(city, tile))
            return false;
        return addTileToTerritoryOfCity(city, tile);
    }
}
