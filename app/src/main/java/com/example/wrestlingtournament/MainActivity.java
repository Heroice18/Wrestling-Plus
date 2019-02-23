package com.example.wrestlingtournament;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static com.example.wrestlingtournament.Login_Start.location;

//Hey Lets DO THIS THING
//Howdy^ /-|-\
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUp();
    }

    public void setUp()
    {
        Bundle box = getIntent().getExtras();

            String check = box.getString("USER");

       // Intent soda = new Intent();
        //String check = soda.getStringExtra(location);
        TextView display = findViewById(R.id.textView4);
        System.out.println("      HEY " + check);
        display.setText(check);
    }

}
