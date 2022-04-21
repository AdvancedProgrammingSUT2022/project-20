package ir.ap.model;

import java.util.ArrayList;
import java.util.Arrays;

public enum Resource {

    ///// Bonus Resources
    BANANAS(1,1,0,0,new ArrayList<>(Arrays.asList(new Enum[]{TerrainType.TerrainFeature.JUNGLE})),Improvement.PLANTATION,null),
    CATTLE(2,1,0,0,new ArrayList<>(Arrays.asList(new Enum[]{TerrainType.GRASSLAND})),Improvement.PASTURE,null),
    DEER(3,1,0,0,new ArrayList<>(Arrays.asList(new Enum[]{TerrainType.TerrainFeature.JUNGLE,TerrainType.TUNDRA,TerrainType.HILL})),Improvement.CAMP,null),
    SHEEP(4,1,0,0,new ArrayList<>(Arrays.asList(new Enum[]{TerrainType.DESERT,TerrainType.GRASSLAND,TerrainType.DESERT,TerrainType.HILL})),Improvement.PASTURE,null),
   // FISH(5,2,0,0,new ArrayList<>(Arrays.asList(new Enum[]{})),Improvement.,new ArrayList<>(Arrays.asList(new Technology[]{}))),
    WHEAT(6,1,0,0,new ArrayList<>(Arrays.asList(new Enum[]{TerrainType.PLAINS,TerrainType.TerrainFeature.FLOOD_PLAINS})),Improvement.FARM,null),

    ////// Strategic Resources
    //ALUMINUM(7,0,1,0,new ArrayList<>(Arrays.asList(new Enum[]{})),Improvement.,new ArrayList<>(Arrays.asList(new Technology[]{}))),
    COAL(8,0,1,0,new ArrayList<>(Arrays.asList(new Enum[]{TerrainType.PLAINS, TerrainType.HILL, TerrainType.GRASSLAND})),Improvement.MINE,Technology.SCIENTIFIC_THEORY),
    HORSES(9,0,1,0,new ArrayList<>(Arrays.asList(new Enum[]{TerrainType.TUNDRA,TerrainType.PLAINS,TerrainType.GRASSLAND})),Improvement.PASTURE,Technology.ANIMAL_HUSBANDRY),
    IRON(10,0,1,0,new ArrayList<>(Arrays.asList(new Enum[]{TerrainType.TUNDRA,TerrainType.PLAINS, TerrainType.DESERT,TerrainType.HILL,TerrainType.GRASSLAND,TerrainType.SNOW})),Improvement.MINE,Technology.IRONWORKING),
    //OIL(11,0,1,0,new ArrayList<>(Arrays.asList(new Enum[]{})),Improvement.,new ArrayList<>(Arrays.asList(new Technology[]{}))),
    //URANIUM(12,0,1,0,new ArrayList<>(Arrays.asList(new Enum[]{})),Improvement.,new ArrayList<>(Arrays.asList(new Technology[]{}))),
    COTTON(13,0,0,2,new ArrayList<>(Arrays.asList(new Enum[]{TerrainType.PLAINS,TerrainType.})),Improvement.,null),
    DYES(14,0,1,0,new ArrayList<>(Arrays.asList(new Enum[]{})),Improvement.,null),
    FURS(15,0,1,0,new ArrayList<>(Arrays.asList(new Enum[]{})),Improvement.,null),
    GEMS(16,0,1,0,new ArrayList<>(Arrays.asList(new Enum[]{})),Improvement.,null),
    GOLD(17,0,1,0,new ArrayList<>(Arrays.asList(new Enum[]{})),Improvement.,null),
    INCENSE(18,0,1,0,new ArrayList<>(Arrays.asList(new Enum[]{})),Improvement.,null),
    IVORY(19,0,1,0,new ArrayList<>(Arrays.asList(new Enum[]{})),Improvement.,null),
    MARBLE(20,0,1,0,new ArrayList<>(Arrays.asList(new Enum[]{})),Improvement.,null),
    PEARLS(21,0,1,0,new ArrayList<>(Arrays.asList(new Enum[]{})),Improvement.,null),
    SILK(22,0,1,0,new ArrayList<>(Arrays.asList(new Enum[]{})),Improvement.,null),
    SILVER(23,0,1,0,new ArrayList<>(Arrays.asList(new Enum[]{})),Improvement.,null),
    SPICES(24,0,1,0,new ArrayList<>(Arrays.asList(new Enum[]{})),Improvement.,null),
    SUGAR(25,0,1,0,new ArrayList<>(Arrays.asList(new Enum[]{})),Improvement.,null),
    WHALES(26,0,1,0,new ArrayList<>(Arrays.asList(new Enum[]{})),Improvement.,null),
    WINE(27,0,1,0,new ArrayList<>(Arrays.asList(new Enum[]{})),Improvement.,null);
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
    private Improvement improvementsRequired;
    private Technology technologyRequired;

    Resource(int id, int foodYield, int productionYield, int goldYield, ArrayList<Enum<?>> canBeFoundOn, Improvement improvementsRequired, Technology technologyRequired)
    {
        this.id = id;
        this.foodYield = foodYield;
        this.productionYield = productionYield;
        this.goldYield = goldYield;
        this.canBeFoundOn = canBeFoundOn;
        this.improvementsRequired = improvementsRequired;
        this.technologyRequired = technologyRequired;
    }
}
