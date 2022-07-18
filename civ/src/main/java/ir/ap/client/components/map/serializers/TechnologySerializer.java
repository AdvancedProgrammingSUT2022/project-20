package ir.ap.client.components.map.serializers;

import java.util.ArrayList;

public class TechnologySerializer {
    private String name;
    private int id;
    private int cost;
    private ArrayList<Enum<?>> objectsUnlocks;

    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
    public int getCost() {
        return cost;
    }
    public ArrayList<Enum<?>> getObjectsUnlocks() {
        return objectsUnlocks;
    }  
}
