package ir.ap.model;

import java.util.HashMap;

public class GameArea {
    private Map map;
    private HashMap<User,Civilization> user2civ;
    private HashMap<Civilization,User> civ2user;

    private int turn;
    private int year;
    private Era era;

    public GameArea(Map map, int turn, int year, Era era) {
        this.map = map;
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
        if(user2civ.get(user) == civilization) return false;
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

    public User getUserByName(String name)
    {
        for(User user: user2civ.keySet())
            if(user.getUsername().equals(name))
                return user;
        return null;
    }

    public User getUserByCivilization(Civilization civilization)
    {
        for(Civilization civilization1 : user2civ.values())
            if(civilization.equals(civilization1))
                return civ2user.get(civilization);
        return null;
    }

    public Civilization getCivilizationByUser(User user)
    {
        for(User user1 : user2civ.keySet())
            if(user.equals(user1))
                return user2civ.get(user);
        return null;
    }
}
