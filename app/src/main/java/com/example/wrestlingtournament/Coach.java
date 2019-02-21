package com.example.wrestlingtournament;

import java.util.List;

public class Coach extends User {

    private String _schoolName;
    private List<Team> _teams;

    Coach(String firstName, String lastName, String schoolName, String email) {
        _schoolName = schoolName;
        super.set_firstName(firstName);
        super.set_lastName(lastName);
        super.set_email(email);
        Team defaultTeam = new Team(this, DivisionNames.MYTEAM, null);
        _teams.add(defaultTeam);
    }

    public String get_schoolName() { return _schoolName; }
    public void set_schoolName(String schoolName) { _schoolName = schoolName; }

    public void makeTeam(DivisionNames division, List<Wrestler> players) {
        Team newTeam = new Team(this, division, players);
        _teams.add(newTeam);
    }

    public void makeWrestler(String firstName, String lastName, String email) {
        Wrestler newWrestler = new Wrestler(firstName, lastName,_schoolName, email);
        _teams.get(0).addPlayer(newWrestler);
    }
}
