package com.example.wrestlingtournament;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

import static android.view.View.VISIBLE;

public class manageTournaments extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ArrayList<String> newTeam = new ArrayList<String>();
    //this is for the list of team names
    ArrayList<String> totalTeam = new ArrayList<String>();
    public String m_Text;
    private View.OnClickListener tap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_tournaments);

        totalTeam.add("Default Tournament");
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, newTeam);
        ArrayAdapter<String> data = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, totalTeam);
        Spinner teamLoad = (Spinner) findViewById(R.id.TournamentSpin);
        newTeam.add("Default Player");
        teamLoad.setOnItemSelectedListener(this);
        teamLoad.setAdapter(adapter);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamLoad.setAdapter(data);
        updateTeam();
        /*db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();*/




    }



    //This updates the listview as the players are added or subtracted
    //Also this is where I have the team select for now
    public void updateTeam(){

       ListView list = (ListView) findViewById(R.id.PlayerList);
       ArrayAdapter<String> adept = new ArrayAdapter<String>(this,
               R.layout.activity_listview, newTeam);
       list.setAdapter(adept);
       list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Context check = getApplicationContext();

               AlertDialog.Builder builder = new AlertDialog.Builder(manageTournaments.this);
               builder.setTitle("Submit wrestler's weight:");

// Set up the input
               final EditText input = new EditText(manageTournaments.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
               input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
               builder.setView(input);

// Set up the buttons
               builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       //email input
                       m_Text = input.getText().toString();

                      /* db.collection("user").document(m_Text)
                               .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                           @Override
                           public void onSuccess(DocumentSnapshot documentSnapshot) {
                               if (!documentSnapshot.exists()) {
                                   Toast.makeText(teamManage.this, "Could not find wrestler", Toast.LENGTH_SHORT).show();
                               } else {
                                   String newFName = (String) documentSnapshot.get("firstName");
                                   String newLName = (String) documentSnapshot.get("lastName");
                                   String wrestlerName = newFName + " " + newLName;
                                   Log.d(TAG, "onSuccess: wrestler being added - " + wrestlerName);

                                   //Moved to the top and made public
                                   newWrestler.put(m_Text, wrestlerName);
                                   updateList();

                                   db.collection("user").document(currentUser.getEmail())
                                           .collection("teams").document("default").set(newWrestler);
                               }
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Toast.makeText(teamManage.this, "Error finding wrestler", Toast.LENGTH_SHORT).show();
                           }
                       });*/
                       //adds the input to the list
                       //newTeam.add(m_Text);
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
       });







        //This part fills the table with the players data
       /* ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, newTeam);


        ListView listView = (ListView) findViewById(R.id.PlayerList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context check = getApplicationContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(check);
                builder.setTitle("Submit wrestler's weight:");

// Set up the input
                final EditText input = new EditText(check);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //email input
                        m_Text = input.getText().toString();


                            }

                        //adds the input to the list
                        //newTeam.add(m_Text);
                    });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });
        listView.setAdapter(adapter);*/

    }






    /*
    The following functions are for the team select dropdown bar
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        //showPlayers();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




}
