package ir.ap.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class City {
    private static ArrayList<City> cities = new ArrayList<>();
    private static ArrayList<String> cityNames = new ArrayList<>();
    public static int GoldsNeedToIncreaseTiles = 5;
    public static int TurnsNeedToIncreaseTiles = 3;

    private static final int DEFAULT_HP = 20;
    private static final int DEFAULT_TERRITORY_RANGE = 5;

    private String name;
    private Civilization civilization;

    private Tile tile;

    private int territoryRange;
    private ArrayList<Tile> territory;
    private ArrayList<Tile> workingTiles;

    private Production currentProduction;
    private int productionSpent;

    private int hp;

    private double population;

    public City(String name) {
        this.name = name;
        civilization = null;

        tile = null;

        territoryRange = DEFAULT_TERRITORY_RANGE;
        territory = new ArrayList<>();
        workingTiles = new ArrayList<>();

        currentProduction = null;
        productionSpent = 0;

        hp = DEFAULT_HP;

        population = 1;
    }

    public City(String name, Civilization civilization, Tile tile) {
        this.name = name;
        this.civilization = civilization;

        this.tile = tile;

        territoryRange = DEFAULT_TERRITORY_RANGE;
        territory = new ArrayList<>();
        workingTiles = new ArrayList<>();

        currentProduction = null;
        productionSpent = 0;

        hp = DEFAULT_HP;

        population = 1;
    }

    public static City getCityByName(String cityName) {
        for (City city : cities) {
            if (city.getName().equals(cityName)) {
                return city;
            }
        }
        return null;
    }

    public static boolean hasCity(String cityName) {
        return getCityByName(cityName) != null;
    }

    public static boolean hasCity(City city) {
        return cities.contains(city);
    }

    public static boolean addCity(City city) {
        if (city == null || hasCity(city))
            return false;
        cities.add(city);
        return true;
    }

    public static boolean removeCity(City city) {
        return cities.remove(city);
    }

    public static void addCityName(String name) {
        cityNames.add(name);
    }

    public static String getCityName(int index) {
        return cityNames.get(index % cityNames.size());
    }

    public String getName() {
        return name;
    }

    public Civilization getCivilization() {
        return this.civilization;
    }

    public void setCivilization(Civilization civilization) {
        this.civilization = civilization;
    }

    public Tile getTile() {
        return this.tile;
    }

    public Unit getCombatUnit() {
        return this.tile.getCombatUnit();
    }

    public void setCombatUnit(Unit combatUnit) {
        this.tile.setCombatUnit(combatUnit);
    }

    public Unit getNonCombatUnit() {
        return this.tile.getNonCombatUnit();
    }

    public void setNonCombatUnit(Unit nonCombatUnit) {
        this.tile.setNonCombatUnit(nonCombatUnit);
    }

    public ArrayList<Tile> getTerritory() {
        return this.territory;
    }

    public void resetTerritory() {
        this.territory = new ArrayList<>();
    }

    public int getTerritoryRange() {
        return territoryRange;
    }

    public void addToTerritory(Tile tile) {
        territory.add(tile);
    }

    public void removeFromTerritory(Tile tile) {
        territory.remove(tile);
    }

    public ArrayList<Tile> getWorkingTiles() {
        return this.workingTiles;
    }

    public boolean hasWorkingTile(Tile tile) {
        return workingTiles.contains(tile);
    }

    public boolean addToWorkingTiles(Tile tile) {
        if (tile.getOwnerCity() != this || hasWorkingTile(tile))
            return false;
        workingTiles.add(tile);
        return true;
    }

    public boolean removeFromWorkingTiles(Tile tile) {
        return workingTiles.remove(tile);
    }

    public int getUnemployedCitizens() {
        return getPopulation() - getWorkingTiles().size();
    }

    public Production getCurrentProduction() {
        return this.currentProduction;
    }

    public void setCurrentProduction(Production currentProduction) {
        this.currentProduction = currentProduction;
    }

    public int getProductionSpent() {
        return this.productionSpent;
    }

    public void setProductionSpent(int value) {
        this.productionSpent = value;
    }

    public void addToProductionSpent(int delta) {
        this.productionSpent += delta;
    }

    public int getCostLeftForProductionConstruction() {
        if (getCurrentProduction() == null)
            return 0;
        return Math.max(0, getCurrentProduction().getCost() - getProductionSpent());
    }

    public int getTurnsLeftForProductionConstruction() {
        return getCostLeftForProductionConstruction() / getProductionYield();
    }

    public int getCombatStrength() {
        return getPopulation() * 3 + (getCombatUnit() != null ? getCombatUnit().getCombatStrength() / 3 : 0);
    }

    public int getHp() {
        return this.hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }

    public void addToHp(int delta) {
        this.hp += delta;
    }

    public void resetHp() {
        this.hp = DEFAULT_HP;
    }

    public boolean isDead() {
        return this.hp <= 0;
    }

    public int getPopulation() {
        return (int) Math.floor(this.population);
    }

    public double getPopulationAsDouble() {
        return this.population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public void setPopulation(double population) {
        this.population = population;
    }

    public void addToPopulation(double delta) {
        this.population += delta;
    }

    public void addToPopulation(int delta) {
        this.population += delta;
    }

    public int getRealPopulation() {
        return (int) Math.round(1000 * Math.pow(getPopulation(), 2.8));
    }

    public int getFoodYield() {
        int foodYield = 2;
        for (Tile tile : territory) {
            foodYield += tile.getFoodYield();
        }
        return foodYield;
    }

    public int getGoldYield() {
        int goldYield = 0;
        for (Tile tile : territory) {
            goldYield += tile.getGoldYield();
        }
        return goldYield;
    }

    public int getProductionYield() {
        int productionYield = 1 + (tile.getTerrainType() == TerrainType.HILL ? 1 : 0);
        for (Tile tile : territory) {
            productionYield += tile.getProductionYield();
        }
        return productionYield;
    }

    public int getScienceYield() {
        return getPopulation();
    }

    public int getResourceCount(Resource rsc) {
        int rscCnt = 0;
        for (Tile tile : territory) {
            rscCnt += tile.getResourceCount(rsc);
        }
        return rscCnt;
    }

    public int getImprovedResourceCount(Resource rsc) {
        int improvedRscCnt = 0;
        for (Tile tile : territory) {
            improvedRscCnt += tile.getImprovedResourceCount(rsc);
        }
        return improvedRscCnt;
    }

    public ArrayList<Resource> getResourcesInCity() {
        ArrayList<Resource> resources = new ArrayList<>();
        for (Tile tile : territory) {
            resources.addAll(tile.getResources());
        }
        return resources;
    }

    public ArrayList<Resource> getImprovedResourcesInCity() {
        ArrayList<Resource> improvedRscs = new ArrayList<>();
        for (Tile tile : territory) {
            improvedRscs.addAll(tile.getImprovedResources());
        }
        return improvedRscs;
    }

    public ArrayList<BuildingType> getBuildingTypesInCity() {
        HashSet<BuildingType> buildingsInTerritory = new HashSet<>();
        for (Tile tile : territory) {
            Building building = tile.getBuilding();
            if (building != null) {
                buildingsInTerritory.add(tile.getBuilding().getType());
            }
        }
        return new ArrayList<>(Arrays.asList(buildingsInTerritory.toArray(new BuildingType[0])));
    }

    @Override
    public boolean equals(Object other) {
        return (other != null) &&
                (other instanceof City && getName().equals(((City) other).getName()));
    }
}
