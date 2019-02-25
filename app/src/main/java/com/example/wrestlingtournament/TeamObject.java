package com.example.wrestlingtournament;

import java.util.List;

public class TeamObject {

    private List<WrestlerObject> _roster;
    private CoachObject _headCoach;
    private DivisionNames _division;

    TeamObject(CoachObject headCoach, DivisionNames division, List<WrestlerObject> roster) {
        _headCoach = headCoach;
        _division = division;
        _roster = roster;
    }

    public List<WrestlerObject> get_roster() { return _roster; }

    public CoachObject get_headCoach() { return _headCoach; }
    public void set_headCoach(CoachObject headCoach) { _headCoach = headCoach; }

    public DivisionNames get_division() { return _division; }
    public void set_division(DivisionNames division) { _division = division; }

    public void addPlayer(WrestlerObject player) {
        player.set_division(_division);
        _roster.add(player);
    }

    public void removePlayer(WrestlerObject player) {
        _roster.remove(player);
    }
}
