package com.example.wrestlingtournament;

import java.util.List;

/**
 * This class contains all of the information for a Wrestler.
 *
 * @author Team 02-01
 */
public class WrestlerObject extends UserObject {
    private List<Float> _weight;
    private String _schoolName;
    private DivisionNames _division;

    WrestlerObject(String firstName, String lastName, String schoolName, String email) {
        _schoolName = schoolName;
        super.setFirstName(firstName);
        super.setLastName(lastName);
        super.setEmail(email);
    }

    public DivisionNames get_division() { return _division; }
    public void set_division(DivisionNames division) { _division = division; }
}