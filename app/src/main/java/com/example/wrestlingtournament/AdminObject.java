package com.example.wrestlingtournament;

import java.util.List;

public class AdminObject extends UserObject {

    private List<TournamentObject> _tournaments;

    AdminObject(String firstName, String lastName, String email) {
        super.set_firstName(firstName);
        super.set_lastName(lastName);
        super.set_email(email);
    }

    public void makeTournament(String tournamentName, List<DivisionNames> divisions) {
        TournamentObject tournament = new TournamentObject(tournamentName, divisions);
        _tournaments.add(tournament);
    }
}
