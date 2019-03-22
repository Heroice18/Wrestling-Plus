package com.example.wrestlingtournament;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.view.View.GONE;
import static com.example.wrestlingtournament.Login_Start.location;

//Hey Lets DO THIS THING
//Howdy^ /-|-\
public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
   // private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        mAuth = FirebaseAuth.getInstance();
        setUp();
    }


    public void setUp() {
        Bundle box = getIntent().getExtras();

        String check = box.getString("USER");

        // Intent soda = new Intent();
        //String check = soda.getStringExtra(location);
        TextView display = findViewById(R.id.textView5);

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
        display.setText(check);
    }

    public void displayAdmin()
    {
        System.out.println("Working fine and dandy");

       // Button see = (Button) findViewById(R.id.ViewAll);
       // see.setVisibility(GONE);
        Button team = (Button) findViewById(R.id.TeamM);
        team.setVisibility(GONE);
        TextView title = findViewById(R.id.textView2);
        title.setVisibility(GONE);


    }

    public void displayCoach()
    {
        Button man = (Button) findViewById(R.id.ManageT);
        man.setVisibility(GONE);
        Button god = (Button) findViewById(R.id.CreateT);
        god.setVisibility(GONE);



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

    }
    public void beginTeam(View s) {
        Intent pass = new Intent(this, teamManage.class);
        startActivity(pass);
    }

    public void startTournament(View t)
    {
        Intent send = new Intent(this, TournamentActivity.class);
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