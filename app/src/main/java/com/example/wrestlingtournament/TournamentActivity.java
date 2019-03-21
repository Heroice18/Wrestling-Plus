package com.example.wrestlingtournament;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tournament);
    db = FirebaseFirestore.getInstance();
    
    final TextView tournamentName = findViewById(R.id.tournamentName);
    db.collection("tournaments").document("test").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
      @Override
      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
          DocumentSnapshot document = task.getResult();
          Log.d(TAG, String.valueOf(document.getData()));
          tournamentName.setText((CharSequence) document.get("name"));
        }
      }
    });
  }
  
  public void viewMatch(View m) {
    Intent send = new Intent(this, MatchActivity.class);
    
    startActivity(send);
  }
  
  
  
  
}