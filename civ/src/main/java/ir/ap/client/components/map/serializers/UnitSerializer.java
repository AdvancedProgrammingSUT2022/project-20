package ir.ap.client.components.map.serializers;

public class UnitSerializer {

    private int unitTypeId;
    private int civId;
    private String unitAction;
    private int hp;
    private int mp;
    private int tileId;

    public int getUnitTypeId() {
        return unitTypeId;
    }

    public int getCivId() {
        return civId;
    }

    public String getUnitAction() {
        return unitAction;
    }

    public int getHp() {
        return hp;
    }

    public int getMp() {
        return mp;
    }

    public int getTileId() {
        return tileId;
    }
}
