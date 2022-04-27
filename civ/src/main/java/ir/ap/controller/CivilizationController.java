package ir.ap.controller;

import ir.ap.model.*;

import java.util.ArrayList;

public class CivilizationController extends AbstractGameController {
    public CivilizationController(GameArea gameArea) {
        super(gameArea);
    }

    public Civilization getCivilizationByUsername(String username)
    {
        Civilization civilization = gameArea.getCivilizationByUser(gameArea.getUserByUsername(username));
        return civilization;
    }

    public ArrayList<Civilization> getAllCivilizations()
    {
        ArrayList<Civilization> civilizations = new ArrayList<Civilization>();
        for(Civilization civilization : gameArea.getCiv2user().keySet())
            civilizations.add(civilization);
        return civilizations;
    }

    public ArrayList<User> getAllPlayers() {
        ArrayList<User> Users = new ArrayList<User>();
        for (User user : gameArea.getCiv2user().values())
            Users.add(user);
        return Users;
    }

    public String nextTurn(String username)
    {
        return null;
    }

    public void selectCity(Civilization civilization, City city)
    {
        civilization.setSelectedUnit(null);
        civilization.setSelectedCity(city);
    }

    public void selectUnit(Civilization civilization, Unit unit)
    {
        civilization.setSelectedCity(null);
        civilization.setSelectedUnit(unit);
    }
}
