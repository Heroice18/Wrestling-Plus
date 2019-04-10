package com.example.wrestlingtournament;

import java.util.ArrayList;
import java.util.List;

/**
 * This contains all of the division names for wrestling tournaments.
 *
 * @author Team 02-01
 */
public enum DivisionNames {
    VARSITY,
    JUNIORVARSITY,
    SOPHOMORE,
    FRESHMAN;
    
    /**
     * This function will put all of the division names into a list and return them.
     *
     * @return Returns the divisions names.
     */
    public static List<String> names() {
        DivisionNames[] states = values();
        List<String> names = new ArrayList<>(states.length);

        for (DivisionNames state : states)
            names.add(state.name());

        return names;
    }

}
