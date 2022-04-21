package ir.ap.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class City {
    private static ArrayList<City> cities = new ArrayList<>();

    private String name;
    private Civilization civilization;

    private Tile tile;

    private Unit combatUnit;
    private Unit nonCombatUnit;

    private ArrayList<Tile> territory;
    private ArrayList<Tile> workingTiles;

    private Production currentProduction;
    private int[] turnsLeftForProductionConstruction;

    private int combatStrength;
    private int hp;

    private int population;
    private int happiness;
    private int unhappiness;
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
        combatUnit = null;
        nonCombatUnit = null;

        territory = new ArrayList<>();
        workingTiles = new ArrayList<>();

        currentProduction = null;
        turnsLeftForProductionConstruction = new int[100];

        combatStrength = 0;
        hp = 0;

        population = 0;
        happiness = 0;
        unhappiness = 0;
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
        combatUnit = null;
        nonCombatUnit = null;

        territory = new ArrayList<>();
        workingTiles = new ArrayList<>();

        currentProduction = null;
        turnsLeftForProductionConstruction = new int[100];

        combatStrength = 0;
        hp = 0;

        population = 0;
        happiness = 0;
        unhappiness = 0;
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
        return this.combatUnit;
    }

    public void setCombatUnit(Unit combatUnit) {
        this.combatUnit = combatUnit;
    }

    public Unit getNonCombatUnit() {
        return this.nonCombatUnit;
    }

    public void setNonCombatUnit(Unit nonCombatUnit) {
        this.nonCombatUnit = nonCombatUnit;
    }

    public ArrayList<Tile> getTerritory() {
        return new ArrayList<>(this.territory);
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
        return this.combatStrength;
    }

    public void setCombatStrength(int combatStrength) {
        this.combatStrength = combatStrength;
    }

    public void addToCombatStrength(int delta) {
        this.combatStrength += delta;
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

    public int getHappiness() {
        return this.happiness;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }

    public void addToHappiness(int delta) {
        this.happiness += delta;
    }

    public int getUnhappiness() {
        return this.unhappiness;
    }

    public void setUnhappiness(int unhappiness) {
        this.unhappiness = unhappiness;
    }

    public void addToUnhappiness(int delta) {
        this.unhappiness += delta;
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
