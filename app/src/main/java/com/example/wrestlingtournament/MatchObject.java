package com.example.wrestlingtournament;

/**
 * This contains all of the information necessary for any given match.
 *
 * @author Team 02-01
 */
public class MatchObject {

    private WrestlerObject _wrestlerTop;
    private WrestlerObject _wrestlerBottom;
    private int _wrestlerTopPoints = 0;
    private int _wrestlerBottomPoints = 0;
    private WrestlerObject _matchWinner = null;
    private WrestlerObject _matchLoser = null;

    MatchObject(WrestlerObject wrestler1, WrestlerObject wrestler2) {
        _wrestlerTop = wrestler1;
        _wrestlerBottom = wrestler2;
    }

    public WrestlerObject get_wrestlerTop() { return _wrestlerTop; }
    public void set_wrestlerTop(WrestlerObject wrestler1) { this._wrestlerTop = wrestler1; }

    public WrestlerObject get_wrestlerBottom() { return _wrestlerBottom; }
    public void set_wrestlerBottom(WrestlerObject _wrestlerBottom) {
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

    public WrestlerObject get_matchWinner() { return _matchWinner; }
    public WrestlerObject get_matchLoser() { return _matchLoser; }
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
