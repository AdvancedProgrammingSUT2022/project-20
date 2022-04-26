package ir.ap.model;

import java.util.ArrayList;

import ir.ap.model.TerrainType.TerrainFeature;

public class Tile {

    public class Coordinate {
        double x, y;

        public Coordinate(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }

    public enum TileKnowledge {
        VISIBLE,
        REVEALED,
        FOG_OF_WAR;
    }

    private Tile[] neighbors;
    private boolean[] hasRiverOnSide;
    private int[] weight;

    private int index;

    private TerrainType terrainType;
    private TerrainFeature terrainFeature;
    private ArrayList<Resource> resources;

    private City city;
    private City ownerCity;
    private Improvement improvement;
    private Building building;

    private Unit combatUnit;
    private Unit nonCombatUnit;

    private boolean hasRoad;
    private boolean hasRailRoad;

    public Tile(int index, TerrainType terrainType, TerrainFeature terrainFeature, ArrayList<Resource> resources) {
        neighbors = new Tile[6];
        hasRiverOnSide = new boolean[6];
        weight = new int[6];

        this.index = index;

        this.terrainType = terrainType;
        this.terrainFeature = terrainFeature;

        this.resources = resources;

        city = null;
        ownerCity = null;
        improvement = null;
        building = null;

        combatUnit = null;
        nonCombatUnit = null;

        hasRoad = false;
        hasRailRoad = false;
    }

    public void setResources(ArrayList<Resource> resources) {
        this.resources = resources;
    }

    public boolean setNeighborOnSide(Direction dir, Tile other) {
        if (dir == null)
            return false;
        neighbors[dir.getId()] = other;
        return true;
    }

    public boolean setNeighborOnSide(int dirId, Tile other) {
        Direction dir = Direction.getDirectionById(dirId);
        return setNeighborOnSide(dir, other);
    }

    public Tile getNeighborOnSide(Direction dir) {
        if (dir == null)
            return null;
        return neighbors[dir.getId()];
    }

    public Tile getNeighborOnSide(int dirId) {
        Direction dir = Direction.getDirectionById(dirId);
        return getNeighborOnSide(dir);
    }

    public boolean setWeightOnSide(Direction dir, int weight) {
        if (dir == null || weight < 0)
            return false;
        this.weight[dir.getId()] = weight;
        return true;
    }

    public boolean setWeightOnSide(int dirId, int weight) {
        Direction dir = Direction.getDirectionById(dirId);
        return setWeightOnSide(dir, weight);
    }

    public int getWeightOnSide(Direction dir) {
        if (dir == null)
            return -1;
        return weight[dir.getId()];
    }

    public int getWeightOnSide(int dirId) {
        Direction dir = Direction.getDirectionById(dirId);
        return getWeightOnSide(dir);
    }

    public boolean setHasRiverOnSide(Direction dir, boolean value) {
        if (dir == null)
            return false;
        hasRiverOnSide[dir.getId()] = value;
        return true;
    }

    public boolean setHasRiverOnSide(int dirId, boolean value) {
        Direction dir = Direction.getDirectionById(dirId);
        return setHasRiverOnSide(dir, value);
    }

    public boolean getHasRiverOnSide(Direction dir) {
        if (dir == null)
            return false;
        return hasRiverOnSide[dir.getId()];
    }

