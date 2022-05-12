package ir.ap.model;

import com.google.gson.JsonObject;
import ir.ap.controller.GameController;
import ir.ap.controller.JsonResponsor;

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
    private int extraHappiness;

    int[] accessibleResourceCount;
    boolean[] technologyReached;
    private ArrayList<String> messageQueue;
    public ArrayList<String> getMessageQueue(int x) {
        ArrayList<String> out = new ArrayList<String>();
        for(int i = Math.max(messageQueue.size()-x,0); i < messageQueue.size(); i++)
            out.add(messageQueue.get(i));
        return out;
    }

    public void addToMessageQueue(String message) {
        this.messageQueue.add(message);
    }

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

        this.addToMessageQueue("Civilization " + name + " with capital city " + capital + " has been initialized");
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
        this.addToMessageQueue("city " + city.getName() + "has benn added to Civilization " + this.getName());
        cities.add(city);
    }

    public ArrayList<City> getCitiesDestroyed() {
        return citiesDestroyed;
    }

    public void addCityDestroyed(City city) {
        this.addToMessageQueue("city " + city.getName() + " from Civilization " + this.getName() + " has been destroyed");
        citiesDestroyed.add(city);
    }

    public ArrayList<City> getCitiesAnnexed() {
        return citiesAnnexed;
    }

    public void addCitiesAnnexed(City city) {
        this.addToMessageQueue("city " + city.getName() + " has been annexed by Civilization " + this.getName());
        citiesAnnexed.add(city);
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void addUnit(Unit unit) {
        this.addToMessageQueue("one unit with type " + unit.getUnitType() + " has been added to Civilization " + this.getName());
        units.add(unit);
    }

    public City getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(City city) {
        this.addToMessageQueue("city " + city.getName() + " has been selected by Civilization " + this.getName());
        this.selectedCity = city;
    }

    public Unit getSelectedUnit() {
        return this.selectedUnit;
    }

    public void setSelectedUnit(Unit selectedUnit) {
        this.addToMessageQueue("one unit whit type " + selectedUnit.getUnitType() + " has been selected by Civilization " + this.getName());
        this.selectedUnit = selectedUnit;
    }

    public Technology getCurrentResearch() {
        return this.currentResearch;
    }

    public void setCurrentResearch(Technology currentResearch) {
        this.addToMessageQueue("Civilization " + this.getName() + " started research about Technology " + currentResearch);
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

    public int getTurnsLeftForResearchFinish() {
        return (int) Math.ceil(1.0 * getScienceLeftForResearchFinish() / getScienceYield());
    }

    public int getGold() {
        return this.gold;
    }

    public void setGold(int gold) {
        this.addToMessageQueue("number of golds in Civilization " + this.getName() + " has been changed to " + gold);
        this.gold = gold;
    }

    public void addToGold(int delta) {
        this.addToMessageQueue("number of golds in Civilization " + this.getName() + " has been changed to " + gold+delta);
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
        this.addToMessageQueue("Science Civilization " + this.getName() + " has been changed to " + science);
        this.science = science;
    }

    public void addToScience(int delta) {
        this.addToMessageQueue("Science Civilization " + this.getName() + " has been changed to " + science+delta);
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
        if (tech == null) return true;
        return this.technologyReached[tech.getId()];
    }

    public void setTechnologyReached(Technology tech, boolean value) {
        if (tech == null) return;
        if(value == true)
            this.addToMessageQueue("Civilization " + this.getName() + " reached to technology " + tech);
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
        this.addToMessageQueue("one unit of type " + unit.getUnitType() + " has been removed from Civilization " + this.getName());
        if (selectedUnit == unit)
            selectedUnit = null;
        units.remove(unit);
    }
    public void removeCity(City city){
        this.addToMessageQueue("city " + city.getName() + " has been removed from Civilization " + this.getName());
        if (selectedCity == city)
            selectedCity = null;
        cities.remove(city);
    }
    public void setCapital(City capital) {
        this.addToMessageQueue("capital of Civilization " + this.getName() + " has been set to city " + capital.getName());
        this.capital = capital;
    }

    public void setName(String name) {
        this.name = name;
    }

    // public int getRealPopulation() {
    //     int res = 0;
    //     for (City city : cities) {
    //         res += city.getRealPopulation();
    //     }
    //     return res;
    // }

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

    public void addToHappiness(int delta) {
        this.addToMessageQueue("happiness of Civilization " + this.getName() + " has been added by " + delta);
        this.extraHappiness += delta;
    }

    public int getHappiness() {
        int happiness = this.extraHappiness;
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

    public boolean isUnhappy() {
        return getHappiness() < 0;
    }

    public void finishResearch() {
        if (currentResearch == null) return;
        this.addToMessageQueue("Civilization " + this.getName() + " reached to research " + currentResearch);
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
