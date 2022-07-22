package ir.ap.client.components.map.serializers;

import ir.ap.client.App;
import ir.ap.client.components.Unit;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class UnitSerializer{

    private int unitTypeId;
    private String unitType;
    private int civId;
    private String unitAction;
    private int hp;
    private int mp;
    private int maxMp;
    private int tileId;
    private boolean isCombat;

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

    public boolean isCombat() {
        return isCombat;
    }

    public String getUnitType() {
        return unitType;
    }

    public int getMaxMp() {
        return maxMp;
    }    
}  
