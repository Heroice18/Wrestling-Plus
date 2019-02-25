package com.example.wrestlingtournament;

import java.util.List;

public class CoachObject extends UserObject {

    private String _schoolName;
    private List<TeamObject> _teams;

    CoachObject(String firstName, String lastName, String schoolName, String email) {
        _schoolName = schoolName;
        super.set_firstName(firstName);
        super.set_lastName(lastName);
        super.set_email(email);
        TeamObject defaultTeam = new TeamObject(this, DivisionNames.MYTEAM, null);
        _teams.add(defaultTeam);
    }

    public String get_schoolName() { return _schoolName; }
    public void set_schoolName(String schoolName) { _schoolName = schoolName; }

    public void makeTeam(DivisionNames division, List<WrestlerObject> players) {
        TeamObject newTeam = new TeamObject(this, division, players);
        _teams.add(newTeam);
    }

    public void makeWrestler(String firstName, String lastName, String email) {
        WrestlerObject newWrestler = new WrestlerObject(firstName, lastName,_schoolName, email);
        _teams.get(0).addPlayer(newWrestler);
    }

}