    public boolean getHasRiverOnSide(int dirId) {
        Direction dir = Direction.getDirectionById(dirId);
        return getHasRiverOnSide(dir);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setTerrainType(TerrainType terrainType) {
        this.terrainType = terrainType;
    }

    public TerrainType getTerrainType() {
        return terrainType;
    }

    public void setTerrainFeature(TerrainFeature terrainFeature) {
        this.terrainFeature = terrainFeature;
    }

    public TerrainFeature getTerrainFeature() {
        return terrainFeature;
    }

    public void addResource(Resource resource) {
        resources.add(resource);
    }

    public void addResource(Resource resource, int count) {
        for (int i = 0; i < count; i++) {
            addResource(resource);
        }
    }

    public int getResourceCount(Resource resource) {
        int count = 0;
        for (Resource curRsc : this.resources) {
            count += (curRsc == resource ? 1 : 0);
        }
        return count;
    }

    public int getImprovedResourceCount(Resource resource) {
        return (resourceIsImproved(resource) ?
            getResourceCount(resource) : 0);
    }

    public ArrayList<Resource> getResources() {
        return new ArrayList<>(resources);
    }

    public ArrayList<Resource> getImprovedResources() {
        ArrayList<Resource> improvedRscs = new ArrayList<>();
        for (Resource curRsc : this.resources) {
            if (resourceIsImproved(curRsc))
                improvedRscs.add(curRsc);
        }
        return improvedRscs;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public City getCity() {
        return this.city;
    }

    public City getOwnerCity() {
        return ownerCity;
    }

    public void setImprovement(Improvement improvement) {
        this.improvement = improvement;
    }

    public Improvement getImprovement() {
        return improvement;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Building getBuilding() {
        return building;
    }

    public void setCombatUnit(Unit combatUnit) {
        this.combatUnit = combatUnit;
    }

    public Unit getCombatUnit() {
        return combatUnit;
    }

    public void setNonCombatUnit(Unit nonCombatUnit) {
        this.nonCombatUnit = nonCombatUnit;
    }

    public Unit getNonCombatUnit() {
        return nonCombatUnit;
    }

    public void setHasRoad(boolean hasRoad) {
        this.hasRoad = hasRoad;
    }

    public boolean getHasRoad() {
        return hasRoad;
    }

    public void setHasRailRoad(boolean hasRailRoad) {
        this.hasRailRoad = hasRailRoad;
    }

    public boolean getHasRailRoad() {
        return hasRailRoad;
    }

    public int getFoodYield() {
        return (terrainType != null ? terrainType.getFoodYield() : 0) +
                (terrainFeature != null ? terrainFeature.getFoodYield() : 0);
    }

    public int getGoldYield() {
        return (terrainType != null ? terrainType.getGoldYield() : 0) +
                (terrainFeature != null ? terrainFeature.getGoldYield() : 0);
    }

    public int getProductionYield() {
        return (terrainType != null ? terrainType.getProductionYield() : 0) +
                (terrainFeature != null ? terrainFeature.getProductionYield() : 0);
    }

    public int getCombatModifier() {
        return (terrainType != null ? terrainType.getCombatModifier() : 0) +
                (terrainFeature != null ? terrainFeature.getCombatModifier() : 0);
    }

    public int getMovementCost() {
        return (terrainType != null ? terrainType.getMovementCost() : 0) +
                (terrainFeature != null ? terrainFeature.getMovementCost() : 0);
    }
    
    public boolean resourceIsImproved(Resource resource) {
        return this.improvement == resource.getImprovementRequired();
    }

    public boolean hasRiver() {
        return getHasRiverOnSide(Direction.UP_RIGHT) ||
                getHasRiverOnSide(Direction.RIGHT) ||
                getHasRiverOnSide(Direction.DOWN_RIGHT) ||
                getHasRiverOnSide(Direction.DOWN_LEFT) ||
                getHasRiverOnSide(Direction.LEFT) ||
                getHasRiverOnSide(Direction.UP_LEFT);
    }

    public boolean isWaterTile() {
        return hasRiver() ||
                (terrainType != null && terrainType.isSourceOfWater()) ||
                (terrainFeature != null && terrainFeature.isSourceOfWater());
    }

    public boolean isFreshWaterTile() {
        return hasRiver() ||
                (terrainFeature != null && terrainFeature.isSourceOfWater());
    }

    public boolean isBlock() {
        return terrainFeature == TerrainFeature.FOREST ||
                terrainType == TerrainType.MOUNTAIN ||
                terrainType == TerrainType.HILL;
    }

}
