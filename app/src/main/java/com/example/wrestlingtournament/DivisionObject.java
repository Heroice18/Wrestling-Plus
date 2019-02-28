package com.example.wrestlingtournament;

import java.util.List;

public class DivisionObject {
    private DivisionNames _division;
    private List<WrestlerObject> _addedPlayers;
    private List<WrestlerObject> _confirmedPlayers;
    private List<BracketObject> _brackets;

    DivisionObject(DivisionNames division) {
        this._division = division;
    }

    public DivisionNames get_division() { return _division; }

    public List<WrestlerObject> get_addedPlayers() { return _addedPlayers; }
    public void set_addedPlayers(List<WrestlerObject> _addedPlayers) {
        this._addedPlayers = _addedPlayers;
    }

    public List<WrestlerObject> get_confirmedPlayers() { return _confirmedPlayers; }
    public void set_confirmedPlayers(List<WrestlerObject> _confirmedPlayers) {
        this._confirmedPlayers = _confirmedPlayers;
    }

    public List<BracketObject> get_brackets() { return _brackets; }
    public void set_brackets(List<BracketObject> _brackets) { this._brackets = _brackets; }
}
