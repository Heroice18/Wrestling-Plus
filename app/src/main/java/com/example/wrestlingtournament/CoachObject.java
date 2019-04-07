package com.example.wrestlingtournament;

import java.util.List;

/**
 * This class holds all of the data that a Coach has control over.
 *
 * @author Team 02-01
 */
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
    
    /**
     * This function will add a team from the _teams list.
     *
     * @param division
     * @param players
     * @return Returns true if the team was made, otherwise returns false.
     */
    public void makeTeam(DivisionNames division, List<WrestlerObject> players) {
        TeamObject newTeam = new TeamObject(this, division, players);
        _teams.add(newTeam);
    }

    /**
     * This function will remove a team from the _teams list.
     *
     * @param division
     * @return Returns true if the team was removed, otherwise returns false.
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
     * This function creates a new Wrestler and adds it to the default _teams entry, MYTEAM.
     *
     * @param firstName
     * @param lastName
     * @param email
     */
    public void makeWrestler(String firstName, String lastName, String email) {
        WrestlerObject newWrestler = new WrestlerObject(firstName, lastName,_schoolName, email);
        _teams.get(0).addPlayer(newWrestler);
    }

    /**
     * This function adds a Wrestler to the desired team in the Coach's _teams list.
     *
     * The function removes the Wrestler from the previous team. Returns true if player was
     * successfully added to a new team, and false if the DivisionNames value passed did not exist
     * in the Coach's _teams list.
     *
     * @param wrestler
     * @param division
     * @return Returns whether the wrestler was successfully added to the team or not.
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
