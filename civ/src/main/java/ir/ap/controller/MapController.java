package ir.ap.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import ir.ap.model.Civilization;
import ir.ap.model.GameArea;
import ir.ap.model.Tile;
import ir.ap.model.UnitType;
import ir.ap.model.User;

public class MapController extends AbstractGameController {
    
    public MapController(GameArea gameArea) {
        super(gameArea);
    }

    public void initCivilizationPositions(){
        ArrayList<Tile> khoshkiHas = gameArea.getMap().getKhoshkiHa() ;
        Collections.shuffle( khoshkiHas , new Random( gameArea.getSeed() ) );
        int i = 0;
        for( Civilization civilization : gameArea.getCiv2user().keySet() ){
            unitController.addUnit(civilization, khoshkiHas.get( i ), UnitType.SETTLER);
            unitController.addUnit(civilization, khoshkiHas.get( i ), UnitType.WARRIOR);
            i ++;
        }
    }
}
