package ir.ap.model;

import java.util.ArrayList;
import java.util.Arrays;

public enum Resource {

    BANANAS(1,1,0,0,new ArrayList<>(Arrays.asList(new TerrainType.TerrainFeature[]{/*TerrainType.TerrainFeature.JUNGLE*/})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    CATTLE(2,1,0,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    DEER(3,1,0,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    FISH(4,2,0,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    SHEEP(5,1,0,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    WHEAT(6,1,0,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    ALUMINUM(7,0,1,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    COAL(8,0,1,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    HORSES(9,0,1,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    IRON(10,0,1,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    OIL(11,0,1,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    URANIUM(12,0,1,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    COTTON(13,0,1,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    DYES(14,0,1,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    FURS(15,0,1,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    GEMS(16,0,1,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    GOLD(17,0,1,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    INCENSE(18,0,1,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    IVORY(19,0,1,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    MARBLE(20,0,1,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    PEARLS(21,0,1,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    SILK(22,0,1,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    SILVER(23,0,1,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    SPICES(24,0,1,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    SUGAR(25,0,1,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    WHALES(26,0,1,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{}))),
    WINE(27,0,1,0,new ArrayList<>(Arrays.asList(new TerrainType[]{})),new ArrayList<>(Arrays.asList(new Improvement[]{})),new ArrayList<>(Arrays.asList(new Technology[]{})));
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
    private ArrayList<Improvement> improvementsRequired;
    private ArrayList<Technology> technologyRequired;

    Resource(int id, int foodYield, int productionYield, int goldYield, ArrayList<Enum<?>> canBeFoundOn, ArrayList<Improvement> improvementsRequired, ArrayList<Technology> technologyRequired)
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
