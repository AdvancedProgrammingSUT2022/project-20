package ir.ap.controller;

import ir.ap.model.BuildingType;
import ir.ap.model.City;
import ir.ap.model.Civilization;
import ir.ap.model.GameArea;
import ir.ap.model.Map;
import ir.ap.model.Production;
import ir.ap.model.Tile;
import ir.ap.model.UnitType;
import ir.ap.model.Tile.TileKnowledge;

public class CityController extends AbstractGameController {
    public CityController(GameArea gameArea) {
        super(gameArea);
    }

    public void nextTurn(City city) {
        if (city == null) return;
        city.addToHp(1);
        city.addToFood(-2 * city.getPopulation());
        if (city.getFood() < 0 && city.getFoodYield() + city.getFood() >= 0) {
            city.setFood(0);
        }
        if (city.getCurrentProduction() != UnitType.SETTLER) {
            city.addToFood(city.getFoodYield());
        }
        city.addToPopulation(city.getFood() / 4.0 / city.getPopulation());
        if (city.getCurrentProduction() != null) {
            city.addToProductionSpent(city.getProductionYield());
            if (city.getCostLeftForProductionConstruction() <= 0) {
                cityConstructProduction(city);
            }
        }
        if( gameArea.getTurn() % City.TURN_NEEDED_TO_EXTEND_TILES == 0 ){
            addRandomTileToCity(city);
        }
    }
    
    public void addRandomTileToCity( City city ){
        if( city == null )return ;
        for(int i = 0 ; i < Map.MAX_H ; i ++){
            for(int j = 0; j < Map.MAX_W ; j ++){
                Tile tile = gameArea.getMap().getTiles()[ i ][ j ] ;
                if( cityPurchaseTile(city, tile) == true )return;
            }
        }        
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
            if (neighbor != null && neighbor.getOwnerCity() != null && neighbor.getOwnerCity().equals(city))
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
            if (neighbor != null && !neighbor.civilizationIsVisiting(civ))
                gameArea.setTileKnowledgeByCivilization(civ, neighbor, TileKnowledge.REVEALED);
        }
        return true;
    }

    public boolean cityAddCitizenToWorkOnTile(City city, Tile tile) {
        if (city == null || tile == null)
            return false;
        if (city.getWorkingTiles().size() >= city.getPopulation())
            return false;
        if (tile.getOwnerCity() != city || gameArea.getDistanceInTiles(city.getTile(), tile) > 2)
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

    public boolean cityChangeCurrentProduction(City city, Production production, boolean cheat) {
        if (city == null) return false;
        city.setProductionSpent(0);
        city.setCurrentProduction(production);
        if( cheat == true ){
            cityConstructProduction(city);
        }
        return true;
    }

    public boolean cityConstructProduction(City city) {
        if (city == null || city.getCurrentProduction() == null)
            return false;
        Production production = city.getCurrentProduction();
        if (city.getCostLeftForProductionConstruction() > 0)
            return false;
        city.setProductionSpent(0);
        city.setCurrentProduction(null);
        if (production instanceof UnitType) {
            unitController.addUnit(city.getCivilization(), city.getTile(), (UnitType) production);
        } else if (production instanceof BuildingType) {
            // TODO: PHASE2
        }
        return true;
    }

    public boolean cityDestroy(City city, Civilization civ) {
        civ.addCityDestroyed(city);
        return removeCity(city);
    }

    public boolean cityAnnex(City city, Civilization civ) {
        civ.addCitiesAnnexed(city);
        return changeCityOwner(city, civ);
    }
}
