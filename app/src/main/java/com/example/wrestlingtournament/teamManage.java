package com.example.wrestlingtournament;

import android.content.DialogInterface;
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
import android.widget.TextView;
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
import java.util.List;
import java.util.Map;

import static android.view.View.VISIBLE;

/**
 * This Activity holds everything we need for the activity_team_manage.xml including creation,
 * button onClick functions, and the spinner selector with its database connections.
 *
 * @author Team 02-01
 */
public class teamManage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TAG = "teamManage";
    //This holds the players
    ArrayList<String> newTeam = new ArrayList<String>();
    //this is for the list of team names
    ArrayList<String> totalTeam = new ArrayList<String>();
    //This holds the coaches added tournaments
    ArrayList<String> coachesTournaments = new ArrayList<>();
    public Map<String, Object> teamMap = new HashMap<>();
    public Map<String, Object> newWrestler = new HashMap<>();
    public Map<String, Object> test = new HashMap<>();

    private String m_Text = "";
    private String dialogSpinnerText = "";

    FirebaseFirestore db;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;

    //Hide the Player information on startup
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_manage);
        Button add = (Button) findViewById(R.id.addPlayer);
        add.setVisibility(VISIBLE);
        Button addToTournament = (Button) findViewById(R.id.add_to_tournament);
        addToTournament.setVisibility(VISIBLE);
        ListView title = findViewById(R.id.team_list);
        title.setVisibility(VISIBLE);
        totalTeam.add("Varsity");
        totalTeam.add("JuniorVarsity");
        totalTeam.add("Freshman");
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, newTeam);
        ArrayAdapter<String> data = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, totalTeam);
        Spinner teamLoad = (Spinner) findViewById(R.id.spinner);
        teamLoad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                newTeam.clear();
                getWrestlersFromTeam();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                newTeam.clear();
                getWrestlersFromTeam();
            }

        });
        teamLoad.setAdapter(adapter);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamLoad.setAdapter(data);
        updateTeam();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        // Add coach's tournaments from database to an array to be fed to the spinner
        db.collection("user").document(currentUser.getEmail()).collection("tournaments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() & task.getResult() != null) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                synchronized (this) {
                                    coachesTournaments.add(document.getId());
                                }
                            }
                        }
                    }
                });
    }

    /**
     * Show buttons on the Activity
     */
    public void showPlayers(){
        Button add = (Button) findViewById(R.id.addPlayer);
        add.setVisibility(VISIBLE);
        Button remove = (Button) findViewById(R.id.add_to_tournament);
        remove.setVisibility(VISIBLE);

        ListView title = findViewById(R.id.team_list);
        title.setVisibility(VISIBLE);
    }

    /**
     * Takes our teams for the coach and adds them to the spinner
     */
    public void updateTeam(){
       //This part fills the table with the players data
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, newTeam);

        ListView listView = (ListView) findViewById(R.id.team_list);
        listView.setAdapter(adapter);
    }

    /**
     * Originally for the popup box now it doesn't do anything
     * This
     * @param w
     */
    @Deprecated
    public void addWrestler(View w){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View pop = inflater.inflate(R.layout.popup_window, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focus = true;
        final PopupWindow popupWindow = new PopupWindow(pop, width, height, focus);
        popupWindow.showAtLocation(w,Gravity.CENTER, 0,0);
        //EditText check = (EditText) pop.findViewById(R.id.playerDetails);
        pop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                popupWindow.dismiss();
                return true;
            }
        });

        updateTeam();
    }

    /**
     * This file opens the input box for the user to type the name and add that
     * to the list and when the user inputs a valid email, adds that wrestler
     * to his default team
     */
    public void confirmAdd(View c)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogBox);
        String total = "Submit Wrestler's Email";
        builder.setTitle(R.string.submit_wrestler);
        builder.setIcon(R.drawable.wplus);

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //email input
                m_Text = input.getText().toString();

                db.collection("user").document(m_Text)
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (!documentSnapshot.exists()) {
                            Toast.makeText(teamManage.this, "Could not find wrestler", Toast.LENGTH_SHORT).show();
                        } else if(!documentSnapshot.get("userType").equals("Wrestler")) {
                            Toast.makeText(teamManage.this, "Email must belong to a wrestler", Toast.LENGTH_SHORT).show();
                        } else {
                            String newFName = (String) documentSnapshot.get("firstName");
                            String newLName = (String) documentSnapshot.get("lastName");
                            String wrestlerName = newFName + " " + newLName;
                            Log.d(TAG, "onSuccess: wrestler being added - " + wrestlerName);

                            //Moved to the top and made public
                            newWrestler.put(m_Text, wrestlerName);
                            updateList();

                            Spinner teamLoad = (Spinner) findViewById(R.id.spinner);

                            db.collection("user").document(currentUser.getEmail())
                                    .collection("teams")
                                    .document(teamLoad.getSelectedItem().toString().toLowerCase())
                                    .set(newWrestler, SetOptions.merge());
                            newTeam.clear();
                            getWrestlersFromTeam();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(teamManage.this, "Error finding wrestler", Toast.LENGTH_SHORT).show();
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

    /**
     * Grab players in selected team and add to listview
     */
    public void updateList(){
        for(Map.Entry<String,Object> entry : teamMap.entrySet()){
            String key = entry.getValue().toString();
            newTeam.add(key);
        }
        updateTeam();
    }

    /**
     * Take all members of currently displayed team and add to a tournament.
     */
    public void addTeamToTournament(View view) {
        final Spinner teamLoad = findViewById(R.id.spinner);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogBox);
        builder.setTitle(R.string.submit_team);
        builder.setIcon(R.drawable.wplus);

        // Set up the input
        final Spinner dialog_spinner = new Spinner(this);
        // Specify the type of input expected
        ArrayAdapter<String> data = new ArrayAdapter<>(this,
                R.layout.spinner_cell, coachesTournaments);
        dialog_spinner.getOnItemSelectedListener();
        data.setDropDownViewResource(R.layout.spinner_cell);
//        dialog_spinner.getBackground().setColorFilter(ContextCompat.getColor(
//                getContext(), R.color.Blue), PorterDuff.Mode.SRC_ATOP        ));
        dialog_spinner.setAdapter(data);
        builder.setView(dialog_spinner);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogSpinnerText = dialog_spinner.getSelectedItem().toString();
                final List<String> divisionsAvailable = new ArrayList<>();
                Log.d(TAG, "onClick: adding team to tournament");
                db.collection("tournaments").document(dialogSpinnerText).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        final String tName = task.getResult().getString("name");
                        Log.d(TAG, "onComplete: the name of the tournament adding is : " + tName);

                        db.collection("tournaments").document(dialogSpinnerText)
                                .collection("divisions").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful() & task.getResult() != null) {
                                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                                divisionsAvailable.add(document.getId().toLowerCase());
                                            }

                                            if(divisionsAvailable.contains(teamLoad.getSelectedItem().toString().toLowerCase())) {
                                                Map<String, Object> tournamentName = new HashMap<>();
                                                tournamentName.put("code", dialogSpinnerText);
                                                tournamentName.put("name", tName);
                                                //tournamentName.put("name", db.collection("user"));

                                                for(Map.Entry<String,Object> entry : teamMap.entrySet()){
                                                    Map<String, Object> playerName = new HashMap<>();
                                                    playerName.put("name", entry.getValue().toString());
                                                    playerName.put("division", teamLoad.getSelectedItem().toString().toLowerCase());

                                                    db.collection("tournaments").document(dialogSpinnerText)
                                                            .collection("addedPlayers").document(entry.getKey())
                                                            .set(playerName, SetOptions.merge());
                                                    db.collection("user").document(entry.getKey())
                                                            .collection("tournaments").document(dialogSpinnerText)
                                                            .set(tournamentName);
                                                    Toast.makeText(getApplicationContext(), teamLoad.getSelectedItem().toString()
                                                            + " was added to the " + dialogSpinnerText + " tournament", Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(), teamLoad.getSelectedItem().toString()
                                                        + " cannot participate in " + dialogSpinnerText + " tournament", Toast.LENGTH_LONG).show();
                                            }
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

    /**
     * Function that get the team info from the database and stores it in a map
     */
    public void getWrestlersFromTeam() {
        Spinner teamLoad = (Spinner) findViewById(R.id.spinner);
        DocumentReference docRef = db.collection("user").document(currentUser.getEmail())
                .collection("teams").document(teamLoad.getSelectedItem().toString().toLowerCase());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Moved the team map to be public at the top of the file
                        teamMap = document.getData();

                         // Here we'll iterate through and assign the names to the list view
                        updateList();
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

    /**
     * This Takes away from the list
     * Currently not in use.
     */
    public void subtractWrestler(View w){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Which Player Do You Want To Remove");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.toString();
                //Iterator<String> i = newTeam.iterator();
                int num = 0;
                int loc = 0;

                //newTeam.findItemInTheList(m_Text);
                int total = newTeam.size();
                   for(num =0; num <total; num++)
                   {
                       if(newTeam.contains(m_Text))
                       {
                           System.out.println(m_Text);

                            loc = num;
                       }
                   }
                newTeam.remove(loc);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

        String test = "Add This";
        newTeam.remove(test);
        updateTeam();
    }

     // This code is being reserved for stretch goals where coaches can add custom teams.
    public void addTeams(View t){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Name Your New Team");

        // Set up the input
        final Spinner input = new Spinner(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        ArrayAdapter<String> data = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, DivisionNames.names());
        ArrayAdapter adapter = new ArrayAdapter<>(this,
                R.layout.activity_listview, newTeam);
        input.setOnItemSelectedListener(this);
        input.setAdapter(adapter);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        input.setAdapter(data);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the selected team name to be added
                m_Text = input.toString();
                //Iterator<String> i = newTeam.iterator();
                // Add the team to the total list of team names.
                totalTeam.add(m_Text);
                // Push to database.
                //db.collection("user").document(currentUser.getEmail())
                       // .collection("teams").document(m_Text).set(m_Text);

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

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        showPlayers();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}