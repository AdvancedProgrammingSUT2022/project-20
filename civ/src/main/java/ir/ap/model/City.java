package ir.ap.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class City {
    private static ArrayList<City> cities = new ArrayList<>();
    private static ArrayList<String> cityNames = new ArrayList<>();

    private static final int DEFAULT_HP = 20;
    private static final int DEFAULT_TERRITORY_RANGE = 5;

    private String name;
    private Civilization civilization;

    private Tile tile;

    private int territoryRange;
    private ArrayList<Tile> territory;
    private ArrayList<Tile> workingTiles;

    private Production currentProduction;
    private int[] turnsLeftForProductionConstruction;

    private int hp;

    private int population;
    private int food;
    private int production;

    private double foodYield;
    private double goldYield;
    private double productionYield;
    private double scienceYield;

    public City(String name) {
        this.name = name;
        civilization = null;

        tile = null;

        territoryRange = DEFAULT_TERRITORY_RANGE;
        territory = new ArrayList<>();
        workingTiles = new ArrayList<>();

        currentProduction = null;
        turnsLeftForProductionConstruction = new int[100];

        hp = DEFAULT_HP;

        population = 1;
        food = 0;
        production = 0;

        foodYield = 0;
        goldYield = 0;
        productionYield = 0;
        scienceYield = 0;
    }

    public City(String name, Civilization civilization, Tile tile) {
        this.name = name;
        this.civilization = civilization;

        this.tile = tile;

        territoryRange = DEFAULT_TERRITORY_RANGE;
        territory = new ArrayList<>();
        workingTiles = new ArrayList<>();

        currentProduction = null;
        turnsLeftForProductionConstruction = new int[100];

        hp = DEFAULT_HP;

        population = 1;
        food = 0;
        production = 0;

        foodYield = 0;
        goldYield = 0;
        productionYield = 0;
        scienceYield = 0;
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
        if (hasCity(city))
            return false;
        cities.add(city);
        return true;
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
        return new ArrayList<>(this.territory);
    }

    public int getTerritoryRange() {
        return territoryRange;
    }

    public void addToTerritory(Tile tile) {
        territory.add(tile);
    }

    public ArrayList<Tile> getWorkingTiles() {
        return new ArrayList<>(this.workingTiles);
    }

    public void addToWorkingTiles(Tile tile) {
        workingTiles.add(tile);
    }

    public Production getCurrentProduction() {
        return this.currentProduction;
    }

    public void setCurrentProduction(Production currentProduction) {
        this.currentProduction = currentProduction;
    }

    public int getTurnsLeftForProductionConstruction(Production production) {
        return this.turnsLeftForProductionConstruction[production.getId()];
    }

    public void setTurnsLeftForProductionConstruction(Production production, int turns) {
        this.turnsLeftForProductionConstruction[production.getId()] = turns;
    }

    public int getCombatStrength() {
        return this.population * 3 + (getCombatUnit() != null ? getCombatUnit().getCombatStrength() / 3 : 0);
    }

    public int getHp() {
        return this.hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void addToHp(int delta) {
        this.hp += delta;
    }

    public int getPopulation() {
        return this.population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public void addToPopulation(int delta) {
        this.population += delta;
    }

    public int getRealPopulation() {
        return (int) Math.round(1000 * Math.pow(getPopulation(), 2.8));
    }

    public int getFood() {
        return this.food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public void addToFood(int delta) {
        this.food += delta;
    }

    public int getProduction() {
        return this.production;
    }

    public void setProduction(int production) {
        this.production = production;
    }

    public void addToProduction(int delta) {
        this.production += delta;
    }

    public double getFoodYield() {
        return this.foodYield;
    }

    public void setFoodYield(double foodYield) {
        this.foodYield = foodYield;
    }

    public void addToFoodYield(double delta) {
        this.foodYield += delta;
    }

    public double getGoldYield() {
        return this.goldYield;
    }

    public void setGoldYield(double goldYield) {
        this.goldYield = goldYield;
    }

    public void addToGoldYield(double delta) {
        this.goldYield += delta;
    }

    public double getProductionYield() {
        return this.productionYield;
    }

    public void setProductionYield(double productionYield) {
        this.productionYield = productionYield;
    }

    public void addToProductionYield(double delta) {
        this.productionYield += delta;
    }

    public double getScienceYield() {
        return this.scienceYield;
    }

    public void setScienceYield(double scienceYield) {
        this.scienceYield = scienceYield;
    }

    public void addToScienceYield(double delta) {
        this.scienceYield += delta;
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
