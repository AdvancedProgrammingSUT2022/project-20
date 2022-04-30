package ir.ap.model;

public class Building {
    private BuildingType type;

    public BuildingType getType() {
        return type;
    }

    public int getFoodYield() {
        return type.getFoodYield();
    }

    public int getGoldYield() {
        return type.getGoldYield();
    }

    public int getProductionYield() {
        return type.getProductionYield();
    }

    public int getHappinessYield() {
        return type.getHappinessYield();
    }

    public int getCost() {
        return type.getCost();
    }
}
