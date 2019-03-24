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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.VISIBLE;

public class manageTournaments extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ArrayList<String> newTeam = new ArrayList<String>();
    ArrayList<String> populate = new ArrayList<String>();
    //this is for the list of team names
    ArrayList<String> totalTeam = new ArrayList<String>();
    public String m_Text;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    public static final String TAG = "manageTournaments";
    public String current_Tournament;
    public Map<String, Object> tournamentMap = new HashMap<>();
    public Map<String, Object> transferMap = new HashMap<>();

    public String type = "freshman";
    public Object player;
    public String playerData;

    RadioButton fresh, junior, varsity;
    RadioGroup playerType;
    public String division;
    ArrayList<String> totalTournaments = new ArrayList<String>();
    public Map<String, Object> TournamentStore = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_tournaments);

        //totalTeam.add("Default Tournament");
        //totalTournaments.add("first");
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, newTeam);
        ArrayAdapter<String> data = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, totalTeam);
        Spinner teamLoad = (Spinner) findViewById(R.id.TournamentSpin);
        fresh = findViewById(R.id.freshman1);
        junior = findViewById(R.id.JV);
        varsity = findViewById(R.id.Varsity);
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
        grabPlayers();






    }

    public void grabPlayers(){
        //Log.d(TAG, "grabPlayers: Current" + currentUser.getDisplayName());
        playerType = findViewById(R.id.radioGroup);
        if (playerType.getCheckedRadioButtonId() != -1) {
            type = ((RadioButton) findViewById(playerType.getCheckedRadioButtonId())).getText().toString().toLowerCase();
            Log.d(TAG, "group: type is: " + type);
        }

        Log.d(TAG, "grabPlayers: tournamnet" + current_Tournament);
        Log.d(TAG, "grabPlayers: type" + type);
        //Log.d(TAG, "grabPlayers: tournamnet" + current_Tournament);
        //DocumentReference docRef = db.collection("tournaments").document("SF");
        db.collection("tournaments").document("testing").collection("divisions").
                document(type).collection("addedPlayersDebug").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.i(TAG, "onComplete: " + "Getting the Player List");
                        Log.d(TAG, document.getId() + " ==> " + document.getData());
                        Log.d(TAG, "onComplete of document: " + document);
                        String name = "name";
                        TournamentStore = document.getData();

                        Log.d(TAG, "onComplete storage: " + TournamentStore);

                        for(Map.Entry<String,Object> entry : TournamentStore.entrySet()){
                            String key = entry.getKey();
                            Log.d(TAG, "onComplete key: " + key);
                            if(key.equals("Player")) {
                                String value = entry.getValue().toString();
                                Log.d(TAG, "onComplete value: " + value);
                                Log.d(TAG, "onComplete: Entering if name");
                                populate.add(value);

                            }
                            Log.d(TAG, "onComplete passing: " + totalTournaments);
                        }
                        newTeam = populate;
                        updateActivity();
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });


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
        ListView listView = (ListView) findViewById(R.id.PlayerList);
        listView.setAdapter(adapter);

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

                                                if(key.equals("code")) {
                                                    String value = entry.getValue().toString();
                                                    Log.d(TAG, "onComplete value: " + value);
                                                    Log.d(TAG, "onComplete: Entering if name");
                                                    totalTournaments.add(value);
                                                    current_Tournament = value;

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

        final ListView list = (ListView) findViewById(R.id.PlayerList);
       ArrayAdapter<String> adept = new ArrayAdapter<String>(this,
               R.layout.activity_listview, newTeam);
       list.setAdapter(adept);
       list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Context check = getApplicationContext();
               //String chech = manageTournaments.this;
               player = (list.getItemAtPosition(position));
               playerData = player.toString();
               Log.d(TAG, "onItemClick To String: " + playerData);


               Log.d(TAG, "onItemClick player: " + player);

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
                       int weight = Integer.parseInt(m_Text);
                       //String player = manageTournaments.this;
                       Log.d(TAG, "onClick: weight " + weight);

                       //This part will have to go into the default tournament setting in the tournaments section
                       db.collection("tournaments").document(/*current_Tournament*/"testing").collection("divisions").
                               document(type).collection("addedPlayersDebug").document("This is Here").update("weight", weight);
                       db.collection("tournaments").document("testing").collection("divisions").
                               document(type).collection("addedPlayersDebug").document("This is Here").update("confirmed", true);
                       db.collection("tournaments").document("testing").collection("divisions").
                               document(type).collection("addedPlayersDebug").document("This is Here").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                                       transferMap = document.getData();
                                       Log.d(TAG, "onComplete Transfer Map: " + transferMap);
                                       db.collection("tournaments").document("testing").collection("divisions").
                                               document(type).collection("confirmedPlayersDebug").add(transferMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                           @Override
                                           public void onSuccess(DocumentReference documentReference) {
                                               Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                           }
                                       })
                                               .addOnFailureListener(new OnFailureListener() {
                                                   @Override
                                                   public void onFailure(@NonNull Exception e) {
                                                       Log.w(TAG, "Error adding document", e);
                                                   }
                                               });


                                   } else {
                                       Log.d(TAG, "No such document");
                                   }
                               } else {
                                   Log.d(TAG, "get failed with ", task.getException());
                               }
                           }
                       });

                       db.collection("tournaments").document("testing").collection("divisions").
                               document(type).collection("addedPlayersDebug").document("This is Here").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {
                               Log.d(TAG, "DocumentSnapshot successfully deleted!");
                           }
                       })
                               .addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       Log.w(TAG, "Error deleting document", e);
                                   }
                               });



                   }
               });
               builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       db.collection("tournaments").document("testing").collection("divisions").
                               document(type).collection("addedPlayersDebug").document("This is Here").update("confirmed", false);
                       //newTeam.add(m_Text);
                       dialog.cancel();
                   }
               });

               builder.show();
               grabPlayers();
               updateActivity();


           }
       });

    }

    /**
     * This function takes input from the activity to add a new coach to the tournament. This
     * happens by adding the tournaments as a document in the coaches tournament collection
     * in firestore
     * @param a
     */
    public void addCoach(View a){

        //when the add coach button is clicked, a dialog box will come up to ask for the coache's email
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
                        Map<String, Object> tournamentMap = new HashMap<>();
                        final String coachEmail = input.getText().toString();

                        //first we need to get the information from the current tournnament so we can give it to the coach.
                        db.collection("tournaments").document(current_Tournament)
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull final Task<DocumentSnapshot> task) {
                                Log.d(TAG, "onComplete: got current tournament" + task.getResult().getData());
                                //now we will make sure the coach exists and has a usertype of coach
                                db.collection("user").document(coachEmail).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> coachTask) {
                                                //make sure the user exists
                                                if (coachTask.getResult().exists()) {
                                                    //make sure the user is a coach
                                                    if (coachTask.getResult().get("userType").equals("Coach")) {
                                                        //now we will put the tournament into the coaches tournament list
                                                        db.collection("user").document(coachEmail)
                                                                .collection("tournaments").document(current_Tournament)
                                                                .set(task.getResult().getData());
                                                        Toast.makeText(getApplicationContext(), "Coach added to tournament!",
                                                                Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Sorry, this coach does not exist",
                                                                Toast.LENGTH_LONG).show();
                                                    }
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Sorry, this coach does not exist",
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
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

            public void refresh(View r){
                grabPlayers();
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
        grabPlayers();
        //showPlayers();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
