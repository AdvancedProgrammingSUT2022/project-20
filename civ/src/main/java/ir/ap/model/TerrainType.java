package ir.ap.model;

import java.util.ArrayList;
import java.util.Arrays;

public enum TerrainType {

    COAST(1,1,0,1,0,1,new ArrayList<>(Arrays.asList(new TerrainFeature[]{TerrainFeature.ICE})),new ArrayList<>(Arrays.asList(new Resource[]{Resource.FISH,Resource.WHALES,Resource.PEARLS,Resource.OIL}))),
    DESERT(2,0,0,0,-33,1,new ArrayList<>(Arrays.asList(new TerrainFeature[]{TerrainFeature.OASIS,TerrainFeature.FLOODPLAINS})),new ArrayList<>(Arrays.asList(new Resource[]{Resource.IRON,Resource.OIL,Resource.ALUMINUM,Resource.URANIUM,Resource.GOLD,Resource.SILVER,Resource.GEMS,Resource.MARBLE,Resource.COTTON,Resource.INCENSE,Resource.SHEEP}))),
    GRASSLAND(3,2,0,0,-33,1,new ArrayList<>(Arrays.asList(new TerrainFeature[]{TerrainFeature.FOREST,TerrainFeature.MARSH})),new ArrayList<>(Arrays.asList(new Resource[]{Resource.IRON,Resource.HORSES,Resource.COAL,Resource.URANIUM,Resource.CATTLE,Resource.GOLD,Resource.GEMS,Resource.MARBLE,Resource.COTTON,Resource.WINE,Resource.SHEEP}))),
    HILL(4,0,2,0,25,2,new ArrayList<>(Arrays.asList(new TerrainFeature[]{TerrainFeature.FOREST,TerrainFeature.JUNGLE})),new ArrayList<>(Arrays.asList(new Resource[]{Resource.IRON,Resource.COAL,Resource.ALUMINUM,Resource.URANIUM,Resource.DEER,Resource.GOLD,Resource.SILVER,Resource.GEMS,Resource.MARBLE,Resource.SHEEP}))),
    MOUNTAIN(5,0,0,0,25,1000000000,new ArrayList<>(Arrays.asList(new TerrainFeature[]{})),new ArrayList<>(Arrays.asList(new Resource[]{}))),
    OCEAN(6,1,0,1,0,1,new ArrayList<>(Arrays.asList(new TerrainFeature[]{TerrainFeature.ICE})),new ArrayList<>(Arrays.asList(new Resource[]{Resource.OIL}))),
    PLAINS(7,1,1,0,-33,1,new ArrayList<>(Arrays.asList(new TerrainFeature[]{TerrainFeature.FOREST, TerrainFeature.JUNGLE})),new ArrayList<>(Arrays.asList(new Resource[]{Resource.IRON,Resource.HORSES,Resource.COAL,Resource.ALUMINUM,Resource.URANIUM,Resource.WHEAT,Resource.GOLD,Resource.GEMS,Resource.MARBLE,Resource.IVORY,Resource.COTTON,Resource.WINE,Resource.INCENSE,Resource.SHEEP}))),
    SNOW(8,0,0,0,-33,1,new ArrayList<>(Arrays.asList(new TerrainFeature[]{})),new ArrayList<>(Arrays.asList(new Resource[]{Resource.IRON,Resource.OIL,Resource.URANIUM}))),
    TUNDRA(9,1,0,0,-33,1,new ArrayList<>(Arrays.asList(new TerrainFeature[]{TerrainFeature.FOREST})),new ArrayList<>(Arrays.asList(new Resource[]{Resource.IRON,Resource.HORSES,Resource.OIL,Resource.ALUMINUM,Resource.URANIUM,Resource.DEER,Resource.SILVER,Resource.GEMS,Resource.MARBLE,Resource.FURS})));

