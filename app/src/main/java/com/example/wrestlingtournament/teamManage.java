package com.example.wrestlingtournament;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class teamManage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //This holds the players
   ArrayList<String> newTeam = new ArrayList<String>();
   //this is for the list of team names
   ArrayList<String> totalTeam = new ArrayList<String>();




    //Hide the Player information on startup
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_manage);
        Button add = (Button) findViewById(R.id.addPlayer);
        add.setVisibility(GONE);
        Button remove = (Button) findViewById(R.id.subPlayer);
        remove.setVisibility(GONE);
        ListView title = findViewById(R.id.team_list);
        title.setVisibility(GONE);





        updateTeam();

    }

    //Shows the player options after a team is selected
    public void showPlayers(){
        Button add = (Button) findViewById(R.id.addPlayer);
        add.setVisibility(VISIBLE);
        Button remove = (Button) findViewById(R.id.subPlayer);
        remove.setVisibility(VISIBLE);

        ListView title = findViewById(R.id.team_list);
        title.setVisibility(VISIBLE);


    }
    //This updates the listview as the players are added or subtracted
    //Also this is where I have the team select for now
    public void updateTeam(){
       //This part fills the table with the players data
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, newTeam);

        ListView listView = (ListView) findViewById(R.id.team_list);
        listView.setAdapter(adapter);
        //Test Values
        totalTeam.add("1");
        totalTeam.add("2");
        totalTeam.add("3");
        //This takes care of the dropdown menu
        ArrayAdapter<String> data = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, totalTeam);
        Spinner teamLoad = (Spinner) findViewById(R.id.spinner);
        teamLoad.setOnItemSelectedListener(this);
        teamLoad.setAdapter(adapter);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamLoad.setAdapter(data);


        //ListView list = (ListView) findViewById(R.id.team_list);
        //list.setAdapter(adapt);
    }

    //Add a wrestler to the list view
    public void addWrestler(View w){
        String test = "Add This";
        newTeam.add(test);
        updateTeam();
    }
    //Remove a Wrestler from the ListView
    public void subtractWrestler(View w){
        String test = "Add This";
        newTeam.remove(test);
        updateTeam();
    }

    public void addTeams(View t){

    }

    /*
    The following functions are for the team select dropdown bar
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        showPlayers();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /* public ArrayList<String> TeamA = new ArrayList<>();
    String[] tester = new String[]{};
    List<String> teams = new ArrayList<String>(Arrays.asList(tester));
    ArrayAdapter<String> adapt = new ArrayAdapter<String>(this,
            R.layout.activity_team_manage, teams);
    ListView list = (ListView) findViewById(R.id.team_list);*/
}
