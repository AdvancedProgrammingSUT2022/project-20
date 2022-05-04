package ir.ap.model;

import java.util.ArrayList;

public class Civilization {
    private int index;
    private String name;
    private City capital;

    /* cities contains all settled, annexed and puppeted cities */
    private ArrayList<City> cities;
    private ArrayList<City> citiesDestroyed;
    private ArrayList<City> citiesAnnexed;
    private ArrayList<Unit> units;

    private City selectedCity;
    private Unit selectedUnit;

    private Technology currentResearch;
    private int scienceSpentForResearch;

    private int gold;
    private int science;    
    private int happiness;

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }

    int[] accessibleResourceCount;
    boolean[] technologyReached;

    public Civilization(int index) {
        this.index = index;
        name = null;
        capital = null;

        cities = new ArrayList<>();
        citiesDestroyed = new ArrayList<>();
        citiesAnnexed = new ArrayList<>();
        units = new ArrayList<>();

        selectedCity = null;
        selectedUnit = null;

        currentResearch = null;
        scienceSpentForResearch = 0;

        gold = 0;
        science = 0;

        accessibleResourceCount = new int[40];
        technologyReached = new boolean[60];
    }

    public Civilization(int index, String name, City capital) {
        this.index = index;
        this.name = name;
        this.capital = capital;

        cities = new ArrayList<>();
        if (capital != null)
            addCity(capital);
        citiesDestroyed = new ArrayList<>();
        citiesAnnexed = new ArrayList<>();
        units = new ArrayList<>();

        selectedCity = null;
        selectedUnit = null;

        currentResearch = null;
        scienceSpentForResearch = 0;

        gold = 0;
        science = 0;

        accessibleResourceCount = new int[40];
        technologyReached = new boolean[60];
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public City getCapital() {
        return capital;
    }

    public boolean hasCapital() {
        return getCapital() != null;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public void addCity(City city) {
        cities.add(city);
    }

    public ArrayList<City> getCitiesDestroyed() {
        return citiesDestroyed;
    }

    public void addCityDestroyed(City city) {
        citiesDestroyed.add(city);
    }

    public ArrayList<City> getCitiesAnnexed() {
        return citiesAnnexed;
    }

    public void addCitiesAnnexed(City city) {
        citiesAnnexed.add(city);
    }

    public ArrayList<Unit> getUnits() {
        return units;
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

    public int getScienceSpentForCurrentResearch() {
        return this.scienceSpentForResearch;
    }

    public void setScienceSpentForCurrentResearch(int value) {
        this.scienceSpentForResearch = value;
    }

    public void addToScienceSpentForCurrentResearch(int delta) {
        this.scienceSpentForResearch += delta;
    }

    public int getScienceLeftForResearchFinish() {
        if (currentResearch == null) return 0;
        return Math.max(0, currentResearch.getCost() - scienceSpentForResearch);
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

    public int getGoldYield() {
        int goldYield = 0;
        for (City city : cities) {
            goldYield += city.getGoldYield();
        }
        return goldYield;
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

    public int getScienceYield() {
        int scienceYield = (hasCapital() ? 3 : 0);
        for (City city : cities) {
            scienceYield += city.getScienceYield();
        }
        return scienceYield;
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
    public void removeUnit(Unit unit){
        units.remove(unit);
    }
    public void removeCity(City city){
        cities.remove(city);
    }
    public void setCapital(City capital) {
        this.capital = capital;
    }

    public void setName(String name) {
        this.name = name;
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

    public void finishResearch() {
        if (currentResearch == null) return;
        technologyReached[currentResearch.getId()] = true;
        scienceSpentForResearch = 0;
        science = 0;
        currentResearch = null;
    }

    @Override
    public boolean equals(Object other) {
        return (other != null && other instanceof Civilization) &&
                getName().equals(((Civilization) other).getName());
    }
}
