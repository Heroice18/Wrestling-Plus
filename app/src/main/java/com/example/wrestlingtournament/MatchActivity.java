package com.example.wrestlingtournament;

import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import static com.example.wrestlingtournament.App.CHANNEL_1_ID;

import java.util.HashMap;
import java.util.Map;

public class MatchActivity extends AppCompatActivity {
    public static final String TAG = "MatchActivity";
    FirebaseFirestore db;
    private NotificationManagerCompat notificationManager;
    String player1name;
    String player1email;
    String player2name;
    String player2email;
    TextView player1Text;
    TextView player2Text;
    TextView player1Score;
    TextView player2Score;
    String tournamentRef;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_match);

      player1name = getIntent().getStringExtra("player1name");
      player1email = getIntent().getStringExtra("player1email");
      player2name = getIntent().getStringExtra("player2name");
      player2email = getIntent().getStringExtra("player2email");
      tournamentRef = getIntent().getStringExtra("tournamentPath");
      Log.d(TAG, "onCreate: tournament path is: " + tournamentRef);
      Log.d(TAG, "onCreate: player 1 name and email is: " + player1name + " - " + player1email);
      Log.d(TAG, "onCreate: player 1 name and email is: " + player2name + " - " + player2email);
      player1Text = findViewById(R.id.player1Name);
      player2Text = findViewById(R.id.player2Name);
      player1Text.setText(player1name);
      player2Text.setText(player2name);

      player1Score = findViewById(R.id.score1);
      player2Score = findViewById(R.id.score2);

      db = FirebaseFirestore.getInstance();
      notificationManager = NotificationManagerCompat.from(this);
  }


    public void sendOnChannel3(View v) {
        Map<String, Object> data = new HashMap<>();
        data.put("ready", true);
        Log.d(TAG, "sendOnChannel3: sucess  " +  db.collection("user").document(player1email).set(data, SetOptions.merge()));
        db.collection("user").document(player1email).set(data, SetOptions.merge());

        db.collection("user").document(player2email).set(data, SetOptions.merge());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Log.d(TAG, "sendOnChannel3: ");
            e.printStackTrace();
        }
        data.clear();
      data.put("ready", false);
      db.collection("user").document(player1email).set(data, SetOptions.merge());
        db.collection("user").document(player2email).set(data, SetOptions.merge());



    }







  public void submitScore(View view) {

      //make sure scores have been entered
      if(player1Score.getText().toString().isEmpty() || player2Score.getText().toString().isEmpty()) {
          Toast.makeText(this, "Please enter valid scores before submitting results", Toast.LENGTH_LONG).show();
          return;
      }

      int score1 = Integer.parseInt(player1Score.getText().toString());
      int score2 = Integer.parseInt(player2Score.getText().toString());

      Log.d(TAG, "submitScore: score 1 - score 2: " + score1 + " - " + score2);
      if (score1 > score2) {
          Log.d(TAG, "submitScore: wrestler1 wins");
          AlertDialog.Builder builder = new AlertDialog.Builder(MatchActivity.this);
          builder.setTitle("Confirm Winner");

          String confirmMessage = player1name + " is the winner with a score of " + score1 + " - " + score2
                  + ". Once you confirm the score, you cannot change it. Press confirm to finalize and submit score";
          builder.setMessage(confirmMessage);
          builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                  // User clicked OK button
                  db.document(tournamentRef).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                      @Override
                      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                          int currentCount = task.getResult().getLong("currentWrestlerCount").intValue();
                          int newCount = currentCount + 1;
                          Log.d(TAG, "onComplete: current wrester count - " + currentCount);

                          //update the winner to be passed on to the next round
                          Map<String, Object> winner = new HashMap<>();
                          winner.put("name", player1name);
                          winner.put("email", player1email);
                          db.document(tournamentRef).collection("wrestlers").document(String.valueOf(newCount)).set(winner);
                          db.document(tournamentRef).update("currentWrestlerCount", newCount);
                          Toast.makeText(MatchActivity.this, "Congratulations! " + player1name + " is the winner!", Toast.LENGTH_LONG).show();
                          MatchActivity.this.finish();
                      }
                  });
              }
          });
          builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                  dialog.cancel();
              }
          });
          builder.show();
      }
      else if (score2 > score1) {
          Log.d(TAG, "submitScore: wrestler2 wins");
          AlertDialog.Builder builder = new AlertDialog.Builder(MatchActivity.this);
          builder.setTitle("Confirm Winner");

          String confirmMessage = player2name + " is the winner with a score of " + score2 + " - " + score1
                  + ". Once you confirm the score, you cannot change it. Press confirm to finalize and submit score";
          builder.setMessage(confirmMessage);
          builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                  // User clicked OK button
                  db.document(tournamentRef).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                      @Override
                      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                          int currentCount = task.getResult().getLong("currentWrestlerCount").intValue();
                          int newCount = currentCount + 1;
                          Log.d(TAG, "onComplete: current wrester count - " + currentCount);

                          //update the winner to be passed on to the next round
                          Map<String, Object> winner = new HashMap<>();
                          winner.put("name", player2name);
                          winner.put("email", player2email);
                          db.document(tournamentRef).collection("wrestlers").document(String.valueOf(newCount)).set(winner);
                          db.document(tournamentRef).update("currentWrestlerCount", newCount);
                          Toast.makeText(MatchActivity.this, "Congratulations! " + player1name + " is the winner!", Toast.LENGTH_LONG).show();
                          MatchActivity.this.finish();
                      }
                  });
              }
          });
          builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                  dialog.cancel();
              }
          });
          builder.show();
      }
      else {
          AlertDialog.Builder builder = new AlertDialog.Builder(MatchActivity.this);
          builder.setTitle("Tied Match");
          builder.setMessage("Tied games cannot be submitted.");
          builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                  dialog.cancel();
              }
          });
          builder.show();
      }
  }
}
