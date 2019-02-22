package com.example.wrestlingtournament;

public class Match {

    private Wrestler _wrestlerTop;
    private Wrestler _wrestlerBottom;
    private int _wrestlerTopPoints = 0;
    private int _wrestlerBottomPoints = 0;
    private Wrestler _matchWinner = null;
    private Wrestler _matchLoser = null;

    Match(Wrestler wrestler1, Wrestler wrestler2) {
        _wrestlerTop = wrestler1;
        _wrestlerBottom = wrestler2;
    }

    public Wrestler get_wrestlerTop() { return _wrestlerTop; }
    public void set_wrestlerTop(Wrestler wrestler1) { this._wrestlerTop = wrestler1; }

    public Wrestler get_wrestlerBottom() { return _wrestlerBottom; }
    public void set_wrestlerBottom(Wrestler _wrestlerBottom) {
        this._wrestlerBottom = _wrestlerBottom;
    }

    public int get_wrestlerTopPoints() { return _wrestlerTopPoints; }
    public void set_wrestlerTopPoints(int _wrestlerTopPoints) {
        this._wrestlerTopPoints = _wrestlerTopPoints;
    }

    public int get_wrestlerBottomPoints() { return _wrestlerBottomPoints; }
    public void set_wrestlerBottomPoints(int _wrestlerBottomPoints) {
        this._wrestlerBottomPoints = _wrestlerBottomPoints;
    }

    public Wrestler get_matchWinner() { return _matchWinner; }
    public Wrestler get_matchLoser() { return _matchLoser; }
    public void set_matchWinner(Winner matchWinner) {
        if(matchWinner == Winner.TOP) {
            this._matchWinner = this._wrestlerTop;
            this._matchLoser = this._wrestlerBottom;
        }
        else if(matchWinner == Winner.BOTTOM) {
            this._matchWinner = this._wrestlerBottom;
            this._matchLoser = this._wrestlerTop;
        }
        else {
            this._matchWinner = null;
            this._matchLoser = null;
        }
    }

}
