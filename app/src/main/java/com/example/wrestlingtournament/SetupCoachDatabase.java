package com.example.wrestlingtournament;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SetupCoachDatabase implements Runnable {
    private FirebaseFirestore db;
    private String _email;

    SetupCoachDatabase(String email) {
        _email = email;
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void run() {
        Map<String, Object> teamsExist = new HashMap<>();
        teamsExist.put("exists", true);
        db.collection("user").document(String.valueOf(_email))
                .collection("teams").document("varsity")
                .set(teamsExist);
        db.collection("user").document(String.valueOf(_email))
                .collection("teams").document("juniorvarsity")
                .set(teamsExist);
        db.collection("user").document(String.valueOf(_email))
                .collection("teams").document("freshman")
                .set(teamsExist);
    }
}
