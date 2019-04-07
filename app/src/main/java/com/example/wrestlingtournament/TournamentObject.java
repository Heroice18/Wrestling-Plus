package com.example.wrestlingtournament;

import java.util.List;

/**
 * This contains all of the information necessary for a tournament.
 *
 * @author Team 02-01
 */
public class TournamentObject {
    private String _tournamentName;
    private List<WrestlerObject> _wrestlers;
    private List<DivisionObject> _divisions;

    TournamentObject(String tournamentName, List<DivisionNames> divisions) {
        _tournamentName = tournamentName;
        for (DivisionNames divisionName : divisions) {
            DivisionObject newDivision = new DivisionObject(divisionName);
            _divisions.add(newDivision);
        }
    }

    public String get_tournamentName() { return _tournamentName; }
    public void set_tournamentName(String _tournamentName) { this._tournamentName = _tournamentName; }

    public List<WrestlerObject> get_wrestlers() { return _wrestlers; }
    public void set_wrestlers(List<WrestlerObject> _wrestlers) { this._wrestlers = _wrestlers; }

    public List<DivisionObject> get_divisions() { return _divisions; }
    public void set_divisions(List<DivisionObject> _divisions) { this._divisions = _divisions; }
}
