package ir.ap.controller;

import java.util.Random;

import ir.ap.model.BuildingType;
import ir.ap.model.GameArea;
import ir.ap.model.Improvement;
import ir.ap.model.Resource;
import ir.ap.model.Technology;
import ir.ap.model.TerrainType;
import ir.ap.model.UnitType;
import ir.ap.model.TerrainType.TerrainFeature;

public abstract class AbstractGameController {
    protected static final Random RANDOM = new Random(System.currentTimeMillis());

    protected GameArea gameArea;

    protected GameController gameController;
    protected CivilizationController civController;
    protected MapController mapController;
    protected UnitController unitController;
    protected CityController cityController;

    public AbstractGameController() {
        BuildingType.initAll();
        Improvement.initAll();
        Resource.initAll();
        Technology.initAll();
        TerrainType.initAll();
        TerrainFeature.initAll();
        UnitType.initAll();
        gameArea = null;
        gameController = null;
        civController = null;
        mapController = null;
        unitController = null;
        cityController = null;
    }

    public AbstractGameController(GameArea gameArea) {
        BuildingType.initAll();
        Improvement.initAll();
        Resource.initAll();
        Technology.initAll();
        TerrainType.initAll();
        TerrainFeature.initAll();
        UnitType.initAll();
        this.gameArea = gameArea;
        gameController = null;
        civController = null;
        mapController = null;
        unitController = null;
        cityController = null;
    }

    public GameArea getGameArea() {
        return this.gameArea;
    }

    public void setGameArea(GameArea gameArea) {
        this.gameArea = gameArea;
    }

    public CivilizationController getCivilizationController() {
        return this.civController;
    }

    public void setCivilizationController(CivilizationController civController) {
        this.civController = civController;
    }

    public MapController getMapController() {
        return this.mapController;
    }

    public void setMapController(MapController mapController) {
        this.mapController = mapController;
    }

    public UnitController getUnitController() {
        return this.unitController;
    }

    public void setUnitController(UnitController unitController) {
        this.unitController = unitController;
    }

    public CityController getCityController() {
        return this.cityController;
    }

    public void setCityController(CityController cityController) {
        this.cityController = cityController;
    }

    public GameController getGameController() {
        return this.gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

}
