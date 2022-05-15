package ir.ap.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNoException;

import org.junit.Before;
import org.junit.Test;

import ir.ap.model.City;
import ir.ap.model.Unit;

public class UnitControllerTest extends AbstractControllerTest {

    private static String username1, username2;

    @Before
    public void init() {
        assertTrue(login(player1));
        assertTrue(login(player2));
        assertTrue(newGame());
        civ1 = getCivController().getCivilizationByUsername(player1.getUsername());
        civ2 = getCivController().getCivilizationByUsername(player2.getUsername());
        username1 = player1.getUsername();
        username2 = player2.getUsername();

        for (Unit unit : civ1.getUnits()) {
            if (unit.isCivilian())
                civ1NonCombatUnit = unit;
            else
                civ1CombatUnit = unit;
        }
        assertNotNull(civ1CombatUnit);
        assertNotNull(civ1NonCombatUnit);
        for (Unit unit : civ2.getUnits()) {
            if (unit.isCivilian())
                civ2NonCombatUnit = unit;
            else
                civ2CombatUnit = unit;
        }
        assertNotNull(civ2CombatUnit);
        assertNotNull(civ2NonCombatUnit);
    }

    @Test
    public void testUnitAttackNull() {
        GAME_CONTROLLER.unitAttack("chert", 0, true);
        GAME_CONTROLLER.unitAttack(username1, 300, true);
        GAME_CONTROLLER.selectUnit(username1, civ1NonCombatUnit.getId());
        GAME_CONTROLLER.unitAttack(username1, -1, false);
        GAME_CONTROLLER.unitAttack(username1, 300, true);
        GAME_CONTROLLER.selectUnit(username1, civ1CombatUnit.getId());
        GAME_CONTROLLER.unitAttack(username1, 300, true);
        GAME_CONTROLLER.unitAttack(username1, 300, false);
    }

    @Test
    public void testUnitAttackCity() {
        assertOk(GAME_CONTROLLER.selectUnit(username1, civ1NonCombatUnit.getId()));
        assertOk(GAME_CONTROLLER.unitFoundCity(username1, false));
        City city1 = civ1.getCities().get(0);

        assertOk(GAME_CONTROLLER.selectUnit(username2, civ2CombatUnit.getId()));
        assertOk(GAME_CONTROLLER.unitMoveTo(username2, city1.getTile().getIndex(), true));

        assertOk(GAME_CONTROLLER.unitAttack(username2, city1.getTile().getIndex(), true));
        assertOk(GAME_CONTROLLER.unitAttack(username2, city1.getTile().getIndex(), true));
        assertOk(GAME_CONTROLLER.cityAnnex(username2, city1.getId()));
    }
}
