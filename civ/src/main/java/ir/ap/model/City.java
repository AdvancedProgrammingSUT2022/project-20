package ir.ap.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class City {
    private static ArrayList<City> cities = new ArrayList<>();
    private static ArrayList<String> cityNames = new ArrayList<>();
    public static final int GOLD_NEEDED_TO_PURCHASE_TILE = 50;
    public static final int TURN_NEEDED_TO_EXTEND_TILES = 20;

    private static final int DEFAULT_HP = 20;
    private static final int DEFAULT_TERRITORY_RANGE = 3;

    private int id;
    private String name;
    private Civilization civilization;

    private Tile tile;

    private int territoryRange;
    private ArrayList<Tile> territory;
    private ArrayList<Tile> workingTiles;

    private Production currentProduction;
    private int productionSpent;
    private boolean actionThisTurn;

    private int hp;
    private int food;

    private double population;

    public City(int id, String name) {
        this.id = id;
        this.name = name;
        civilization = null;

        tile = null;

        territoryRange = DEFAULT_TERRITORY_RANGE;
        territory = new ArrayList<>();
        workingTiles = new ArrayList<>();

        currentProduction = null;
        productionSpent = 0;
        actionThisTurn = false;

        hp = DEFAULT_HP;
        food = 0;

        population = 1;
    }

    public City(int id, String name, Civilization civilization, Tile tile) {
        this.id = id;
        this.name = name;
        this.civilization = civilization;

        this.tile = tile;

        territoryRange = DEFAULT_TERRITORY_RANGE;
        territory = new ArrayList<>();
        workingTiles = new ArrayList<>();

        currentProduction = null;
        productionSpent = 0;
        actionThisTurn = false;

        hp = DEFAULT_HP;
        food = 0;

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
        return cityNames.get(Math.abs(index) % cityNames.size());
    }

    public int getId() {
        return id;
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

    public boolean canProduce(Production production) {
        return production != null && 
            getCivilization().getTechnologyReached(production.getTechnologyRequired()) &&
                !(production == UnitType.SETTLER && (civilization.isUnhappy() || getPopulation() < 2));
    }

    public boolean setCurrentProduction(Production production, boolean checkReachable) {
        if (production == null) {
            this.currentProduction = null;
            return true;
        }
        if (checkReachable && !canProduce(production)) {
            if (production != null)
                getCivilization().addToMessageQueue("Unable to set production " + production.getName() + " for city "
                    + getName() + " with population " + getPopulation());
            return false;
        }
        this.currentProduction = production;
        // getCivilization().addToMessageQueue("Production " + production.getName() + " set for " + getName());
        return true;
    }

    public boolean setCurrentProduction(Production production) {
        return setCurrentProduction(production, true);
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

    public void setActionThisTurn(boolean value) {
        actionThisTurn = value;
    }

    public boolean didActionThisTurn() {
        return actionThisTurn;
    }

    public int getCostLeftForProductionConstruction() {
        if (getCurrentProduction() == null)
            return 0;
        return Math.max(0, getCurrentProduction().getCost() - getProductionSpent());
    }

    public int getCostLeftForProductionConstruction(Production production) {
        return production == null ? 0 : production.getCost();
    }

    public int getTurnsLeftForProductionConstruction() {
        return (int) Math.ceil(1.0 * getCostLeftForProductionConstruction() / getProductionYield());
    }

    public int getTurnsLeftForProductionConstruction(Production production) {
        if (production == getCurrentProduction())
            return getTurnsLeftForProductionConstruction();
        return (int) Math.ceil(1.0 * getCostLeftForProductionConstruction(production) / getProductionYield());
    }

    public int getCombatStrength() {
        return getPopulation() * 3 + (getCombatUnit() != null ? getCombatUnit().getCombatStrength() / 3 : 0) + (getTile().getTerrainType() == TerrainType.HILL ? 3 : 0);
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

    public boolean canAttack() {
        return !didActionThisTurn() && !isDead();
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

    public double getPopulationGrowth() {
        return getExtraFood() / 4.0 / getPopulation();
    }

    public int getTurnsLeftForNextCitizen() {
        return (int) Math.ceil(1.0 / getPopulationGrowth());
    }

    public int getRealPopulation() {
        return (int) Math.round(1000 * Math.pow(getPopulation(), 2.8));
    }

    public int getFoodYield() {
        int foodYield = 2;
        for (Tile tile : territory) {
            if (getWorkingTiles().contains(tile))
                foodYield += tile.getFoodYield();
        }
        if (civilization.isUnhappy())
            foodYield /= 3;
        return foodYield;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public void addToFood(int delta) {
        this.food += delta;
    }

    public int getExtraFood() {
        int extraFood = -2 * getPopulation();
        if (getCurrentProduction() != UnitType.SETTLER) {
            extraFood += getFoodYield();
        } else if (extraFood < 0 && extraFood + getFoodYield() >= 0) {
            extraFood = 0;
        }
        return extraFood;
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
