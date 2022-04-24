package ir.ap.model;

import ir.ap.model.UnitType.CombatType;
import ir.ap.model.UnitType.UnitAction;

public class Unit {
    private static final int MAX_HP = 10;

    private final UnitType unitType;
    private UnitAction unitAction;
    private int mp;
    private int hp;
    private Civilization civilization;
    private Tile tile;

    public Unit(UnitType unitType) {
        this.unitType = unitType;
        unitAction = null;
        mp = unitType.getMovement();
        hp = MAX_HP;
        civilization = null;
        tile = null;
    }

    public Unit(UnitType unitType, Civilization civilization, Tile tile) {
        this.unitType = unitType;
        unitAction = null;
        mp = unitType.getMovement();
        hp = MAX_HP;
        this.civilization = civilization;
        this.tile = tile;
    }

    public Unit(UnitType unitType, UnitAction unitAction, int mp, int hp, Civilization civilization, Tile tile) {
        this.unitType = unitType;
        this.unitAction = unitAction;
        this.mp = mp;
        this.hp = hp;
        this.civilization = civilization;
        this.tile = tile;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public UnitAction getUnitAction() {
        return unitAction;
    }

    public void setUnitAction(UnitAction unitAction) {
        this.unitAction = unitAction;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public void addToMp(int delta) {
        mp += delta;
    }

    public void resetMp() {
        mp = unitType.getMovement();
    }

    public int getHp() {
        return hp;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void addToHp(int delta) {
        hp += delta;
    }

    public void resetHp() {
        hp = MAX_HP;
    }

    public Civilization getCivilization() {
        return civilization;
    }

    public void setCivilization(Civilization civilization) {
        this.civilization = civilization;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public int getCost() {
        return unitType.getCost();
    }

    public CombatType getCombatType() {
        return unitType.getCombatType();
    }

    public int getCombatStrength() {
        return unitType.getCombatStrength();
    }

    public int getRangedCombatStrength() {
        return unitType.getRangedCombatStrength();
    }

    public int getRange() {
        return unitType.getRange();
    }

    public int getMovement() {
        return unitType.getMovement();
    }

    public Resource getResourceRequired() {
        return unitType.getResourceRequired();
    }

    public Technology getTechnologyRequired() {
        return unitType.getTechnologyRequired();
    }

    public Era getEra() {
        return unitType.getEra();
    }

}
