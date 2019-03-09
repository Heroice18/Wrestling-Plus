package com.example.wrestlingtournament;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class teamManage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //This holds the players
   ArrayList<String> newTeam = new ArrayList<String>();
   //this is for the list of team names
   ArrayList<String> totalTeam = new ArrayList<String>();

   private String m_Text = "";




    //Hide the Player information on startup
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_manage);
        Button add = (Button) findViewById(R.id.addPlayer);
        add.setVisibility(VISIBLE);
        Button remove = (Button) findViewById(R.id.subPlayer);
        remove.setVisibility(VISIBLE);
        ListView title = findViewById(R.id.team_list);
        title.setVisibility(VISIBLE);
        totalTeam.add("1");
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, newTeam);
        ArrayAdapter<String> data = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, totalTeam);
        Spinner teamLoad = (Spinner) findViewById(R.id.spinner);
        teamLoad.setOnItemSelectedListener(this);
        teamLoad.setAdapter(adapter);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamLoad.setAdapter(data);

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

        /*totalTeam.add("2");
        totalTeam.add("3");*/
        //This takes care of the dropdown menu



        //ListView list = (ListView) findViewById(R.id.team_list);
        //list.setAdapter(adapt);
    }

    //Add a wrestler to the list view
    public void addWrestler(View w){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View pop = inflater.inflate(R.layout.popup_window, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focus = true;
        final PopupWindow popupWindow = new PopupWindow(pop, width, height, focus);
        popupWindow.showAtLocation(w,Gravity.CENTER, 0,0);
        //EditText check = (EditText) pop.findViewById(R.id.playerDetails);
        pop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                popupWindow.dismiss();
                return true;
            }
        });

        //String test = "Add This";
        //newTeam.add(test);
        updateTeam();
    }

    public void confirmAdd(View c)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Write The Player's Name");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                newTeam.add(m_Text);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();





    }
    //Remove a Wrestler from the ListView
    public void subtractWrestler(View w){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Which Player Do You Want To Remove");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                //Iterator<String> i = newTeam.iterator();
                int num = 0;
                int loc = 0;

                //newTeam.findItemInTheList(m_Text);
                int total = newTeam.size();
                   for(num =0; num <total; num++)
                   {
                       if(newTeam.contains(m_Text))
                       {
                           System.out.println(m_Text);

                            loc = num;
                       }

                   }


                newTeam.remove(loc);


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();


        String test = "Add This";
        newTeam.remove(test);
        updateTeam();
    }

    public void addTeams(View t){
        totalTeam.add("1");
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


}
//Junk Code

       /* LayoutInflater inflated = (LayoutInflater)
                this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        PopupWindow window = new PopupWindow(inflated.inflate(R.layout.popup_window, null, false),
                100,100,true);
        window.showAtLocation(this.findViewById(R.id.addPlayer),
                Gravity.CENTER,0,0);*/
       /* public ArrayList<String> TeamA = new ArrayList<>();
    String[] tester = new String[]{};
    List<String> teams = new ArrayList<String>(Arrays.asList(tester));
    ArrayAdapter<String> adapt = new ArrayAdapter<String>(this,
            R.layout.activity_team_manage, teams);
    ListView list = (ListView) findViewById(R.id.team_list);*/


       /*  AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();*/


        /*LayoutInflater inflater = (LayoutInflater) teamManage.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_window,
                (ViewGroup) teamManage.this.findViewById(R.id.popup_element));
        Button confirm = (Button)findViewById(R.id.addConfirm);
        LayoutInflater factory = getLayoutInflater();
        View regis = factory.inflate(R.layout.popup_window, null);
        EditText user = (EditText) regis.findViewById(R.id.playerDetails);
        String total = user.getText().toString();
        System.out.println(total);
        newTeam.add(total);
//        EditText player = (EditText)findViewById(R.id.playerDetails);

        //System.out.println( " *value is: " + player);
        //String content = player.getText().toString();
        //System.out.println(content);
        //String name = player.getText().toString();
        //System.out.println(name);
        //newTeam.add(name);*/
