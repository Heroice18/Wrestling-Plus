package com.example.wrestlingtournament;

import java.util.List;

public class WrestlerObject extends UserObject {
    private List<Float> _weight;
    private String _schoolName;
    private DivisionNames _division;

    WrestlerObject(String firstName, String lastName, String schoolName, String email) {
        _schoolName = schoolName;
        super.set_firstName(firstName);
        super.set_lastName(lastName);
        super.set_email(email);
        _division = DivisionNames.MYTEAM;
    }

    public DivisionNames get_division() { return _division; }
    public void set_division(DivisionNames division) { _division = division; }

    public String get_schoolName() { return _schoolName; }
    public void set_schoolName(String _schoolName) { this._schoolName = _schoolName; }
}