package ir.ap.model;

public enum BuildingType implements Production {
    // WARN: Start id's from 50 (UnitType 0->49, BuildingType 50->99)
    BARRACKS,
    GRANARY,
    LIBRARY,
    MONUMENT,
    WALLS,
    WATER_MILL,
    ARMORY,
    BURIAL_TOMB,
    CIRCUS,
    COLOSSEUM,
    COURTHOUSE,
    STABLE,
    TEMPLE,
    CASTLE,
    FORGE,
    GARDEN,
    MARKET,
    MINT,
    MONASTERY,
    UNIVERSITY,
    WORKSHOP,
    BANK,
    MILITARY_ACADEMY,
    MUSEUM,
    OPERA_HOUSE,
    PUBLIC_SCHOOL,
    SATRAP_S_COURT,
    THEATER,
    WINDMILL,
    ARSENAL,
    BROADCAST_TOWER,
    FACTORY,
    HOSPITAL,
    MILITARY_BASE,
    STOCK_EXCHANGE;

    private int id;
    private int cost;

    private int foodYield;
    private int productionYield;
    private int goldYield;
    private int happinessYield;
    private Technology technologyRequired;

    public int getId() {
        return id;
    }

    public String getName() {
        return this.name(); 
    }

    public Technology getTechnologyRequired() {
        return technologyRequired;
    }

    public int getHappinessYield() {
        return happinessYield;
    }

    public int getFoodYield() {
        return foodYield;
    }

    public int getProductionYield() {
        return productionYield;
    }

    public int getGoldYield() {
        return goldYield;
    }

    public int getCost() {
        return cost;
    }
}
