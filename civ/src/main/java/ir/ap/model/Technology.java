package ir.ap.model;

import ir.ap.model.UnitType.UnitAction;

import java.util.ArrayList;
import java.util.Arrays;

public enum Technology {
    AGRICULTURE(1,20,new ArrayList<>(Arrays.asList(new Technology[]{})),Era.ANCIENT,new ArrayList<>(Arrays.asList(new Object[]{Improvement.FARM}))),
    ANIMAL_HUSBANDRY(2,35, new ArrayList<>(Arrays.asList(new Technology[]{Technology.AGRICULTURE})),Era.ANCIENT,new ArrayList<>(Arrays.asList(new Object[]{Resource.HORSES, Improvement.PASTURE}))),
    ARCHERY(3,35, new ArrayList<>(Arrays.asList(new Technology[]{Technology.AGRICULTURE})),Era.ANCIENT,new ArrayList<>(Arrays.asList(new Object[]{UnitType.ARCHER}))),
    POTTERY(8,35, new ArrayList<>(Arrays.asList(new Technology[]{Technology.AGRICULTURE})),Era.ANCIENT,new ArrayList<>(Arrays.asList(new Object[]{BuildingType.GRANARY}))),
    CALENDAR(5,70, new ArrayList<>(Arrays.asList(new Technology[]{Technology.POTTERY})),Era.ANCIENT,new ArrayList<>(Arrays.asList(new Object[]{Improvement.PLANTATION}))),
    MINING(7,20, new ArrayList<>(Arrays.asList(new Technology[]{Technology.AGRICULTURE})),Era.ANCIENT,new ArrayList<>(Arrays.asList(new Object[]{Improvement.MINE,UnitAction.REMOVE_FOREST}))),
    MASONRY(6,55, new ArrayList<>(Arrays.asList(new Technology[]{Technology.MINING})),Era.ANCIENT,new ArrayList<>(Arrays.asList(new Object[]{BuildingType.WALLS,Improvement.QUARRY,UnitAction.REMOVE_MARSH}))),
    BRONZE_WORKING(4,55, new ArrayList<>(Arrays.asList(new Technology[]{Technology.MINING})),Era.ANCIENT,new ArrayList<>(Arrays.asList(new Object[]{UnitType.SPEARMAN,BuildingType.BARRACKS,UnitAction.REMOVE_JUNGLE}))),
    THE_WHEEL(9,55, new ArrayList<>(Arrays.asList(new Technology[]{Technology.ANIMAL_HUSBANDRY})),Era.ANCIENT,new ArrayList<>(Arrays.asList(new Object[]{UnitType.CHARIOT_ARCHER,BuildingType.WATER_MILL,UnitAction.BUILD_ROAD}))),
    TRAPPING(10,55, new ArrayList<>(Arrays.asList(new Technology[]{Technology.ANIMAL_HUSBANDRY})),Era.ANCIENT,new ArrayList<>(Arrays.asList(new Object[]{Improvement.TRADING_POST,Improvement.CAMP}))),
    WRITING(11,55, new ArrayList<>(Arrays.asList(new Technology[]{Technology.POTTERY})),Era.ANCIENT,new ArrayList<>(Arrays.asList(new Object[]{BuildingType.LIBRARY}))),
    /// Classical Era
    CONSTRUCTION(12,100, new ArrayList<>(Arrays.asList(new Technology[]{Technology.MASONRY})),Era.CLASSICAL,new ArrayList<>(Arrays.asList(new Object[]{BuildingType.COLOSSEUM,UnitAction.BUILD_BRIDGE}))),
    HORSEBACK_RIDING(13,100, new ArrayList<>(Arrays.asList(new Technology[]{Technology.THE_WHEEL})),Era.CLASSICAL,new ArrayList<>(Arrays.asList(new Object[]{UnitType.HORSEMAN,BuildingType.STABLE,BuildingType.CIRCUS}))),
    IRON_WORKING(14,150, new ArrayList<>(Arrays.asList(new Technology[]{Technology.BRONZE_WORKING})),Era.CLASSICAL,new ArrayList<>(Arrays.asList(new Object[]{UnitType.SWORDSMAN,BuildingType.ARMORY,Resource.IRON}))),
    MATHEMATICS(15,100, new ArrayList<>(Arrays.asList(new Technology[]{Technology.THE_WHEEL,Technology.ARCHERY})),Era.CLASSICAL,new ArrayList<>(Arrays.asList(new Object[]{UnitType.CATAPULT,BuildingType.COURTHOUSE}))),
    PHILOSOPHY(16,100, new ArrayList<>(Arrays.asList(new Technology[]{Technology.WRITING})),Era.CLASSICAL,new ArrayList<>(Arrays.asList(new Object[]{BuildingType.BURIAL_TOMB,BuildingType.TEMPLE}))),
    /// Medieval Era
    CIVIL_SERVICE(18,400, new ArrayList<>(Arrays.asList(new Technology[]{Technology.PHILOSOPHY,Technology.TRAPPING})),Era.MEDIEVAL,new ArrayList<>(Arrays.asList(new Object[]{UnitType.PIKEMAN}))),
    CURRENCY(19,250, new ArrayList<>(Arrays.asList(new Technology[]{Technology.MATHEMATICS})),Era.MEDIEVAL,new ArrayList<>(Arrays.asList(new Object[]{BuildingType.MARKET}))),
    CHIVALRY(17,440, new ArrayList<>(Arrays.asList(new Technology[]{Technology.CIVIL_SERVICE,Technology.HORSEBACK_RIDING,Technology.CURRENCY})),Era.MEDIEVAL,new ArrayList<>(Arrays.asList(new Object[]{UnitType.KNIGHT,BuildingType.CASTLE}))),
    THEOLOGY(26,250, new ArrayList<>(Arrays.asList(new Technology[]{Technology.CALENDAR,Technology.PHILOSOPHY})),Era.MEDIEVAL,new ArrayList<>(Arrays.asList(new Object[]{BuildingType.MONASTERY,BuildingType.GARDEN}))),
    EDUCATION(20,440, new ArrayList<>(Arrays.asList(new Technology[]{Technology.THEOLOGY})),Era.MEDIEVAL,new ArrayList<>(Arrays.asList(new Object[]{BuildingType.UNIVERSITY}))),
    ENGINEERING(21,250, new ArrayList<>(Arrays.asList(new Technology[]{Technology.MATHEMATICS,Technology.CONSTRUCTION})),Era.MEDIEVAL,new ArrayList<>(Arrays.asList(new Object[]{}))),
    MACHINERY(22,440, new ArrayList<>(Arrays.asList(new Technology[]{Technology.ENGINEERING})),Era.MEDIEVAL,new ArrayList<>(Arrays.asList(new Object[]{UnitType.CROSSBOWMAN}))),
    METAL_CASTING(23,240, new ArrayList<>(Arrays.asList(new Technology[]{Technology.IRON_WORKING})),Era.MEDIEVAL,new ArrayList<>(Arrays.asList(new Object[]{BuildingType.FORGE,BuildingType.WORKSHOP}))),
    PHYSICS(24,440, new ArrayList<>(Arrays.asList(new Technology[]{Technology.ENGINEERING,Technology.METAL_CASTING})),Era.MEDIEVAL,new ArrayList<>(Arrays.asList(new Object[]{UnitType.TREBUCHET}))),
    STEEL(25,440, new ArrayList<>(Arrays.asList(new Technology[]{Technology.METAL_CASTING})),Era.MEDIEVAL,new ArrayList<>(Arrays.asList(new Object[]{UnitType.LONGSWORDSMAN}))),
    /// Renaissance Era
    ACOUSTICS(27,650, new ArrayList<>(Arrays.asList(new Technology[]{Technology.EDUCATION})),Era.RENAISSANCE,new ArrayList<>(Arrays.asList(new Object[]{}))),
    ARCHAEOLOGY(28,1300, new ArrayList<>(Arrays.asList(new Technology[]{Technology.ACOUSTICS})),Era.RENAISSANCE,new ArrayList<>(Arrays.asList(new Object[]{BuildingType.MUSEUM}))),
    BANKING(29,650, new ArrayList<>(Arrays.asList(new Technology[]{Technology.EDUCATION,Technology.CHIVALRY})),Era.RENAISSANCE,new ArrayList<>(Arrays.asList(new Object[]{BuildingType.SATRAP_S_COURT,BuildingType.BANK}))),
    GUNPOWDER(33,680, new ArrayList<>(Arrays.asList(new Technology[]{Technology.PHYSICS,Technology.STEEL})),Era.RENAISSANCE,new ArrayList<>(Arrays.asList(new Object[]{UnitType.MUSKETMAN}))),
    CHEMISTRY(30,900, new ArrayList<>(Arrays.asList(new Technology[]{Technology.GUNPOWDER})),Era.RENAISSANCE,new ArrayList<>(Arrays.asList(new Object[]{Technology.IRON_WORKING}))),
    PRINTING_PRESS(36,650, new ArrayList<>(Arrays.asList(new Technology[]{Technology.MACHINERY,Technology.PHYSICS})),Era.RENAISSANCE,new ArrayList<>(Arrays.asList(new Object[]{BuildingType.THEATER}))),
    ECONOMICS(31,900, new ArrayList<>(Arrays.asList(new Technology[]{Technology.BANKING,Technology.PRINTING_PRESS})),Era.RENAISSANCE,new ArrayList<>(Arrays.asList(new Object[]{BuildingType.WINDMILL}))),
    FERTILIZER(32,1300, new ArrayList<>(Arrays.asList(new Technology[]{Technology.CHEMISTRY})),Era.RENAISSANCE,new ArrayList<>(Arrays.asList(new Object[]{}))),
    METALLURGY(34,900, new ArrayList<>(Arrays.asList(new Technology[]{Technology.GUNPOWDER})),Era.RENAISSANCE,new ArrayList<>(Arrays.asList(new Object[]{UnitType.LANCER}))),
    MILITARY_SCIENCE(35,1300, new ArrayList<>(Arrays.asList(new Technology[]{Technology.ECONOMICS,Technology.CHEMISTRY})),Era.RENAISSANCE,new ArrayList<>(Arrays.asList(new Object[]{UnitType.CAVALRY,BuildingType.MILITARY_ACADEMY}))),
    RIFLING(37,1425, new ArrayList<>(Arrays.asList(new Technology[]{Technology.METALLURGY})),Era.RENAISSANCE,new ArrayList<>(Arrays.asList(new Object[]{UnitType.RIFLEMAN}))),
    SCIENTIFIC_THEORY(38,1300, new ArrayList<>(Arrays.asList(new Technology[]{Technology.ACOUSTICS})),Era.RENAISSANCE,new ArrayList<>(Arrays.asList(new Object[]{BuildingType.PUBLIC_SCHOOL,Resource.COAL}))),
    /// Industrial Era
    BIOLOGY(39,1680, new ArrayList<>(Arrays.asList(new Technology[]{Technology.ARCHAEOLOGY,Technology.SCIENTIFIC_THEORY})),Era.INDUSTRIAL,new ArrayList<>(Arrays.asList(new Object[]{}))),
    DYNAMITE(41,1900, new ArrayList<>(Arrays.asList(new Technology[]{Technology.FERTILIZER,Technology.RIFLING})),Era.INDUSTRIAL,new ArrayList<>(Arrays.asList(new Object[]{UnitType.ARTILLERY}))),
    STEAM_POWER(46,1680, new ArrayList<>(Arrays.asList(new Technology[]{Technology.SCIENTIFIC_THEORY,Technology.MILITARY_SCIENCE})),Era.INDUSTRIAL,new ArrayList<>(Arrays.asList(new Object[]{BuildingType.FACTORY}))),
    ELECTRICITY(42,1900, new ArrayList<>(Arrays.asList(new Technology[]{Technology.BIOLOGY,Technology.STEAM_POWER})),Era.INDUSTRIAL,new ArrayList<>(Arrays.asList(new Object[]{BuildingType.STOCK_EXCHANGE}))),
    RADIO(43,2200, new ArrayList<>(Arrays.asList(new Technology[]{Technology.ELECTRICITY})),Era.INDUSTRIAL,new ArrayList<>(Arrays.asList(new Object[]{BuildingType.BROADCAST_TOWER}))),
    RAILROAD(44,1900, new ArrayList<>(Arrays.asList(new Technology[]{Technology.STEAM_POWER})),Era.INDUSTRIAL,new ArrayList<>(Arrays.asList(new Object[]{BuildingType.ARSENAL, UnitAction.BUILD_RAILROAD}))),
    REPLACEABLE(45,1900, new ArrayList<>(Arrays.asList(new Technology[]{Technology.STEAM_POWER})),Era.INDUSTRIAL,new ArrayList<>(Arrays.asList(new Object[]{UnitType.ANTI_TANK_GUN}))),
    COMBUSTION(40,2200, new ArrayList<>(Arrays.asList(new Technology[]{Technology.REPLACEABLE,Technology.RAILROAD,Technology.DYNAMITE})),Era.INDUSTRIAL,new ArrayList<>(Arrays.asList(new Object[]{}))),
    TELEGRAPH(47,2200, new ArrayList<>(Arrays.asList(new Technology[]{Technology.ELECTRICITY})),Era.INDUSTRIAL,new ArrayList<>(Arrays.asList(new Object[]{BuildingType.MILITARY_BASE})));

    Technology(int id, int cost, ArrayList<Technology> technologiesRequired, Era era, ArrayList<Object> objectsUnlocks)
    {
        this.id = id;
        this.cost = cost;
        this.technologiesRequired = technologiesRequired;
        this.era = era;
        this.objectsUnlocks = objectsUnlocks;
    }

    private int id;
    private int cost;
    private ArrayList<Technology> technologiesRequired;
    private Era era;
    private ArrayList<Object> objectsUnlocks;

    public int getId()
    {
        return this.id;
    }
    public int getCost()
    {
        return this.cost;
    }
    public ArrayList<Technology> getTechnologiesRequired()
    {
        return this.technologiesRequired;
    }
    public Era getEra() {
        return era;
    }
    public ArrayList<Object> getObjectsUnlocks()
    {
        return this.objectsUnlocks;
    }
}
