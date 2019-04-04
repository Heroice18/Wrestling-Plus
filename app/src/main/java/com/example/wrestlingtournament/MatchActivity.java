package com.example.wrestlingtournament;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MatchActivity extends AppCompatActivity {
    public static final String TAG = "MatchActivity";
    String player1name;
    String player1email;
    String player2name;
    String player2email;
    TextView player1Text;
    TextView player2Text;
    TextView player1Score;
    TextView player2Score;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_match);

      player1name = getIntent().getStringExtra("player1name");
      player1email = getIntent().getStringExtra("player1email");
      player2name = getIntent().getStringExtra("player2name");
      player2email = getIntent().getStringExtra("player2email");
      Log.d(TAG, "onCreate: player 1 name and email is: " + player1name + " - " + player1email);
      Log.d(TAG, "onCreate: player 1 name and email is: " + player2name + " - " + player2email);
      player1Text = findViewById(R.id.player1Name);
      player2Text = findViewById(R.id.player2Name);
      player1Text.setText(player1name);
      player2Text.setText(player2name);

      player1Score = findViewById(R.id.score1);
      player2Score = findViewById(R.id.score2);
  }

  public void submitScore(View view) {

      int score1 = Integer.parseInt(player1Score.getText().toString());
      int score2 = Integer.parseInt(player2Score.getText().toString());

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
