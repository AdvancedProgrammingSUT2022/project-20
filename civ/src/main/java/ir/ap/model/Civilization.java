package ir.ap.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Civilization {
    private String name;
    private City capital;

    /* cities contains all settled, annexed and puppeted cities */
    private ArrayList<City> cities;
    private ArrayList<City> citiesDestroyed;
    private ArrayList<City> citiesAnnexed;
    private ArrayList<City> citiesPuppeted;
    private ArrayList<Unit> units;

    private City selectedCity;
    private Unit selectedUnit;

    private Technology currentResearch;

    private int gold;
    private int science;

    int[] accessibleResourceCount;
    boolean[] technologyReached;

    public Civilization() {
        name = null;
        capital = null;

        cities = new ArrayList<>();
        citiesDestroyed = new ArrayList<>();
        citiesAnnexed = new ArrayList<>();
        citiesPuppeted = new ArrayList<>();
        units = new ArrayList<>();

        selectedCity = null;
        selectedUnit = null;

        currentResearch = null;

        gold = 0;
        science = 0;

        accessibleResourceCount = new int[40];
        technologyReached = new boolean[60];
    }

    public Civilization(String name, City capital) {
        this.name = name;
        this.capital = capital;

        cities = new ArrayList<>(Arrays.asList(capital));
        citiesDestroyed = new ArrayList<>();
        citiesAnnexed = new ArrayList<>();
        citiesPuppeted = new ArrayList<>();
        units = new ArrayList<>();

        selectedCity = null;
        selectedUnit = null;

        currentResearch = null;

        gold = 0;
        science = 0;

        accessibleResourceCount = new int[40];
        technologyReached = new boolean[60];
    }

    public String getName() {
        return name;
    }

    public City getCapital() {
        return capital;
    }

    public ArrayList<City> getCities() {
        return new ArrayList<>(cities);
    }

    public void addCity(City city) {
        cities.add(city);
    }

    public ArrayList<City> getCitiesDestroyed() {
        return new ArrayList<>(citiesDestroyed);
    }

    public void addCityDestroyed(City city) {
        citiesDestroyed.add(city);
    }

    public ArrayList<City> getCitiesAnnexed() {
        return new ArrayList<>(citiesAnnexed);
    }

    public void addCitiesAnnexed(City city) {
        citiesAnnexed.add(city);
    }

    public ArrayList<City> getCitiesPuppeted() {
        return new ArrayList<>(citiesPuppeted);
    }

    public void addCitiesPuppeted(City city) {
        citiesPuppeted.add(city);
    }

    public ArrayList<Unit> getUnits() {
        return new ArrayList<>(units);
    }

    public void addUnit(Unit unit) {
        units.add(unit);
    }

    public City getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(City city) {
        this.selectedCity = city;
    }

    public Unit getSelectedUnit() {
        return this.selectedUnit;
    }

    public void setSelectedUnit(Unit selectedUnit) {
        this.selectedUnit = selectedUnit;
    }

    public Technology getCurrentResearch() {
        return this.currentResearch;
    }

    public void setCurrentResearch(Technology currentResearch) {
        this.currentResearch = currentResearch;
    }

    public int getGold() {
        return this.gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void addToGold(int delta) {
        this.gold += delta;
    }

    public int getScience() {
        return this.science;
    }

    public void setScience(int science) {
        this.science = science;
    }

    public void addToScience(int delta) {
        this.science += delta;
    }

    public boolean getTechnologyReached(Technology tech) {
        return this.technologyReached[tech.getId()];
    }

    public void setTechnologyReached(Technology tech, boolean value) {
        this.technologyReached[tech.getId()] = value;
    }

    public int getCitiesPopulationSum() {
        int res = 0;
        for (City city : cities) {
            res += city.getPopulation();
        }
        return res;
    }

    public int getRealPopulation() {
        int res = 0;
        for (City city : cities) {
            res += city.getRealPopulation();
        }
        return res;
    }

    public int[] getAllResourcesCount() {
        int[] resourceCount = new int[40];
        for (City city : cities) {
            ArrayList<Resource> curRscs = city.getResourcesInCity();
            for (Resource curRsc : curRscs) {
                ++resourceCount[curRsc.getId()];
            }
        }
        return resourceCount;
    }

    public int[] getAllAccessibleResourcesCount() {
        return accessibleResourceCount;
    }

    public int getResourceCount(Resource rsc) {
        int rscCnt = 0;
        for (City city : cities) {
            rscCnt += city.getResourceCount(rsc);
        }
        return rscCnt;
    }

    public int getAccessibleResourceCount(Resource rsc) {
        return accessibleResourceCount[rsc.getId()];
    }

    public int getHappiness() {
        int happiness = 0;
        happiness += -getCitiesPopulationSum();
        happiness += -3 * cities.size();
        happiness += -2 * citiesAnnexed.size();

        for (Resource luxuryRsc : Resource.getLuxuryResources()) {
            happiness += (getAccessibleResourceCount(luxuryRsc) != 0 ? 4 : 0);
        }
        for (City city : cities) {
            for (BuildingType building : city.getBuildingTypesInCity()) {
                happiness += building.getHappinessYield();
            }
        }

        return happiness;
    }
}
