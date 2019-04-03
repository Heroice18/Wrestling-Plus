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
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
  Intent send;
  ArrayAdapter<String> myAdapter;
  ListView matchList;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tournament);
    db = FirebaseFirestore.getInstance();
    mAuth = FirebaseAuth.getInstance();
    user = mAuth.getCurrentUser();
    
    setTournamentName();
    setMatchList();
  }
  
  private void setMatchList() {
    send = new Intent(this, MatchActivity.class);
    matchList = findViewById(R.id.matchList);
    final String[] players;
    myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
    db.collection("tournaments").document("fc2019").collection("divisions")
            .document("freshman").collection("test").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
      @Override
      public void onComplete(@NonNull Task<QuerySnapshot> task) {
        Vector<String> players = new Vector<String>();
        for (QueryDocumentSnapshot document : task.getResult()) {
          Log.d(TAG, document.getId() + " => " + document.getData());
          players.add(document.getData().get("name").toString());
          //myAdapter.add(document.getData().get("name").toString());
        }
        for(int i = 0; i < players.size(); i++) {
          String player1 = players.get(i++);
          String player2 = players.get(i);
          myAdapter.add(player1 + " VS " + player2);
        }
        matchList.setAdapter(myAdapter);
      }
    });
    //String[] temp = {"Player 1 vs Player 2", "Player 3 vs Player 4", "Player 5 vs Player 6", "Player 7 vs Player 8", "Player 9 vs Player 10", "Player 11 vs Player 12"};


    

  
    AdapterView.OnItemClickListener listClick = new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // String itemValue = (String) matchList.getItemAtPosition(position);
        
        startActivity(send);
      }
    };
  
    matchList.setOnItemClickListener(listClick);
  }
  
  private void setTournamentName() {
    tournamentName = getIntent().getStringExtra("tournamentName");
    
    final TextView tournamentNameView = findViewById(R.id.tournamentName);
    tournamentNameView.setText(tournamentName);
  }
}