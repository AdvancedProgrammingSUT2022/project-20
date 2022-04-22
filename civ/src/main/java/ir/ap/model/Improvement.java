package ir.ap.model;

import java.util.ArrayList;
import java.util.Arrays;

public enum Improvement {
    CAMP(1,0,0,0,Technology.TRAPPING,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.TerrainFeature.FOREST,TerrainType.TUNDRA,TerrainType.PLAINS,TerrainType.HILL}))),
    FARM(2,1,0,0,Technology.AGRICULTURE,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.PLAINS,TerrainType.DESERT,TerrainType.GRASSLAND}))),
    LUMBER_MILL(3,0,1,0,Technology.CONSTRUCTION,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.TerrainFeature.FOREST}))),
    MINE(4,0,1,0,Technology.MINING,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.PLAINS,TerrainType.DESERT,TerrainType.GRASSLAND,TerrainType.TUNDRA,TerrainType.SNOW,TerrainType.HILL,TerrainType.TerrainFeature.FOREST, TerrainType.TerrainFeature.JUNGLE,TerrainType.TerrainFeature.MARSH}))),
    PASTURE(5,0,0,0,Technology.ANIMAL_HUSBANDRY,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.PLAINS,TerrainType.DESERT,TerrainType.GRASSLAND,TerrainType.TUNDRA,TerrainType.HILL}))),
    PLANTATION(6,0,0,0,Technology.CALENDAR,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.PLAINS,TerrainType.DESERT,TerrainType.GRASSLAND,TerrainType.TerrainFeature.FOREST,TerrainType.TerrainFeature.JUNGLE,TerrainType.TerrainFeature.MARSH, TerrainType.TerrainFeature.FLOOD_PLAINS}))),
    QUARRY(7,0,0,0,Technology.MASONRY,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.PLAINS,TerrainType.DESERT,TerrainType.GRASSLAND,TerrainType.TUNDRA,TerrainType.HILL}))),
    TRADING_POST(8,0,0,1,Technology.TRAPPING,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.PLAINS,TerrainType.DESERT,TerrainType.GRASSLAND,TerrainType.TUNDRA}))),
    FACTORY(9,0,2,0,Technology.ENGINEERING,new ArrayList<Enum<?>>(Arrays.asList(new Enum<?>[]{TerrainType.PLAINS,TerrainType.DESERT,TerrainType.GRASSLAND,TerrainType.TUNDRA,TerrainType.SNOW})));

    public Improvement getImprovementById(int id)
    {
        if(id == 1) return CAMP;
        if(id == 2) return FARM;
        if(id == 3) return LUMBER_MILL;
        if(id == 4) return MINE;
        if(id == 5) return PASTURE;
        if(id == 6) return PLANTATION;
        if(id == 7) return QUARRY;
        if(id == 8) return TRADING_POST;
        if(id == 9) return FACTORY;
        return null;
    }

    int id;
    int foodYield;
    int productionYield;
    int goldYield;
    Technology technologyRequired;
    ArrayList<Enum<?>> terrainsRequired;

    Improvement(int id, int foodYield, int productionYield, int goldYield, Technology technologyRequired, ArrayList<Enum<?>> terrainsRequired) {
        this.id = id;
        this.foodYield = foodYield;
        this.productionYield = productionYield;
        this.goldYield = goldYield;
        this.technologyRequired = technologyRequired;
        this.terrainsRequired = terrainsRequired;
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
    public Technology getTechnologyRequired() {
        return technologyRequired;
    }
    public ArrayList<Enum<?>> getTerrainsRequired() {
        return terrainsRequired;
    }
}
