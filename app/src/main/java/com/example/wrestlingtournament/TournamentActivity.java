package com.example.wrestlingtournament;

import android.app.Notification;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static com.example.wrestlingtournament.App.CHANNEL_1_ID;

/**
 * Displays the tournament information to the user.
 * When created, it populates the listView and textView with the tournament info from Firebase.
 *
 * Admins, coaches, and wrestlers will see the name of the tournament, the list of matches for
 * each round of the tournament, and pick which level and weight class brackets they want to view.
 */
public class TournamentActivity extends AppCompatActivity {
  FirebaseFirestore db;
    private NotificationManagerCompat notificationManager;
  public static final String TAG = "TournamentActivity";
    FirebaseUser currentUser;
  FirebaseUser user;
  FirebaseAuth mAuth;
  String tournamentName;
  String tournamentCode;
  String tournamentRef;
  //players submited to tournament
  int initialPlayerCount;
  //players are copied to end of list when they win. This is how many are now in the list
  int currentPlayerCount;
  //how many players are in the first round. It is equal to inital player count, unless
    // its odd, then ine initial player count - 1
  int firstRoundCount;
  //these counters tell us which players we need to loop through in each round
  int minCount;
  int maxCount;
  int roundSize;
  //handles the next round if a person is passed on to the next round because of odd numbers
  boolean extraOdd;

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
  TextView roundNumTextView;

  //these are so we don't call spinner listeners when we first create the page
  boolean isDivisionCreated = false;
  boolean isWeightClassCreated = false;
  Vector<String> players;


    @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tournament);
    db = FirebaseFirestore.getInstance();
    mAuth = FirebaseAuth.getInstance();
    user = mAuth.getCurrentUser();
    tournamentCode = getIntent().getStringExtra("tournamentCode");
    round = 1;
    extraOdd = false;
    roundNumTextView = findViewById(R.id.roundNumView);
    //setLevels();

    players = new Vector<>();
    divisionSpinner = findViewById(R.id.level);
    weighClassSpinner = findViewById(R.id.weightClass);
    division = divisionSpinner.getSelectedItem().toString();
    weightClass = weighClassSpinner.getSelectedItem().toString();

    //we need to create a listener to update the list when a new division is selected
    divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (isDivisionCreated) {
                division = divisionSpinner.getItemAtPosition(i).toString();
                Log.d(TAG, "onItemSelected: new division selected is: " + division);
                tournamentRef = "/tournaments/" + tournamentCode + "/divisions/" + division + "/brackets/" + weightClass;
                Log.d(TAG, "onItemSelected: about to update the tournament list");
                initializeMatch();
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
                tournamentRef = "/tournaments/" + tournamentCode + "/divisions/" + division + "/brackets/" + weightClass;
                Log.d(TAG, "onItemSelected: the weight was changed. Updating the list view");
                Log.d(TAG, "onItemSelected: new weigth is " + weightClass);
                initializeMatch();
            }
            else {
                isWeightClassCreated = true;
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    });

    tournamentRef = "/tournaments/" + tournamentCode + "/divisions/" + division + "/brackets/" + weightClass;
    setTournamentName();

    round = 1;
    nextRoundBtn = findViewById(R.id.nextRoundButton);
    lastRoundBtn = findViewById(R.id.lastRoundButton);
    lastRoundBtn.setEnabled(false);

      notificationManager = NotificationManagerCompat.from(this);


  }

    @Override
    protected void onStart() {
        super.onStart();
        initializeMatch();



        Log.i(TAG, "On Start .....");
        if (initialPlayerCount % 2 == 0){
            extraOdd = false;
            maxCount = initialPlayerCount;
        }
        else {
            extraOdd = true;
            maxCount = initialPlayerCount - 1;
        }
        roundSize = maxCount - minCount;
        lastRoundBtn.setEnabled(false);
    }

    public void sendOnChannel1(View v) {

        String title = "Hey";
        String message = "Message";

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }

  private void initializeMatch() {
        round = 1;
        db.document(tournamentRef).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    initialPlayerCount = task.getResult().getLong("initialWrestlerCount").intValue();
                    if (initialPlayerCount % 2 == 0) {
                        firstRoundCount = initialPlayerCount;
                    } else {
                        firstRoundCount = initialPlayerCount - 1;
                        extraOdd = true;
                    }
                    Log.d(TAG, "onComplete: initialplayercount for bracket - " + initialPlayerCount);
                    currentPlayerCount = task.getResult().getLong("currentWrestlerCount").intValue();
                    Log.d(TAG, "onComplete: current player count for this bracket - " + currentPlayerCount);

                    minCount = 0;
                    if (initialPlayerCount % 2 == 0) {
                        maxCount = initialPlayerCount;
                    }
                    else {
                        maxCount = initialPlayerCount - 1;
                    }
                    roundSize = maxCount - minCount;

                    db.document(tournamentRef).collection("wrestlers").orderBy("id").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "adding player to player vector: " + document.getData().get("name").toString());
                                players.add(document.getData().get("name").toString());
                                playerMap.put(document.getData().get("email").toString(), document.getData().get("name").toString());
                            }
                            setMatchList();
                        }
                    });
                }
                else {
                    Log.d(TAG, "onComplete: could not find bracket");
                    initialPlayerCount = 0;
                    currentPlayerCount = 0;
                    minCount = 0;
                    maxCount = 0;
                    setMatchList();
                }
            }
        });
  }

  private void setMatchList() {
        String currentRound = "Round: " + round;
      Log.d(TAG, "setMatchList: current round string = " + currentRound );
        roundNumTextView.setText(currentRound);
      matchList = findViewById(R.id.matchList);
      myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

      for(int i = minCount; i < maxCount; i++) {
          String player1 = players.get(i++);
          String player2 = players.get(i);
          myAdapter.add(player1 + " VS " + player2);
      }
      matchList.setAdapter(myAdapter);

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
      minCount = maxCount;
      maxCount = minCount + (roundSize / 2);
      Log.d(TAG, "nextRound: maxcount - " + maxCount);
      Log.d(TAG, "nextRound: new minimum counter: " + minCount);
      Log.d(TAG, "nextRound: new maximum counter " + maxCount);
      if (extraOdd) {
          maxCount++;
          Log.d(TAG, "nextRound: there was a odd to be added. maxcount is now - " + maxCount);
          extraOdd = false;
      }
      if (maxCount >= currentPlayerCount) {
          maxCount = currentPlayerCount;
          Log.d(TAG, "nextRound: max count was too big. It is now - " + maxCount);
          nextRoundBtn.setEnabled(false);
      }
      if (maxCount % 2 != 0) {
          maxCount--;
          Log.d(TAG, "nextRound: maxcount was odd. It is now - " + maxCount);
          extraOdd = true;
      }
      roundSize = maxCount - minCount;
      setMatchList();
  }

  public void previousRound(View view) {
      /*round--;
      if (round <= 1) {
          //if we are on the first round, we cant go back any further
          lastRoundBtn.setEnabled(false);
      }*/
      //for now the back button just puts us back to the first round
      round = 1;
      Log.d(TAG, "nextRound: current round being viewed - " + round);
      minCount = 0;
      if (initialPlayerCount % 2 == 0){
          extraOdd = false;
          maxCount = initialPlayerCount;
      }
      else {
          extraOdd = true;
          maxCount = initialPlayerCount - 1;
      }
      roundSize = maxCount - minCount;
      lastRoundBtn.setEnabled(false);
      nextRoundBtn.setEnabled(true);
      setMatchList();
  }
}