package com.example.wrestlingtournament;

import java.util.List;

public class BracketObject {

    private List<WrestlerObject> _wrestlerList;
    private List<MatchObject> _matchList;
    private DivisionNames _division;
    private int _weightClass;

    BracketObject(List<WrestlerObject> wrestlerList, DivisionNames division, int weightClass) {
        this._wrestlerList = wrestlerList;
        this._division = division;
        this._weightClass = weightClass;
        //Algorithm runs here that sets wrestlerList into matches and populates the matchList
    }

    public List<WrestlerObject> get_wrestlerList() { return _wrestlerList; }
    public void set_wrestlerList(List<WrestlerObject> _wrestlerList) {
        this._wrestlerList = _wrestlerList;
    }

    public List<MatchObject> get_matchList() { return _matchList; }
    public void set_matchList(List<MatchObject> _matchList) { this._matchList = _matchList; }

    public DivisionNames get_division() { return _division; }
    public void set_division(DivisionNames _division) { this._division = _division; }

}
