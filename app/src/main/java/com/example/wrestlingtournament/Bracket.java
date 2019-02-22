package com.example.wrestlingtournament;

import java.util.List;

public class Bracket {

    private List<Wrestler> _wrestlerList;
    private List<Match> _matchList;
    private DivisionNames _division;

    Bracket(List<Wrestler> wrestlerList, DivisionNames division) {
        this._wrestlerList = wrestlerList;
        this._division = division;
        //Algorithm runs here that sets wrestlerList into matches and populates the matchList
    }

    public List<Wrestler> get_wrestlerList() { return _wrestlerList; }
    public void set_wrestlerList(List<Wrestler> _wrestlerList) {
        this._wrestlerList = _wrestlerList;
    }

    public List<Match> get_matchList() { return _matchList; }
    public void set_matchList(List<Match> _matchList) { this._matchList = _matchList; }

    public DivisionNames get_division() { return _division; }
    public void set_division(DivisionNames _division) { this._division = _division; }

}
