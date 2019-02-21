package com.example.wrestlingtournament;

import java.util.List;

public class Wrestler extends User {
    private List<Float> _weight;
    private String _schoolName;
    private String _division;

    Wrestler(String firstName, String lastName, String schoolName) {
        _schoolName = schoolName;
        super.set_firstName(firstName);
        super.set_lastName(lastName);
    }

}