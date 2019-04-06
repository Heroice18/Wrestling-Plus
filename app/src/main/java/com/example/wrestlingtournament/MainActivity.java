package com.example.wrestlingtournament;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.wrestlingtournament.Login_Start.location;

//Hey Lets DO THIS THING
//Howdy^ /-|-\
public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    ListView tournamentList;
    Intent send;
    ArrayList<String> totalTournaments = new ArrayList<String>();
    public Map<String, Object> TournamentStore = new HashMap<>();
    ArrayAdapter<String> myAdapter;
    public static final String TAG = "MainActivity";
    public String emailName;
    public TextView display;
   // private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        
        setUp();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        setTournamentList();
    }
    
    /**
     * Fills the ListView with all of the tournaments under the current admin's control
     */
    public void setTournamentList() {
        send = new Intent(this, TournamentActivity.class);
        tournamentList = findViewById(R.id.tournamentList);
        totalTournaments = null;
        totalTournaments = new ArrayList<String>();
    
        db.collection("user").document(currentUser.getEmail()).collection("tournaments")
            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    Log.d(TAG, "Task was successful. Now to populating the array and map");
                
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, "Current document: " + document);
                    
                        TournamentStore = document.getData();
                    
                        Log.d(TAG, "Storage: " + TournamentStore);
                    
                        for(Map.Entry<String,Object> entry : TournamentStore.entrySet()){
                            String key = entry.getKey();
                            Log.d(TAG, "Current key: " + key);
                        
                            if(key.equals("name")) {
                                String value = entry.getValue().toString();
                                Log.d(TAG, "Value: " + value);
                                Log.d(TAG, "Entering if name");
                                totalTournaments.add(value);
                                Log.d(TAG, "Check totalTournaments: " + totalTournaments);
                            }
                        }
                    }
                    tournamentList.setAdapter(myAdapter);
                }
            }
        });
    
        myAdapter = new ArrayAdapter<>(this, R.layout.activity_listview , totalTournaments);
        
        AdapterView.OnItemClickListener listClick = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String) tournamentList.getItemAtPosition(position);
            
                send.putExtra("tournamentName", itemValue);
                startActivity(send);
            }
        };
    
        tournamentList.setOnItemClickListener(listClick);
    }

    public void setUp() {
        Bundle box = getIntent().getExtras();

        String check = box.getString("USER");

        // Intent soda = new Intent();
        //String check = soda.getStringExtra(location);
        display = findViewById(R.id.textView5);


        db.collection("user").document(currentUser.getEmail()).
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    Log.d(TAG, "onComplete: document " + document);
                    emailName = document.getString("firstName");
                    emailName = emailName + "'s Account";
                    display.setText(emailName);


                    }
                    Log.d(TAG, "onComplete: of this: " + emailName);
                }
            });
//        });

        switch (check){
            case "Admin":
                displayAdmin();
                break;
            case "Coach":
                displayCoach();
                break;
            case "Wrestler":
                displayWrestler();
                break;

            default:
                displayWrestler();
        }

        System.out.println("      HEY " + check);
        Log.d(TAG, "setUp: name " + emailName);

    }

    public void displayAdmin()
    {
        System.out.println("Working fine and dandy");

        Button see = (Button) findViewById(R.id.ViewAll);
        see.setVisibility(GONE);
        Button team = (Button) findViewById(R.id.TeamM);
        team.setVisibility(GONE);
        TextView title = findViewById(R.id.textView2);
        title.setVisibility(GONE);
        TextView tournamentC = findViewById(R.id.textViewCoach);
        tournamentC.setVisibility(GONE);
    }

    public void displayCoach()
    {
        Button man = (Button) findViewById(R.id.ManageT);
        man.setVisibility(GONE);
        Button god = (Button) findViewById(R.id.CreateT);
        god.setVisibility(GONE);
        ListView list = findViewById(R.id.tournamentList);
        list.setVisibility(VISIBLE);
        TextView tournamentA = findViewById(R.id.textViewAdmin);
        tournamentA.setVisibility(GONE);


    }

    public void displayWrestler()
    {
        Button man = (Button) findViewById(R.id.ManageT);
        man.setVisibility(GONE);
        Button god = (Button) findViewById(R.id.CreateT);
        god.setVisibility(GONE);
        Button team = (Button) findViewById(R.id.TeamM);
        team.setVisibility(GONE);
        TextView title = findViewById(R.id.textView2);
        title.setVisibility(GONE);
        ListView list = findViewById(R.id.tournamentList);
        list.setVisibility(VISIBLE);
        TextView tournamentA = findViewById(R.id.textViewAdmin);
        tournamentA.setVisibility(GONE);
    }
    
    public void beginTeam(View s) {
        Intent pass = new Intent(this, teamManage.class);
        startActivity(pass);
    }

    public void startTournament(View t)
    {
        Intent send = new Intent(this, TournamentActivity.class);
        send.putExtra("tournamentCode", "byuiWrestlingWI19");
        startActivity(send);
    }
    public void manageTournament(View m){
        Intent start = new Intent (this, manageTournaments.class);
        startActivity(start);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);*/
       return true;

    }

    //log out of account
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        FirebaseUser user = mAuth.getCurrentUser();
        updateActivity(user);
    }

    //update the activity depending on if the user is logged in or not
    public void updateActivity(FirebaseUser user) {
        if (user == null) {
            Intent intent = new Intent(this, Login_Start.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }


    public void createTournament(View view) {
        Intent intent = new Intent(this, CreateTournamentActivity.class);
        startActivity(intent);
    }

}

//Side bar code
        /*drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        NavigationView navigationView = findViewById(R.id.nav_view);

        drawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
*/