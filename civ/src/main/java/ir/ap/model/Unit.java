package ir.ap.model;

import ir.ap.model.UnitType.UnitAction;

public class Unit {
    UnitType unitType ;
    UnitAction unitAction ;
    int mp ;
    int hp ;
    Civilization civilization ;
    Tile tile ;
    
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

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
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
    
}
