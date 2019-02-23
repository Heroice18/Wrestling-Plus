package com.example.wrestlingtournament;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
//Hey Lets DO THIS THING
//Howdy^ /-|-\

public class Login_Start extends AppCompatActivity{

    public static final String location = "com.example.wrestlingtournament.USER";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }
    //String check;
    //Intent send = new Intent(this, MainActivity.class);

    public void logButton(View b)
    {
        Toast q = Toast.makeText(this, " Not Active", Toast.LENGTH_LONG);
        q.show();
    }

    public void adminLog(View a)
    {
        Intent send = new Intent(this, MainActivity.class);
        String check = "Admin";
        send.putExtra("USER", check);
        startActivity(send);
    }

    public void coachLog(View c)
    {
        Intent send = new Intent(this, MainActivity.class);

        String check = "Coach";
        send.putExtra("USER", check);
        startActivity(send);
    }

    public void playerLog(View p)
    {
        Intent send = new Intent(this, MainActivity.class);

        String check = "Player";
        send.putExtra("USER", check);
        startActivity(send);
    }






}
