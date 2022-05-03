package ir.ap.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import ir.ap.model.City;
import ir.ap.model.Civilization;
import ir.ap.model.GameArea;
import ir.ap.model.Improvement;
import ir.ap.model.Tile;
import ir.ap.model.Unit;
import ir.ap.model.UnitType;

public class MapController extends AbstractGameController {
    
    public MapController(GameArea gameArea) {
        super(gameArea);
    }

    public void initCivilizationPositions(){
        ArrayList<Tile> khoshkiHas = gameArea.getMap().getKhoshkiHa() ;
        Collections.shuffle( khoshkiHas , new Random( gameArea.getSeed() ) );
        int i = 0;
        for( Civilization civilization : gameArea.getCiv2user().keySet() ){
            unitController.addUnit(civilization, khoshkiHas.get( i ), UnitType.SETTLER);
            unitController.addUnit(civilization, khoshkiHas.get( i ), UnitType.WARRIOR);
            i ++;
        }
    }

    public boolean addImprovement(Tile tile, Improvement impr) {
        if (tile == null || impr == null) return false;
        tile.setImprovement(impr);
        return true;
    }

    public boolean addRoad(Tile tile) {
        if (tile == null) return false;
        tile.setHasRoad(true);
        return true;
    }

    public boolean addRailRoad(Tile tile) {
        if (tile == null) return false;
        tile.setHasRailRoad(true);
        return true;
    }

    public boolean removeRoad(Tile tile) {
        // TODO : udpate distance?
        if (tile == null) return false;
        tile.setHasRoad(false);
        return true;
    }

    public boolean removeRailRoad(Tile tile) {
        if (tile == null) return false;
        tile.setHasRailRoad(false);
        return true;
    }

    public boolean removeTerrainFeature(Tile tile) {
        if (tile == null) return false;
        tile.setTerrainFeature(null);
        gameArea.getMap().updateDistances();
        return true;
    }

    public int getDistanceInTiles(Tile tile1, Tile tile2) {
        return gameArea.getDistanceInTiles(tile1, tile2);
    }
    
    public int getWeightedDistance(Tile tile1, Tile tile2) {
        return gameArea.getWeightedDistance(tile1, tile2);
    }

    public ArrayList<Tile> getTilesInRange(Tile tile, int range, boolean checkIsNonBlock) {
        if (tile == null)
            return new ArrayList<>();
        ArrayList<Tile> retTiles = new ArrayList<>();
        HashMap<Tile, Boolean> visited = new HashMap<>();
        HashMap<Tile, Integer> dist = new HashMap<>();
        Queue<Tile> queue = new LinkedList<>();
        queue.add(tile);
        dist.put(tile, 0);
        while (!queue.isEmpty()) {
            Tile curTile = queue.poll();
            if (visited.get(curTile) != null)
                continue;
            visited.put(curTile, true);
            retTiles.add(curTile);
            if (checkIsNonBlock && curTile.isBlock())
                continue;
            for (Tile tileInDepth : curTile.getNeighbors()) {
                int curDist = dist.get(curTile);
                int depthDist = curDist + 1;
                if (visited.get(tileInDepth) != null && depthDist <= range) {
                    dist.put(tileInDepth, depthDist);
                    queue.add(tileInDepth);
                }
            }
        }
        return retTiles;
    }

    public ArrayList<Tile> getTilesInRange(Tile tile, int range) {
        return getTilesInRange(tile, range, false);
    }

    public ArrayList<Tile> getTilesInRange(Unit unit, int dist) {
        if (unit == null)
            return new ArrayList<>();
        return getTilesInRange(unit.getTile(), dist);
    }

    public ArrayList<Tile> getTilesInRange(City city, int dist) {
        if (city == null)
            return new ArrayList<>();
        return getTilesInRange(city.getTile(), dist);
    }

    public ArrayList<Tile> getTilesInRange(Unit unit, int dist, boolean checkIsNonBlock) {
        if (unit == null)
            return new ArrayList<>();
        return getTilesInRange(unit.getTile(), dist, checkIsNonBlock);
    }

    public ArrayList<Tile> getTilesInRange(City city, int dist, boolean checkIsNonBlock) {
        if (city == null)
            return new ArrayList<>();
        return getTilesInRange(city.getTile(), dist, checkIsNonBlock);
    }

    public ArrayList<Tile> getUnitVisitingTilesInRange(Unit unit, int range) {
        return getTilesInRange(unit, range, true);
    }

    public ArrayList<Tile> getUnitVisitingTiles(Unit unit) {
        return getUnitVisitingTilesInRange(unit, unit.getVisitingRange());
    }
}
