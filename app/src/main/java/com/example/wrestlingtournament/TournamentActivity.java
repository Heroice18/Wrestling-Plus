package com.example.wrestlingtournament;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Displays the tournament information to the user.
 * When created, it populates the listView and textView with the tournament info from Firebase.
 *
 * Admins, coaches, and wrestlers will see the name of the tournament, the list of matches for
 * each round of the tournament, and pick which level and weight class brackets they want to view.
 */
public class TournamentActivity extends AppCompatActivity {
  FirebaseFirestore db;
  public static final String TAG = "TournamentActivity";
  FirebaseUser user;
  FirebaseAuth mAuth;
  String tournamentName;
  String tournamentCode;
  String tournamentRef;

  Spinner divisionSpinner;
  Spinner weighClassSpinner;

  String division;
  String weightClass;
  int round;

  ArrayAdapter<String> myAdapter;
  ListView matchList;
  public Map<String, String> playerMap = new HashMap<>();
  ImageButton nextRoundBtn;
  ImageButton lastRoundBtn;

  //these are so we don't call spinner listeners when we first create the page
  boolean isDivisionCreated = false;
  boolean isWeightClassCreated = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tournament);
    db = FirebaseFirestore.getInstance();
    mAuth = FirebaseAuth.getInstance();
    user = mAuth.getCurrentUser();
    tournamentCode = getIntent().getStringExtra("tournamentCode");

    //setLevels();


    divisionSpinner = (Spinner) findViewById(R.id.level);
    weighClassSpinner = (Spinner) findViewById(R.id.weightClass);
    division = divisionSpinner.getSelectedItem().toString();
    weightClass = weighClassSpinner.getSelectedItem().toString();

    //we need to create a listener to update the list when a new division is selected
    divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (isDivisionCreated) {
                division = divisionSpinner.getItemAtPosition(i).toString();
                Log.d(TAG, "onItemSelected: new division selected is: " + division);
                tournamentRef = "/tournaments/" + tournamentCode + "/divisions/" + division + "/brackets/" + weightClass + "/wrestlers";
                Log.d(TAG, "onItemSelected: about to update the tournament list");
                setMatchList();
            }
            else {
                isDivisionCreated = true;
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    });
    weighClassSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (isWeightClassCreated) {
                weightClass = weighClassSpinner.getItemAtPosition(i).toString();
                tournamentRef = "/tournaments/" + tournamentCode + "/divisions/" + division + "/brackets/" + weightClass + "/wrestlers";
                Log.d(TAG, "onItemSelected: the weight was changed. Updating the list view");
                Log.d(TAG, "onItemSelected: new weigth is " + weightClass);
                setMatchList();
            }
            else {
                isWeightClassCreated = true;
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    });

    tournamentRef = "/tournaments/" + tournamentCode + "/divisions/" + division + "/brackets/" + weightClass + "/wrestlers";
    setTournamentName();

    round = 1;
    setMatchList();
    nextRoundBtn = (ImageButton) findViewById(R.id.nextRoundButton);
    lastRoundBtn = (ImageButton) findViewById(R.id.lastRoundButton);
    lastRoundBtn.setEnabled(false);
  }

  /*
  private void setLevels() {
     String dbRef = "/tournaments/" + tournamentCode + "/divisions";
      Log.d(TAG, "setLevels: filling in level list with path: " + dbRef);
     db.collection(dbRef).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
         @Override
         public void onComplete(@NonNull Task<QuerySnapshot> task) {
             if (task.isSuccessful()) {
                 ArrayList<String> levels = new ArrayList<String>();
                 for (QueryDocumentSnapshot document : task.getResult()) {
                     levels.add(document.getId());
                     Log.d(TAG, document.getId() + " => " + document.getData());
                 }
                 ArrayAdapter<String> adapter = new ArrayAdapter<String>(TournamentActivity.this, android.R.layout.simple_spinner_item, levels);
                 divisionSpinner.setAdapter(adapter);
             }
         }
     });
  }*/

  private void setMatchList() {

      matchList = findViewById(R.id.matchList);
      final String[] players;
      myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

      Log.d(TAG, "setMatchList: match list being set with path to : " + tournamentRef);
      db.collection(tournamentRef).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
          @Override
          public void onComplete(@NonNull Task<QuerySnapshot> task) {
            Vector<String> players = new Vector<String>();
            for (QueryDocumentSnapshot document : task.getResult()) {
              Log.d(TAG, document.getId() + " => " + document.getData());
              //check if the player has reached the given round
                //int playerWins = document.getData().get("winCount");
             // if ((int)document.getData().get("winCount") >= (round - 1)) {
                  players.add(document.getData().get("name").toString());
                  playerMap.put(document.getData().get("email").toString(), document.getData().get("name").toString());
            }
            for(int i = 0; i < players.size(); i++) {
              String player1 = players.get(i++);
              String player2 = players.get(i);
              myAdapter.add(player1 + " VS " + player2);
            }
            matchList.setAdapter(myAdapter);
          }
        });


  
    AdapterView.OnItemClickListener listClick = new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

          //get the string from the array list
          String match = matchList.getItemAtPosition(position).toString();
        Log.d(TAG, "onItemClick: match is: " + match);

        //find where in the match string is vs so we can break the string into two separate names
        int findVs = match.indexOf(" VS ");
        Log.d(TAG, "onItemClick: vs was found at indx: " + findVs);

        //get the players names from the match string
        String player1 = match.substring(0, findVs);
        String player2 = match.substring(findVs + 4);

        Log.d(TAG, "onItemClick: player 1 is: " + player1);
        Log.d(TAG, "onItemClick: player 2 is: " + player2);

        Intent intent = new Intent(TournamentActivity.this, MatchActivity.class);
        intent.putExtra("tournamentPath", tournamentRef);
        for(HashMap.Entry<String, String>entry : playerMap.entrySet()){
              String playerName = entry.getValue();
              //pass the two players information on to the match activity
              if(playerName.equals(player1)) {

                  intent.putExtra("player1name", entry.getValue());
                  intent.putExtra("player1email", entry.getKey());
              }
            if(playerName.equals(player2)) {
                intent.putExtra("player2name", entry.getValue());
                intent.putExtra("player2email", entry.getKey());
            }
          }

        startActivity(intent);
      }
    };
  
    matchList.setOnItemClickListener(listClick);
  }

  private void setTournamentName() {
    tournamentName = getIntent().getStringExtra("tournamentName");
    
    final TextView tournamentNameView = findViewById(R.id.tournamentName);
    tournamentNameView.setText(tournamentName);
  }

  public void nextRound(View view) {
      round++;
      //after a next round we will always be on atleast round two, so the previous button should be enabled
      lastRoundBtn.setEnabled(true);
      Log.d(TAG, "nextRound: current round being viewed - " + round);
  }

  public void previousRound(View view) {
      round--;
      if (round <= 1) {
          //if we are on the first round, we cant go back any further
          lastRoundBtn.setEnabled(false);
      }
      Log.d(TAG, "nextRound: current round being viewed - " + round);

  }
}