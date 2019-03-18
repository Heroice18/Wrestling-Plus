package com.example.wrestlingtournament;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
//Hey Lets DO THIS THING
//Howdy^ /-|-\

public class Login_Start extends AppCompatActivity{

    public static final String location = "com.example.wrestlingtournament.USER";
    public static final String TAG = "Login_Start";
    EditText first, last, emailEditText, passwordEditText;
    RadioButton adminButton, coachButton, playerButton;
    RadioGroup userType;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    UserObject currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        first = findViewById(R.id.fName);
        first.setVisibility(GONE);
        last = findViewById(R.id.lName);
        last.setVisibility(GONE);
        Button man = (Button) findViewById(R.id.Finish);
        man.setVisibility(GONE);

        userType = findViewById(R.id.userType);
        adminButton = findViewById(R.id.admin);
        coachButton = findViewById(R.id.coach);
        playerButton = findViewById(R.id.wrestler);

        adminButton.setVisibility(GONE);
        coachButton.setVisibility(GONE);
        playerButton.setVisibility(GONE);
        Button back = (Button) findViewById(R.id.goBack);
        back.setVisibility(GONE);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateActivity(currentUser);
    }
    //String check;
    //Intent send = new Intent(this, MainActivity.class);

    //Creates a new account using email and password to firebase
    //Also submits the firstname, lastname, email, and usertype to create a user document in firestore
    public void signUpUser(final View view) {
        Log.d(TAG, "signUpUser: creating new user");
        final String firstName = first.getText().toString();
        final String lastName = last.getText().toString();
        final String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        final String type;
        RadioGroup userType = findViewById(R.id.userType);
        if (userType.getCheckedRadioButtonId() != -1) {
            type = ((RadioButton) findViewById(userType.getCheckedRadioButtonId())).getText().toString();
            Log.d(TAG, "signUpUser: type is: " + type);
        }
        else {
            Log.d(TAG, "signUpUser: error");
            return;
        }
        if (firstName == "") {
            first.setError("Please Enter First Name");
            return;
        }
        if (lastName == "") {
            last.setError("Please Enter Last Name");
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter valid email");
            return;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "User created", Toast.LENGTH_SHORT).show();

                    //create a user in the database with name and email
                    Map<String, Object> user = new HashMap<>();
                    user.put("firstName", firstName);
                    user.put("lastName", lastName);
                    user.put("email", email.toLowerCase());
                    user.put("userType", type);

                    db.collection("user").document(String.valueOf(email))
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: added user");

                                    if (type.equals("Coach")) {
                                        new Thread(new SetupCoachDatabase(String.valueOf(email))).start();
                                    }

                                    //go to sign in page
                                    returnBack(view);
                                }
                            });
                }
                else {
                    Log.d(TAG, "onComplete: account not created");
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "Username already registered", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public void logInUser(View b)
    {
        final String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email == "") {
            emailEditText.setError("Please enter email");
            return;
        }
        if (password == ""){
            passwordEditText.setError("Please enter password");
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter valid email");
            return;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return;
        }

        Log.d(TAG, "logInUser: Beggining attempt to log in user");
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "logInUser: Successfully logged in");
                    FirebaseUser user = mAuth.getCurrentUser();
                    /*Create a Shared Preference with the json/gson
                    of the firebase object here
                     */
                    updateActivity(user);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateActivity(FirebaseUser user) {
       if (user != null) {
            //find the user in firebase and fill in the user object
           String userEmail = user.getEmail();
           Log.d(TAG, "updateActivity: User email: " + userEmail);
                    db.collection("user").document(String.valueOf(userEmail))
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            currentUser = documentSnapshot.toObject(UserObject.class);
                            Log.d(TAG, "onSuccess: successfully got user info");
                            final Intent intent = new Intent(Login_Start.this, MainActivity.class);
                            intent.putExtra("USER", currentUser.getUserType());

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: failed to get user info");
                            return;
                        }
                    });
        }
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
        EditText email = findViewById(R.id.email);
        email.setVisibility(VISIBLE);
        Button man = (Button) findViewById(R.id.Finish);
        man.setVisibility(VISIBLE);
        RadioButton adminButton = findViewById(R.id.admin);
        adminButton.setVisibility(VISIBLE);
        RadioButton coachButton = findViewById(R.id.coach);
        coachButton.setVisibility(VISIBLE);
        RadioButton playerButton = findViewById(R.id.wrestler);
        playerButton.setVisibility(VISIBLE);
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
        RadioButton adminButton = findViewById(R.id.admin);
        adminButton.setVisibility(GONE);
        RadioButton coachButton = findViewById(R.id.coach);
        coachButton.setVisibility(GONE);
        RadioButton playerButton = findViewById(R.id.wrestler);
        playerButton.setVisibility(GONE);
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
