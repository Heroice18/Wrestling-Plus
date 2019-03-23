package com.example.wrestlingtournament;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create Tournament activity handles admins creating new tournaments
 */
public class CreateTournamentActivity extends AppCompatActivity {

    public static final String TAG = "CreateTournament";
    EditText tournamentNameText, tournamentCodeText, tournamentDateText;
    CheckBox freshmanCheck, jvCheck, varsityCheck;
    FirebaseUser user;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tournament);

        tournamentNameText = findViewById(R.id.tournamentName);
        tournamentCodeText = findViewById(R.id.tournamentCode);
        tournamentDateText = findViewById(R.id.tournamentDate);

        freshmanCheck = findViewById(R.id.checkBox_Freshman);
        jvCheck = findViewById(R.id.checkBox_JV);
        varsityCheck = findViewById(R.id.checkBox_Varsity);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Add a tounrmanet to firestore. The tournament code becomes the
     * name of the document, and adds the admin's email, tournament name,
     * and tournament date as fields in the document.
     * @param view
     */
    public void createTournament(View view) {
        final String tName = tournamentNameText.getText().toString();
        final String tCode = tournamentCodeText.getText().toString();
        final String tDate = tournamentDateText.getText().toString();

        final String adminEmail = user.getEmail();

        //check to make sure form is filled out appropriately
        if (tName == "") {
            tournamentNameText.setError("Please enter a name");
            return;
        }
        if (tName == "") {
            tournamentCodeText.setError("Please enter tournament code");
            return;
        }

        //This validates the date is valid and in the correct format
        String regex = "^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(tDate);
        if (!matcher.matches()) {
            tournamentDateText.setError("Please enter a valid date in the correct format. ex. 02/15/2020");
            return;
        }

        //get path to the tournament and code for it
        DocumentReference tournamentDoc = db.collection("tournaments").document(tCode);

        //we need to check if the document code already exists
        tournamentDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                //we have to make sure the tournament code does not already exist
                if(!task.getResult().exists()) {
                    //add the tournament and everything to the database
                    Map<String, Object> tournament = new HashMap<>();
                    tournament.put("adminEmail", adminEmail);
                    tournament.put("date", tDate);
                    tournament.put("name", tName);

                    //initalize maps for when we need to add divisions
                    final Map<String, Object> fMap = new HashMap<>();
                    final Map<String, Object> jMap = new HashMap<>();
                    final Map<String, Object> vMap = new HashMap<>();
                    fMap.put("division", "Freshman");
                    jMap.put("division", "Junior Varsity");
                    vMap.put("division", "Varsity");

                    Log.d(TAG, "createActivity: tName: " + tName + ", tCode: " + tCode + ", tDate: " + tDate);
                    db.collection("tournaments").document(tCode).set(tournament)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Add tournament tCode to the Admin's account
                                        final Map<String, Object> adminTournamentAddition = new HashMap<>();
                                        adminTournamentAddition.put("name", tName);
                                        adminTournamentAddition.put("code", tCode);
                                        adminTournamentAddition.put("date", tDate);
                                        db.collection("user").document(adminEmail).collection("tournaments")
                                                .document(tCode).set(adminTournamentAddition);

                                        Log.d(TAG, "onComplete: Successfully added tournament to database");
                                        if (freshmanCheck.isChecked()) {
                                            db.collection("tournaments").document(tCode)
                                                    .collection("divisions").document("freshman").set(fMap);
                                        }
                                        if (jvCheck.isChecked()) {
                                            db.collection("tournaments").document(tCode)
                                                    .collection("divisions").document("jv").set(jMap);
                                        }
                                        if (varsityCheck.isChecked()) {
                                            db.collection("tournaments").document(tCode)
                                                    .collection("divisions").document("varsity").set(vMap);
                                        }
                                        Toast.makeText(getApplicationContext(), "Tournament " + tName + " has successfully been created", Toast.LENGTH_LONG).show();
                                        Log.d(TAG, "onComplete: every successfully added to the database for the tournament");
                                    } else {
                                        Log.d(TAG, "onComplete: Error adding tournament");
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "Sorry, this tournament code has already been used", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
