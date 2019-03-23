package com.example.wrestlingtournament;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.VISIBLE;

public class manageTournaments extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ArrayList<String> newTeam = new ArrayList<String>();
    //this is for the list of team names
    ArrayList<String> totalTeam = new ArrayList<String>();
    public String m_Text;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    public static final String TAG = "manageTournaments";
    public String current_Tournament;
    public Map<String, Object> tournamentMap = new HashMap<>();

    ArrayList<String> totalTournaments = new ArrayList<String>();
    public Map<String, Object> TournamentStore = new HashMap<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_tournaments);

        totalTeam.add("Default Tournament");
        //totalTournaments.add("first");
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, newTeam);
        ArrayAdapter<String> data = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, totalTeam);
        Spinner teamLoad = (Spinner) findViewById(R.id.TournamentSpin);
        newTeam.add("Default Player");
        teamLoad.setOnItemSelectedListener(this);
        teamLoad.setAdapter(adapter);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        teamLoad.setAdapter(data);
        updateSelection();
        updateonClick();






    }

    /**
     * This function will update the Activity which includes the Spinner and list of players
     */
    public void updateActivity(){

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, newTeam);
        ArrayAdapter<String> data = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, totalTournaments);
        Spinner teamLoad = (Spinner) findViewById(R.id.TournamentSpin);
        teamLoad.setOnItemSelectedListener(this);
        teamLoad.setAdapter(adapter);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        teamLoad.setAdapter(data);

    }

    /**
     * This function grabs all of the tournaments under the Admin's control and displays them in the spinner
     */
    public void updateSelection(){

        db.collection("user").document(currentUser.getEmail()).collection("tournaments")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.i(TAG, "onComplete: " + "Getting the Tournamnet List");
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                Log.d(TAG, "onComplete of document: " + document);
                                String name = "name";
                                TournamentStore = document.getData();

                                    Log.d(TAG, "onComplete storage: " + TournamentStore);

                                        for(Map.Entry<String,Object> entry : TournamentStore.entrySet()){
                                            String key = entry.getKey();
                                            Log.d(TAG, "onComplete key: " + key);

                                                if(key.equals("name")) {
                                                    String value = entry.getValue().toString();
                                                    Log.d(TAG, "onComplete value: " + value);
                                                    Log.d(TAG, "onComplete: Entering if name");
                                                    totalTournaments.add(value);

                                                }
                                            Log.d(TAG, "onComplete passing: " + totalTournaments);
                                        }
                                updateActivity();
                            }
                        }
                            else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                });
    }


    //This updates the listview as the players are added or subtracted
    //Also this is where I have the team select for now
    public void updateonClick(){

       ListView list = (ListView) findViewById(R.id.PlayerList);
       ArrayAdapter<String> adept = new ArrayAdapter<String>(this,
               R.layout.activity_listview, newTeam);
       list.setAdapter(adept);
       list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Context check = getApplicationContext();

               AlertDialog.Builder builder = new AlertDialog.Builder(manageTournaments.this);
               builder.setTitle("Submit wrestler's weight:");

// Set up the input
               final EditText input = new EditText(manageTournaments.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
               input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
               builder.setView(input);

// Set up the buttons
               builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       //email input
                       m_Text = input.getText().toString();

                       DocumentReference docRef = db.collection("tournaments").document(currentUser.getDisplayName())
                               .collection("test").document(currentUser.getDisplayName());
                       docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                           @Override
                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                               if (task.isSuccessful()) {
                                   DocumentSnapshot document = task.getResult();
                                   if (document.exists()) {
                                       //MOved the team map to be public at the top of the file
                                       tournamentMap = document.getData();
                                       /****
                                        * Here we'll iterate through and assign the names to the list view
                                        */
                                       Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                   } else {
                                       Log.d(TAG, "No such document");
                                   }
                               } else {
                                   Log.d(TAG, "get failed with ", task.getException());
                               }
                           }
                       });
                   }
               });
               builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.cancel();
                   }
               });

               builder.show();

           }
       });

    }

    public void addCoach(View a){

                AlertDialog.Builder builder = new AlertDialog.Builder(manageTournaments.this);
                builder.setTitle("Enter Coaches Email:");

        // Set up the input
                final EditText input = new EditText(manageTournaments.this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                builder.setView(input);
        // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //email input
                        m_Text = input.getText().toString();

                        DocumentReference docRef = db.collection("tournaments").document(currentUser.getDisplayName())
                                .collection("test").document(currentUser.getDisplayName());
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        //MOved the team map to be public at the top of the file
                                        tournamentMap = document.getData();
                                        /****
                                         * Here we'll iterate through and assign the names to the list view
                                         */
                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }






    /*
    The following functions are for the team select dropdown bar
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        current_Tournament = item;

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        //showPlayers();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




}
