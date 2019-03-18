package com.example.wrestlingtournament;

import java.util.List;

public class CoachObject extends UserObject {

    private String _schoolName;
    private List<TeamObject> _teams;

    CoachObject(String firstName, String lastName, String schoolName, String email) {
        _schoolName = schoolName;
        super.setFirstName(firstName);
        super.setLastName(lastName);
        super.setEmail(email);
        TeamObject defaultTeam = new TeamObject(this, DivisionNames.VARSITY, null);
        _teams.add(defaultTeam);
    }

    public String get_schoolName() { return _schoolName; }
    public void set_schoolName(String schoolName) { _schoolName = schoolName; }

    public void makeTeam(DivisionNames division, List<WrestlerObject> players) {
        TeamObject newTeam = new TeamObject(this, division, players);
        _teams.add(newTeam);
    }

    /**
     * removeTeam();
     *
     * parameter DivisionNames division
     *
     * return boolean
     *
     * This
     */
    public boolean removeTeam(DivisionNames division) {
        if (division == DivisionNames.VARSITY)
            return false;

        for (TeamObject team : _teams) {
            if (team.get_division() == division) {
                for (WrestlerObject wrestler : team.get_roster()) {
                    _teams.get(0).addPlayer(wrestler);
                }
                _teams.remove(team);
                return true;
            }
        }

        return false;
    }

    /**
     * makeWrestler();
     *
     * parameter String firstName
     * parameter String lastName
     * parameter String email
     *
     * return void
     *
     * This function creates a new Wrestler and adds it to the default _teams entry, MYTEAM.
     */
    public void makeWrestler(String firstName, String lastName, String email) {
        WrestlerObject newWrestler = new WrestlerObject(firstName, lastName,_schoolName, email);
        _teams.get(0).addPlayer(newWrestler);
    }

    /**
     * addWrestlerToTeam();
     *
     * parameter WrestlerObject
     * parameter DivisionNames
     *
     * return boolean
     *
     * This function adds a Wrestler to the desired team in the Coach's _teams list.
     * The function removes the Wrestler from the previous team. Returns true if player was
     * successfully added to a new team, and false if the DivisionNames value passed did not exist
     * in the Coach's _teams list.
     */
    public boolean addWrestlerToTeam(WrestlerObject wrestler, DivisionNames division) {

        boolean flag = false;
        DivisionNames currentDivision = wrestler.get_division();

        // Search for division in Coach's list and add the player to that team if it exists.
        for (TeamObject team : _teams) {
            if (team.get_division() == division) {
                team.addPlayer(wrestler);
                flag = true;
            }
        }

        // If a wrestler was successfully added to another team, remove him from old team.
        if (flag)
            for (TeamObject team : _teams)
                if (team.get_division() == currentDivision)
                    team.removePlayer(wrestler);

        return flag;
    }
}
