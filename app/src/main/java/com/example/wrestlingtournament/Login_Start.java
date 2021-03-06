package com.example.wrestlingtournament;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * First Activity that will start up once the app starts. Once started the
 * app with prompt the User with a Login Form Field with the option to log in
 * or create a profile. The User can log in as a Wrestler, Coach, or Admin which
 * will display specific buttons and options to the User to interact with the app.
 */

public class Login_Start extends AppCompatActivity{
    
    public static final String location = "com.example.wrestlingtournament.USER";
    /**
     * This is used for debugging purposes.
     */
    public static final String TAG = "Login_Start";
    EditText first, last, emailEditText, passwordEditText;
    RadioButton adminButton, coachButton, playerButton;
    RadioGroup userType;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser fbUser;
    UserObject currentUser;
    Button debugAD, debugCH, debugWR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Creating activity");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
        debugAD = findViewById(R.id.Admin);
        debugAD.setVisibility(GONE);
        debugCH = findViewById(R.id.Coach);
        debugCH.setVisibility(GONE);
        debugWR = findViewById(R.id.Player);
        debugWR.setVisibility(GONE);

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
//        Button create = findViewById(R.id.login);
//        create.setBackgroundColor(Color.BLUE);
        //emailEditText.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart: Starting activity");
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        fbUser = mAuth.getCurrentUser();
        updateActivity();
    }
    //String check;
    //Intent send = new Intent(this, MainActivity.class);

    //Creates a new account using email and password to firebase
    //Also submits the firstname, lastname, email, and usertype to create a user document in firestore
    
    /**
     * Creates a new User account.
     *
     * Creates a new user account to Firebase using their email and password.
     * It also submits the firstname, lastname, email, and usertype to create a user document in Firestore.
     *
     * @param view
     */
    public void signUpUser(final View view) {
        Log.d(TAG, "signUpUser: creating new user");
        final String firstName = first.getText().toString();
        final String lastName = last.getText().toString();
        final String email = emailEditText.getText().toString().toLowerCase();
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
                    user.put("ready", false);

                    db.collection("user").document(String.valueOf(email))
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: added user");

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
    
    /**
     * Handles the logging into the app.
     *
     * The function will check the fields to ensure they are all filled out before
     * connecting to Firebase to authenticate the user.
     *
     * @param b
     */
    public void logInUser(View b)
    {
        Toast.makeText(getApplicationContext(), "Please wait logging you in now", Toast.LENGTH_LONG).show();
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
                    fbUser = mAuth.getCurrentUser();
                    /*Create a Shared Preference with the json/gson
                    of the firebase object here
                     */
                    updateActivity();
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    
    /**
     * This function updates the Login Activity to send an intent to the Main Activity.
     */
    public void updateActivity() {
        Log.d(TAG, "updateActivity: updating activity, seeing if user is already logged in");
       if (fbUser != null) {
           Log.d(TAG, "updateActivity: user was already logged in");
            //find the user in firebase and fill in the user object
           String userEmail = fbUser.getEmail();
           Log.d(TAG, "updateActivity: email :" + userEmail);
           Log.d(TAG, "updateActivity: User email: " + userEmail);
                    db.collection("user").document(String.valueOf(userEmail))
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Log.d(TAG, "onSuccess: snapshot - " + documentSnapshot);
                            currentUser = documentSnapshot.toObject(UserObject.class);
                            Log.d(TAG, "onSuccess: successfully got user info");
                            Log.d(TAG, "onSuccess: user usertype: " + currentUser.getUserType());
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
    
    /**
     * This function updates the UI of the Login Activity.
     *
     * This function allows a user to see the new account creation information by updating the visibility of items on the UI.
     * @param c
     */
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
//        Button log = (Button) findViewById(R.id.login);
//        log.setVisibility(GONE);
//        Button in = (Button) findViewById(R.id.login);
//        in.setVisibility(GONE);
        Button ate = (Button) findViewById(R.id.Create);
        ate.setVisibility(GONE);
        Button login = (Button) findViewById(R.id.logButton);
        login.setVisibility(GONE);
        Button back = (Button) findViewById(R.id.goBack);
        back.setVisibility(VISIBLE);



    }
    
    /**
     * This function update the UI of the Login Activity.
     *
     * After a user has created a new account, this function updates the UI back to the normal login screen.
     * @param B
     */
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

//        Button log = (Button) findViewById(R.id.login);
//        log.setVisibility(VISIBLE);
//        Button in = (Button) findViewById(R.id.login);
//        in.setVisibility(VISIBLE);
        Button ate = (Button) findViewById(R.id.Create);
        ate.setVisibility(VISIBLE);
        Button login = (Button) findViewById(R.id.logButton);
        login.setVisibility(VISIBLE);
        //Button back = (Button) findViewById(R.id.goBack);

    }
}
