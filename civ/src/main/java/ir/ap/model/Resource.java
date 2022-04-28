package ir.ap.model;

import java.util.ArrayList;
import java.util.Arrays;

public enum Resource {

    ///// Bonus Resources
    BANANAS(1,1,0,0,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.TerrainFeature.JUNGLE})),Improvement.PLANTATION,null, ResourceType.BONUS),
    CATTLE(2,1,0,0,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.GRASSLAND})),Improvement.PASTURE,null, ResourceType.BONUS),
    DEER(3,1,0,0,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.TerrainFeature.FOREST,TerrainType.TUNDRA,TerrainType.HILL})),Improvement.CAMP,null, ResourceType.BONUS),
    SHEEP(4,1,0,0,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.PLAINS,TerrainType.GRASSLAND,TerrainType.DESERT,TerrainType.HILL})),Improvement.PASTURE,null, ResourceType.BONUS),
    // FISH(5,2,0,0,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{})),Improvement.,null, ResourceType.BONUS),
    WHEAT(6,1,0,0,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.PLAINS,TerrainType.TerrainFeature.FLOOD_PLAINS})),Improvement.FARM,null, ResourceType.BONUS),

    ////// Strategic Resources
    //ALUMINUM(7,0,1,0,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{})),Improvement.,null, ResourceType.STRATEGIC),
    COAL(8,0,1,0,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.PLAINS, TerrainType.HILL, TerrainType.GRASSLAND})),Improvement.MINE,Technology.SCIENTIFIC_THEORY, ResourceType.STRATEGIC),
    HORSES(9,0,1,0,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.TUNDRA,TerrainType.PLAINS,TerrainType.GRASSLAND})),Improvement.PASTURE,Technology.ANIMAL_HUSBANDRY, ResourceType.STRATEGIC),
    IRON(10,0,1,0,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.TUNDRA,TerrainType.PLAINS, TerrainType.DESERT,TerrainType.HILL,TerrainType.GRASSLAND,TerrainType.SNOW})),Improvement.MINE,Technology.IRON_WORKING, ResourceType.STRATEGIC),
    //OIL(11,0,1,0,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{})),Improvement.,null, ResourceType.STRATEGIC),
    //URANIUM(12,0,1,0,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{})),Improvement.,null, ResourceType.STRATEGIC),
    ////// LUXURY
    COTTON(13,0,0,2,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.PLAINS,TerrainType.DESERT,TerrainType.GRASSLAND})),Improvement.PLANTATION,null,ResourceType.LUXURY),
    DYES(14,0,0,2,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.TerrainFeature.FOREST, TerrainType.TerrainFeature.JUNGLE})),Improvement.PLANTATION,null,ResourceType.LUXURY),
    FURS(15,0,0,2,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.TerrainFeature.FOREST, TerrainType.TUNDRA})),Improvement.CAMP,null,ResourceType.LUXURY),
    GEMS(16,0,0,3,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.TerrainFeature.JUNGLE,TerrainType.TUNDRA,TerrainType.PLAINS,TerrainType.DESERT,TerrainType.GRASSLAND,TerrainType.HILL})),Improvement.MINE,null,ResourceType.LUXURY),
    GOLD(17,0,0,2,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.PLAINS,TerrainType.DESERT,TerrainType.GRASSLAND,TerrainType.HILL})),Improvement.MINE,null,ResourceType.LUXURY),
    INCENSE(18,0,0,2,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.PLAINS,TerrainType.DESERT})),Improvement.PLANTATION,null,ResourceType.LUXURY),
    IVORY(19,0,0,2,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.PLAINS})),Improvement.CAMP,null,ResourceType.LUXURY),
    MARBLE(20,0,0,2,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.TUNDRA,TerrainType.PLAINS,TerrainType.DESERT,TerrainType.GRASSLAND,TerrainType.HILL})),Improvement.QUARRY,null,ResourceType.LUXURY),
    //PEARLS(21,0,0,2,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{})),Improvement.,null,ResourceType.LUXURY),
    SILK(22,0,0,2,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.TerrainFeature.FOREST})),Improvement.PLANTATION,null,ResourceType.LUXURY),
    SILVER(23,0,0,2,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.TUNDRA,TerrainType.DESERT,TerrainType.HILL})),Improvement.MINE,null,ResourceType.LUXURY),
    //SPICES(24,0,0,2,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{})),Improvement.,null,ResourceType.LUXURY),
    SUGAR(25,0,0,2,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.TerrainFeature.FLOOD_PLAINS, TerrainType.TerrainFeature.MARSH})),Improvement.PLANTATION,null,ResourceType.LUXURY);
    //WHALES(26,0,0,2,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{})),Improvement.,null,ResourceType.LUXURY),
    //WINE(27,0,0,2,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{})),Improvement.,null,ResourceType.LUXURY);

    public enum ResourceType {
        BONUS,
        STRATEGIC,
        LUXURY;
    }

    private int id;
    private int foodYield;
    private int productionYield;
    private int goldYield;
    private ArrayList<Enum<?>> canBeFoundOn;
    private Improvement improvementRequired;
    private Technology technologyRequired;
    private ResourceType type;

    Resource(int id, int foodYield, int productionYield, int goldYield, ArrayList<Enum<?>> canBeFoundOn,
            Improvement improvementRequired, Technology technologyRequired, ResourceType resourceType) {
        this.id = id;
        this.foodYield = foodYield;
        this.productionYield = productionYield;
        this.goldYield = goldYield;
        this.canBeFoundOn = canBeFoundOn;
        this.improvementRequired = improvementRequired;
        this.technologyRequired = technologyRequired;
        this.type = resourceType;
    }

    public static Resource[] getLuxuryResources() {
        return new Resource[] {COTTON, DYES, FURS, GEMS, GOLD, INCENSE, IVORY, MARBLE, SILK, SILVER, SUGAR};
    }

    public int getId() {
        return id;
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

    public ArrayList<Enum<?>> getCanBeFoundOn() {
        return canBeFoundOn;
    }

    public Improvement getImprovementRequired() {
        return improvementRequired;
    }

    public Technology getTechnologyRequired() {
        return technologyRequired;
    }

    public ResourceType getType() {
        return type;
    }
}
