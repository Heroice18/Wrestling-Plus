package com.example.wrestlingtournament;

import org.junit.Test;

import static org.junit.Assert.*;

public class UnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void createWrestler() {
        WrestlerObject player = new WrestlerObject("Bradley", "Dawson", "BYUI", "email@byui.edu");

        assertEquals("Bradley", player.get_firstName());
        assertEquals("Dawson", player.get_lastName());
        assertEquals("BYUI", player.get_schoolName());
        assertEquals("email@byui.edu", player.get_email());
        assertEquals( DivisionNames.MYTEAM, player.get_division());
    }

    @Test
    public void createCoach() {
        CoachObject coach = new CoachObject("CJ", "Waisath", "BYUI", "email2@byui.edu");

        assertEquals("CJ", coach.get_firstName());
        assertEquals("Waisath", coach.get_lastName());
        assertEquals("BYUI", coach.get_schoolName());
        assertEquals("email2@byui.edu", coach.get_email());
    }

    @Test
    public void createMatch() {
        WrestlerObject player1 = new WrestlerObject("Bradley", "Dawson", "BYUI", "email@byui.edu");
        WrestlerObject player2 = new WrestlerObject("CJ", "Waisath", "BYUI", "email2@byui.edu");

        MatchObject match = new MatchObject(player1, player2);

        assertEquals("CJ", match.get_wrestlerBottom().get_firstName());
    }
}
