package com.example.wrestlingtournament;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
//Hey Lets DO THIS THING
//Howdy^ /-|-\

public class Login_Start extends AppCompatActivity{

    public static final String location = "com.example.wrestlingtournament.USER";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        EditText first = findViewById(R.id.fName);
        first.setVisibility(GONE);
        EditText last = findViewById(R.id.lName);
        last.setVisibility(GONE);
        Button man = (Button) findViewById(R.id.Finish);
        man.setVisibility(GONE);
        CheckBox deus = findViewById(R.id.checkAdmin);
        deus.setVisibility(GONE);
        CheckBox side = findViewById(R.id.checkCoach);
        side.setVisibility(GONE);
        CheckBox jog = findViewById(R.id.checkPlayer);
        jog.setVisibility(GONE);
        Button back = (Button) findViewById(R.id.goBack);
        back.setVisibility(GONE);
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

        String check = "Wrestler";
        send.putExtra("USER", check);
        startActivity(send);
    }
    UserObject test = new UserObject();
    public void Create (View c)  {
        EditText first = findViewById(R.id.fName);
        first.setVisibility(VISIBLE);
        EditText last = findViewById(R.id.lName);
        last.setVisibility(VISIBLE);
        EditText email = findViewById(R.id.Email);
        email.setVisibility(VISIBLE);
        Button man = (Button) findViewById(R.id.Finish);
        man.setVisibility(VISIBLE);
        CheckBox deus = findViewById(R.id.checkAdmin);
        deus.setVisibility(VISIBLE);
        CheckBox side = findViewById(R.id.checkCoach);
        side.setVisibility(VISIBLE);
        CheckBox jog = findViewById(R.id.checkPlayer);
        jog.setVisibility(VISIBLE);
        Button log = (Button) findViewById(R.id.login);
        log.setVisibility(GONE);
        Button in = (Button) findViewById(R.id.login);
        in.setVisibility(GONE);
        Button ate = (Button) findViewById(R.id.Create);
        ate.setVisibility(GONE);
        Button login = (Button) findViewById(R.id.logButton);
        login.setVisibility(GONE);
        Button back = (Button) findViewById(R.id.goBack);
        back.setVisibility(VISIBLE);



    }

    public void popUp(View p){

    }
    public void returnBack(View B)
    {
        EditText first = findViewById(R.id.fName);
        first.setVisibility(GONE);
        EditText last = findViewById(R.id.lName);
        last.setVisibility(GONE);
        Button man = (Button) findViewById(R.id.Finish);
        man.setVisibility(GONE);
        CheckBox deus = findViewById(R.id.checkAdmin);
        deus.setVisibility(GONE);
        CheckBox side = findViewById(R.id.checkCoach);
        side.setVisibility(GONE);
        CheckBox jog = findViewById(R.id.checkPlayer);
        jog.setVisibility(GONE);
        Button back = (Button) findViewById(R.id.goBack);
        back.setVisibility(GONE);

        Button log = (Button) findViewById(R.id.login);
        log.setVisibility(VISIBLE);
        Button in = (Button) findViewById(R.id.login);
        in.setVisibility(VISIBLE);
        Button ate = (Button) findViewById(R.id.Create);
        ate.setVisibility(VISIBLE);
        Button login = (Button) findViewById(R.id.logButton);
        login.setVisibility(VISIBLE);
        //Button back = (Button) findViewById(R.id.goBack);

    }

    /* Junk and Test Data
    //popUp();
        //test.login();
        /*LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(c, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });*/









}
