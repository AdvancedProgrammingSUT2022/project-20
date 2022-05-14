package ir.ap.controller;

import ir.ap.model.BuildingType;
import ir.ap.model.City;
import ir.ap.model.Civilization;
import ir.ap.model.GameArea;
import ir.ap.model.Map;
import ir.ap.model.Production;
import ir.ap.model.Tile;
import ir.ap.model.Unit;
import ir.ap.model.UnitType;
import ir.ap.model.Tile.TileKnowledge;

public class CityController extends AbstractGameController {
    public CityController(GameArea gameArea) {
        super(gameArea);
    }

    public City getCityById(int cityId) {
        for (Civilization civ : civController.getAllCivilizations()) {
            for (City city : civ.getCities()) {
                if (city.getId() == cityId)
                    return city;
            }
        }
        return null;
    }

    public void nextTurn(City city) {
        if (city == null) return;
        city.addToHp(1);
        city.addToFood(city.getExtraFood());
        city.addToPopulation(city.getPopulationGrowth());
        if (city.getCurrentProduction() != null) {
            city.addToProductionSpent(city.getProductionYield());
            if (city.getCostLeftForProductionConstruction() <= 0) {
                city.getCivilization().addToMessageQueue("City " + city.getName() + " constructed " + city.getCurrentProduction().getName());
                cityConstructProduction(city);
            }
        }
        if( gameArea.getTurn() % City.TURN_NEEDED_TO_EXTEND_TILES == 0 ){
            Tile tile = addRandomTileToCity(city);
            city.getCivilization().addToMessageQueue("City " + city.getName() + " added tile " + tile.getIndex() + " to territory");
        }
    }
    
    public Tile addRandomTileToCity( City city ){
        if( city == null )return null;
        for(int i = 0 ; i < Map.MAX_H ; i ++){
            for(int j = 0; j < Map.MAX_W ; j ++){
                Tile tile = gameArea.getMap().getTiles()[ i ][ j ] ;
                if (cityPurchaseTile(city, tile))
                    return tile;
            }
        }
        return null;
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
        city.getCivilization().addToMessageQueue("city " + city.getName() + " has been added to Civilization " + city.getCivilization().getName());
        return true;
    }

    public boolean removeCity(City city) {
        City.removeCity(city);
        Civilization civ = city.getCivilization();
        if (civ != null)
            civ.removeCity(city);
        city.getCivilization().addToMessageQueue("city " + city.getName() + " has been removed from Civilization " + city.getCivilization().getName());
        return removeCityFromMap(city);
    }

    public boolean changeCityOwner(City city, Civilization newCiv) {
        removeCity(city);
        city.setCivilization(newCiv);
        city.getCivilization().addToMessageQueue("owner of city " + city.getName() + " has been changed to Civilization " + city.getCivilization().getName());
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

    public boolean cityAttack(City city, Tile target, boolean cheat) {
        if (city == null || (!cheat && city.didActionThisTurn())) return false;
        Civilization civilization = city.getCivilization();
        Tile curTile = city.getTile();
        Unit enemyUnit = target.getCombatUnit();
        City enemyCity = target.getCity();
        if (enemyUnit == null || enemyUnit.getCivilization() == civilization)
            enemyUnit = target.getNonCombatUnit();
        if(enemyUnit == null && enemyCity == null) return false;
        Civilization otherCiv = (enemyUnit == null ? (enemyCity == null ? null : enemyCity.getCivilization()) : enemyUnit.getCivilization());
        if (otherCiv == null) return false;

        int dist = mapController.getDistanceInTiles(curTile, target);
        if (dist > city.getTerritoryRange()) return false;
        int combatStrength = (cheat ? 1000 : city.getCombatStrength());
        if (enemyCity != null && enemyCity.getCivilization() != civilization) {
            enemyCity.setHp(enemyCity.getHp() - combatStrength);
            city.setActionThisTurn(true);

            if (enemyCity.isDead()) {
                changeCityOwner(enemyCity, civilization);
            }

            return true;
        }
        else if(enemyUnit != null && enemyUnit.getCivilization() != civilization){
            enemyUnit.setHp(enemyUnit.getHp() - combatStrength);
            city.setActionThisTurn(true);

            if (enemyUnit.getHp() <= 0) {
                unitController.removeUnit(enemyUnit);
            }

            return true;
        }
        return false;
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
        if (!cheat && !city.getCivilization().getTechnologyReached(production.getTechnologyRequired()))
            return false;
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
