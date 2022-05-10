package ir.ap.model;

public enum UnitType implements Production {
    // Fields that their value is N/A and their type is primitive(like int, ...) are -1
    ARCHER(1, 70, CombatType.ARCHERY, 4, 6, 2, 2, null, Technology.ARCHERY, Era.ANCIENT),
    CHARIOT_ARCHER(2, 60, CombatType.MOUNTED, 3, 6, 2, 4, Resource.HORSES, Technology.THE_WHEEL, Era.ANCIENT),
    SCOUT(3, 25, CombatType.RECON, 4, -1, -1, 2, null, null, Era.ANCIENT),
    SETTLER(4, 89, CombatType.CIVILIAN, -1, -1, -1, 2, null, null, Era.ANCIENT),
    SPEARMAN(5, 50, CombatType.MELEE, 7, -1, -1, 2, null, Technology.BRONZE_WORKING, Era.ANCIENT),
    WARRIOR(6, 40, CombatType.MELEE, 6, -1, -1, 2, null, null, Era.ANCIENT),
    WORKER(7, 70, CombatType.CIVILIAN, -1, -1, -1, 2, null, null, Era.ANCIENT),
    CATAPULT(8, 100, CombatType.SIEGE, 4, 14, 2, 2, Resource.IRON, Technology.MATHEMATICS, Era.CLASSICAL),
    HORSEMAN(9, 80, CombatType.MOUNTED, 12, -1, -1, 4, Resource.HORSES, Technology.HORSEBACK_RIDING, Era.CLASSICAL),
    SWORDSMAN(10, 80, CombatType.MELEE, 11, -1, -1, 2, Resource.IRON, Technology.IRON_WORKING, Era.CLASSICAL),
    CROSSBOWMAN(11, 120, CombatType.ARCHERY, 6, 12, 2, 2, null, Technology.MACHINERY, Era.MEDIEVAL),
    KNIGHT(12, 150, CombatType.MOUNTED, 18, -1, -1, 3, Resource.HORSES, Technology.CHIVALRY, Era.MEDIEVAL),
    LONGSWORDSMAN(13, 150, CombatType.MELEE, 18, -1, -1, 3, Resource.IRON, Technology.STEEL, Era.MEDIEVAL),
    PIKEMAN(14, 100, CombatType.MELEE, 10, -1, -1, 2, null, Technology.CIVIL_SERVICE, Era.MEDIEVAL),
    TREBUCHET(15, 170, CombatType.SIEGE, 6, 20, 2, 2, Resource.IRON, Technology.PHYSICS, Era.MEDIEVAL),
    CANON(16, 250, CombatType.SIEGE, 10, 26, 2, 2, null, Technology.CHEMISTRY, Era.RENAISSANCE),
    CAVALRY(17, 260, CombatType.MOUNTED, 25, -1, -1, 3, Resource.HORSES, Technology.MILITARY_SCIENCE, Era.RENAISSANCE),
    LANCER(18, 220, CombatType.MOUNTED, 22, -1, -1, 4, Resource.HORSES, Technology.METALLURGY, Era.RENAISSANCE),
    MUSKETMAN(19, 120, CombatType.GUNPOWDER, 16, -1, -1, 2, null, Technology.GUNPOWDER, Era.RENAISSANCE),
    RIFLEMAN(20, 200, CombatType.GUNPOWDER, 25, -1, -1, 2, null, Technology.RIFLING, Era.RENAISSANCE),
    ANTI_TANK_GUN(21, 300, CombatType.GUNPOWDER, 32, -1, -1, 2, null, Technology.REPLACEABLE, Era.INDUSTRIAL),
    ARTILLERY(22, 420, CombatType.SIEGE, 16, 32, 3, 2, null, Technology.DYNAMITE, Era.INDUSTRIAL),
    INFANTRY(23, 300, CombatType.GUNPOWDER, 36, -1, -1, 2, null, Technology.REPLACEABLE, Era.INDUSTRIAL),
    PANZER(24, 450, CombatType.ARMORED, 60, -1, -1, 5, null, Technology.COMBUSTION, Era.INDUSTRIAL),
    TANK(25, 450, CombatType.ARMORED, 50, -1, -1, 4, null, Technology.COMBUSTION, Era.INDUSTRIAL);

    public enum CombatType {
        ARCHERY,
        MOUNTED,
        RECON,
        CIVILIAN,
        MELEE,
        SIEGE,
        GUNPOWDER,
        ARMORED;
    }

    public enum UnitAction {
        MOVETO(1), 
        SLEEP(2), 
        ALERT(3), 
        FORTIFY(4), 
        FORTIFY_HEAL(5), 
        GARRISON(6), 
        SETUP_RANGED(7), 
        ATTACK(8), 
        FOUND_CITY(9), 
        CANCEL_MISSION(10), 
        WAKE(11), 
        DELETE(12), 
        BUILD_ROAD(13, Technology.THE_WHEEL),
        BUILD_RAILROAD(14, Technology.RAILROAD),
        BUILD_BRIDGE(15, Technology.CONSTRUCTION),
        BUILD_FARM(16), 
        BUILD_MINE(17), 
        BUILD_TRADINGPOST(18), 
        BUILD_LUMBERMILL(19), 
        BUILD_PASTURE(20), 
        BUILD_CAMP(21), 
        BUILD_PLANTATION(22), 
        BUILD_QUARRY(23), 
        REMOVE_JUNGLE(24, Technology.BRONZE_WORKING),
        REMOVE_FOREST(25, Technology.MINING),
        REMOVE_MARSH(26, Technology.MASONRY),
        REMOVE_ROUTE(27), 
        REPAIR(28); 

        private Technology technologyRequired;
        private int id;

        UnitAction(int id) {
            technologyRequired = null;
        }

        UnitAction(int id, Technology technologyRequired) {
            this.technologyRequired = technologyRequired;
        }

        public Technology getTechnologyRequired() {
            return technologyRequired;
        }

        public int getId(){
            return id;
        }
    }

    public static final int MAX_MOVEMENT = 5;
    private int id;
    private int cost;
    private CombatType combatType; 
    private int combatStrength;
    private int rangedCombatStrength;
    private int range;
    private int movement;
    private Resource resourceRequired;
    private Technology technologyRequired;
    private Era era;

    private UnitType(int id, int cost, CombatType combatType, int combatStrength, int rangedCombatStrength, int range,
            int movement, Resource resourceRequired, Technology technologyRequired, Era era) {
        this.id = id;
        this.cost = cost;
        this.combatType = combatType;
        this.combatStrength = combatStrength;
        this.rangedCombatStrength = rangedCombatStrength;
        this.range = range;
        this.movement = movement;
        this.resourceRequired = resourceRequired;
        this.technologyRequired = technologyRequired;
        this.era = era;
    }

    public int getId() {
        return id;
    }

    public int getCost() {
        return cost;
    }

    public String getName(){
       return this.name();
    }

    public CombatType getCombatType() {
        return combatType;
    }

    public int getCombatStrength() {
        return combatStrength;
    }

    public int getRangedCombatStrength() {
        return rangedCombatStrength;
    }

    public int getRange() {
        return range;
    }

    public int getMovement() {
        return movement;
    }

    public Resource getResourceRequired() {
        return resourceRequired;
    }

    public Technology getTechnologyRequired() {
        return technologyRequired;
    }

    public Era getEra() {
        return era;
    }

    public boolean isCivilian() {
        return combatType == CombatType.CIVILIAN;
    }
}
