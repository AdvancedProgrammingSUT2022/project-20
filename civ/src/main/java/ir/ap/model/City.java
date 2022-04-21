package ir.ap.model;

import java.util.ArrayList;

public class City {
    private String name;
    private Civilization civilization;

    private Tile tile;

    private Unit combatUnit;
    private Unit nonCombatUnit;

    private ArrayList<Tile> territory;
    private ArrayList<Tile> workingTiles;
}
