package com.example.wrestlingtournament;

import java.util.List;

public class Team {

    private List<Wrestler> _roster;
    private Coach _headCoach;
    private DivisionNames _division;

    Team(Coach headCoach, DivisionNames division, List<Wrestler> roster) {
        _headCoach = headCoach;
        _division = division;
        _roster = roster;
    }

    public List<Wrestler> get_roster() { return _roster; }

    public Coach get_headCoach() { return _headCoach; }
    public void set_headCoach(Coach headCoach) { _headCoach = headCoach; }

    public DivisionNames get_division() { return _division; }
    public void set_division(DivisionNames division) { _division = division; }

    public void addPlayer(Wrestler player) {
        player.set_division(_division);
        _roster.add(player);
    }
}