    public enum TerrainFeature{
        BARRINGERCRATER(1,0,2,3,0,1000000000,new ArrayList<>(Arrays.asList(new Resource[]{}))),
        FALLOUT(2,3, -3, -3, -33, 2,new ArrayList<>(Arrays.asList(new Resource[]{}))),
        FLOODPLAINS(3,2,0,0,-33,1,new ArrayList<>(Arrays.asList(new Resource[]{Resource.WHEAT,Resource.SUGAR}))),
        FOREST(4,1,1,0,25,2,new ArrayList<>(Arrays.asList(new Resource[]{Resource.URANIUM,Resource.DEER,Resource.FURS,Resource.DYES,Resource.SILK}))),
        GRANDMESA(5,0,2,3,0,1000000000,new ArrayList<>(Arrays.asList(new Resource[]{}))),
        GREATBARRIERREEF(6,0,2,3,0,1000000000,new ArrayList<>(Arrays.asList(new Resource[]{}))),
        ICE(7,0,0,0,0,1000000000,new ArrayList<>(Arrays.asList(new Resource[]{}))),
        JUNGLE(8,1,-1,0,25,2,new ArrayList<>(Arrays.asList(new Resource[]{Resource.OIL,Resource.URANIUM,Resource.BANANAS,Resource.GEMS,Resource.DYES,Resource.SPICES}))),
        KRAKATOA(9,0,2,3,0,1000000000,new ArrayList<>(Arrays.asList(new Resource[]{}))),
        LAKES(10,2,0,1,0,1000000000,new ArrayList<>(Arrays.asList(new Resource[]{}))),
        MARSH(11,-1,0,0,-33,2,new ArrayList<>(Arrays.asList(new Resource[]{Resource.OIL,Resource.URANIUM,Resource.SUGAR}))),
        MTFUJI(12,0,2,3,0,1000000000,new ArrayList<>(Arrays.asList(new Resource[]{}))),
        OASIS(13,3,0,1,-33,1,new ArrayList<>(Arrays.asList(new Resource[]{}))),
        OLDFAITHFULL(14,0,2,3,0,1000000000,new ArrayList<>(Arrays.asList(new Resource[]{}))),
        RIVERS(15,0,0,1,0,1,new ArrayList<>(Arrays.asList(new Resource[]{}))),
        ROCKOFGIBRALTAR(16,0,2,3,0,1000000000,new ArrayList<>(Arrays.asList(new Resource[]{})));

        private int id;
        private int foodYield;
        private int productionYield;
        private int goldYield;
        private int combatModifier;
        private int movementCost;
        private ArrayList<Resource> resourcesPossible;
        TerrainFeature(int id, int foodYield, int productionYield, int goldYield, int combatModifier, int movementCost, ArrayList<Resource> resourcesPossible) {
            this.id = id;
            this.foodYield = foodYield;
            this.productionYield = productionYield;
            this.goldYield = goldYield;
            this.combatModifier = combatModifier;
            this.movementCost = movementCost;
            this.resourcesPossible = resourcesPossible;
        }
    }

    private int id;
    private int foodYield;
    private int productionYield;
    private int goldYield;
    private int combatModifier;
    private int movementCost;
    private ArrayList<TerrainFeature> featuresPossible;
    private ArrayList<Resource> resourcesPossible;

    /// seters
    public void setId(int x)
    {
        this.id = x;
    }
    public void setFoodYield(int x)
    {
        this.foodYield = x;
    }
    public void setProductionYield(int x)
    {
        this.productionYield = x;
    }
    public void setGoldYield(int x)
    {
        this.goldYield = x;
    }
    public void setCombatModifier(int x)
    {
        this.combatModifier = x;
    }
    public void setMovementCost(int x)
    {
        this.movementCost = x;
    }
    public void setFeaturesPossible(ArrayList<TerrainFeature> x)
    {
        this.featuresPossible = x;
    }
    public void setResourcesPossible(ArrayList<Resource> x)
    {
        this.resourcesPossible = x;
    }

    /// geters
    public int getId()
    {
        return this.id;
    }
    public int getFoodYield()
    {
        return this.foodYield;
    }
    public int getProductionYield()
    {
        return this.productionYield;
    }
    public int getGoldYield()
    {
        return this.goldYield;
    }
    public int getCombatModifier()
    {
        return this.combatModifier;
    }
    public int getMovementCost()
    {
        return this.movementCost;
    }
    public ArrayList<TerrainFeature> getFeaturesPossible()
    {
        return this.featuresPossible;
    }
    public ArrayList<Resource> getResourcesPossible()
    {
        return this.resourcesPossible;
    }

    /// constructor

    TerrainType(int id, int foodYield, int productionYield, int goldYield, int combatModifier, int movementCost, ArrayList<TerrainFeature> featuresPossible, ArrayList<Resource> resourcesPossible) {
        this.id = id;
        this.foodYield = foodYield;
        this.productionYield = productionYield;
        this.goldYield = goldYield;
        this.combatModifier = combatModifier;
        this.movementCost = movementCost;
        this.featuresPossible = featuresPossible;
        this.resourcesPossible = resourcesPossible;
    }
}
