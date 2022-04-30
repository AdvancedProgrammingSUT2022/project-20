package ir.ap.model;

import java.util.HashMap;

import ir.ap.model.Tile.TileKnowledge;

public class GameArea {
    private static final int MAX_USERS = 10;
    private static final int MAX_LAND_TILES = 1000;

    private Map map;
    private HashMap<User,Civilization> user2civ;
    private HashMap<Civilization,User> civ2user;
    private TileKnowledge[][] tilesKnowledges = new TileKnowledge[ MAX_USERS ][ MAX_LAND_TILES ] ;

    private int turn;
    private int year;
    private Era era;
    private final long seed;

    public GameArea(long seed) {
        map = new Map( seed );
        user2civ = new HashMap<>();
        civ2user = new HashMap<>();
        for (int i = 0; i < MAX_USERS; i++)
            for (int j = 0; j < MAX_LAND_TILES; j++)
                tilesKnowledges[i][j] = TileKnowledge.FOG_OF_WAR;
        turn = 0;
        year = -4000; // 4000 BC
        era = Era.ANCIENT;
        this.seed = seed;
    }

    public Map getMap() {
        return map;
    }

    public HashMap<User, Civilization> getUser2civ() {
        return user2civ;
    }

    public HashMap<Civilization,User> getCiv2user() {
        return civ2user;
    }

    public int getTurn() {
        return turn;
    }

    public int getYear() {
        return year;
    }

    public Era getEra() {
        return era;
    }

    public long getSeed() {
        return seed;
    }

    public void setTileKnowledgeByCivilization(Civilization civ, Tile tile, TileKnowledge tileKnowledge) {
        if (civ == null || tile == null || tileKnowledge == null) return;
        tilesKnowledges[civ.getIndex()][tile.getIndex()] = tileKnowledge;
    }

    public TileKnowledge getTileKnowledgeByCivilization(Civilization civ, Tile tile) {
        if (civ == null || tile == null) return null;
        return tilesKnowledges[civ.getIndex()][tile.getIndex()];
    }

    public boolean addUser(User user, Civilization civilization)
    {
        if (user.getId() >= MAX_USERS) return false;
        if (user2civ.get(user) != null) return false;
        if (civ2user.get(civilization) != null) return false;
        user2civ.put(user,civilization);
        civ2user.put(civilization,user);
        return true;
    }

    public Civilization getCivilizationByName(String civName)
    {
        for(Civilization civilization: user2civ.values())
            if(civilization.getName().equals(civName))
                return civilization;
        return null;
    }

    public User getUserByUsername(String username)
    {
        for(User user: user2civ.keySet())
            if(user.getUsername().equals(username))
                return user;
        return null;
    }

    public User getUserByCivilization(Civilization civilization)
    {
        return civ2user.get(civilization);
    }

    public Civilization getCivilizationByUser(User user)
    {
        return user2civ.get(user);
    }

    public void updateDistancesInMap(){
        map.updateDistances();
    }

    public int getDistanceInTiles(Tile tile1, Tile tile2){
        return map.getDistanceInTiles(tile1, tile2);
    }

    public int getWeightedDistance(Tile tile1, Tile tile2){
        return map.getWeightedDistance(tile1, tile2);
    }
}
