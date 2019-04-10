package com.example.wrestlingtournament;

import java.util.List;

/**
 * This class holds all of the data that an Admin has control over.
 *
 * @author Team 02-01
 */
public class AdminObject extends UserObject {
    
    private List<TournamentObject> _tournaments;
    
    AdminObject(String firstName, String lastName, String email) {
        super.setFirstName(firstName);
        super.setLastName(lastName);
        super.setEmail(email);
    }
    
    /**
     * Creates a new tournament to put into the list of tournaments an Admin owns.
     * @param tournamentName
     * @param divisions
     */
    public void makeTournament(String tournamentName, List<DivisionNames> divisions) {
        TournamentObject tournament = new TournamentObject(tournamentName, divisions);
        _tournaments.add(tournament);
    }
}
