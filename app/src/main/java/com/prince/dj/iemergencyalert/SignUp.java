package com.prince.dj.iemergencyalert;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    CheckBox voters,nid,nhis;
    Button alert;
    TextView textCartItemCount;
    int mCartItemCount = 0;

EditText fname,lname,oname,phone,address,emergency,username,password,cpassword;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    private DatabaseReference mref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();



        mAuth = FirebaseAuth.getInstance();
        mref = FirebaseDatabase.getInstance().getReference();
        // [END initialize_auth]*/

        voters = findViewById(R.id.voters);
        nid = findViewById(R.id.nid);
        nhis = findViewById(R.id.nhis);

        fname= findViewById(R.id.fname);
        lname= findViewById(R.id.lname);
        oname= findViewById(R.id.oname);
        phone= findViewById(R.id.etmobile);
        address=findViewById(R.id.gps);
        emergency=findViewById(R.id.emergency);
        username=findViewById(R.id.uname);
        password=findViewById(R.id.pass);
        cpassword=findViewById(R.id.cpass);

        if (validate()){

        }


        voters.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (voters.isChecked()){
                    nid.setChecked(false);
                    nhis.setChecked(false);
                }
            }
        });

        nid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (nid.isChecked()){
                    voters.setChecked(false);
                    nhis.setChecked(false);
                }
            }
        });
        nhis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (nhis.isChecked()){
                    voters.setChecked(false);
                    nid.setChecked(false);
                }
            }
        });

    }

    private void createAccount(String email, String password) {
        if (!validate()) {
            return;
        }

        Utils.showProgressDialog(this,"a moment ...");

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            final FirebaseUser user = mAuth.getCurrentUser();

                            // Sign in is successful
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username.getText().toString())
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Utils.hideProgressDialog(SignUp.this);
                                                sendVerificationEmail(user);
                                                Log.d("success", "User profile updated.");
                                            }
                                        }
                                    });

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUp.this, " Failed to Sign Up.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        Utils.hideProgressDialog(SignUp.this);
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }



    private boolean validate() {
        if (fname.getText().toString().trim().equals("")){
            fname.requestFocus();
            fname.setError("First Name cant be empty");
            return false;
        }else {
            fname.setError(null);

        }if (lname.getText().toString().trim().equals("")){
            lname.requestFocus();
            lname.setError("Last Name cant be empty");
            return false;
        }else {
            lname.setError(null);

        }

//        if (oname.getText().toString().trim().equals("")){
//            oname.requestFocus();
//            oname.setError("Other Name cant be empty");
//            return false;
//        }else {
//            oname.setError(null);
//
//        }
        if (phone.getText().toString().trim().equals("")){
            phone.requestFocus();
            phone.setError("Phone number cant be empty");
            return false;
        }else {
            phone.setError(null);

        }if (address.getText().toString().trim().equals("")){
            address.requestFocus();
            address.setError("Address cant be empty");
            return false;
        }else {
            address.setError(null);

        }if (emergency.getText().toString().trim().equals("")){
            emergency.requestFocus();
            emergency.setError("Emergency Contact cant be empty");
            return false;
        }else {
            emergency.setError(null);

        }if (username.getText().toString().trim().equals("")){
            username.requestFocus();
            username.setError("User Name cant be empty");
            return false;
        }else {
            username.setError(null);

        }if (password.getText().toString().trim().equals("")){
            password.requestFocus();
            password.setError("Password cant be empty");
            return false;
        }else {
            password.setError(null);
        }if (!password.getText().toString().trim().equals(cpassword.getText().toString().trim())){
            cpassword.requestFocus();
            cpassword.setError("Password does not match");
            return false;
        }else {
            cpassword.setError(null);
        }
        return true;

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void updateUI(FirebaseUser user) {
        Utils.hideProgressDialog(SignUp.this);
        if (user != null) {
            onBackPressed();
            Database dbUser=new Database(
                    fname.getText().toString(),
                    lname.getText().toString(),
                    oname.getText().toString(),
                    "",
                    "",
                    phone.getText().toString(),
                    address.getText().toString(),
                    emergency.getText().toString(),
                    username.getText().toString()
            );



            SharedPreferences.Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
            prefEditor.putString("emergency","0247018765");
//            prefEditor.putString("email",etEmail.getText().toString());
//            prefEditor.putString("name", name.getText().toString());
//            prefEditor.putString("phone", etphone.getText().toString());
            prefEditor.apply();

            mref.child("iEmergency").child(user.getUid()).setValue(dbUser);


          //  startActivity(new Intent(SignUp.this,Login.class));
        }
    }


    public void finish(View view) {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
       // updateUI(currentUser);
    }

    @Override
    public void onStop() {
        super.onStop();
        Utils.hideProgressDialog(this);
    }

    public void CreateAccount(View view) {
        if (Utils.haveNetworkConnection(this))
            createAccount(username.getText().toString(), password.getText().toString());
        else
            Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show();

        // String sepnames[] = nam1.split(":");
        //Toast.makeText(getApplicationContext(),sepnames[0]+" and "+sepnames[1],Toast.LENGTH_LONG).show();
    }

    public void GoToLogin(View view) {
        finish();
    }

    private void sendVerificationEmail(FirebaseUser user)
    {
     //   FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Success", Toast.LENGTH_SHORT).show();
                            // email sent
                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(SignUp.this, Login.class));
                            finish();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do
                            Toast.makeText(SignUp.this, "Verification Email Sending Failed", Toast.LENGTH_SHORT).show();

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }

}