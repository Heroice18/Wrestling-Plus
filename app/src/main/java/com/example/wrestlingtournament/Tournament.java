package com.example.wrestlingtournament;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Tournament extends AppCompatActivity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tournament);
  }
  
  public void viewMatch(View m) {
    Intent send = new Intent(this, MatchActivity.class);
    
    startActivity(send);
  }
}