package ir.ap.model;

import java.util.HashMap;

public class GameArea {
    private Map map;
    private HashMap<User,Civilization> user2civ;
    private HashMap<Civilization,User> civ2user;

    private int turn;
    private int year;
    private Era era;

    public GameArea() {
        map = new Map();
        user2civ = new HashMap<>();
        civ2user = new HashMap<>();
        turn = 0;
        year = -4000; // 4000 BC
        era = Era.ANCIENT;
    }

    public GameArea(Map map) {
        this.map = map;
        user2civ = new HashMap<>();
        civ2user = new HashMap<>();
        turn = 0;
        year = -4000; // 4000 BC
        era = Era.ANCIENT;
    }

    public GameArea(Map map, int turn, int year, Era era) {
        this.map = map;
        user2civ = new HashMap<>();
        civ2user = new HashMap<>();
        this.turn = turn;
        this.year = year;
        this.era = era;
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

    public boolean addUser(User user, Civilization civilization)
    {
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
}
