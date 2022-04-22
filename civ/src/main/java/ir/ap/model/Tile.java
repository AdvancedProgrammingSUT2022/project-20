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

    private TileKnowledge tileKnowledge;

    private ArrayList<Resource> resources;

    private Improvement improvement;
    private Building building;

    private Unit combatUnit;
    private Unit nonCombatUnit;

    private boolean hasRoad;
    private boolean hasRailRoad;

    public Tile() {
        neighbors = new Tile[6];
        hasRiverOnSide = new boolean[6];
        weight = new int[6];

        index = -1;

        terrainType = null;
        terrainFeature = null;

        tileKnowledge = null;

        resources = new ArrayList<>();

        improvement = null;
        building = null;

        combatUnit = null;
        nonCombatUnit = null;

        hasRoad = false;
        hasRailRoad = false;
    }

    public Tile(int index, TerrainType terrainType, TerrainFeature terrainFeature) {
        neighbors = new Tile[6];
        hasRiverOnSide = new boolean[6];
        weight = new int[6];

        this.index = index;

        this.terrainType = terrainType;
        this.terrainFeature = terrainFeature;

        tileKnowledge = null;

        resources = new ArrayList<>();

        improvement = null;
        building = null;

        combatUnit = null;
        nonCombatUnit = null;

        hasRoad = false;
        hasRailRoad = false;
    }

    public Tile(int index, TerrainType terrainType, TerrainFeature terrainFeature, TileKnowledge tileKnowledge) {
        neighbors = new Tile[6];
        hasRiverOnSide = new boolean[6];
        weight = new int[6];

        this.index = index;

        this.terrainType = terrainType;
        this.terrainFeature = terrainFeature;

        this.tileKnowledge = tileKnowledge;

        resources = new ArrayList<>();

        improvement = null;
        building = null;

        combatUnit = null;
        nonCombatUnit = null;

        hasRoad = false;
        hasRailRoad = false;
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

    public void setTileKnowledge(TileKnowledge tileKnowledge) {
        this.tileKnowledge = tileKnowledge;
    }

    public TileKnowledge getTileKnowledge() {
        return tileKnowledge;
    }

    public void addResource(Resource resource) {
        resources.add(resource);
    }

    public ArrayList<Resource> getResources() {
        return new ArrayList<>(resources);
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
