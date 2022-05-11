package ir.ap;

import ir.ap.model.BuildingType;
import ir.ap.model.Improvement;
import ir.ap.model.Resource;
import ir.ap.model.Technology;
import ir.ap.model.TerrainType;
import ir.ap.model.UnitType;
import ir.ap.model.TerrainType.TerrainFeature;
import ir.ap.model.UnitType.UnitAction;
import ir.ap.view.AbstractMenuView;

public class Main {
    public static void main(String[] args) {
        BuildingType.initAll();
        Improvement.initAll();
        Resource.initAll();
        Technology.initAll();
        TerrainType.initAll();
        TerrainFeature.initAll();
        UnitType.initAll();
        UnitAction.initAll();;
        AbstractMenuView.run();
        AbstractMenuView.close();
    }
}
