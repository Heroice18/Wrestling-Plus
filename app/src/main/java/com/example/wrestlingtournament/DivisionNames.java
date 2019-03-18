package com.example.wrestlingtournament;

import java.util.ArrayList;
import java.util.List;

public enum DivisionNames {
    VARSITY,
    JUNIORVARSITY,
    SOPHOMORE,
    FRESHMAN;

    public static List<String> names() {
        DivisionNames[] states = values();
        List<String> names = new ArrayList<>(states.length);

        for (DivisionNames state : states)
            names.add(state.name());

        return names;
    }

}
