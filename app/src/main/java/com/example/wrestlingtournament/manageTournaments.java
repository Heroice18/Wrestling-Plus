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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_tournaments);

        totalTeam.add("Default Tournament");
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, newTeam);
        ArrayAdapter<String> data = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, totalTeam);
        Spinner teamLoad = (Spinner) findViewById(R.id.TournamentSpin);
        newTeam.add("Default Player");
        teamLoad.setOnItemSelectedListener(this);
        teamLoad.setAdapter(adapter);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamLoad.setAdapter(data);
        updateSelection();
        updateonClick();






    }

    //Brandon Is working on this part
    public void updateSelection(){
/*        db.collection("tournaments").whereEqualTo("adminEmail", currentUser.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                        }
                    }
                });*/


        /* This is test code, not important
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
                         *
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });*/


        for(Map.Entry<String, Object> entry : tournamentMap.entrySet()){
            String key = entry.getKey();
            Log.d(TAG, "updateSelection: " + key);
        }

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
